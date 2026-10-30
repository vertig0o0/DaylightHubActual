package dev.daylight.hub

import dev.daylight.hub.calendar.*
import dev.daylight.hub.data.*
import java.time.*
import org.junit.Assert.*
import org.junit.Test

class RecurrenceTest {
    private fun time(s:String)=ZonedDateTime.parse(s).toInstant().toEpochMilli()
    private fun event(start:String,rule:String="")=CalendarEvent(id=1,title="Test",startAt=time(start),endAt=time(start)+3600000,zoneId=ZonedDateTime.parse(start).zone.id,recurrence=rule)
    @Test fun monthlySkipsMissingDay() {
        val e=event("2026-01-31T09:00:00+03:00[Europe/Istanbul]","FREQ=MONTHLY")
        val items=RecurrenceEngine.between(e,emptyList(),e.startAt,time("2026-04-01T00:00:00+03:00[Europe/Istanbul]"))
        assertEquals(listOf(1,3),items.map{Instant.ofEpochMilli(it.startAt).atZone(ZoneId.of(e.zoneId)).monthValue})
    }
    @Test fun dailyPreservesLocalHourAcrossDst() {
        val e=event("2026-03-07T09:00:00-05:00[America/New_York]","FREQ=DAILY")
        val items=RecurrenceEngine.between(e,emptyList(),e.startAt,e.startAt+3*86400000)
        assertEquals(listOf(9,9,9,9),items.map{Instant.ofEpochMilli(it.startAt).atZone(ZoneId.of(e.zoneId)).hour})
        assertEquals(23*3600000L,items[1].startAt-items[0].startAt)
    }
    @Test fun cancelledAndMovedOccurrencesUseOriginalIdentity() {
        val e=event("2026-10-30T09:00:00+03:00[Europe/Istanbul]","FREQ=DAILY")
        val moved=e.copy(startAt=e.startAt+5*86400000,endAt=e.endAt+5*86400000)
        val exceptions=listOf(EventException(1,e.startAt,true),EventException(1,e.startAt+86400000,false,moved.toJson().toString()))
        val items=RecurrenceEngine.between(e,exceptions,e.startAt+5*86400000,e.startAt+6*86400000)
        assertTrue(items.any{it.originalStart==e.startAt+86400000 && it.startAt==moved.startAt})
        assertTrue(RecurrenceEngine.between(e,exceptions,e.startAt,e.startAt+86400000).isEmpty())
    }
    @Test fun allDayReminderUsesNineLocalAndThirtyMinuteSteps() {
        val e=event("2026-10-30T00:00:00+03:00[Europe/Istanbul]").copy(allDay=true,endAt=time("2026-10-31T00:00:00+03:00[Europe/Istanbul]"),reminderMinutes=90)
        assertEquals(time("2026-10-30T07:30:00+03:00[Europe/Istanbul]"),RecurrenceEngine.reminderAt(Occurrence(e,e.startAt,e.startAt,e.endAt)))
        assertTrue(runCatching{validateEvent(e.copy(reminderMinutes=15))}.isFailure)
    }
    @Test fun repeatEndIsInclusiveAndOverlapIsIncluded() {
        val e=event("2026-10-30T09:00:00+03:00[Europe/Istanbul]","FREQ=DAILY")
        val items=RecurrenceEngine.between(e.copy(repeatUntil=e.startAt+86400000),emptyList(),e.startAt+1800000,e.startAt+4*86400000)
        assertEquals(2,items.size)
    }
}
