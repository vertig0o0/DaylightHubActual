package dev.daylight.hub.focus

import android.content.Context
import android.os.SystemClock
import android.provider.Settings
import androidx.room.withTransaction
import dev.daylight.hub.data.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class FocusRepository(private val context:Context,private val db:HubDatabase,
    private val elapsed:()->Long={SystemClock.elapsedRealtime()},
    private val bootCount:()->Int={Settings.Global.getInt(context.contentResolver,Settings.Global.BOOT_COUNT,0)},
    private val wallTime:()->Long={System.currentTimeMillis()},
    private val notify:(FocusSession?)->Unit={FocusNotifications(context).refresh(it)}) {
    companion object { private val mutations=Mutex() }
    private fun boot()=bootCount()
    suspend fun start(taskId:Long?,projectId:Long?,minutes:Int,kind:String="FOCUS"):FocusSession = mutations.withLock { db.withTransaction {
        require(minutes in 1..180) { "Choose a duration from 1 to 180 minutes." }
        require(kind in listOf("FOCUS","BREAK"))
        check(db.focus().current()==null) { "End or resume the current session first." }
        val task=taskId?.let { db.planner().get(it)?.takeIf { item->item.deletedAt==null || kind=="BREAK" } ?: if(kind=="BREAK")null else error("Task is unavailable.") }
        val project=(task?.projectId ?: projectId)?.let { db.workspace().project(it) ?: if(kind=="BREAK")null else error("Project is unavailable.") }
        require(task!=null || project!=null || kind=="BREAK") { "Choose a task or project." }
        val now=elapsed();val duration=minutes*60000L
        val session=FocusSession(title=task?.title ?: project?.name ?: "Break",taskId=task?.id ?: taskId.takeIf{kind=="BREAK"},projectId=project?.id ?: projectId.takeIf{kind=="BREAK"},
            durationMs=duration,remainingMs=duration,sinceElapsed=now,checkpointElapsed=now,bootCount=boot(),kind=kind,startedAt=wallTime())
        session.copy(id=db.focus().insert(session))
    }.also { notify(it) } }

    suspend fun command(id:Long,revision:Long,command:String):FocusSession? = mutations.withLock {
        val next=db.withTransaction {
            val old=db.focus().current() ?: return@withTransaction null
            if(old.id!=id || old.revision!=revision) return@withTransaction old
            require(command in listOf("PAUSE","RESUME","END","EXPIRE","CHECKPOINT","RECOVER"))
            val now=elapsed()
            val rebooted=old.bootCount!=boot() || command=="RECOVER"
            val reading=FocusClock.read(old.remainingMs,old.creditedMs,old.sinceElapsed,if(rebooted) old.checkpointElapsed else now)
            val end=command=="END" || reading.expired
            val running=!end && !rebooted && (command=="RESUME" || old.status=="RUNNING" && command!="PAUSE")
            if(command=="EXPIRE" && !reading.expired && !rebooted) return@withTransaction old
            val updated=old.copy(remainingMs=reading.remainingMs,creditedMs=reading.creditedMs,
                sinceElapsed=if(running) now else null,checkpointElapsed=now,bootCount=boot(),
                status=if(end) if(reading.expired) "COMPLETED" else "ENDED" else if(running) "RUNNING" else "PAUSED",
                activeSlot=if(end) null else 1,endedAt=if(end) wallTime() else null,
                revision=if(command=="CHECKPOINT" && !end && !rebooted) old.revision else old.revision+1)
            db.focus().update(updated);updated
        }
        if(command!="CHECKPOINT" || next?.activeSlot==null) notify(next)
        next
    }

    suspend fun reconcile(rebooted:Boolean=false) {
        val current=db.focus().current() ?: return
        command(current.id,current.revision,if(rebooted) "RECOVER" else "EXPIRE")
    }

    suspend fun handoff(taskId:Long?,projectId:Long?,text:String,noteId:Long?=null) = db.withTransaction {
        require(text.isNotBlank() && text.length<=1000) { "Write a next step of up to 1,000 characters." }
        require(taskId!=null || projectId!=null || noteId!=null) { "Choose a task, project, or note." }
        val key=taskId?.let { "task:$it" } ?: noteId?.let{"note:$it"} ?: "project:$projectId"
        val old=db.focus().marker(key)
        val next=ResumeMarker(id=old?.id ?: 0,contextKey=key,taskId=taskId,projectId=projectId,noteId=noteId,nextStep=text.trim())
        if(old==null) db.focus().insertMarker(next) else db.focus().updateMarker(next)
    }
}
