@file:OptIn(com.mohamedrejeb.richeditor.annotation.ExperimentalRichTextApi::class)
package dev.daylight.hub

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.json.*
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RichDocumentTest {
    @Test fun formattingColorsHighlightsAndLinksRoundTrip() {
        val input="""{"v":1,"blocks":[{"id":"b0","type":"paragraph","text":"Hello 👋 link","spans":[{"k":"bold","r":[0,4]},{"k":"italic","r":[0,4]},{"k":"color","r":[0,4],"argb":"FF2563EB"},{"k":"highlight","r":[0,4],"argb":"FFFEF08A"},{"k":"link","r":[9,12],"url":"daylighthub://note/7"}]}]}"""
        val a=RichTextState().setJson(input)
        val serialized=a.toJson()
        val b=RichTextState().setJson(serialized)
        assertEquals(a.toText(),b.toText());assertEquals(serialized,b.toJson())
        assertTrue(serialized.contains("bold"));assertTrue(serialized.contains("highlight"));assertTrue(serialized.contains("daylighthub://note/7"))
    }
}
