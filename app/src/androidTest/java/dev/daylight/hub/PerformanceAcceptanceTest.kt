package dev.daylight.hub

import android.app.ActivityManager
import android.content.Context
import android.os.*
import android.view.FrameMetrics
import android.view.Window
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagingSource
import androidx.room.Room
import androidx.room.withTransaction
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import dev.daylight.hub.ai.*
import dev.daylight.hub.data.*
import dev.daylight.hub.reading.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import org.junit.Assert.*
import org.junit.Assume
import org.junit.Rule
import org.junit.Test
import java.io.File
import java.util.Collections
import java.util.UUID

/** Emulator diagnostics, not a substitute for measurements on the target ARM64 phone. */
class PerformanceAcceptanceTest {
    @get:Rule val compose=createAndroidComposeRule<MainActivity>()
    private val context=ApplicationProvider.getApplicationContext<Context>()

    @Test fun largeLibrariesAndLongHistoriesRemainPaged()=runBlocking {
        val db=Room.inMemoryDatabaseBuilder(context,HubDatabase::class.java).build()
        try {
            val start=SystemClock.elapsedRealtime()
            db.withTransaction {
                repeat(10000){index->db.reading().insert(Book(sha256="scale-$index",title="Book ${index.toString().padStart(5,'0')}",originalName="book.epub",originalUri="",format="EPUB",originalFormat="EPUB",fileName="",bytes=1))}
                repeat(1000){index->db.notes().insert(Note(title="Note $index",body="Bounded body. ".repeat(512)))}
                repeat(1000){index->db.reading().insertSession(ReadingSession(bookId=null,title="History $index",startedAt=index.toLong(),lastCheckpointAt=index.toLong(),endedAt=index.toLong(),activeSlot=null,creditedMs=5000,localDate="2026-10-31",zoneId="UTC"))}
                db.reading().insert(Book(sha256="unicode",title="Çalışma [literal]*_?",originalName="book.epub",originalUri="",format="EPUB",originalFormat="EPUB",fileName="",bytes=1))
            }
            val seeded=SystemClock.elapsedRealtime()
            val first=db.reading().library("","*","","","TITLE").load(PagingSource.LoadParams.Refresh(null,32,false)) as PagingSource.LoadResult.Page
            val next=db.reading().library("","*","","","TITLE").load(PagingSource.LoadParams.Append(first.nextKey!!,32,false)) as PagingSource.LoadResult.Page
            assertEquals(32,first.data.size);assertEquals(32,next.data.size)
            assertTrue(first.data.map{it.id}.intersect(next.data.map{it.id}.toSet()).isEmpty())
            val notes=db.notes().page("","*","",false).load(PagingSource.LoadParams.Refresh(null,32,false)) as PagingSource.LoadResult.Page
            assertEquals(32,notes.data.size);assertTrue(notes.data.all{it.preview.length<=180})
            val history=db.reading().sessionHistory().load(PagingSource.LoadParams.Refresh(null,40,false)) as PagingSource.LoadResult.Page
            assertEquals(40,history.data.size);assertNotNull(history.nextKey)
            val query="çalışma [literal]*_?"
            val match=db.reading().library(query,CaseInsensitiveGlob.pattern(query),"","","TITLE").load(PagingSource.LoadParams.Refresh(null,32,false)) as PagingSource.LoadResult.Page
            assertEquals("unicode",match.data.single().sha256)
            File(context.filesDir,"v3-paging-benchmark.txt").writeText("API ${Build.VERSION.SDK_INT}; 10001 books, 1000 notes (~7 MB body text), 1000 sessions\nSeed: ${seeded-start} ms; bounded page/search checks: ${SystemClock.elapsedRealtime()-seeded} ms\nBook/note pages:32; history page:40; no duplicates between book pages; literal Unicode search passed.\n")
        }finally{db.close()}
    }

    @Test fun noteScrollingRemainsInteractiveDuring360MNativeGeneration()=runBlocking {
        val file=File(context.filesDir,"models/smoke360.gguf")
        Assume.assumeTrue("Provide the cached 360M regression model",file.isFile)
        lateinit var vm:HubViewModel
        compose.activityRule.scenario.onActivity{vm=ViewModelProvider(it)[HubViewModel::class.java];vm.section="Today"}
        val marker="Benchmark-${UUID.randomUUID()}"
        val notes=mutableListOf<Note>();val frames=Collections.synchronizedList(mutableListOf<Long>())
        val handler=HandlerThread("frame-diagnostics").apply{start()}
        val listener=Window.OnFrameMetricsAvailableListener{_,metrics,_->frames.add(metrics.getMetric(FrameMetrics.TOTAL_DURATION))}
        var listenerAttached=false
        val scope=CoroutineScope(SupervisorJob()+Dispatchers.Default);var generation:Job?=null
        val parameters=GenerationParameters(contextSize=1024,threads=2,maxTokens=256,seed=0)
        val profile=ModelProfile("benchmark-${UUID.randomUUID()}","Temporary benchmark",file.name,file.length(),GgufInspector.inspect(file),parameters.json())
        try {
            vm.db.withTransaction { repeat(200){index->val note=Note(title="$marker ${index.toString().padStart(4,'0')}",body="A bounded scrolling fixture. ".repeat(256));notes.add(note.copy(id=vm.db.notes().insert(note)))};vm.db.models().put(profile) }
            compose.onNodeWithText("Notes").performClick()
            compose.onNodeWithText("Search notes and tags").performTextReplacement(marker)
            compose.activityRule.scenario.onActivity{androidx.core.view.WindowCompat.getInsetsController(it.window,it.window.decorView).hide(androidx.core.view.WindowInsetsCompat.Type.ime())}
            compose.waitUntil(10000){var hidden=false;compose.runOnIdle{hidden=androidx.core.view.ViewCompat.getRootWindowInsets(compose.activity.window.decorView)?.isVisible(androidx.core.view.WindowInsetsCompat.Type.ime())!=true};hidden}
            compose.onNodeWithText("All notebooks").performClick()
            if(compose.onAllNodesWithText("Recently updated").fetchSemanticsNodes().isNotEmpty())compose.onNodeWithText("Recently updated").performClick()
            compose.waitUntil(10000){compose.onAllNodesWithText("$marker 0000").fetchSemanticsNodes().isNotEmpty()}
            withTimeout(90000){vm.engine.load(profile)}
            generation=scope.launch { vm.engine.generate(listOf("user" to "Write a long numbered list of one hundred practical ideas for keeping a desk tidy."),parameters).collect() }
            withTimeout(10000){while(!vm.engine.state.value.busy && generation?.isActive==true)delay(10)}
            assertTrue("Generation must overlap the UI check",vm.engine.state.value.busy)
            compose.activityRule.scenario.onActivity{it.window.addOnFrameMetricsAvailableListener(listener,Handler(handler.looper));listenerAttached=true}
            var overlapping=0
            repeat(6){if(vm.engine.state.value.busy)overlapping++;compose.onNode(hasScrollToIndexAction()).performTouchInput{swipeUp(durationMillis=350)}}
            assertTrue("At least one swipe must overlap native inference",overlapping>0)
            compose.onNodeWithText("Search notes and tags").assertExists()
            val manager=context.getSystemService(ActivityManager::class.java)
            val processes=manager.runningAppProcesses.orEmpty().filter{it.processName==context.packageName || it.processName==context.packageName+":inference"}
            val memory=processes.zip(manager.getProcessMemoryInfo(processes.map{it.pid}.toIntArray()).toList()).joinToString("\n"){(process,info)->"${process.processName}: ${info.totalPss} KB PSS"}
            val measured=synchronized(frames){frames.sorted()}
            assertTrue("Scrolling must render frames",measured.isNotEmpty())
            fun percentile(value:Double)=measured[((measured.size-1)*value).toInt()]/1000000.0
            File(context.filesDir,"v3-ui-inference-benchmark.txt").writeText("API ${Build.VERSION.SDK_INT}; x86_64 emulator; software graphics; 200 notes; SmolLM2 360M,1024 context,2 CPU threads\nNative generation overlapped $overlapping of six swipes; UI remained interactive.\nFrames:${measured.size}; p50:${percentile(.5)} ms; p95:${percentile(.95)} ms; max:${percentile(1.0)} ms\n$memory\nThese emulator diagnostics do not establish physical ARM64 frame rate, battery or memory behavior.\n")
        }finally{
            generation?.cancelAndJoin();scope.cancel();runCatching{vm.engine.unload()}
            if(listenerAttached)compose.activityRule.scenario.onActivity{it.window.removeOnFrameMetricsAvailableListener(listener)};handler.quitSafely()
            vm.db.withTransaction{notes.forEach{vm.db.notes().delete(it)};vm.db.models().delete(profile)}
        }
    }
}
