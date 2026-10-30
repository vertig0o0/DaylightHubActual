package dev.daylight.hub

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.sqlite.db.*
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.room.Room
import dev.daylight.hub.data.*
import dev.daylight.hub.ai.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.first
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MigrationAndApprovalTest {
    private val context=ApplicationProvider.getApplicationContext<Context>()
    @Test fun v1MigrationPreservesAllFieldsAndValidatesV2()=runBlocking {
        val name="migration-acceptance.db";context.deleteDatabase(name)
        val helper=FrameworkSQLiteOpenHelperFactory().create(SupportSQLiteOpenHelper.Configuration.builder(context).name(name)
            .callback(object:SupportSQLiteOpenHelper.Callback(1){
                override fun onCreate(db:SupportSQLiteDatabase) {
                    db.execSQL("CREATE TABLE IF NOT EXISTS notes (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,title TEXT NOT NULL,body TEXT NOT NULL,updatedAt INTEGER NOT NULL)")
                    db.execSQL("CREATE TABLE IF NOT EXISTS planner_items (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,title TEXT NOT NULL,details TEXT NOT NULL,dueAt INTEGER NOT NULL,isDone INTEGER NOT NULL)")
                    db.execSQL("INSERT INTO notes VALUES (7,'Old title','Unicode 👋\\nLiteral <b>text</b>',123456)")
                    db.execSQL("INSERT INTO planner_items VALUES (9,'Old task','Details',987654,1)")
                }
                override fun onUpgrade(db:SupportSQLiteDatabase,oldVersion:Int,newVersion:Int){}
            }).build())
        helper.writableDatabase;helper.close()
        val db=Room.databaseBuilder(context,HubDatabase::class.java,name).addMigrations(HubDatabase.MIGRATION_1_2).build()
        try{
            val note=db.notes().get(7)!!;assertEquals("Old title",note.title);assertEquals(123456L,note.updatedAt);assertTrue(note.body.contains("<b>text</b>"));assertEquals("",note.document);assertEquals("Inbox",note.notebook)
            val task=db.planner().get(9)!!;assertEquals(987654L,task.dueAt);assertTrue(task.isDone);assertEquals(0L,task.revision)
            assertEquals(7L,db.notes().search("Old*").single().id)
            val repo=HubRepository(db);val gate=ApprovalGateway()
            val p=gate.propose("Change note",note.body,"Approved"){_,_->repo.saveNote(note.copy(body="Approved"),note.revision);Unit}
            assertEquals(note.body,db.notes().get(7)!!.body)
            repo.saveNote(note.copy(title="Manual change"),note.revision)
            assertTrue(runCatching{gate.approve(p.id)}.isFailure);assertEquals(note.body,db.notes().get(7)!!.body)
            val current=db.notes().get(7)!!
            val fresh=gate.propose("Edit",current.body,"Approved"){_,_->repo.saveNote(current.copy(body="Approved"),current.revision);Unit}
            gate.approve(fresh.id);assertEquals("Approved",db.notes().get(7)!!.body)
            assertEquals(7L,db.notes().search("Approved").single().id)
            assertTrue(runCatching{gate.approve(fresh.id)}.isFailure)
        }finally{db.close();context.deleteDatabase(name)}
    }
    @Test fun backlinksAreStableAndNoInternetPermission()=runBlocking {
        val db=Room.inMemoryDatabaseBuilder(context,HubDatabase::class.java).build()
        try {
            val repo=HubRepository(db);val target=repo.saveNote(Note(title="Target",body=""))
            val source=repo.saveNote(Note(title="Source",body="link",document="""{"v":1,"blocks":[{"id":"b0","type":"paragraph","text":"link","spans":[{"k":"link","r":[0,3],"url":"daylighthub://note/"""+target+""""}]}]}"""))
            assertEquals(source,db.notes().backlinks(target).first().single().id)
            val before=db.notes().get(target)!!;repo.saveNote(before.copy(title="Renamed"),before.revision)
            assertEquals(source,db.notes().backlinks(target).first().single().id)
            repo.deleteNote(target,db.notes().get(target)!!.revision)
            assertTrue(db.notes().get(source)!!.document.contains("daylighthub://note/"+target))
            val requested=context.packageManager.getPackageInfo(context.packageName,android.content.pm.PackageManager.GET_PERMISSIONS).requestedPermissions.orEmpty()
            assertFalse(requested.contains("android.permission.INTERNET"))
        }finally{db.close()}
    }
}
