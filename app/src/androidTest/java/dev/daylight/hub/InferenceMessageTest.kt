package dev.daylight.hub

import android.os.Bundle
import android.os.Message
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.daylight.hub.ai.*
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class InferenceMessageTest {
    @Test fun queuedLoadSurvivesAndroidRecyclingItsMessage() {
        val message=Message.obtain().apply {
            what=InferenceProtocol.LOAD;arg1=41
            data=Bundle().apply {
                putString("file","chosen.gguf");putString("parameters","{\"contextSize\":2048}")
                putStringArray("roles",arrayOf("user"));putStringArray("contents",arrayOf("Original prompt"))
            }
        }
        // Delay the worker until after the handler's message has been returned to the pool.
        val request=message.snapshotInferenceRequest()
        val queuedWork={request}
        message.recycle()
        val reused=Message.obtain().apply{what=InferenceProtocol.GENERATE;arg1=99;data=Bundle()}
        try {
            val received=queuedWork()
            assertEquals(InferenceProtocol.LOAD,received.kind);assertEquals(41,received.id)
            assertEquals("chosen.gguf",received.file)
            assertEquals("{\"contextSize\":2048}",received.parameters)
            assertEquals(listOf("Original prompt"),received.contents)
        }finally{reused.recycle()}
    }
}
