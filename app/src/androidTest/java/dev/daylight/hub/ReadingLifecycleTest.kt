package dev.daylight.hub

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.daylight.hub.data.HubDatabase
import dev.daylight.hub.reading.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.time.*

@RunWith(AndroidJUnit4::class)
class ReadingLifecycleTest {
    @Test fun idleBackgroundAndCrashRecoveryNeverCreditAbsentTime()=runBlocking {
        val context=ApplicationProvider.getApplicationContext<Context>()
        val db=Room.inMemoryDatabaseBuilder(context,HubDatabase::class.java).build()
        var monotonic=1000L;var wall=1700000000000L
        fun tracker()=ReadingTracker(db,{monotonic},{wall},{ZoneId.of("UTC")})
        try {
            val sample=Book(sha256="lifecycle",title="Lifecycle",originalName="book.epub",originalUri="",format="EPUB",originalFormat="EPUB",fileName="",bytes=1)
            val book=sample.copy(id=db.reading().insert(sample))
            val first=tracker();first.start(book)
            repeat(30){monotonic+=5000;wall+=5000;first.checkpoint()}
            assertEquals(120000L,db.reading().activeSession()!!.creditedMs)
            first.interacted();monotonic+=5000;wall-=3600000;first.checkpoint()
            assertEquals(125000L,db.reading().activeSession()!!.creditedMs)
            first.finish();monotonic+=300000;wall+=300000
            assertNull(db.reading().activeSession())
            val second=tracker();second.start(book);monotonic+=5000;second.checkpoint()
            val persisted=db.reading().activeSession()!!
            // A new process/reboot has a new tracker and can only retain committed credit.
            monotonic=100;wall+=900000;tracker().recover()
            assertNull(db.reading().activeSession())
            val history=db.reading().sessions().first()
            assertEquals(130000L,history.sumOf{it.creditedMs})
            assertEquals(persisted.lastCheckpointAt,history.single{it.id==persisted.id}.endedAt)
        }finally{db.close()}
    }

    @Test fun aMidnightCheckpointSplitsCreditByLocalDate()=runBlocking {
        val context=ApplicationProvider.getApplicationContext<Context>()
        val db=Room.inMemoryDatabaseBuilder(context,HubDatabase::class.java).build()
        val zone=ZoneId.of("Europe/Berlin")
        var wall=ZonedDateTime.of(2026,10,24,23,59,58,0,zone).toInstant().toEpochMilli();var monotonic=1000L
        try {
            val sample=Book(sha256="midnight",title="Midnight",originalName="book.epub",originalUri="",format="EPUB",originalFormat="EPUB",fileName="",bytes=1)
            val book=sample.copy(id=db.reading().insert(sample));val tracker=ReadingTracker(db,{monotonic},{wall},{zone})
            tracker.start(book);wall+=5000;monotonic+=5000;tracker.checkpoint();tracker.finish()
            val totals=db.reading().dailyTime("2026-10-01").first().associate{it.localDate to it.milliseconds}
            assertEquals(2000L,totals["2026-10-24"]);assertEquals(3000L,totals["2026-10-25"])
        }finally{db.close()}
    }
}
