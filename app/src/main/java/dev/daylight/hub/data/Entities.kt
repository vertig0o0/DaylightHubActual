package dev.daylight.hub.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes", indices = [androidx.room.Index("notebook")])
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val body: String,
    val updatedAt: Long = System.currentTimeMillis(),
    @androidx.room.ColumnInfo(defaultValue = "''") val document: String = "",
    @androidx.room.ColumnInfo(defaultValue = "'Inbox'") val notebook: String = "Inbox",
    @androidx.room.ColumnInfo(defaultValue = "''") val tags: String = "",
    @androidx.room.ColumnInfo(defaultValue = "0") val revision: Long = 0
)

@Entity(tableName = "planner_items")
data class PlannerItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val details: String = "",
    val dueAt: Long,
    val isDone: Boolean = false,
    @androidx.room.ColumnInfo(defaultValue = "0") val revision: Long = 0
)

@Entity(tableName = "note_links", primaryKeys = ["sourceId", "targetId"], indices = [androidx.room.Index("targetId")])
data class NoteLink(val sourceId: Long, val targetId: Long)

@Entity(tableName = "calendar_events")
data class CalendarEvent(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String = "",
    val startAt: Long,
    val endAt: Long,
    val zoneId: String = java.time.ZoneId.systemDefault().id,
    val allDay: Boolean = false,
    val color: Long = 0xFF52796F,
    val recurrence: String = "",
    val repeatUntil: Long? = null,
    val reminderMinutes: Int? = 30,
    val reminderMode: String = "NOTIFICATION",
    val revision: Long = 0
)

@Entity(tableName = "event_exceptions", primaryKeys = ["eventId", "originalStart"])
data class EventException(val eventId: Long, val originalStart: Long, val cancelled: Boolean = false, val replacementJson: String = "")

@Entity(tableName = "model_profiles")
data class ModelProfile(@PrimaryKey val id: String, val name: String, val fileName: String, val bytes: Long,
    val metadata: String, val parameters: String = "{}")

@androidx.room.Fts4(contentEntity = Note::class)
@Entity(tableName = "notes_fts")
data class NoteSearch(val title:String,val body:String,val notebook:String,val tags:String)
