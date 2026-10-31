package dev.daylight.hub

import dev.daylight.hub.reading.*
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.ByteArrayInputStream
import java.util.zip.ZipFile

class BookFilesTest {
    @Test fun htmlConversionPreservesStructureAndDeclaredEncodingWithoutScripts() {
        val source=java.io.File.createTempFile("html-structure",".html");val destination=java.io.File.createTempFile("html-structure",".epub")
        try {
            source.writeBytes("<html><head><meta charset=windows-1254><title>Türkçe</title></head><body><h1>Başlık</h1><ul><li><strong>İlk</strong> adım</li></ul><script>unsafe()</script><img src=https://example.invalid/p.png alt=Illustration></body></html>".toByteArray(charset("windows-1254")))
            dev.daylight.hub.reading.BookFiles.htmlToEpub(source,destination,"Fallback")
            java.util.zip.ZipFile(destination).use{zip->
                val text=zip.getInputStream(zip.getEntry("section-0.xhtml")).reader().readText()
                assertTrue(text.contains("Başlık"));assertTrue(text.contains("<strong>İlk</strong>"));assertTrue(text.contains("<ul>"))
                assertFalse(text.contains("unsafe()"));assertFalse(text.contains("<img"));assertFalse(text.contains("example.invalid"))
            }
        }finally{source.delete();destination.delete()}
    }
    @get:Rule val temporary=TemporaryFolder()
    @Test fun boundedStreamEnforcesActualBytesAndInputStreamContract() {
        BookFiles.boundedStream(ByteArrayInputStream(byteArrayOf(1,2,3,4)),3).use{input->
            assertEquals(0,input.read(ByteArray(0),0,0));assertEquals(1,input.read());assertEquals(2,input.skip(99))
            assertEquals(0,input.skip(-4));assertFalse(input.markSupported());assertTrue(runCatching{input.read()}.isFailure)
        }
        BookFiles.boundedStream(ByteArrayInputStream(byteArrayOf(1,2,3)),3).use{input->
            val buffer=ByteArray(8);assertEquals(3,input.read(buffer));assertEquals(-1,input.read(buffer));assertEquals(-1,input.read())
        }
    }
    @Test fun archivePathsRejectTraversalAndPlatformSeparators() {
        listOf("../book","a/../../book","/book","C:\\book","a\\b").forEach{assertTrue(it,runCatching{BookFiles.requireSafeEntry(it)}.isFailure)}
        BookFiles.requireSafeEntry("OEBPS/Images/cover image.png")
    }
    @Test fun utf16TextBecomesBoundedUnicodeEpubChapters() {
        val text="A <chapter> & 👋\n".repeat(13000)
        val source=temporary.newFile("book.txt").apply{writeBytes(byteArrayOf(0xff.toByte(),0xfe.toByte())+text.toByteArray(Charsets.UTF_16LE))}
        val destination=temporary.newFile("book.epub")
        BookFiles.textToEpub(source,destination,"Unicode & text")
        assertEquals("UTF-16LE",BookFiles.textEncoding(source))
        assertEquals("EPUB",BookFiles.inspect(destination,"book.epub",null).format)
        ZipFile(destination).use{zip->
            val chapters=zip.entries().toList().filter{it.name.startsWith("section-")}
            assertTrue(chapters.size>2)
            val all=chapters.joinToString(""){zip.getInputStream(it).reader().readText()}
            assertTrue(all.contains("&lt;chapter&gt; &amp; 👋"));assertFalse(all.contains('\uFFFD'));assertFalse(all.contains('\uFEFF'))
            assertTrue(chapters.all{it.size<300000})
        }
    }
    @Test fun conversionChecksCancellationWhileStreaming() {
        val source=temporary.newFile("cancel.txt").apply{writeText("x".repeat(100000))}
        var checks=0
        assertTrue(runCatching{BookFiles.textToEpub(source,temporary.newFile("cancel.epub"),"Cancel",checkCancelled={checks++;if(checks>=3)error("cancelled")})}.isFailure)
        assertEquals(3,checks)
    }
    @Test fun locatorsRoundTripAndRejectInvalidProgress() {
        val expected=BookPosition(5,locator="epubcfi(/6/2!/4/2:40)",fraction=.45,quote="Example")
        assertEquals(expected.copy(updatedAt=0),BookPosition.parse(5,expected.json()).copy(updatedAt=0))
        listOf(expected.copy(fraction=Double.NaN),expected.copy(fraction=1.01),expected.copy(zoom=20f),expected.copy(pageIndex=-1)).forEach{assertTrue(runCatching{it.validate()}.isFailure)}
    }
    @Test fun readingTimeUsesMonotonicIntervalsAndIdleCap() {
        assertEquals(5000,ReadingClock.credit(10000,15000,10000))
        assertEquals(0,ReadingClock.credit(10000,5000,10000))
        assertEquals(2000,ReadingClock.credit(128000,135000,10000))
        assertEquals(0,ReadingClock.credit(140000,145000,10000))
        assertEquals(15000,ReadingClock.credit(0,99999,99999))
    }
    @Test fun comicFilenamesUseNumericPageOrderingWithoutIntegerOverflow() {
        val names=listOf("page10.png","page2.png","page1.png","page9999999999999999999.png")
        assertEquals(listOf("page1.png","page2.png","page10.png","page9999999999999999999.png"),names.sortedWith(::naturalCompare))
    }
}
