package dev.daylight.hub.data

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.flow.Flow

@Dao interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY updatedAt DESC") fun observeAll(): Flow<List<Note>>
    @Query("SELECT * FROM notes ORDER BY updatedAt DESC") suspend fun all(): List<Note>
    @Query("SELECT * FROM notes WHERE id = :id") suspend fun get(id: Long): Note?
    @Query("SELECT notes.* FROM notes JOIN notes_fts ON notes.id = notes_fts.rowid WHERE notes_fts MATCH :query LIMIT 50") suspend fun search(query:String):List<Note>
    @Insert suspend fun insert(note: Note): Long
    @Update suspend fun update(note: Note)
    @Delete suspend fun delete(note: Note)
    @Query("DELETE FROM note_links WHERE sourceId = :id") suspend fun clearLinks(id: Long)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun link(link: NoteLink)
    @Query("SELECT notes.* FROM notes INNER JOIN note_links ON notes.id = note_links.sourceId WHERE note_links.targetId = :id")
    fun backlinks(id: Long): Flow<List<Note>>
}
@Dao interface PlannerDao {
    @Query("SELECT * FROM planner_items ORDER BY isDone ASC, dueAt ASC") fun observeAll(): Flow<List<PlannerItem>>
    @Query("SELECT * FROM planner_items ORDER BY dueAt ASC") suspend fun all(): List<PlannerItem>
    @Query("SELECT * FROM planner_items WHERE id = :id") suspend fun get(id: Long): PlannerItem?
    @Insert suspend fun insert(item: PlannerItem): Long
    @Update suspend fun update(item: PlannerItem)
    @Delete suspend fun delete(item: PlannerItem)
}
@Dao interface CalendarDao {
    @Query("SELECT * FROM calendar_events ORDER BY startAt") fun observeAll(): Flow<List<CalendarEvent>>
    @Query("SELECT * FROM calendar_events ORDER BY startAt") suspend fun all(): List<CalendarEvent>
    @Query("SELECT * FROM calendar_events WHERE id = :id") suspend fun get(id: Long): CalendarEvent?
    @Insert suspend fun insert(event: CalendarEvent): Long
    @Update suspend fun update(event: CalendarEvent)
    @Delete suspend fun delete(event: CalendarEvent)
    @Query("SELECT * FROM event_exceptions") fun observeExceptions(): Flow<List<EventException>>
    @Query("SELECT * FROM event_exceptions WHERE eventId = :id") suspend fun exceptions(id: Long): List<EventException>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun putException(exception: EventException)
    @Query("DELETE FROM event_exceptions WHERE eventId = :id") suspend fun clearExceptions(id: Long)
}
@Dao interface ModelDao {
    @Query("SELECT * FROM model_profiles ORDER BY name") fun observeAll(): Flow<List<ModelProfile>>
    @Query("SELECT * FROM model_profiles WHERE id = :id") suspend fun get(id: String): ModelProfile?
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun put(profile: ModelProfile)
    @Delete suspend fun delete(profile: ModelProfile)
}
@Database(entities = [Note::class, PlannerItem::class, NoteLink::class, CalendarEvent::class, EventException::class, ModelProfile::class,NoteSearch::class], version = 2, exportSchema = true)
abstract class HubDatabase : RoomDatabase() {
    abstract fun notes(): NoteDao
    abstract fun planner(): PlannerDao
    abstract fun calendar(): CalendarDao
    abstract fun models(): ModelDao
    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE notes ADD COLUMN document TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE notes ADD COLUMN notebook TEXT NOT NULL DEFAULT 'Inbox'")
                db.execSQL("ALTER TABLE notes ADD COLUMN tags TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE notes ADD COLUMN revision INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE planner_items ADD COLUMN revision INTEGER NOT NULL DEFAULT 0")
                db.execSQL("CREATE TABLE IF NOT EXISTS note_links (sourceId INTEGER NOT NULL, targetId INTEGER NOT NULL, PRIMARY KEY(sourceId, targetId))")
                db.execSQL("CREATE TABLE IF NOT EXISTS calendar_events (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, title TEXT NOT NULL, description TEXT NOT NULL, startAt INTEGER NOT NULL, endAt INTEGER NOT NULL, zoneId TEXT NOT NULL, allDay INTEGER NOT NULL, color INTEGER NOT NULL, recurrence TEXT NOT NULL, repeatUntil INTEGER, reminderMinutes INTEGER, reminderMode TEXT NOT NULL, revision INTEGER NOT NULL)")
                db.execSQL("CREATE TABLE IF NOT EXISTS event_exceptions (eventId INTEGER NOT NULL, originalStart INTEGER NOT NULL, cancelled INTEGER NOT NULL, replacementJson TEXT NOT NULL, PRIMARY KEY(eventId, originalStart))")
                db.execSQL("CREATE TABLE IF NOT EXISTS model_profiles (id TEXT NOT NULL PRIMARY KEY, name TEXT NOT NULL, fileName TEXT NOT NULL, bytes INTEGER NOT NULL, metadata TEXT NOT NULL, parameters TEXT NOT NULL)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_notes_notebook ON notes(notebook)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_note_links_targetId ON note_links(targetId)")
                db.execSQL("CREATE VIRTUAL TABLE IF NOT EXISTS `notes_fts` USING FTS4(`title` TEXT NOT NULL, `body` TEXT NOT NULL, `notebook` TEXT NOT NULL, `tags` TEXT NOT NULL, content=`notes`)")
                db.execSQL("INSERT INTO notes_fts(notes_fts) VALUES('rebuild')")
            }
        }
        @Volatile private var instance: HubDatabase? = null
        fun get(context: Context): HubDatabase = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(context.applicationContext, HubDatabase::class.java, "daylight-hub.db")
                .addMigrations(MIGRATION_1_2).build().also { instance = it }
        }
    }
}
