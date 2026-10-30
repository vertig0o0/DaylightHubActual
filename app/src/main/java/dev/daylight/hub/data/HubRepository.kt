package dev.daylight.hub.data

import androidx.room.withTransaction
import org.json.JSONObject
import java.time.ZoneId

/** Shared write path. AI calls can reach this only through ApprovalGateway. */
class HubRepository(val db: HubDatabase, private val changed: () -> Unit = {}) {
    suspend fun saveModelParameters(expected: ModelProfile, parameters: String) = db.withTransaction {
        check(db.models().get(expected.id) == expected) { "Model profile changed. Review again." }
        db.models().put(expected.copy(parameters = parameters))
    }
    suspend fun saveNote(note: Note, expected: Long? = null): Long = db.withTransaction {
        val old = if (note.id != 0L) db.notes().get(note.id) ?: error("This note was deleted.") else null
        if (expected != null) check(old?.revision == expected) { "This note changed. Review a fresh proposal." }
        val saved = note.copy(updatedAt = System.currentTimeMillis(), revision = (old?.revision ?: -1) + 1)
        val id = if (old == null) db.notes().insert(saved) else { db.notes().update(saved); saved.id }
        db.notes().clearLinks(id)
        Regex("daylighthub://note/(\\d+)").findAll(saved.document).mapNotNull { it.groupValues[1].toLongOrNull() }
            .distinct().forEach { db.notes().link(NoteLink(id, it)) }
        id
    }
    suspend fun deleteNote(id: Long, expected: Long) = db.withTransaction {
        val old = db.notes().get(id) ?: error("Note no longer exists.")
        check(old.revision == expected) { "Note changed. Review it again." }
        db.notes().clearLinks(id); db.notes().delete(old)
    }
    suspend fun saveTask(task: PlannerItem, expected: Long? = null): Long = db.withTransaction {
        val old = if (task.id != 0L) db.planner().get(task.id) ?: error("Task was deleted.") else null
        if (expected != null) check(old?.revision == expected) { "Task changed. Review it again." }
        val saved = task.copy(revision = (old?.revision ?: -1) + 1)
        if (old == null) db.planner().insert(saved) else { db.planner().update(saved); saved.id }
    }
    suspend fun deleteTask(id: Long, expected: Long) = db.withTransaction {
        val old = db.planner().get(id) ?: error("Task was deleted.")
        check(old.revision == expected) { "Task changed. Review it again." }; db.planner().delete(old)
    }
    suspend fun saveEvent(event: CalendarEvent, expected: Long? = null): Long {
        validateEvent(event)
        val id = db.withTransaction {
            val old = if (event.id != 0L) db.calendar().get(event.id) ?: error("Event was deleted.") else null
            if (expected != null) check(old?.revision == expected) { "Event changed. Review it again." }
            val saved = event.copy(revision = (old?.revision ?: -1) + 1)
            if (old == null) db.calendar().insert(saved) else { db.calendar().update(saved); saved.id }
        }
        changed(); return id
    }
    suspend fun deleteEvent(id: Long, expected: Long) {
        db.withTransaction {
            val old = db.calendar().get(id) ?: error("Event was deleted.")
            check(old.revision == expected) { "Event changed. Review it again." }
            db.calendar().clearExceptions(id); db.calendar().delete(old)
        }; changed()
    }
    suspend fun saveOccurrence(eventId: Long, original: Long, replacement: CalendarEvent?, expected: Long) {
        replacement?.let(::validateEvent)
        db.withTransaction {
            val event = db.calendar().get(eventId) ?: error("Event was deleted.")
            check(event.revision == expected) { "Event changed. Review it again." }
            require(event.recurrence.isNotBlank()) { "This event does not repeat." }
            require(dev.daylight.hub.calendar.RecurrenceEngine.between(event,emptyList(),original,original+1).any { it.originalStart==original }) { "This occurrence is not part of the event series." }
            db.calendar().putException(EventException(eventId, original, replacement == null, replacement?.toJson()?.toString() ?: ""))
            db.calendar().update(event.copy(revision = event.revision + 1))
        }; changed()
    }
}
fun validateEvent(event: CalendarEvent) {
    require(event.title.isNotBlank()) { "Add an event title." }
    require(event.endAt > event.startAt) { "End must be after start." }
    ZoneId.of(event.zoneId)
    require(event.recurrence in listOf("", "FREQ=DAILY", "FREQ=WEEKLY", "FREQ=MONTHLY")) { "Unsupported repeat rule." }
    require(event.repeatUntil == null || event.repeatUntil >= event.startAt) { "Repeat end is before the first event." }
    require(event.reminderMinutes == null || event.reminderMinutes >= 0 && event.reminderMinutes % 30 == 0) { "Choose a reminder in 30-minute increments." }
    require(event.reminderMode in listOf("NOTIFICATION", "ALARM")) { "Choose notification or ringing alarm." }
}
fun CalendarEvent.toJson() = JSONObject().put("id", id).put("title", title).put("description", description)
    .put("startAt", startAt).put("endAt", endAt).put("zoneId", zoneId).put("allDay", allDay).put("color", color)
    .put("recurrence", recurrence).put("repeatUntil", repeatUntil ?: JSONObject.NULL)
    .put("reminderMinutes", reminderMinutes ?: JSONObject.NULL).put("reminderMode", reminderMode).put("revision", revision)
fun eventFromJson(j: JSONObject) = CalendarEvent(j.optLong("id"), j.getString("title"), j.optString("description"),
    j.getLong("startAt"), j.getLong("endAt"), j.getString("zoneId"), j.optBoolean("allDay"), j.optLong("color", 0xFF52796F),
    j.optString("recurrence"), if(j.isNull("repeatUntil")) null else j.getLong("repeatUntil"),
    if(j.isNull("reminderMinutes")) null else j.getInt("reminderMinutes"), j.getString("reminderMode"), j.optLong("revision"))
