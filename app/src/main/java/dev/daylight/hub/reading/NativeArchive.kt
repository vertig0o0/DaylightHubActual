package dev.daylight.hub.reading

import kotlinx.coroutines.*
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean

object NativeArchive {
    init{System.loadLibrary("daylight_archive")}
    private external fun convert(source:String,destination:String,cancelled:AtomicBoolean):Int
    suspend fun toZip(source:File,destination:File):Int=coroutineScope {
        val flag=AtomicBoolean(false)
        val watch=launch(Dispatchers.Default,start=CoroutineStart.UNDISPATCHED){try{awaitCancellation()}finally{flag.set(true)}}
        try { withContext(Dispatchers.IO){convert(source.absolutePath,destination.absolutePath,flag)} }
        finally { watch.cancel();withContext(NonCancellable){watch.join()} }
    }
}
