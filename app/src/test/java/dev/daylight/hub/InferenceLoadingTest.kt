package dev.daylight.hub

import dev.daylight.hub.ai.*
import dev.daylight.hub.data.ModelProfile
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class InferenceLoadingTest {
    private class FakeTransport:InferenceTransport {
        override val replies=Channel<InferenceReply>(Channel.UNLIMITED)
        override var onDisconnect:((String)->Unit)?=null
        val requests=mutableListOf<InferenceRequest>()
        val cancellations=mutableListOf<Int>()
        var resets=0
        var sent:(InferenceRequest)->Unit={}
        override suspend fun send(request:InferenceRequest){requests.add(request);sent(request)}
        override fun cancel(id:Int){cancellations.add(id)}
        override fun reset(){
            resets++;onDisconnect?.invoke("Process restarted")
            replies.trySend(InferenceReply(requests.last().id,InferenceProtocol.ERROR,"Old process stopped"))
        }
        override fun close(){}
        fun respond(kind:Int,text:String="",percent:Int = -1,id:Int=requests.last().id){
            replies.trySend(InferenceReply(id,kind,text,percent))
        }
    }
    private fun profile(id:String="test")=ModelProfile(id,id,id+".gguf",100,"GGUF",GenerationParameters().json())
    @Test fun reportsStagesAndDoesNotTreatProgressAsCompletion()=runTest {
        val transport=FakeTransport();val engine=InferenceEngine(transport,clockMillis={testScheduler.currentTime})
        val loading=async{engine.load(profile())};runCurrent()
        transport.respond(InferenceProtocol.PROGRESS,"Mapping model weights",42);runCurrent()
        assertTrue(engine.state.value.loading);assertTrue(engine.state.value.busy)
        assertNull(engine.state.value.loadedId);assertEquals(42,engine.state.value.progress)
        advanceTimeBy(1100);runCurrent();assertEquals(1L,engine.state.value.elapsedSeconds)
        transport.respond(InferenceProtocol.PROGRESS,"Allocating context",-1);runCurrent()
        assertNull(engine.state.value.progress)
        transport.respond(InferenceProtocol.LOADED,"Ready");loading.await()
        assertEquals("test",engine.state.value.loadedId);assertFalse(engine.state.value.busy)
    }
    @Test fun unresponsiveLoadTimesOutAndCanRetryWithoutOldRepliesWinning()=runTest {
        val transport=FakeTransport();val engine=InferenceEngine(transport,loadTimeoutMillis=1500,clockMillis={testScheduler.currentTime})
        val loading=async{runCatching{engine.load(profile())}};runCurrent()
        transport.respond(InferenceProtocol.PROGRESS,"Allocating context");runCurrent()
        advanceTimeBy(1500);runCurrent()
        assertTrue(loading.await().isFailure);assertEquals(1,transport.resets)
        assertFalse(engine.state.value.busy);assertTrue(engine.state.value.error!!.contains("Allocating context"))
        transport.sent={request->
            transport.respond(InferenceProtocol.LOADED,"Stale load",id=1)
            transport.respond(InferenceProtocol.LOADED,"New load",id=request.id)
        }
        engine.load(profile("retry"))
        assertEquals("retry",engine.state.value.loadedId);assertEquals("New load",engine.state.value.description)
    }
    @Test fun cancellingHungLoadRestartsOnlyInferenceAndReleasesBusyState()=runTest {
        val transport=FakeTransport();val engine=InferenceEngine(transport,loadTimeoutMillis=10000,cancelGraceMillis=500,clockMillis={testScheduler.currentTime})
        val loading=async{engine.load(profile())};runCurrent()
        engine.cancel();assertTrue(engine.state.value.cancelling)
        advanceTimeBy(2000);runCurrent();loading.await()
        assertEquals(listOf(1),transport.cancellations);assertEquals(1,transport.resets)
        assertFalse(engine.state.value.busy);assertNull(engine.state.value.loadedId)
        assertTrue(engine.state.value.description.contains("cancelled"))
    }
    @Test fun nativeFailureAndProcessDeathNeverLeaveLoadingStuck()=runTest {
        val transport=FakeTransport();val engine=InferenceEngine(transport)
        transport.sent={transport.respond(InferenceProtocol.ERROR,"Unsupported model architecture")}
        assertTrue(runCatching{engine.load(profile())}.isFailure)
        assertFalse(engine.state.value.loading);assertFalse(engine.state.value.busy)
        assertEquals("Unsupported model architecture",engine.state.value.error)
        transport.sent={transport.respond(InferenceProtocol.LOADED,"Ready")}
        engine.load(profile())
        transport.onDisconnect?.invoke("Inference process stopped")
        assertNull(engine.state.value.loadedId);assertEquals("Inference process stopped",engine.state.value.error)
    }
    @Test fun cancellationAcknowledgementAndOldGenerationRepliesAreSafe()=runTest {
        val transport=FakeTransport();val engine=InferenceEngine(transport)
        val loading=async{engine.load(profile())};runCurrent()
        engine.cancel();transport.respond(InferenceProtocol.CANCELLED);loading.await()
        assertFalse(engine.state.value.busy);assertEquals(0,transport.resets)
        transport.sent={request->transport.respond(InferenceProtocol.DONE,"Old reply",id=1);transport.respond(InferenceProtocol.LOADED,"Ready",id=request.id)}
        engine.load(profile())
        transport.sent={transport.respond(InferenceProtocol.CANCELLED)}
        assertTrue(engine.generate(listOf("user" to "Hello"),GenerationParameters()).toList().isEmpty())
        assertFalse(engine.state.value.busy)
    }
}
