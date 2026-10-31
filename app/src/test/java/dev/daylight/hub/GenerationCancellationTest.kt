package dev.daylight.hub

import dev.daylight.hub.ai.*
import dev.daylight.hub.data.ModelProfile
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GenerationCancellationTest {
    private class Backend(val acknowledge:Boolean):InferenceTransport {
        override val replies=Channel<InferenceReply>(Channel.UNLIMITED)
        override var onDisconnect:((String)->Unit)?=null
        var last:InferenceRequest?=null;var cancelled=0;var resets=0
        override suspend fun send(request:InferenceRequest) { last=request;if(request.kind==InferenceProtocol.LOAD) replies.send(InferenceReply(request.id,InferenceProtocol.LOADED,"Ready")) }
        override fun cancel(id:Int) { cancelled=id;if(acknowledge) replies.trySend(InferenceReply(id,InferenceProtocol.CANCELLED)) }
        override fun reset() { resets++ }
        override fun close() {}
    }
    @Test fun cancellingAFlowCancelsOnlyItsNativeRequestAndKeepsTheLoadedModel()=runTest {
        val backend=Backend(true);val engine=InferenceEngine(backend,cancelGraceMillis=100)
        engine.load(ModelProfile("test","Test","test.gguf",1,"",GenerationParameters().json()))
        val job=launch { engine.generate(listOf("user" to "short task"),GenerationParameters(),inputTokenLimit=512).collect() }
        runCurrent();assertEquals(512,backend.last!!.inputTokenLimit)
        val id=backend.last!!.id;job.cancelAndJoin()
        assertEquals(id,backend.cancelled);assertEquals(0,backend.resets)
        assertFalse(engine.state.value.busy);assertEquals("test",engine.state.value.loadedId)
    }
    @Test fun unresponsiveCancellationRestartsOnlyTheInferenceProcess()=runTest {
        val backend=Backend(false);val engine=InferenceEngine(backend,cancelGraceMillis=100)
        engine.load(ModelProfile("test","Test","test.gguf",1,"",GenerationParameters().json()))
        val job=launch { engine.generate(listOf("user" to "short task"),GenerationParameters()).collect() }
        runCurrent();job.cancel();advanceTimeBy(101);runCurrent();job.join()
        assertEquals(1,backend.resets);assertNull(engine.state.value.loadedId);assertFalse(engine.state.value.busy)
    }
}
