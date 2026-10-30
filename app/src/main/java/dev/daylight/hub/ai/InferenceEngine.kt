package dev.daylight.hub.ai

import android.content.Context
import dev.daylight.hub.data.ModelProfile
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.atomic.AtomicInteger

data class EngineState(
    val loadedId:String?=null,val busy:Boolean=false,val description:String="No model loaded",val error:String?=null,
    val loading:Boolean=false,val progress:Int?=null,val elapsedSeconds:Long=0,val cancelling:Boolean=false
)
class InferenceEngine internal constructor(
    private val transport:InferenceTransport,
    private val loadTimeoutMillis:Long=180000,
    private val cancelGraceMillis:Long=3000,
    private val clockMillis:()->Long={System.nanoTime()/1000000}
) {
    constructor(context:Context):this(MessengerInferenceTransport(context))
    private val mutex=Mutex()
    private val sequence=AtomicInteger()
    @Volatile private var activeId=0
    @Volatile private var loadCancelled=false
    @Volatile private var expectedDisconnect=false
    private val mutableState=MutableStateFlow(EngineState())
    val state=mutableState.asStateFlow()
    init{
        transport.onDisconnect={reason->
            if(expectedDisconnect){expectedDisconnect=false}
            else mutableState.value=EngineState(error=reason)
        }
    }
    private fun restartProcess(){expectedDisconnect=true;transport.reset()}
    private suspend fun reply(id:Int):InferenceReply {
        while(true){
            val received=transport.replies.receive()
            if(received.id==id)return received
        }
    }
    suspend fun load(profile:ModelProfile)=mutex.withLock {
        val id=sequence.incrementAndGet();activeId=id;loadCancelled=false
        val start=clockMillis();var cancelStart:Long?=null;var stage="Starting native runtime"
        mutableState.value=EngineState(busy=true,loading=true,description=stage+"…")
        try{
            withTimeout(loadTimeoutMillis){
                transport.send(InferenceRequest(id,InferenceProtocol.LOAD,profile.fileName,profile.parameters))
                while(true){
                    if(loadCancelled){
                        if(cancelStart==null)cancelStart=clockMillis()
                        if(clockMillis()-cancelStart!!>=cancelGraceMillis){
                            restartProcess()
                            mutableState.value=EngineState(description="Loading cancelled. The inference process was restarted.")
                            return@withTimeout
                        }
                    }
                    val message=withTimeoutOrNull(if(loadCancelled)100 else 1000){reply(id)}
                    mutableState.value=state.value.copy(elapsedSeconds=(clockMillis()-start)/1000)
                    if(message==null)continue
                    when(message.kind){
                        InferenceProtocol.PROGRESS->{
                            stage=message.text
                            mutableState.value=state.value.copy(description=stage,progress=message.percent.takeIf{it in 0..100})
                        }
                        InferenceProtocol.LOADED->{
                            if(loadCancelled){restartProcess();mutableState.value=EngineState(description="Loading cancelled.")}
                            else {expectedDisconnect=false;mutableState.value=EngineState(loadedId=profile.id,description=message.text)}
                            return@withTimeout
                        }
                        InferenceProtocol.CANCELLED->{mutableState.value=EngineState(description="Loading cancelled.");return@withTimeout}
                        InferenceProtocol.ERROR->error(message.text)
                    }
                }
            }
        }catch(e:TimeoutCancellationException){
            restartProcess()
            if(!currentCoroutineContext().isActive){mutableState.value=EngineState(description="Loading cancelled.");throw e}
            val reason="Model loading timed out at: "+stage+". The inference process was restarted. Try a smaller GGUF or lower context size."
            mutableState.value=EngineState(error=reason);throw IllegalStateException(reason,e)
        }catch(e:CancellationException){
            restartProcess();mutableState.value=EngineState(description="Loading cancelled.");throw e
        }catch(e:Exception){
            mutableState.value=EngineState(error=e.message ?: "Model loading failed.");throw e
        }finally{activeId=0}
    }
    suspend fun unload()=mutex.withLock {
        val id=sequence.incrementAndGet();activeId=id
        try{
            withTimeout(15000){
                transport.send(InferenceRequest(id,InferenceProtocol.UNLOAD))
                val response=reply(id);check(response.kind==InferenceProtocol.UNLOADED){response.text}
            }
            mutableState.value=EngineState()
        }catch(e:Exception){restartProcess();mutableState.value=EngineState(error="Could not unload the model; the inference process was restarted.");throw e}
        finally{activeId=0}
    }
    fun generate(messages:List<Pair<String,String>>,parameters:GenerationParameters,grammar:String=""):Flow<String> = flow {
        mutex.withLock {
            check(state.value.loadedId!=null){"Import and load a GGUF in Settings → Local models first."}
            val id=sequence.incrementAndGet();activeId=id
            mutableState.value=state.value.copy(busy=true,error=null)
            try{
                transport.send(InferenceRequest(id,InferenceProtocol.GENERATE,parameters=parameters.json(),
                    roles=messages.map{it.first},contents=messages.map{it.second},grammar=grammar))
                while(true){
                    val response=reply(id)
                    if(response.kind==InferenceProtocol.ERROR)error(response.text)
                    if(response.kind==InferenceProtocol.CANCELLED)break
                    if(response.kind==InferenceProtocol.TOKEN || response.kind==InferenceProtocol.DONE)emit(response.text)
                    if(response.kind==InferenceProtocol.DONE)break
                }
            }finally{activeId=0;mutableState.value=state.value.copy(busy=false)}
        }
    }
    fun cancel(){
        if(state.value.loading){loadCancelled=true;mutableState.value=state.value.copy(cancelling=true,description="Cancelling model load…")}
        transport.cancel(activeId)
    }
    fun close(){cancel();transport.close()}
}
