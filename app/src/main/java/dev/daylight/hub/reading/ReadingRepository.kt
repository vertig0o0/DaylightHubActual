package dev.daylight.hub.reading

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.OpenableColumns
import androidx.room.withTransaction
import dev.daylight.hub.data.HubDatabase
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.*
import java.security.MessageDigest
import java.util.UUID
import java.util.zip.ZipFile

class ReadingRepository(private val context:Context,private val db:HubDatabase) {
    companion object { private val imports=Mutex() }
    fun file(relative:String):File {
        val root=File(context.filesDir,"books").canonicalFile
        val candidate=File(context.filesDir,relative).canonicalFile
        require(candidate.path.startsWith(root.path+File.separator)) { "Invalid private book path." }
        return candidate
    }
    /** Remove only known incomplete staging files. Completed original copies are never discarded. */
    suspend fun recoverImports()=imports.withLock { withContext(Dispatchers.IO) {
        db.reading().interruptImports()
        val root=File(context.filesDir,"books").canonicalFile
        root.listFiles()?.forEach { entry ->
            if(entry.isFile && entry.name.matches(Regex("import-[0-9a-f-]{36}\\.part")) && entry.canonicalFile.parentFile==root)entry.delete()
            if(entry.isDirectory && entry.name.matches(Regex("[0-9a-f]{64}")) && entry.canonicalFile.parentFile==root) {
                entry.listFiles()?.filter { it.name=="converted-text.part" || it.name.matches(Regex("normalized-v1\\.(epub|fb2|cbz)\\.part")) }
                    ?.filter{it.isFile && it.canonicalFile.parentFile==entry.canonicalFile}?.forEach{it.delete()}
            }
        }
    } }
    suspend fun import(uri:Uri,onProgress:(BookImport)->Unit={}):Book = imports.withLock { withContext(Dispatchers.IO) {
        require(uri.scheme=="content"){"Choose a book through Android’s file picker."}
        val importContext=currentCoroutineContext()
        val ownedFiles=mutableListOf<File>()
        var contentHash:String?=null
        var name="Book";var expected:Long?=null
        context.contentResolver.query(uri,arrayOf(OpenableColumns.DISPLAY_NAME,OpenableColumns.SIZE),null,null,null)?.use { cursor ->
            if(cursor.moveToFirst()) {
                val nameIndex=cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if(nameIndex>=0) name=cursor.getString(nameIndex).orEmpty().take(512).ifBlank{"Book"}
                val sizeIndex=cursor.getColumnIndex(OpenableColumns.SIZE)
                if(sizeIndex>=0 && !cursor.isNull(sizeIndex)) expected=cursor.getLong(sizeIndex).takeIf{it>=0}
            }
        }
        require(expected==null || expected!!<=BookFiles.MAX_IMPORT){"Choose a book up to 2 GB."}
        val id=UUID.randomUUID().toString()
        val staging=File(context.filesDir,"books/import-$id.part").apply{parentFile!!.mkdirs()}
        var job=BookImport(id,uri.toString(),name,expectedBytes=expected)
        suspend fun report(stage:String,copied:Long=job.bytesCopied,message:String="") {
            job=job.copy(stage=stage,bytesCopied=copied,message=message,updatedAt=System.currentTimeMillis())
            db.reading().saveImport(job);runCatching{onProgress(job)}
        }
        try {
            require(expected==null || staging.parentFile!!.usableSpace>expected!!+10*1024*1024){"There is not enough free storage to copy this book."}
            report("COPYING")
            val digest=MessageDigest.getInstance("SHA-256");var copied=0L;var lastReport=0L
            context.contentResolver.openInputStream(uri)?.use { source ->
                FileOutputStream(staging).use { destination ->
                    val buffer=ByteArray(65536)
                    while(true) {
                        currentCoroutineContext().ensureActive()
                        val count=source.read(buffer);if(count<0)break
                        copied+=count;require(copied<=BookFiles.MAX_IMPORT){"This file exceeds the 2 GB limit."}
                        digest.update(buffer,0,count);destination.write(buffer,0,count)
                        if(copied-lastReport>=1024*1024){report("COPYING",copied);lastReport=copied}
                    }
                    destination.fd.sync()
                }
            } ?: error("The file provider could not open this book. Choose a locally available copy.")
            require(copied>0 && (expected==null || copied==expected)){"The file was incomplete. Select the source again."}
            val hash=digest.digest().joinToString(""){"%02x".format(it)}
            contentHash=hash
            val duplicate=db.reading().byHash(hash)
            val relative="books/$hash/original"
            val original=file(relative).apply{parentFile!!.mkdirs()}
            if(!original.exists()) { check(staging.renameTo(original)){"Could not finish copying the book."};ownedFiles.add(original) }
            else staging.delete()
            if(duplicate!=null && (duplicate.normalizedFile==null || file(duplicate.normalizedFile).isFile)) {
                val restored=duplicate.copy(deletedAt=null,revision=duplicate.revision+if(duplicate.deletedAt!=null)1 else 0)
                if(restored!=duplicate) db.reading().update(restored)
                report("DONE",copied,"Already in your library. Position retained.");return@withContext restored
            }
            report("VALIDATING",copied)
            val info=BookFiles.inspect(original,name,context.contentResolver.getType(uri))
            var format=info.format;var normalized:String?=null
            if(format in listOf("TXT","HTML","RTF","FB2_ZIP","CBR") || format=="FB2" && original.length()>BookFiles.MAX_TEXT) {
                report("CONVERTING",copied)
                normalized="books/$hash/normalized-v1."+when(format){"FB2_ZIP"->"fb2";"CBR"->"cbz";else->"epub"}
                var finalTarget=file(normalized)
                var target=File(finalTarget.path+".part")
                ownedFiles.add(target);if(!finalTarget.exists())ownedFiles.add(finalTarget)
                when(format) {
                    "TXT"->{BookFiles.textToEpub(original,target,info.title,checkCancelled={importContext.ensureActive()});format="EPUB"}
                    "FB2_ZIP"->{ZipFile(original).use{zip->
                        val entry=zip.getEntry(info.fb2Entry!!) ?: error("Missing FB2 file.")
                        zip.getInputStream(entry).use{input->target.outputStream().use{output->
                            val bounded=BookFiles.boundedStream(input,BookFiles.MAX_IMPORT);val buffer=ByteArray(65536)
                            while(true){importContext.ensureActive();val count=bounded.read(buffer);if(count<0)break;output.write(buffer,0,count)}
                        }}
                    };format="FB2"}
                    "HTML"->{
                        BookFiles.htmlToEpub(original,target,info.title){importContext.ensureActive()}
                        format="EPUB"
                    }
                    "RTF"->{
                        val textFile=file("books/$hash/converted-text.part")
                        try {
                            original.inputStream().buffered().use { input -> textFile.outputStream().buffered().use { output ->
                                val cancellable=object:FilterInputStream(input) {
                                    override fun read():Int { importContext.ensureActive();return `in`.read() }
                                    override fun read(buffer:ByteArray,offset:Int,length:Int):Int { importContext.ensureActive();return `in`.read(buffer,offset,length) }
                                }
                                com.rtfparserkit.converter.text.StreamTextConverter().convert(com.rtfparserkit.parser.RtfStreamSource(cancellable),output,"UTF-8")
                            } }
                            BookFiles.textToEpub(textFile,target,info.title,checkCancelled={importContext.ensureActive()});format="EPUB"
                        } finally { textFile.delete() }
                    }
                    "CBR"->{NativeArchive.toZip(original,target);format="CBZ"}
                }
                if(format=="FB2" && target.length()>BookFiles.MAX_TEXT || info.format=="FB2") {
                    val textFile=file("books/$hash/converted-text.part")
                    val fb2Source=if(info.format=="FB2")original else target
                    val epubTarget=file("books/$hash/normalized-v1.epub.part");ownedFiles.add(epubTarget)
                    try { Fb2Normalizer.toText(fb2Source,textFile){importContext.ensureActive()};BookFiles.textToEpub(textFile,epubTarget,info.title,checkCancelled={importContext.ensureActive()}) }
                    finally{textFile.delete()}
                    if(target!=epubTarget)target.delete()
                    target=epubTarget;normalized="books/$hash/normalized-v1.epub";finalTarget=file(normalized);ownedFiles.add(finalTarget);format="EPUB"
                }
                // Publish a normalized copy only after conversion and validation finish.
                if(format in listOf("EPUB","CBZ"))BookFiles.inspect(target,if(format=="CBZ")"book.cbz" else "book.epub",null)
                check(target.renameTo(finalTarget)){"Could not save the offline reading copy."}
            }
            val coverTarget=file("books/$hash/cover.jpg")
            if(!coverTarget.exists())ownedFiles.add(coverTarget)
            val cover=if(info.format=="EPUB" && info.coverEntry!=null) runCatching { extractCover(original,info.coverEntry,coverTarget);"books/$hash/cover.jpg" }.getOrNull() else null
            val item=duplicate?.copy(deletedAt=null,normalizedFile=normalized,coverFile=cover ?: duplicate.coverFile,format=format,revision=duplicate.revision+1) ?: Book(sha256=hash,title=info.title,author=info.author,originalName=name,originalUri=uri.toString(),
                format=format,originalFormat=info.format,fileName=relative,normalizedFile=normalized,coverFile=cover,bytes=copied)
            val result=if(duplicate==null)item.copy(id=db.reading().insert(item)) else item.also{db.reading().update(it)}
            report("DONE",copied,"Added to your library.");result
        } catch(e:CancellationException) {
            withContext(NonCancellable){report("CANCELLED",message="Import cancelled.")};throw e
        } catch(e:Exception) {
            report("FAILED",message=e.message ?: "The book could not be imported.");throw e
        } finally {
            staging.delete()
            withContext(NonCancellable) {
                if(contentHash!=null && db.reading().byHash(contentHash!!)==null) {
                    ownedFiles.forEach{it.delete()}
                    ownedFiles.firstOrNull()?.parentFile?.let{directory->if(directory.list()?.isEmpty()==true)directory.delete()}
                }
            }
        }
    } }
    private fun extractCover(source:File,entry:String,target:File) {
        val bytes=ZipFile(source).use{BookFiles.zipBytes(it,entry,BookFiles.MAX_RESOURCE)} ?: error("No cover")
        val bounds=BitmapFactory.Options().apply{inJustDecodeBounds=true};BitmapFactory.decodeByteArray(bytes,0,bytes.size,bounds)
        require(bounds.outWidth>0 && bounds.outHeight>0)
        var sample=1;while(bounds.outWidth/sample>384 || bounds.outHeight/sample>576)sample*=2
        val image=BitmapFactory.decodeByteArray(bytes,0,bytes.size,BitmapFactory.Options().apply{inSampleSize=sample}) ?: error("No cover")
        try { target.outputStream().use{image.compress(Bitmap.CompressFormat.JPEG,85,it)} }finally{image.recycle()}
    }
    suspend fun savePosition(position:BookPosition)=db.withTransaction {
        position.validate();val book=db.reading().book(position.bookId) ?: error("This book is unavailable.")
        check(book.deletedAt==null){"This book is unavailable."}
        db.reading().savePosition(position);db.reading().updateProgress(position.bookId,position.fraction,System.currentTimeMillis())
    }
    suspend fun status(id:Long,expected:Long,status:String)=db.withTransaction {
        require(status in listOf("WANT_TO_READ","READING","PAUSED","FINISHED"))
        val old=db.reading().book(id) ?: error("This book is unavailable.")
        check(old.revision==expected && old.deletedAt==null){"Book changed. Reopen its details."}
        if(old.status!=status) {
            db.reading().update(old.copy(status=status,revision=old.revision+1))
            db.reading().insertStatusEvent(ReadingStatusEvent(bookId=id,title=old.title,status=status))
        }
    }
    suspend fun editDetails(book:Book,title:String,author:String,status:String)=db.withTransaction {
        require(title.isNotBlank() && title.length<=512 && author.length<=512)
        require(status in listOf("WANT_TO_READ","READING","PAUSED","FINISHED"))
        val old=db.reading().book(book.id) ?: error("Book is unavailable.")
        check(old.deletedAt==null && old.revision==book.revision){"Book changed. Reopen its details."}
        db.reading().update(old.copy(title=title.trim(),author=author.trim(),status=status,metadataEdited=true,revision=old.revision+1))
        if(status!=old.status)db.reading().insertStatusEvent(ReadingStatusEvent(bookId=old.id,title=title.trim(),status=status))
    }
    suspend fun bookmark(position:BookPosition,label:String)=db.withTransaction {
        position.validate();require(label.isNotBlank() && label.length<=200)
        db.reading().insertBookmark(Bookmark(bookId=position.bookId,label=label,position=position.json()))
    }
    suspend fun remove(book:Book)=db.withTransaction {
        val current=db.reading().book(book.id) ?: error("Book was removed.")
        check(current.revision==book.revision){"Book changed. Review its details."}
        db.reading().update(current.copy(deletedAt=System.currentTimeMillis(),revision=current.revision+1))
    }
    suspend fun restore(id:Long,expected:Long)=db.withTransaction {
        val current=db.reading().book(id) ?: error("Book is unavailable.")
        check(current.revision==expected && current.deletedAt!=null){"Book changed. Review Trash."}
        db.reading().update(current.copy(deletedAt=null,revision=current.revision+1))
    }
}
