package dev.daylight.hub

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.daylight.hub.ai.*
import dev.daylight.hub.data.ModelProfile
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class NativeLoadingTest {
    private val context=ApplicationProvider.getApplicationContext<android.content.Context>()
    private fun profile(id:String="smoke360",name:String="SmolLM2-360M-Instruct Q4_K_M"):ModelProfile {
        val file=File(context.filesDir,"models/"+id+".gguf")
        Assume.assumeTrue("Provide a local regression GGUF: "+file.name,file.isFile)
        return ModelProfile(id,name,file.name,file.length(),"",
            GenerationParameters(temperature=0f,maxTokens=32,contextSize=2048,threads=4).json())
    }
    private suspend fun verifyModel(profile:ModelProfile)=coroutineScope {
        val engine=InferenceEngine(context);val stages=mutableSetOf<String>()
        val observation=launch(Dispatchers.Unconfined){engine.state.collect{if(it.loading)stages.add(it.description)}}
        try {
            val started=android.os.SystemClock.elapsedRealtime()
            withTimeout(60000){engine.load(profile)}
            val loadMillis=android.os.SystemClock.elapsedRealtime()-started
            assertEquals(profile.id,engine.state.value.loadedId)
            assertTrue("Expected progress from the native loader",stages.any{it.contains("Mapping model weights") || it.contains("Allocating context")})
            var reply=""
            withTimeout(60000){engine.generate(listOf("user" to "Say hello."),GenerationParameters.parse(profile.parameters)).collect{reply=it}}
            assertTrue(reply.isNotBlank())
            File(context.filesDir,profile.id+"-load-report.txt").writeText(profile.name+"\nContext: 2048; CPU threads: 4\nNative load: "+loadMillis+" ms\nStages:\n"+stages.joinToString("\n"))
            engine.unload()
        }finally{observation.cancelAndJoin();engine.close()}
    }
    @Test fun model360LoadsWithNativeProgressAndGenerates()=runBlocking {verifyModel(profile())}
    @Test fun lfm230LoadsWithNativeProgressAndGenerates()=runBlocking {verifyModel(profile("lfm230","LFM2.5-230M Q4_K_M"))}
    @Test fun cancelledLoadCanBeRetriedAndOldCompletionCannotWin()=runBlocking {
        val profile=profile();val engine=InferenceEngine(context)
        try {
            val loading=async{engine.load(profile)}
            withTimeout(5000){while(!engine.state.value.loading)delay(5)}
            engine.cancel()
            withTimeout(10000){loading.await()}
            assertFalse(engine.state.value.busy);assertNull(engine.state.value.loadedId)
            withTimeout(60000){engine.load(profile)}
            assertEquals(profile.id,engine.state.value.loadedId)
            engine.unload()
        }finally{engine.close()}
    }
    @Test fun invalidNativeModelShowsErrorAndAllowsAnotherLoad()=runBlocking {
        val profile=profile();val bad=File(context.filesDir,"models/invalid-load-test.gguf")
        val engine=InferenceEngine(context)
        try {
            bad.writeBytes(ByteArray(64))
            val failure=withTimeout(15000){runCatching{engine.load(profile.copy(id="bad",fileName=bad.name))}}
            assertTrue(failure.isFailure);assertFalse(engine.state.value.busy)
            assertFalse(engine.state.value.error.isNullOrBlank())
            withTimeout(60000){engine.load(profile)}
            assertEquals(profile.id,engine.state.value.loadedId)
            engine.unload()
        }finally{bad.delete();engine.close()}
    }
}
