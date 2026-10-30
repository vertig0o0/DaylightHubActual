package dev.daylight.hub.ai

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import dev.daylight.hub.data.ModelProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import java.io.File
import java.io.RandomAccessFile
import java.util.UUID
import kotlin.coroutines.coroutineContext

class ModelImporter(private val context: Context) {
    suspend fun import(uri: Uri, progress: (String) -> Unit): ModelProfile = withContext(Dispatchers.IO) {
        val directory = File(context.filesDir, "models").apply { mkdirs() }
        directory.listFiles()?.filter{it.name.matches(Regex("[a-f0-9-]{36}\\.partial"))}?.forEach{it.delete()}
        var name = "Local model.gguf"; var size = -1L
        context.contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE), null, null, null)?.use {
            if(it.moveToFirst()) { name = it.getString(0) ?: name; if(!it.isNull(1)) size = it.getLong(1) }
        }
        require(name.endsWith(".gguf", true)) { "Select a .gguf text model." }
        if(size > 0) require(directory.usableSpace > size + 128L * 1024 * 1024) { "Not enough storage to copy this GGUF." }
        val id = UUID.randomUUID().toString(); val file = File(directory, "$id.partial")
        val complete=File(directory,"$id.gguf")
        try {
            context.contentResolver.openInputStream(uri)?.use { input ->
                file.outputStream().use { output ->
                    val buffer = ByteArray(1024*1024); var copied=0L
                    while(true) {
                        coroutineContext.ensureActive()
                        val n=input.read(buffer); if(n<0) break
                        require(directory.usableSpace > n + 32L*1024*1024) { "Storage is full." }
                        output.write(buffer,0,n); copied+=n
                        progress(if(size>0) "Importing " + (copied*100/size) + "%" else "Importing " + copied/(1024*1024) + " MB")
                    }
                }
            } ?: error("Could not read the selected file.")
            val metadata = GgufInspector.inspect(file)
            check(file.renameTo(complete)) { "Could not finalize the imported model." }
            ModelProfile(id, name, complete.name, complete.length(), metadata, GenerationParameters().json())
        } catch(e: Throwable) { file.delete(); complete.delete(); throw e }
    }
}
/** Bounded GGUF metadata reader; never maps model tensors during import. */
object GgufInspector {
    fun inspect(file: File): String = RandomAccessFile(file,"r").use { f ->
        fun uint() = Integer.toUnsignedLong(Integer.reverseBytes(f.readInt()))
        fun ulong(): Long = java.lang.Long.reverseBytes(f.readLong()).also { require(it >= 0) { "Invalid GGUF length." } }
        fun text(): String { val n=ulong(); require(n <= 1024*1024 && n <= f.length()-f.filePointer) { "Invalid GGUF string." }; return ByteArray(n.toInt()).also(f::readFully).toString(Charsets.UTF_8) }
        require(f.length() >= 24 && f.readInt() == 0x47475546) { "This file is not GGUF." }
        val version=uint(); require(version in 2L..3L) { "Only GGUF v2/v3 are supported." }
        val tensors=ulong(); val entries=ulong()
        require(tensors > 0 && entries in 1..100000) { "Invalid GGUF header." }
        val info=mutableListOf("GGUF v$version", "$tensors tensors")
        fun skip(count: Long) { require(count >= 0 && count <= f.length()-f.filePointer && f.filePointer+count <= 64L*1024*1024) { "Invalid/oversized GGUF metadata." }; f.seek(f.filePointer+count) }
        fun value(type: Long, depth: Int): String {
            require(depth < 4) { "Invalid GGUF nesting." }
            return when(type.toInt()) {
                0,1,7 -> f.readUnsignedByte().toString()
                2,3 -> java.lang.Short.reverseBytes(f.readShort()).toString()
                4,5,6 -> uint().toString()
                8 -> text()
                9 -> { val child=uint(); val n=ulong(); require(n<=10000000) { "Oversized GGUF array." }; repeat(n.toInt()) { value(child,depth+1) }; "" }
                10,11,12 -> java.lang.Long.reverseBytes(f.readLong()).toString()
                else -> error("Invalid GGUF metadata type.")
            }
        }
        repeat(entries.toInt()) {
            require(f.filePointer < 64L*1024*1024) { "GGUF metadata is too large." }
            val key=text(); val type=uint()
            // Strings in tokenizer arrays can be large but are read one at a time.
            val v=value(type,0)
            if(key in setOf("general.name","general.architecture","general.file_type") || key.endsWith(".context_length")) info.add("$key: " + v.take(200))
        }
        require(f.filePointer < f.length()) { "GGUF has no tensor data." }
        info.joinToString("\n")
    }
}
