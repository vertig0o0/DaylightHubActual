package dev.daylight.hub.ai

import android.content.*
import android.os.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout

/** Android recycles Message after dispatch. Retain only copied request values on workers. */
internal fun Message.snapshotInferenceRequest()=InferenceRequest(
    id=arg1,kind=what,file=data.getString("file") ?: "",parameters=data.getString("parameters") ?: "{}",
    roles=data.getStringArray("roles")?.toList() ?: emptyList(),
    contents=data.getStringArray("contents")?.toList() ?: emptyList(),grammar=data.getString("grammar") ?: ""
)

/** Each reply belongs to one request; late tokens cannot complete a later load. */
internal class MessengerInferenceTransport(private val context:Context):InferenceTransport {
    @Volatile private var service:Messenger?=null
    @Volatile private var requestId=0
    private var resetRequestId:Int?=null
    private var closed=false
    override var onDisconnect:((String)->Unit)?=null
    override val replies=Channel<InferenceReply>(Channel.UNLIMITED)
    private val receiver=Messenger(Handler(Looper.getMainLooper()){message->
        replies.trySend(InferenceReply(message.arg1,message.what,message.data.getString("text") ?: "",message.arg2))
        true
    })
    private val connection=object:ServiceConnection {
        override fun onServiceConnected(name:ComponentName,binder:IBinder){service=Messenger(binder)}
        override fun onServiceDisconnected(name:ComponentName){
            service=null
            val reason="The inference process stopped. Reload the model, or try a smaller GGUF or context."
            onDisconnect?.invoke(reason)
            replies.trySend(InferenceReply(resetRequestId ?: requestId,InferenceProtocol.ERROR,reason))
            resetRequestId=null
        }
        override fun onBindingDied(name:ComponentName){
            onServiceDisconnected(name)
            runCatching{context.unbindService(this)}
            if(!closed)bind()
        }
        override fun onNullBinding(name:ComponentName){onServiceDisconnected(name)}
    }
    init{bind()}
    private fun bind(){check(context.bindService(Intent(context,InferenceService::class.java),connection,Context.BIND_AUTO_CREATE)){"Cannot start the private inference service."}}
    override suspend fun send(request:InferenceRequest){
        requestId=request.id
        withTimeout(15000){while(service==null)delay(50)}
        service!!.send(Message.obtain(null,request.kind).apply{
            arg1=request.id;replyTo=receiver
            data=Bundle().apply{
                putString("file",request.file);putString("parameters",request.parameters)
                putStringArray("roles",request.roles.toTypedArray());putStringArray("contents",request.contents.toTypedArray())
                putString("grammar",request.grammar)
            }
        })
    }
    override fun cancel(id:Int){runCatching{service?.send(Message.obtain(null,InferenceProtocol.CANCEL).apply{arg1=id})}}
    override fun reset(){
        resetRequestId=requestId
        val previous=service;service=null
        runCatching{previous?.send(Message.obtain(null,InferenceProtocol.RESET))}
    }
    override fun close(){closed=true;runCatching{context.unbindService(connection)}}
}
