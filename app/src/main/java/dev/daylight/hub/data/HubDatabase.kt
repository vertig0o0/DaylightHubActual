package dev.daylight.hub.data

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY updatedAt DESC") fun observeAll(): Flow<List<Note>>
    @Query("SELECT * FROM notes WHERE id = :id") suspend fun get(id: Long): Note?
    @Insert suspend fun insert(note: Note): Long
    @Update suspend fun update(note: Note)
    @Delete suspend fun delete(note: Note)
}

@Dao
interface PlannerDao {
    @Query("SELECT * FROM planner_items ORDER BY isDone ASC, dueAt ASC") fun observeAll(): Flow<List<PlannerItem>>
    @Insert suspend fun insert(item: PlannerItem): Long
    @Update suspend fun update(item: PlannerItem)
    @Delete suspend fun delete(item: PlannerItem)
}

@Database(entities = [Note::class, PlannerItem::class], version = 1, exportSchema = false)
abstract class HubDatabase : RoomDatabase() {
    abstract fun notes(): NoteDao
    abstract fun planner(): PlannerDao

    companion object {
        @Volatile private var INSTANCE: HubDatabase? = null
        fun get(context: Context): HubDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(context.applicationContext, HubDatabase::class.java, "daylight-hub.db")
                .build().also { INSTANCE = it }
        }
    }
}
