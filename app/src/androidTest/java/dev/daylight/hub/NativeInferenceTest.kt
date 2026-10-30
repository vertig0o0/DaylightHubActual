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
class NativeInferenceTest {
    @Test fun actualGgufStreamsCancelsAndProducesConstrainedJson()=runBlocking {
        val context=ApplicationProvider.getApplicationContext<android.content.Context>()
        val file=File(context.filesDir,"models/smoke.gguf")
        Assume.assumeTrue("Provide a smoke GGUF in app-private models/smoke.gguf; see README.",file.isFile)
        val parameters=GenerationParameters(temperature=0f,maxTokens=64,contextSize=1024,threads=2)
        val profile=ModelProfile("smoke","Native smoke",file.name,file.length(),GgufInspector.inspect(file),parameters.json())
        val engine=InferenceEngine(context)
        try {
            withTimeout(120000){engine.load(profile)}
            assertEquals("smoke",engine.state.value.loadedId)
            var pieces=0;var output=""
            withTimeout(120000){
                engine.generate(listOf("system" to "You are a helpful assistant.","user" to "Say hello in one short sentence."),parameters).collect{pieces++;output=it}
            }
            assertTrue("Expected streamed native text",output.isNotBlank());assertTrue(pieces>1)
            var cancelPieces=0
            withTimeout(120000){
                engine.generate(listOf("user" to "Count to one hundred, one number at a time."),parameters).collect{
                    cancelPieces++;engine.cancel()
                }
            }
            assertTrue(cancelPieces in 1..12)
            val grammar=context.assets.open("actions.gbnf").bufferedReader().use{it.readText()}
            var json=""
            withTimeout(120000){
                engine.generate(listOf("system" to "Return a JSON object only.","user" to "Return exactly this JSON: {\"reply\":\"hello\"}"),parameters.copy(maxTokens=128),grammar).collect{json=it}
            }
            assertTrue(org.json.JSONObject(json).has("reply"))
            assertTrue("An oversized current message must fail instead of overrunning context",runCatching {
                withTimeout(30000){engine.generate(listOf("user" to "hello ".repeat(5000)),parameters).collect()}
            }.isFailure)
            val automation=androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().uiAutomation
            fun shell(command:String)=android.os.ParcelFileDescriptor.AutoCloseInputStream(automation.executeShellCommand(command))
                .bufferedReader().use{it.readText().trim()}
            val pid=shell("pidof "+context.packageName+":inference")
            assertTrue("Expected the separate inference process",pid.matches(Regex("[0-9]+")))
            shell("run-as "+context.packageName+" kill -9 "+pid)
            withTimeout(15000){while(engine.state.value.error==null)delay(50)}
            assertNull(engine.state.value.loadedId)
            withTimeout(120000){engine.load(profile)}
            assertEquals("smoke",engine.state.value.loadedId)
            engine.unload();assertNull(engine.state.value.loadedId)
        }finally{engine.close()}
    }
}
