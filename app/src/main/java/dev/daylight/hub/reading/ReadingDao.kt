package dev.daylight.hub.reading

import androidx.paging.PagingSource
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao interface ReadingDao {
    @Query("SELECT * FROM books WHERE deletedAt IS NULL AND (:query='' OR title GLOB :pattern OR author GLOB :pattern) AND (:status='' OR status=:status) AND (:format='' OR originalFormat=:format) ORDER BY CASE WHEN :sort='TITLE' THEN title END COLLATE NOCASE, CASE WHEN :sort='AUTHOR' THEN author END COLLATE NOCASE, CASE WHEN :sort='PROGRESS' THEN progress END DESC, CASE WHEN :sort='IMPORTED' THEN importedAt END DESC, lastOpenedAt DESC, importedAt DESC, id")
    fun library(query:String,pattern:String,status:String,format:String,sort:String):PagingSource<Int,Book>
    @Query("SELECT * FROM books WHERE id=:id") suspend fun book(id:Long):Book?
    @Query("SELECT * FROM books WHERE id=:id") fun observeBook(id:Long):Flow<Book?>
    @Query("SELECT * FROM books WHERE deletedAt IS NOT NULL ORDER BY deletedAt DESC LIMIT 100") fun trash():Flow<List<Book>>
    @Query("SELECT * FROM books WHERE sha256=:hash") suspend fun byHash(hash:String):Book?
    @Query("SELECT * FROM books WHERE deletedAt IS NULL AND lastOpenedAt IS NOT NULL ORDER BY lastOpenedAt DESC LIMIT 8") fun recent():Flow<List<Book>>
    @Query("SELECT * FROM books WHERE deletedAt IS NULL AND status='READING' ORDER BY lastOpenedAt DESC LIMIT 20") fun currentlyReading():Flow<List<Book>>
    @Query("SELECT * FROM books WHERE deletedAt IS NULL AND status='FINISHED' ORDER BY lastOpenedAt DESC LIMIT 200") fun finished():Flow<List<Book>>
    @Insert suspend fun insert(book:Book):Long
    @Update suspend fun update(book:Book)
    @Query("UPDATE books SET progress=:fraction,lastOpenedAt=:at WHERE id=:id AND deletedAt IS NULL") suspend fun updateProgress(id:Long,fraction:Double,at:Long)
    @Query("SELECT * FROM book_positions WHERE bookId=:id") suspend fun position(id:Long):BookPosition?
    @Insert(onConflict=OnConflictStrategy.REPLACE) suspend fun savePosition(position:BookPosition)
    @Query("SELECT * FROM bookmarks WHERE bookId=:id ORDER BY createdAt DESC") fun bookmarks(id:Long):Flow<List<Bookmark>>
    @Insert suspend fun insertBookmark(bookmark:Bookmark):Long
    @Query("DELETE FROM bookmarks WHERE id=:id") suspend fun deleteBookmark(id:Long)
    @Query("SELECT * FROM reading_sessions WHERE activeSlot=1 LIMIT 1") suspend fun activeSession():ReadingSession?
    @Insert suspend fun insertSession(session:ReadingSession):Long
    @Update suspend fun updateSession(session:ReadingSession)
    @Query("SELECT * FROM reading_sessions ORDER BY startedAt DESC, id DESC LIMIT 20") fun sessions():Flow<List<ReadingSession>>
    @Query("SELECT * FROM reading_sessions ORDER BY startedAt DESC, id DESC") fun sessionHistory():PagingSource<Int,ReadingSession>
    @Query("SELECT localDate, SUM(creditedMs) AS milliseconds FROM reading_sessions WHERE localDate>=:from GROUP BY localDate ORDER BY localDate") fun dailyTime(from:String):Flow<List<ReadingDay>>
    @Query("SELECT * FROM reading_status_events WHERE status='FINISHED' ORDER BY changedAt DESC LIMIT 200") fun completions():Flow<List<ReadingStatusEvent>>
    @Query("SELECT * FROM reading_status_events WHERE status='FINISHED' ORDER BY changedAt DESC, id DESC") fun completionHistory():PagingSource<Int,ReadingStatusEvent>
    @Insert suspend fun insertStatusEvent(event:ReadingStatusEvent)
    @Query("SELECT * FROM reading_goals") fun goals():Flow<List<ReadingGoal>>
    @Insert(onConflict=OnConflictStrategy.REPLACE) suspend fun saveGoal(goal:ReadingGoal)
    @Insert(onConflict=OnConflictStrategy.REPLACE) suspend fun saveImport(job:BookImport)
    @Query("SELECT * FROM book_imports WHERE stage NOT IN ('DONE','CANCELLED') ORDER BY updatedAt DESC LIMIT 10") fun imports():Flow<List<BookImport>>
    @Query("UPDATE book_imports SET stage='INTERRUPTED',message='Import was interrupted. Select the source again to retry.' WHERE stage IN ('COPYING','CONVERTING','VALIDATING')") suspend fun interruptImports()
    @Query("DELETE FROM book_imports WHERE id=:id AND stage IN ('FAILED','INTERRUPTED','CANCELLED','DONE')") suspend fun dismissImport(id:String)
}
data class ReadingDay(val localDate:String,val milliseconds:Long)
