package dev.daylight.hub.ai

import android.content.*
import android.os.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

data class EngineState(val loadedId: String? = null, val busy: Boolean = false, val description: String = "No model loaded", val error: String? = null)
class InferenceEngine(private val context: Context) {
    @Volatile private var service: Messenger? = null
    private val replies = Channel<Pair<Int, String>>(Channel.UNLIMITED)
    private val mutex = Mutex()
    private val mutableState = MutableStateFlow(EngineState())
    val state = mutableState.asStateFlow()
    private val receiver = Messenger(Handler(Looper.getMainLooper()) { msg ->
        replies.trySend(msg.what to (msg.data.getString("text") ?: "")); true
    })
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, binder: IBinder) { service = Messenger(binder) }
        override fun onServiceDisconnected(name: ComponentName) {
            service = null
            mutableState.value = EngineState(error = "The inference process stopped. Reload a smaller model or lower context size.")
            replies.trySend(InferenceService.ERROR to mutableState.value.error!!)
        }
        override fun onBindingDied(name: ComponentName) { onServiceDisconnected(name); context.unbindService(this); bind() }
    }
    init { bind() }
    private fun bind() { context.bindService(Intent(context, InferenceService::class.java), connection, Context.BIND_AUTO_CREATE) }
    private suspend fun send(kind: Int, data: Bundle = Bundle()) {
        kotlinx.coroutines.withTimeout(15000) { while(service == null) kotlinx.coroutines.delay(50) }
        while(replies.tryReceive().isSuccess) { }
        service!!.send(Message.obtain(null, kind).apply { this.data = data; replyTo = receiver })
    }
    suspend fun load(profile: dev.daylight.hub.data.ModelProfile) = mutex.withLock {
        mutableState.value = EngineState(busy = true, description = "Loading " + profile.name + "…")
        try {
            send(InferenceService.LOAD, Bundle().apply { putString("file", profile.fileName); putString("parameters", profile.parameters) })
            val (kind, text) = replies.receive()
            check(kind == InferenceService.LOADED) { text }
            mutableState.value = EngineState(loadedId = profile.id, description = text)
        } catch(e: Exception) { mutableState.value = EngineState(error = e.message); throw e }
    }
    suspend fun unload() = mutex.withLock {
        send(InferenceService.UNLOAD)
        val (kind, text) = replies.receive(); check(kind == InferenceService.UNLOADED) { text }
        mutableState.value = EngineState()
    }
    fun generate(messages: List<Pair<String,String>>, parameters: GenerationParameters, grammar: String = ""): Flow<String> = flow {
        mutex.withLock {
            check(state.value.loadedId != null) { "Import and load a GGUF in Settings → Models first." }
            mutableState.value = state.value.copy(busy = true, error = null)
            try {
                send(InferenceService.GENERATE, Bundle().apply {
                    putStringArray("roles", messages.map { it.first }.toTypedArray())
                    putStringArray("contents", messages.map { it.second }.toTypedArray())
                    putString("parameters", parameters.json()); putString("grammar", grammar)
                })
                while(true) {
                    val (kind, text) = replies.receive()
                    if(kind == InferenceService.ERROR) error(text)
                    if(kind == InferenceService.TOKEN || kind == InferenceService.DONE) emit(text)
                    if(kind == InferenceService.DONE) break
                }
            } finally { mutableState.value = state.value.copy(busy = false) }
        }
    }
    fun cancel() { runCatching { service?.send(Message.obtain(null, InferenceService.CANCEL)) } }
    fun close() { cancel(); runCatching { context.unbindService(connection) } }
}
