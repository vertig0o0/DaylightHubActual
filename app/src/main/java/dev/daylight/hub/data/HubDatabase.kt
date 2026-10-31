package dev.daylight.hub.data

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.flow.Flow
import dev.daylight.hub.reading.*

@Dao interface NoteDao {
    @Query("SELECT id,title,SUBSTR(body,1,180) AS preview,notebook,tags,projectId,labelId,updatedAt,revision FROM notes WHERE deletedAt IS NULL ORDER BY updatedAt DESC") fun summaries():Flow<List<NoteSummary>>
    @Query("SELECT id,title,SUBSTR(body,1,180) AS preview,notebook,tags,projectId,labelId,updatedAt,revision FROM notes WHERE deletedAt IS NOT NULL ORDER BY deletedAt DESC LIMIT 100") fun trash():Flow<List<NoteSummary>>
    @Query("SELECT id,title,SUBSTR(body,1,180) AS preview,notebook,tags,projectId,labelId,updatedAt,revision FROM notes WHERE deletedAt IS NULL AND (:notebook='' OR notebook=:notebook) AND (:query='' OR title GLOB :pattern OR body GLOB :pattern OR tags GLOB :pattern) ORDER BY CASE WHEN :byTitle THEN title END COLLATE NOCASE,updatedAt DESC,id")
    fun page(query:String,pattern:String,notebook:String,byTitle:Boolean):androidx.paging.PagingSource<Int,NoteSummary>
    @Query("SELECT * FROM notes WHERE deletedAt IS NULL ORDER BY updatedAt DESC") fun observeAll(): Flow<List<Note>>
    @Query("SELECT * FROM notes WHERE deletedAt IS NULL ORDER BY updatedAt DESC") suspend fun all(): List<Note>
    @Query("SELECT * FROM notes WHERE id = :id") suspend fun get(id: Long): Note?
    @Query("SELECT notes.* FROM notes JOIN notes_fts ON notes.id = notes_fts.rowid WHERE notes_fts MATCH :query AND notes.deletedAt IS NULL LIMIT 50") suspend fun search(query:String):List<Note>
    @Insert suspend fun insert(note: Note): Long
    @Update suspend fun update(note: Note)
    @Delete suspend fun delete(note: Note)
    @Query("DELETE FROM note_links WHERE sourceId = :id") suspend fun clearLinks(id: Long)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun link(link: NoteLink)
    @Query("SELECT notes.* FROM notes INNER JOIN note_links ON notes.id = note_links.sourceId WHERE note_links.targetId = :id AND notes.deletedAt IS NULL")
    fun backlinks(id: Long): Flow<List<Note>>
}
@Dao interface PlannerDao {
    @Query("SELECT COALESCE(MAX(sortOrder),0) FROM planner_items") suspend fun lastOrder():Long
    @Query("SELECT * FROM planner_items WHERE deletedAt IS NOT NULL ORDER BY deletedAt DESC LIMIT 100") fun trash():Flow<List<PlannerItem>>
    @Query("SELECT * FROM planner_items WHERE deletedAt IS NULL ORDER BY isDone ASC, dueAt IS NULL, dueAt ASC") fun observeAll(): Flow<List<PlannerItem>>
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
@Database(entities = [Note::class, PlannerItem::class, NoteLink::class, CalendarEvent::class, EventException::class, ModelProfile::class,NoteSearch::class,
    Project::class, TaskDependency::class, Label::class, ProjectTemplate::class, Conversation::class, ConversationMessage::class,
    FocusSession::class, ResumeMarker::class, EditorRecovery::class,Book::class,BookPosition::class,Bookmark::class,
    ReadingSession::class,ReadingStatusEvent::class,ReadingGoal::class,BookImport::class], version = 5, exportSchema = true)
abstract class HubDatabase : RoomDatabase() {
    abstract fun notes(): NoteDao
    abstract fun planner(): PlannerDao
    abstract fun calendar(): CalendarDao
    abstract fun models(): ModelDao
    abstract fun workspace(): WorkspaceDao
    abstract fun focus(): FocusDao
    abstract fun reading():ReadingDao
    companion object {
        val MIGRATION_4_5 = object:Migration(4,5) {
            override fun migrate(db:SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE books(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,sha256 TEXT NOT NULL,title TEXT NOT NULL,author TEXT NOT NULL,originalName TEXT NOT NULL,originalUri TEXT NOT NULL,format TEXT NOT NULL,originalFormat TEXT NOT NULL,fileName TEXT NOT NULL,normalizedFile TEXT,coverFile TEXT,bytes INTEGER NOT NULL,status TEXT NOT NULL,progress REAL NOT NULL,importedAt INTEGER NOT NULL,lastOpenedAt INTEGER,metadataEdited INTEGER NOT NULL,revision INTEGER NOT NULL,deletedAt INTEGER)")
                db.execSQL("CREATE UNIQUE INDEX index_books_sha256 ON books(sha256)")
                db.execSQL("CREATE INDEX index_books_status ON books(status)")
                db.execSQL("CREATE INDEX index_books_lastOpenedAt ON books(lastOpenedAt)")
                db.execSQL("CREATE TABLE book_positions(bookId INTEGER PRIMARY KEY NOT NULL,kind TEXT NOT NULL,locator TEXT NOT NULL,section INTEGER NOT NULL,pageIndex INTEGER NOT NULL,offsetX REAL NOT NULL,offsetY REAL NOT NULL,zoom REAL NOT NULL,quote TEXT NOT NULL,chapter TEXT NOT NULL,fraction REAL NOT NULL,engineVersion INTEGER NOT NULL,updatedAt INTEGER NOT NULL,FOREIGN KEY(bookId) REFERENCES books(id) ON DELETE CASCADE)")
                db.execSQL("CREATE TABLE bookmarks(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,bookId INTEGER NOT NULL,label TEXT NOT NULL,position TEXT NOT NULL,createdAt INTEGER NOT NULL,FOREIGN KEY(bookId) REFERENCES books(id) ON DELETE CASCADE)")
                db.execSQL("CREATE INDEX index_bookmarks_bookId ON bookmarks(bookId)")
                db.execSQL("CREATE TABLE reading_sessions(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,bookId INTEGER,title TEXT NOT NULL,startedAt INTEGER NOT NULL,lastCheckpointAt INTEGER NOT NULL,endedAt INTEGER,creditedMs INTEGER NOT NULL,localDate TEXT NOT NULL,zoneId TEXT NOT NULL,activeSlot INTEGER,revision INTEGER NOT NULL,FOREIGN KEY(bookId) REFERENCES books(id) ON DELETE SET NULL)")
                db.execSQL("CREATE INDEX index_reading_sessions_bookId ON reading_sessions(bookId)")
                db.execSQL("CREATE UNIQUE INDEX index_reading_sessions_activeSlot ON reading_sessions(activeSlot)")
                db.execSQL("CREATE INDEX index_reading_sessions_localDate ON reading_sessions(localDate)")
                db.execSQL("CREATE TABLE reading_status_events(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,bookId INTEGER NOT NULL,title TEXT NOT NULL,status TEXT NOT NULL,changedAt INTEGER NOT NULL)")
                db.execSQL("CREATE INDEX index_reading_status_events_bookId ON reading_status_events(bookId)")
                db.execSQL("CREATE TABLE reading_goals(period TEXT PRIMARY KEY NOT NULL,minutes INTEGER NOT NULL,enabled INTEGER NOT NULL)")
                db.execSQL("CREATE TABLE book_imports(id TEXT PRIMARY KEY NOT NULL,uri TEXT NOT NULL,originalName TEXT NOT NULL,stage TEXT NOT NULL,bytesCopied INTEGER NOT NULL,expectedBytes INTEGER,message TEXT NOT NULL,updatedAt INTEGER NOT NULL)")
            }
        }
        val MIGRATION_3_4 = object:Migration(3,4) {
            override fun migrate(db:SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE focus_sessions(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,title TEXT NOT NULL,taskId INTEGER,projectId INTEGER,durationMs INTEGER NOT NULL,remainingMs INTEGER NOT NULL,creditedMs INTEGER NOT NULL,sinceElapsed INTEGER,checkpointElapsed INTEGER NOT NULL,bootCount INTEGER NOT NULL,status TEXT NOT NULL,kind TEXT NOT NULL,activeSlot INTEGER,startedAt INTEGER NOT NULL,endedAt INTEGER,revision INTEGER NOT NULL)")
                db.execSQL("CREATE UNIQUE INDEX index_focus_sessions_activeSlot ON focus_sessions(activeSlot)")
                db.execSQL("CREATE INDEX index_focus_sessions_projectId ON focus_sessions(projectId)")
                db.execSQL("CREATE INDEX index_focus_sessions_taskId ON focus_sessions(taskId)")
                db.execSQL("CREATE TABLE resume_markers(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,contextKey TEXT NOT NULL,taskId INTEGER,projectId INTEGER,noteId INTEGER,nextStep TEXT NOT NULL,updatedAt INTEGER NOT NULL)")
                db.execSQL("CREATE UNIQUE INDEX index_resume_markers_contextKey ON resume_markers(contextKey)")
                db.execSQL("CREATE TABLE editor_recovery(draftKey TEXT NOT NULL PRIMARY KEY,baseRevision INTEGER NOT NULL,title TEXT NOT NULL,notebook TEXT NOT NULL,tags TEXT NOT NULL,document TEXT NOT NULL,projectId INTEGER,labelId INTEGER,updatedAt INTEGER NOT NULL)")
            }
        }
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                val taskSequence=db.query("SELECT seq FROM sqlite_sequence WHERE name='planner_items'").use{if(it.moveToFirst())it.getLong(0) else 0L}
                db.execSQL("ALTER TABLE notes ADD COLUMN projectId INTEGER")
                db.execSQL("ALTER TABLE notes ADD COLUMN labelId INTEGER")
                db.execSQL("ALTER TABLE notes ADD COLUMN deletedAt INTEGER")
                db.execSQL("CREATE TABLE planner_items_v3 (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, title TEXT NOT NULL, details TEXT NOT NULL, dueAt INTEGER, isDone INTEGER NOT NULL, revision INTEGER NOT NULL DEFAULT 0, projectId INTEGER, parentId INTEGER, priority INTEGER NOT NULL DEFAULT 1, estimateMinutes INTEGER, workState TEXT NOT NULL DEFAULT 'TODO', manualBlocker TEXT NOT NULL DEFAULT '', sortOrder INTEGER NOT NULL DEFAULT 0, labelId INTEGER, deletedAt INTEGER)")
                db.execSQL("INSERT INTO planner_items_v3(id,title,details,dueAt,isDone,revision) SELECT id,title,details,dueAt,isDone,revision FROM planner_items")
                db.execSQL("DROP TABLE planner_items")
                db.execSQL("ALTER TABLE planner_items_v3 RENAME TO planner_items")
                db.execSQL("INSERT INTO sqlite_sequence(name,seq) SELECT 'planner_items',? WHERE NOT EXISTS(SELECT 1 FROM sqlite_sequence WHERE name='planner_items')",arrayOf(taskSequence))
                db.execSQL("UPDATE sqlite_sequence SET seq=MAX(seq,?) WHERE name='planner_items'",arrayOf(taskSequence))
                db.execSQL("CREATE TABLE projects(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name TEXT NOT NULL, description TEXT NOT NULL, pinned INTEGER NOT NULL, archived INTEGER NOT NULL, sortOrder INTEGER NOT NULL, updatedAt INTEGER NOT NULL, revision INTEGER NOT NULL)")
                db.execSQL("CREATE TABLE task_dependencies(taskId INTEGER NOT NULL, prerequisiteId INTEGER NOT NULL, PRIMARY KEY(taskId,prerequisiteId), FOREIGN KEY(taskId) REFERENCES planner_items(id) ON DELETE CASCADE, FOREIGN KEY(prerequisiteId) REFERENCES planner_items(id) ON DELETE RESTRICT)")
                db.execSQL("CREATE INDEX index_task_dependencies_prerequisiteId ON task_dependencies(prerequisiteId)")
                db.execSQL("CREATE TABLE labels(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name TEXT NOT NULL)")
                db.execSQL("CREATE UNIQUE INDEX index_labels_name ON labels(name)")
                db.execSQL("CREATE TABLE project_templates(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name TEXT NOT NULL, payload TEXT NOT NULL, updatedAt INTEGER NOT NULL)")
                db.execSQL("CREATE TABLE conversations(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, title TEXT NOT NULL, projectId INTEGER, updatedAt INTEGER NOT NULL)")
                db.execSQL("CREATE INDEX index_conversations_projectId ON conversations(projectId)")
                db.execSQL("CREATE TABLE conversation_messages(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, conversationId INTEGER NOT NULL, user INTEGER NOT NULL, text TEXT NOT NULL, createdAt INTEGER NOT NULL, FOREIGN KEY(conversationId) REFERENCES conversations(id) ON DELETE CASCADE)")
                db.execSQL("CREATE INDEX index_conversation_messages_conversationId ON conversation_messages(conversationId)")
            }
        }
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
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5).build().also { instance = it }
        }
    }
}
