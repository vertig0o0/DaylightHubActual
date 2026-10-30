package dev.daylight.hub.calendar

import dev.daylight.hub.data.*
import org.dmfs.rfc5545.DateTime
import org.dmfs.rfc5545.recur.RecurrenceRule
import java.time.*
import java.util.TimeZone

data class Occurrence(val event: CalendarEvent, val originalStart: Long, val startAt: Long, val endAt: Long)
object RecurrenceEngine {
    fun between(event: CalendarEvent, exceptions: List<EventException>, from: Long, until: Long): List<Occurrence> {
        val zone = ZoneId.of(event.zoneId)
        val exceptionMap = exceptions.filter { it.eventId == event.id }.associateBy { it.originalStart }
        val out = mutableListOf<Occurrence>()
        fun add(start: Long) {
            val exception = exceptionMap[start]
            if(exception?.cancelled == true) return
            val replacement = exception?.replacementJson?.takeIf { it.isNotBlank() }?.let { eventFromJson(org.json.JSONObject(it)) }
            val duration = event.endAt - event.startAt
            val end = if(event.allDay) {
                val days = java.time.temporal.ChronoUnit.DAYS.between(
                    Instant.ofEpochMilli(event.startAt).atZone(zone).toLocalDate(),
                    Instant.ofEpochMilli(event.endAt).atZone(zone).toLocalDate()).coerceAtLeast(1)
                Instant.ofEpochMilli(start).atZone(zone).toLocalDate().plusDays(days).atStartOfDay(zone).toInstant().toEpochMilli()
            } else start + duration
            val item = Occurrence(replacement ?: event, start, replacement?.startAt ?: start, replacement?.endAt ?: end)
            if(item.startAt < until && item.endAt > from) out.add(item)
        }
        if(event.recurrence.isBlank()) add(event.startAt) else {
            val iterator = RecurrenceRule(event.recurrence).iterator(DateTime(TimeZone.getTimeZone(event.zoneId), event.startAt))
            iterator.fastForward(maxOf(event.startAt, from - (event.endAt - event.startAt)))
            var iterations=0
            while(iterator.hasNext() && iterations++ < 100000) {
                val start=iterator.nextMillis()
                if(event.repeatUntil != null && start > event.repeatUntil) break
                if(start >= until) break
                add(start)
            }
        }
        // Moved occurrences can enter this window from outside the original range.
        exceptionMap.values.filter { !it.cancelled && it.replacementJson.isNotBlank() }.forEach { ex ->
            if(out.none { it.originalStart == ex.originalStart }) {
                val replacement=eventFromJson(org.json.JSONObject(ex.replacementJson))
                if(replacement.startAt < until && replacement.endAt > from)
                    out.add(Occurrence(replacement,ex.originalStart,replacement.startAt,replacement.endAt))
            }
        }
        return out.sortedBy { it.startAt }
    }
    fun reminderAt(item: Occurrence): Long? {
        val minutes=item.event.reminderMinutes ?: return null
        val reference=if(item.event.allDay) Instant.ofEpochMilli(item.startAt).atZone(ZoneId.of(item.event.zoneId))
            .toLocalDate().atTime(9,0).atZone(ZoneId.of(item.event.zoneId)).toInstant().toEpochMilli() else item.startAt
        return reference - minutes.toLong()*60000L
    }
}
