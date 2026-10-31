package dev.daylight.hub

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.daylight.hub.data.*
import dev.daylight.hub.focus.FocusRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FocusLifecycleTest {
    @Test fun pauseProcessRecoveryClockChangesAndStaleCommandsPreserveCredit()=runBlocking {
        val context=ApplicationProvider.getApplicationContext<Context>();val db=Room.inMemoryDatabaseBuilder(context,HubDatabase::class.java).build()
        var elapsed=1000L;var wall=100000L;var boot=5
        fun repository()=FocusRepository(context,db,{elapsed},{boot},{wall},{})
        try {
            val task=HubRepository(db).saveTask(PlannerItem(title="Focus lifecycle"))
            val start=repository().start(task,null,1)
            elapsed+=12000;wall-=3600000
            val paused=repository().command(start.id,start.revision,"PAUSE")!!
            assertEquals(12000L,paused.creditedMs);assertEquals(48000L,paused.remainingMs)
            elapsed+=30000
            val stale=repository().command(start.id,start.revision,"RESUME")!!;assertEquals("PAUSED",stale.status)
            val resumed=repository().command(paused.id,paused.revision,"RESUME")!!
            elapsed+=10000
            val checkpoint=repository().command(resumed.id,resumed.revision,"CHECKPOINT")!!
            assertEquals(22000L,checkpoint.creditedMs);assertEquals(resumed.revision,checkpoint.revision)
            // A new repository models process death; same-boot elapsed time keeps advancing.
            elapsed+=50000;repository().reconcile()
            assertNull(db.focus().current())
            db.openHelper.readableDatabase.query("SELECT creditedMs,status FROM focus_sessions WHERE id=${start.id}").use{it.moveToFirst();assertEquals(60000L,it.getLong(0));assertEquals("COMPLETED",it.getString(1))}
            assertNull(repository().command(start.id,start.revision,"END"))
        }finally{db.close()}
    }
    @Test fun rebootPausesAtLastCheckpointAndHandoffsRemainIndependent()=runBlocking {
        val context=ApplicationProvider.getApplicationContext<Context>();val db=Room.inMemoryDatabaseBuilder(context,HubDatabase::class.java).build()
        var elapsed=1000L;var boot=1
        fun repository()=FocusRepository(context,db,{elapsed},{boot},{100000L},{})
        try {
            val hub=HubRepository(db);val task=hub.saveTask(PlannerItem(title="Before reboot"));val note=hub.saveNote(Note(title="Note",body="Body"))
            val session=repository().start(task,null,1)
            elapsed+=8000;repository().command(session.id,session.revision,"CHECKPOINT")
            boot++;elapsed=100
            repository().reconcile(rebooted=true)
            val recovered=db.focus().current()!!;assertEquals("PAUSED",recovered.status);assertEquals(8000L,recovered.creditedMs);assertEquals(52000L,recovered.remainingMs)
            repository().handoff(task,null,"Open the draft.");repository().handoff(null,null,"Continue the second paragraph.",note)
            assertEquals("Open the draft.",db.focus().marker("task:$task")!!.nextStep)
            assertEquals(note,db.focus().marker("note:$note")!!.noteId)
            repository().command(recovered.id,recovered.revision,"END")
            hub.deleteTask(task,0)
            assertEquals("BREAK",repository().start(task,null,5,"BREAK").kind)
        }finally{db.close()}
    }
}
