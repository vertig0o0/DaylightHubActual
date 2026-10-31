package dev.daylight.hub.reading

import android.os.SystemClock
import androidx.room.withTransaction
import dev.daylight.hub.data.HubDatabase
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.time.*

object ReadingClock {
    const val IDLE_MS=120000L
    fun credit(previous:Long,now:Long,lastInteraction:Long):Long =
        (minOf(now,lastInteraction+IDLE_MS)-previous).coerceIn(0,15000)
}

/** Foreground reader only; crashed sessions end at their last committed checkpoint. */
class ReadingTracker(private val db:HubDatabase,
    private val elapsed:()->Long={SystemClock.elapsedRealtime()},
    private val wall:()->Long={System.currentTimeMillis()},
    private val timeZone:()->ZoneId={ZoneId.systemDefault()}) {
    private val mutex=Mutex()
    private var session:ReadingSession?=null
    private var tick=0L
    @Volatile private var interaction=0L
    suspend fun recover()=mutex.withLock { db.withTransaction {
        db.reading().activeSession()?.let{db.reading().updateSession(it.copy(endedAt=it.lastCheckpointAt,activeSlot=null,revision=it.revision+1))}
    } }
    fun interacted(){interaction=elapsed()}
    suspend fun start(book:Book)=mutex.withLock {
        if(session?.bookId==book.id)return@withLock
        finishNow()
        val now=wall();val zone=timeZone()
        val next=ReadingSession(bookId=book.id,title=book.title,startedAt=now,lastCheckpointAt=now,localDate=Instant.ofEpochMilli(now).atZone(zone).toLocalDate().toString(),zoneId=zone.id)
        session=next.copy(id=db.reading().insertSession(next));tick=elapsed();interaction=tick
    }
    suspend fun checkpoint()=mutex.withLock { checkpointNow() }
    private suspend fun checkpointNow() {
        val old=session ?: return
        val monotonic=elapsed();val credit=ReadingClock.credit(tick,monotonic,interaction);tick=monotonic
        val now=wall();val zone=timeZone();val date=Instant.ofEpochMilli(now).atZone(zone).toLocalDate().toString()
        if(date!=old.localDate || zone.id!=old.zoneId) {
            // At most one five-second checkpoint crosses a boundary. Split at local midnight.
            val startOfDay=LocalDate.parse(date).atStartOfDay(zone).toInstant().toEpochMilli()
            val currentCredit=if(zone.id==old.zoneId)(now-startOfDay).coerceIn(0,credit) else credit
            db.withTransaction {
                db.reading().updateSession(old.copy(creditedMs=old.creditedMs+credit-currentCredit,endedAt=now-currentCredit,lastCheckpointAt=now-currentCredit,activeSlot=null,revision=old.revision+1))
                val next=ReadingSession(bookId=old.bookId,title=old.title,startedAt=now-currentCredit,lastCheckpointAt=now,creditedMs=currentCredit,localDate=date,zoneId=zone.id)
                session=next.copy(id=db.reading().insertSession(next))
            }
        } else {
            val next=old.copy(creditedMs=old.creditedMs+credit,lastCheckpointAt=now,revision=old.revision+1)
            db.reading().updateSession(next);session=next
        }
    }
    suspend fun finish()=mutex.withLock { finishNow() }
    private suspend fun finishNow() {
        checkpointNow()
        session?.let{db.reading().updateSession(it.copy(endedAt=it.lastCheckpointAt,activeSlot=null,revision=it.revision+1))}
        session=null
    }
}
