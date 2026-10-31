package dev.daylight.hub.reading

import androidx.room.*
import org.json.JSONObject

@Entity(tableName="books",indices=[Index(value=["sha256"],unique=true),Index("status"),Index("lastOpenedAt")])
data class Book(@PrimaryKey(autoGenerate=true) val id:Long=0,
    val sha256:String,val title:String,val author:String="",val originalName:String,val originalUri:String,
    val format:String,val originalFormat:String,val fileName:String,val normalizedFile:String?=null,
    val coverFile:String?=null,val bytes:Long,val status:String="WANT_TO_READ",val progress:Double=0.0,
    val importedAt:Long=System.currentTimeMillis(),val lastOpenedAt:Long?=null,
    val metadataEdited:Boolean=false,val revision:Long=0,val deletedAt:Long?=null)

@Entity(tableName="book_positions",foreignKeys=[ForeignKey(entity=Book::class,parentColumns=["id"],childColumns=["bookId"],onDelete=ForeignKey.CASCADE)])
data class BookPosition(@PrimaryKey val bookId:Long,val kind:String="REFLOW",val locator:String="",
    val section:Int=0,val pageIndex:Int=0,val offsetX:Float=0f,val offsetY:Float=0f,val zoom:Float=1f,
    val quote:String="",val chapter:String="",val fraction:Double=0.0,val engineVersion:Int=1,
    val updatedAt:Long=System.currentTimeMillis()) {
    fun json():String=JSONObject().put("kind",kind).put("locator",locator).put("section",section).put("pageIndex",pageIndex)
        .put("offsetX",offsetX).put("offsetY",offsetY).put("zoom",zoom).put("quote",quote).put("chapter",chapter)
        .put("fraction",fraction).put("engineVersion",engineVersion).toString()
    companion object {
        fun parse(bookId:Long,text:String):BookPosition {
            val j=JSONObject(text)
            val item=BookPosition(bookId,j.getString("kind"),j.optString("locator"),j.optInt("section"),j.optInt("pageIndex"),
                j.optDouble("offsetX",0.0).toFloat(),j.optDouble("offsetY",0.0).toFloat(),j.optDouble("zoom",1.0).toFloat(),
                j.optString("quote"),j.optString("chapter"),j.optDouble("fraction"),j.optInt("engineVersion",1))
            item.validate();return item
        }
    }
    fun validate() {
        require(kind in listOf("REFLOW","PAGE") && locator.length<=4096 && quote.length<=160 && chapter.length<=512)
        require(section>=0 && pageIndex>=0 && fraction.isFinite() && fraction in 0.0..1.0)
        require(offsetX.isFinite() && offsetY.isFinite() && offsetX in 0f..1f && offsetY in 0f..1f && zoom.isFinite() && zoom in .1f..10f)
    }
}

@Entity(tableName="bookmarks",indices=[Index("bookId")],foreignKeys=[ForeignKey(entity=Book::class,parentColumns=["id"],childColumns=["bookId"],onDelete=ForeignKey.CASCADE)])
data class Bookmark(@PrimaryKey(autoGenerate=true) val id:Long=0,val bookId:Long,val label:String,val position:String,val createdAt:Long=System.currentTimeMillis())

@Entity(tableName="reading_sessions",indices=[Index("bookId"),Index(value=["activeSlot"],unique=true),Index("localDate")],foreignKeys=[ForeignKey(entity=Book::class,parentColumns=["id"],childColumns=["bookId"],onDelete=ForeignKey.SET_NULL)])
data class ReadingSession(@PrimaryKey(autoGenerate=true) val id:Long=0,val bookId:Long?,val title:String,
    val startedAt:Long,val lastCheckpointAt:Long,val endedAt:Long?=null,val creditedMs:Long=0,
    val localDate:String,val zoneId:String,val activeSlot:Int?=1,val revision:Long=0)

@Entity(tableName="reading_status_events",indices=[Index("bookId")])
data class ReadingStatusEvent(@PrimaryKey(autoGenerate=true) val id:Long=0,val bookId:Long,val title:String,
    val status:String,val changedAt:Long=System.currentTimeMillis())

@Entity(tableName="reading_goals")
data class ReadingGoal(@PrimaryKey val period:String,val minutes:Int,val enabled:Boolean=true)

@Entity(tableName="book_imports")
data class BookImport(@PrimaryKey val id:String,val uri:String,val originalName:String="",val stage:String="COPYING",
    val bytesCopied:Long=0,val expectedBytes:Long?=null,val message:String="",val updatedAt:Long=System.currentTimeMillis())
