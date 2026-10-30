package dev.daylight.hub.ai

import android.app.Service
import android.content.Intent
import android.os.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.concurrent.Executors

internal object NativeBridge {
    init { System.loadLibrary("daylight_inference") }
    external fun load(path: String, context: Int, threads: Int): String
    external fun unload()
    external fun cancel()
    external fun prepare()
    external fun generate(roles: Array<String>, contents: Array<String>, temperature: Float, topP: Float, topK: Int,
        minP: Float, repeat: Float, seed: Long, maxTokens: Int, grammar: String, listener: NativeCallback)
}
internal interface NativeCallback { fun onPiece(bytes: ByteArray) }

/** No database or write tools exist in the native process. */
class InferenceService : Service() {
    private var active = false
    private val handler: Handler = Handler(Looper.getMainLooper()) { msg ->
        val reply = msg.replyTo; val data = msg.data
        fun respond(kind: Int, value: String = "") = runCatching {
            reply?.send(Message.obtain(null, kind).apply { this.data = Bundle().apply { putString("text", value) } })
        }
        if (msg.what == CANCEL) { runCatching { NativeBridge.cancel() }; return@Handler true }
        if (active) { respond(ERROR, "The model is busy. Stop generation first."); return@Handler true }
        if(msg.what==GENERATE) NativeBridge.prepare()
        active = true
        worker.execute {
            try {
                when (msg.what) {
                    LOAD -> {
                        val name = data.getString("file") ?: error("No model file.")
                        require(name == File(name).name && name.endsWith(".gguf")) { "Invalid model file." }
                        val file = File(filesDir, "models/$name")
                        require(file.isFile) { "Model file is missing. Import it again." }
                        val p = GenerationParameters.parse(data.getString("parameters") ?: "{}")
                        respond(LOADED, NativeBridge.load(file.absolutePath, p.contextSize, p.threads))
                    }
                    UNLOAD -> { NativeBridge.unload(); respond(UNLOADED) }
                    GENERATE -> {
                        val p = GenerationParameters.parse(data.getString("parameters") ?: "{}")
                        val bytes = ByteArrayOutputStream()
                        NativeBridge.generate(data.getStringArray("roles")!!, data.getStringArray("contents")!!,
                            p.temperature, p.topP, p.topK, p.minP, p.repetitionPenalty, p.seed, p.maxTokens,
                            data.getString("grammar") ?: "", object : NativeCallback {
                                override fun onPiece(bytesPiece: ByteArray) {
                                    bytes.write(bytesPiece); respond(TOKEN, bytes.toString("UTF-8"))
                                }
                            })
                        respond(DONE, bytes.toString("UTF-8"))
                    }
                }
            } catch (e: Throwable) { respond(ERROR, e.message ?: "Native inference failed. Use a smaller model.") }
            finally { handler.post { active = false } }
        }; true
    }
    private val messenger = Messenger(handler)
    override fun onBind(intent: Intent): IBinder = messenger.binder
    override fun onDestroy() {
        runCatching { NativeBridge.cancel() }
        worker.execute { runCatching { NativeBridge.unload() } }; super.onDestroy()
    }
    companion object {
        // Service recreation must not race an older service's queued native teardown.
        private val worker = Executors.newSingleThreadExecutor()
        const val LOAD=1; const val UNLOAD=2; const val GENERATE=3; const val CANCEL=4
        const val LOADED=11; const val UNLOADED=12; const val TOKEN=13; const val DONE=14; const val ERROR=15
    }
}
