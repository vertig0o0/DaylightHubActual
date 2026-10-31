package dev.daylight.hub

import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.daylight.hub.reading.*
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.util.UUID
import java.util.concurrent.CopyOnWriteArrayList

@RunWith(AndroidJUnit4::class)
class ReaderCorpusTest {
    private fun await(events:List<ReaderEvent>,predicate:()->Boolean) {
        val end=android.os.SystemClock.elapsedRealtime()+45000
        while(android.os.SystemClock.elapsedRealtime()<end){
            if(events.any{it is ReaderEvent.Error})fail(events.filterIsInstance<ReaderEvent.Error>().joinToString())
            if(predicate())return
            Thread.sleep(25)
        }
        fail("Corpus reader timed out: ${events.takeLast(5)}")
    }
    private fun verify(name:String) {
        val instrumentation=InstrumentationRegistry.getInstrumentation();val context=instrumentation.targetContext
        val file=File(context.cacheDir,"corpus-${UUID.randomUUID()}-$name")
        var asset:ReflowAsset?=null
        try {
            instrumentation.context.assets.open("books/$name").use{input->file.outputStream().use(input::copyTo)}
            val info=BookFiles.inspect(file,name,null)
            val book=Book(id=8501,sha256="corpus",title=info.title,originalName=name,originalUri="",format=info.format,originalFormat=info.format,fileName="",bytes=file.length())
            asset=ReflowAsset(book,file);val events=CopyOnWriteArrayList<ReaderEvent>()
            ActivityScenario.launch(MainActivity::class.java).use{scenario->
                var reader:ReflowReader?=null
                try {
                    scenario.onActivity{activity->reader=ReflowReader(activity,asset!!,null,ReaderAppearance(reducedMotion=true),events::add);activity.setContentView(reader)}
                    await(events){events.any{it is ReaderEvent.Opened} && events.any{it is ReaderEvent.Location}}
                    val opened=events.filterIsInstance<ReaderEvent.Opened>().last()
                    assertTrue("Metadata: ${opened.title}",opened.title.contains("Alice",true))
                    assertTrue("Chapter navigation",opened.contents.size>=10)
                    val before=events.filterIsInstance<ReaderEvent.Location>().size
                    scenario.onActivity{reader!!.command("goTo",locator=opened.contents[2].locator)}
                    await(events){events.filterIsInstance<ReaderEvent.Location>().size>before}
                    assertTrue(events.filterIsInstance<ReaderEvent.Location>().last().position.locator.isNotEmpty())
                    scenario.onActivity{reader!!.command("search",query="White Rabbit")}
                    await(events){events.any{it is ReaderEvent.Search}}
                    assertTrue(events.filterIsInstance<ReaderEvent.Search>().last().results.isNotEmpty())
                }finally{scenario.onActivity{reader?.release()}}
            }
        }finally{asset?.close();file.delete()}
    }
    @Test fun publicDomainEpub3OpensWithMetadataContentsAndSearch()=verify("alice.epub")
    @Test fun publicDomainMobiOpensWithMetadataContentsAndSearch()=verify("alice.mobi")
    @Test fun publicDomainAzw3OpensWithMetadataContentsAndSearch()=verify("alice.azw3")
}
