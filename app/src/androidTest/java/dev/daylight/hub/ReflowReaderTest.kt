package dev.daylight.hub

import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.daylight.hub.reading.*
import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.util.UUID
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicReference
import java.util.zip.*

@RunWith(AndroidJUnit4::class)
class ReflowReaderTest {
    private val context=ApplicationProvider.getApplicationContext<Context>()
    private fun await(timeout:Long=30000,predicate:()->Boolean) {
        val end=android.os.SystemClock.elapsedRealtime()+timeout
        while(android.os.SystemClock.elapsedRealtime()<end){if(predicate())return;Thread.sleep(25)}
        assertTrue("Reader condition timed out",predicate())
    }
    @Test fun epubOpensSearchesAndRestoresLogicalLocationAcrossFontChanges() {
        val text=File(context.cacheDir,"reflow-${UUID.randomUUID()}.txt")
        val epub=File(context.cacheDir,"reflow-${UUID.randomUUID()}.epub")
        val events=CopyOnWriteArrayList<ReaderEvent>()
        var asset:ReflowAsset?=null
        try {
            text.writeText((1..1800).joinToString("\n"){"Paragraph $it. This is a stable reading-location fixture with enough words for many pages."}+"\nNeedleAtDocumentEnd")
            BookFiles.textToEpub(text,epub,"Reflow acceptance")
            val book=Book(id=7001,sha256="fixture",title="Reflow acceptance",originalName="fixture.epub",originalUri="",format="EPUB",originalFormat="EPUB",fileName="",bytes=epub.length())
            asset=ReflowAsset(book,epub)
            ActivityScenario.launch(MainActivity::class.java).use{scenario->
                var reader:ReflowReader?=null
                scenario.onActivity{activity->reader=ReflowReader(activity,asset!!,null,ReaderAppearance(reducedMotion=true),events::add);activity.setContentView(reader)}
                await{events.any{it is ReaderEvent.Opened} || events.any{it is ReaderEvent.Error}}
                assertEquals(events.filterIsInstance<ReaderEvent.Error>().joinToString(),0,events.filterIsInstance<ReaderEvent.Error>().size)
                val diagnostics=AtomicReference<String?>(null)
                scenario.onActivity{reader!!.evaluateJavascript("JSON.stringify((function(){var v=document.querySelector('foliate-view');var r=v&&v.renderer;var rect=r&&r.getBoundingClientRect();return {width:innerWidth,height:innerHeight,rect:rect&&{width:rect.width,height:rect.height},size:r&&r.size,pages:r&&r.pages,viewSize:r&&r.viewSize,last:!!(v&&v.lastLocation),contents:r&&r.getContents().length}})())"){diagnostics.set(it)}}
                await{diagnostics.get()!=null}
                try { await{events.any{it is ReaderEvent.Location} || events.any{it is ReaderEvent.Error}} }
                catch(error:AssertionError){throw AssertionError("${error.message}; events=$events; layout=${diagnostics.get()}",error)}
                assertTrue(events.toString(),events.none{it is ReaderEvent.Error})
                val initial=events.filterIsInstance<ReaderEvent.Location>().last().position
                repeat(4){index->val before=events.filterIsInstance<ReaderEvent.Location>().size;scenario.onActivity{reader!!.command("next")};await{events.filterIsInstance<ReaderEvent.Location>().size>before};assertTrue("Turn $index",events.filterIsInstance<ReaderEvent.Location>().last().position.locator.isNotBlank())}
                val beforeFont=events.filterIsInstance<ReaderEvent.Location>().last().position
                assertNotEquals(initial.locator,beforeFont.locator);assertTrue(beforeFont.fraction>initial.fraction)
                val count=events.filterIsInstance<ReaderEvent.Location>().size
                scenario.onActivity{reader!!.appearance(ReaderAppearance(fontSize=28,lineHeight=1.9f,reducedMotion=true))}
                await{events.filterIsInstance<ReaderEvent.Location>().size>count}
                val afterFont=events.filterIsInstance<ReaderEvent.Location>().last().position
                assertEquals("Font changes must keep the logical position",beforeFont.fraction,afterFont.fraction,.02)
                scenario.onActivity{reader!!.command("search",query="NeedleAtDocumentEnd")}
                await{events.any{it is ReaderEvent.Search} || events.any{it is ReaderEvent.Error}}
                assertTrue(events.filterIsInstance<ReaderEvent.Search>().last().results.any{it.excerpt.contains("NeedleAtDocumentEnd")})
                events.clear()
                scenario.onActivity{activity->reader!!.release();reader=ReflowReader(activity,asset!!,beforeFont,ReaderAppearance(fontSize=22,reducedMotion=true),events::add);activity.setContentView(reader)}
                await{events.any{it is ReaderEvent.Opened} && events.any{it is ReaderEvent.Location} || events.any{it is ReaderEvent.Error}}
                assertTrue(events.filterIsInstance<ReaderEvent.Error>().isEmpty())
                assertEquals(beforeFont.fraction,events.filterIsInstance<ReaderEvent.Location>().last().position.fraction,.02)
                scenario.onActivity{reader!!.release()}
            }
        }finally{asset?.close();text.delete();epub.delete()}
    }
    @Test fun publicationScriptsCannotExecuteInTheTrustedFrame() {
        val source=File(context.cacheDir,"sandbox-${UUID.randomUUID()}.epub")
        var asset:ReflowAsset?=null
        try {
            ZipOutputStream(source.outputStream()).use{zip->
                fun entry(name:String,text:String){zip.putNextEntry(ZipEntry(name));zip.write(text.toByteArray());zip.closeEntry()}
                entry("META-INF/container.xml","""<container xmlns="urn:oasis:names:tc:opendocument:xmlns:container"><rootfiles><rootfile full-path="book.opf" media-type="application/oebps-package+xml"/></rootfiles></container>""")
                entry("book.opf","""<package xmlns="http://www.idpf.org/2007/opf" version="3.0" unique-identifier="id"><metadata xmlns:dc="http://purl.org/dc/elements/1.1/"><dc:identifier id="id">sandbox</dc:identifier><dc:title>Sandbox</dc:title><dc:language>en</dc:language></metadata><manifest><item id="s1" href="page.xhtml" media-type="application/xhtml+xml" properties="scripted"/></manifest><spine><itemref idref="s1"/></spine></package>""")
                entry("page.xhtml","""<html xmlns="http://www.w3.org/1999/xhtml"><head><title>Fixture</title><script>window.top.document.title='UNSAFE_SCRIPT_EXECUTED';</script></head><body><p>Readable content remains available.</p></body></html>""")
            }
            val book=Book(id=7002,sha256="sandbox",title="Sandbox",originalName="sandbox.epub",originalUri="",format="EPUB",originalFormat="EPUB",fileName="",bytes=source.length())
            asset=ReflowAsset(book,source);val events=CopyOnWriteArrayList<ReaderEvent>()
            ActivityScenario.launch(MainActivity::class.java).use{scenario->
                var reader:ReflowReader?=null
                scenario.onActivity{activity->reader=ReflowReader(activity,asset!!,null,ReaderAppearance(),events::add);activity.setContentView(reader)}
                await{events.any{it is ReaderEvent.Opened} || events.any{it is ReaderEvent.Error}}
                assertTrue(events.filterIsInstance<ReaderEvent.Error>().joinToString(),events.filterIsInstance<ReaderEvent.Error>().isEmpty())
                val title=AtomicReference<String?>(null)
                scenario.onActivity{reader!!.evaluateJavascript("document.title"){title.set(it)}}
                await{title.get()!=null};assertFalse(title.get()!!.contains("UNSAFE_SCRIPT_EXECUTED"))
                scenario.onActivity{assertFalse(reader!!.settings.allowContentAccess);assertFalse(reader!!.settings.allowFileAccess);reader!!.release()}
            }
        }finally{asset?.close();source.delete()}
    }
    @Test fun fixedLayoutEpubKeepsPageDesignDuringNavigationZoomAndReopen() {
        val source=File(context.cacheDir,"fixed-${UUID.randomUUID()}.epub");var asset:ReflowAsset?=null
        try {
            ZipOutputStream(source.outputStream()).use{zip->
                fun entry(name:String,text:String){zip.putNextEntry(ZipEntry(name));zip.write(text.toByteArray());zip.closeEntry()}
                entry("META-INF/container.xml","""<container xmlns="urn:oasis:names:tc:opendocument:xmlns:container"><rootfiles><rootfile full-path="book.opf" media-type="application/oebps-package+xml"/></rootfiles></container>""")
                entry("book.opf","""<package xmlns="http://www.idpf.org/2007/opf" version="3.0" unique-identifier="id" prefix="rendition: http://www.idpf.org/vocab/rendition/#"><metadata xmlns:dc="http://purl.org/dc/elements/1.1/"><dc:identifier id="id">fixed</dc:identifier><dc:title>Fixed pages</dc:title><dc:language>en</dc:language><meta property="rendition:layout">pre-paginated</meta><meta property="rendition:spread">none</meta></metadata><manifest><item id="a" href="a.xhtml" media-type="application/xhtml+xml"/><item id="b" href="b.xhtml" media-type="application/xhtml+xml"/></manifest><spine><itemref idref="a"/><itemref idref="b"/></spine></package>""")
                listOf("a","b").forEach{page->entry("$page.xhtml","""<html xmlns="http://www.w3.org/1999/xhtml"><head><meta name="viewport" content="width=600,height=800"/><title>Page $page</title></head><body style="margin:0;width:600px;height:800px;background:#fff"><h1>Fixed page $page</h1><p>Page design stays at its original size.</p></body></html>""")}
            }
            val book=Book(id=7003,sha256="fixed",title="Fixed pages",originalName="fixed.epub",originalUri="",format="EPUB",originalFormat="EPUB",fileName="",bytes=source.length())
            asset=ReflowAsset(book,source);val events=CopyOnWriteArrayList<ReaderEvent>()
            ActivityScenario.launch(MainActivity::class.java).use{scenario->
                var reader:ReflowReader?=null
                scenario.onActivity{activity->reader=ReflowReader(activity,asset!!,null,ReaderAppearance(reducedMotion=true),events::add);activity.setContentView(reader)}
                await{events.any{it is ReaderEvent.Error} || events.any{it is ReaderEvent.Opened} && events.any{it is ReaderEvent.Location}}
                assertTrue(events.toString(),events.none{it is ReaderEvent.Error})
                assertTrue(events.filterIsInstance<ReaderEvent.Opened>().single().fixedLayout)
                val initial=events.filterIsInstance<ReaderEvent.Location>().last().position
                scenario.onActivity{reader!!.command("next")}
                await{events.filterIsInstance<ReaderEvent.Location>().any{it.position.section==1}}
                val next=events.filterIsInstance<ReaderEvent.Location>().last().position
                assertTrue(next.fraction>initial.fraction);assertEquals(1.0,next.fraction,0.0)
                scenario.onActivity{reader!!.appearance(ReaderAppearance(fontSize=32,fixedZoom="1.5"))}
                val result=AtomicReference<String?>(null)
                scenario.onActivity{reader!!.evaluateJavascript("JSON.stringify((()=>{const r=document.querySelector('foliate-view').renderer;return {zoom:r.getAttribute('zoom'),display:getComputedStyle(r).display}})())"){result.set(it)}}
                await{result.get()!=null};assertTrue(result.get()!!,result.get()!!.contains("flex"))
                events.clear()
                scenario.onActivity{activity->reader!!.release();reader=ReflowReader(activity,asset!!,next,ReaderAppearance(),events::add);activity.setContentView(reader)}
                await{events.any{it is ReaderEvent.Error} || events.any{it is ReaderEvent.Opened} && events.any{it is ReaderEvent.Location}}
                assertTrue(events.toString(),events.none{it is ReaderEvent.Error});assertEquals(1,events.filterIsInstance<ReaderEvent.Location>().last().position.section)
                scenario.onActivity{reader!!.release()}
            }
        }finally{asset?.close();source.delete()}
    }
    @Test fun fb2ReadsUtf8AndDeclaredLegacyEncodingOffline() {
        listOf("UTF-8","windows-1251").forEachIndexed{index,encoding->
            val source=File(context.cacheDir,"fb2-${UUID.randomUUID()}.fb2");var asset:ReflowAsset?=null
            try {
                val xml="""<?xml version="1.0" encoding="$encoding"?><FictionBook xmlns="http://www.gribuser.ru/xml/fictionbook/2.0"><description><title-info><genre>prose</genre><author><first-name>Reader</first-name><last-name>Fixture</last-name></author><book-title>FB2 encoding acceptance</book-title><lang>ru</lang></title-info></description><body><section id="one"><title><p>Chapter one</p></title><p>Привет мир. This offline chapter contains a searchable encoding fixture.</p></section></body></FictionBook>"""
                source.writeBytes(xml.toByteArray(charset(encoding)))
                val book=Book(id=7030L+index,sha256="fb2-$index",title="FB2 encoding acceptance",originalName="fixture.fb2",originalUri="",format="FB2",originalFormat="FB2",fileName="",bytes=source.length())
                asset=ReflowAsset(book,source);val events=CopyOnWriteArrayList<ReaderEvent>()
                ActivityScenario.launch(MainActivity::class.java).use{scenario->
                    var reader:ReflowReader?=null
                    scenario.onActivity{activity->reader=ReflowReader(activity,asset!!,null,ReaderAppearance(reducedMotion=true),events::add);activity.setContentView(reader)}
                    await{events.any{it is ReaderEvent.Error} || events.any{it is ReaderEvent.Opened} && events.any{it is ReaderEvent.Location}}
                    assertTrue("$encoding: $events",events.none{it is ReaderEvent.Error})
                    scenario.onActivity{reader!!.command("search",query="Привет")}
                    await{events.any{it is ReaderEvent.Search} || events.any{it is ReaderEvent.Error}}
                    assertTrue("$encoding: $events",events.filterIsInstance<ReaderEvent.Search>().last().results.any{it.excerpt.contains("Привет")})
                    scenario.onActivity{reader!!.release()}
                }
            }finally{asset?.close();source.delete()}
        }
    }
}
