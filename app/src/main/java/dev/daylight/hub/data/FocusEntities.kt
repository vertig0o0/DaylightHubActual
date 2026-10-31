package dev.daylight.hub.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName="focus_sessions",indices=[Index(value=["activeSlot"],unique=true),Index("projectId"),Index("taskId")])
data class FocusSession(@PrimaryKey(autoGenerate=true) val id:Long=0,
    val title:String,val taskId:Long?=null,val projectId:Long?=null,
    val durationMs:Long,val remainingMs:Long,val creditedMs:Long=0,
    val sinceElapsed:Long?=null,val checkpointElapsed:Long=0,val bootCount:Int=0,
    val status:String="RUNNING",val kind:String="FOCUS",val activeSlot:Int?=1,
    val startedAt:Long=System.currentTimeMillis(),val endedAt:Long?=null,val revision:Long=0)

@Entity(tableName="resume_markers",indices=[Index(value=["contextKey"],unique=true)])
data class ResumeMarker(@PrimaryKey(autoGenerate=true) val id:Long=0,
    val contextKey:String,val taskId:Long?=null,val projectId:Long?=null,val noteId:Long?=null,
    val nextStep:String,val updatedAt:Long=System.currentTimeMillis())

@Entity(tableName="editor_recovery")
data class EditorRecovery(@PrimaryKey val draftKey:String,val baseRevision:Long,
    val title:String,val notebook:String,val tags:String,val document:String,
    val projectId:Long?,val labelId:Long?,val updatedAt:Long=System.currentTimeMillis())

@Dao interface FocusDao {
    @Query("SELECT * FROM focus_sessions WHERE activeSlot=1 LIMIT 1") fun active():Flow<FocusSession?>
    @Query("SELECT * FROM focus_sessions WHERE activeSlot=1 LIMIT 1") suspend fun current():FocusSession?
    @Query("SELECT * FROM focus_sessions ORDER BY startedAt DESC LIMIT 500") fun history():Flow<List<FocusSession>>
    @Query("SELECT * FROM focus_sessions ORDER BY startedAt DESC,id DESC") fun pagedHistory():androidx.paging.PagingSource<Int,FocusSession>
    @Query("SELECT COALESCE(SUM(creditedMs),0) FROM focus_sessions WHERE kind='FOCUS'") fun total():Flow<Long>
    @Query("SELECT COALESCE(SUM(creditedMs),0) FROM focus_sessions WHERE kind='FOCUS' AND projectId=:id") fun projectTotal(id:Long):Flow<Long>
    @Query("SELECT COALESCE(SUM(creditedMs),0) FROM focus_sessions WHERE kind='FOCUS' AND taskId=:id") fun taskTotal(id:Long):Flow<Long>
    @Insert suspend fun insert(session:FocusSession):Long
    @Update suspend fun update(session:FocusSession)
    @Query("SELECT * FROM resume_markers ORDER BY updatedAt DESC") fun markers():Flow<List<ResumeMarker>>
    @Query("SELECT * FROM resume_markers WHERE contextKey=:key") suspend fun marker(key:String):ResumeMarker?
    @Insert suspend fun insertMarker(marker:ResumeMarker):Long
    @Update suspend fun updateMarker(marker:ResumeMarker)
    @Query("DELETE FROM resume_markers WHERE id=:id") suspend fun removeMarker(id:Long)
    @Query("SELECT * FROM editor_recovery WHERE draftKey=:key") suspend fun draft(key:String):EditorRecovery?
    @Insert(onConflict=OnConflictStrategy.REPLACE) suspend fun saveDraft(draft:EditorRecovery)
    @Query("DELETE FROM editor_recovery WHERE draftKey=:key") suspend fun clearDraft(key:String)
    @Query("SELECT * FROM editor_recovery ORDER BY updatedAt DESC LIMIT 100") fun drafts():Flow<List<EditorRecovery>>
}
