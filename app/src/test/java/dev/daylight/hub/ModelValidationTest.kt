package dev.daylight.hub

import dev.daylight.hub.ai.*
import java.io.*
import org.junit.Assert.*
import org.junit.Test

class ModelValidationTest {
    @Test fun invalidGgufIsRejectedBeforeLoading() {
        val file=File.createTempFile("invalid",".gguf")
        try { file.writeText("not a model");assertTrue(runCatching{GgufInspector.inspect(file)}.isFailure) }finally{file.delete()}
    }
    @Test fun defaultsRoundTripAndNonFiniteParametersAreRejected() {
        val p=GenerationParameters();assertEquals(p,GenerationParameters.parse(p.json()))
        assertTrue(runCatching{p.copy(temperature=Float.NaN).validate()}.isFailure)
        assertTrue(runCatching{p.copy(maxTokens=p.contextSize).validate()}.isFailure)
        assertTrue(runCatching{p.copy(seed=-2).validate()}.isFailure)
    }
    @Test fun partialRepliesDoNotExposeToolJson() {
        assertEquals("",partialReply("""{"tool":"delete_note","args":{"id":1}}"""))
        assertEquals("Hello\nworld",partialReply("""{"reply":"Hello\nworld"""))
    }
}
