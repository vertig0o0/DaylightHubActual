package dev.daylight.hub.ai

import android.app.Service
import android.content.Intent
import android.os.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.concurrent.Executors

internal object NativeBridge {
    init{System.loadLibrary("daylight_inference")}
    external fun load(path:String,context:Int,threads:Int,listener:NativeLoadCallback):String
    external fun unload()
    external fun cancel()
    external fun prepare()
    external fun generate(roles:Array<String>,contents:Array<String>,temperature:Float,topP:Float,topK:Int,
        minP:Float,repeat:Float,seed:Long,maxTokens:Int,grammar:String,listener:NativeCallback)
}
internal interface NativeCallback{fun onPiece(bytes:ByteArray)}
internal interface NativeLoadCallback{fun onProgress(stage:String,percent:Int)}

/** JNI initialization and all heavyweight work stay off the service's main thread too. */
class InferenceService:Service(){
    private var activeId:Int?=null
    @Volatile private var cancelled=false
    @Volatile private var nativeReady=false
    private val handler:Handler=Handler(Looper.getMainLooper()){message->
        val request=message.snapshotInferenceRequest()
        val id=request.id;val kind=request.kind;val reply=message.replyTo
        fun respond(type:Int,text:String="",percent:Int = -1)=runCatching{
            reply?.send(Message.obtain(null,type).apply{arg1=id;arg2=percent;this.data=Bundle().apply{putString("text",text)}})
        }
        if(kind==InferenceProtocol.RESET){
            // This non-exported service can restart itself without touching the UI process.
            android.os.Process.killProcess(android.os.Process.myPid());return@Handler true
        }
        if(kind==InferenceProtocol.CANCEL){
            if(activeId==id){cancelled=true;if(nativeReady)runCatching{NativeBridge.cancel()}}
            return@Handler true
        }
        if(kind !in setOf(InferenceProtocol.LOAD,InferenceProtocol.UNLOAD,InferenceProtocol.GENERATE)){
            respond(InferenceProtocol.ERROR,"Unknown inference operation.");return@Handler true
        }
        if(activeId!=null){respond(InferenceProtocol.ERROR,"The model is busy. Stop the current operation first.");return@Handler true}
        activeId=id;cancelled=false
        worker.execute{
            var terminal=InferenceProtocol.ERROR;var result="Native inference failed."
            try{
                if(kind==InferenceProtocol.LOAD)respond(InferenceProtocol.PROGRESS,"Starting native runtime")
                NativeBridge.prepare();nativeReady=true
                if(cancelled){NativeBridge.cancel();throw InterruptedException()}
                when(kind){
                    InferenceProtocol.LOAD->{
                        val name=request.file
                        require(name==File(name).name && name.endsWith(".gguf")){"Invalid model file."}
                        val file=File(filesDir,"models/"+name)
                        require(file.isFile){"Model file is missing. Import it again."}
                        val p=GenerationParameters.parse(request.parameters)
                        result=NativeBridge.load(file.absolutePath,p.contextSize,p.threads,object:NativeLoadCallback{
                            override fun onProgress(stage:String,percent:Int){respond(InferenceProtocol.PROGRESS,stage,percent)}
                        })
                        if(cancelled){NativeBridge.unload();terminal=InferenceProtocol.CANCELLED;result=""}
                        else terminal=InferenceProtocol.LOADED
                    }
                    InferenceProtocol.UNLOAD->{NativeBridge.unload();terminal=InferenceProtocol.UNLOADED;result=""}
                    InferenceProtocol.GENERATE->{
                        val p=GenerationParameters.parse(request.parameters)
                        val output=ByteArrayOutputStream()
                        NativeBridge.generate(request.roles.toTypedArray(),request.contents.toTypedArray(),
                            p.temperature,p.topP,p.topK,p.minP,p.repetitionPenalty,p.seed,p.maxTokens,
                            request.grammar,object:NativeCallback{
                                override fun onPiece(bytes:ByteArray){output.write(bytes);respond(InferenceProtocol.TOKEN,output.toString("UTF-8"))}
                            })
                        terminal=InferenceProtocol.DONE;result=output.toString("UTF-8")
                    }
                }
            }catch(e:Throwable){
                terminal=if(cancelled || e is InterruptedException)InferenceProtocol.CANCELLED else InferenceProtocol.ERROR
                result=e.message ?: "Native inference failed. Try a smaller GGUF or lower context."
            }finally{
                val completedKind=terminal;val completedText=result
                handler.post{
                    activeId=null
                    // Clear busy before replying, so the next operation cannot race completion.
                    respond(completedKind,completedText)
                }
            }
        }
        true
    }
    private val messenger=Messenger(handler)
    override fun onBind(intent:Intent):IBinder=messenger.binder
    override fun onDestroy(){
        cancelled=true;if(nativeReady)runCatching{NativeBridge.cancel()}
        worker.execute{if(nativeReady)runCatching{NativeBridge.unload()}}
        super.onDestroy()
    }
    companion object{private val worker=Executors.newSingleThreadExecutor()}
}
