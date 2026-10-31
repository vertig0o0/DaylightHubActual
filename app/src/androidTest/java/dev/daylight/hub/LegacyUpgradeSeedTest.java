package dev.daylight.hub;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import androidx.test.platform.app.InstrumentationRegistry;
import org.junit.Test;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import static org.junit.Assert.*;

/** Runs against the original public v2.0.1 APK, without referencing any v3 app class. */
public class LegacyUpgradeSeedTest {
    public static final long NOTE_ID=900001L,TASK_ID=900002L,DUE_AT=1893558600000L;
    public static final String BODY="Keep Unicode 👋 and literal <b>text</b>.\nSecond line.";
    public static final String PARAMETERS="{\"temperature\":0.42,\"topP\":0.91,\"topK\":30,\"minP\":0.05,\"repetitionPenalty\":1.1,\"seed\":7,\"maxTokens\":64,\"contextSize\":1024,\"threads\":4,\"systemPrompt\":\"Keep this prompt.\"}";
    @Test public void seedOriginalV2DataForInPlaceUpgrade() throws Exception {
        Context context=InstrumentationRegistry.getInstrumentation().getTargetContext();
        org.junit.Assume.assumeTrue("This fixture only seeds the original public v2 APK", "v2.0.1".equals(context.getPackageManager().getPackageInfo(context.getPackageName(),0).versionName));
        File path=context.getDatabasePath("daylight-hub.db");assertTrue("Launch v2 once before seeding",path.isFile());
        try(SQLiteDatabase db=SQLiteDatabase.openDatabase(path.getAbsolutePath(),null,SQLiteDatabase.OPEN_READWRITE)) {
            assertEquals("Only the original v2 schema may be seeded",2,db.getVersion());
            db.beginTransaction();try {
                ContentValues note=new ContentValues();note.put("id",NOTE_ID);note.put("title","Upgrade note");note.put("body",BODY);note.put("document","");note.put("notebook","Preserved notebook");note.put("tags","upgrade,keep");note.put("updatedAt",123456L);note.put("revision",7L);db.insertOrThrow("notes",null,note);
                ContentValues task=new ContentValues();task.put("id",TASK_ID);task.put("title","Upgrade dated task");task.put("details","Keep this detail");task.put("dueAt",DUE_AT);task.put("isDone",1);task.put("revision",3L);db.insertOrThrow("planner_items",null,task);
                ContentValues profile=new ContentValues();profile.put("id","upgrade-preservation");profile.put("name","Upgrade preservation fixture (not a model)");profile.put("fileName","upgrade-preservation.txt");profile.put("bytes",29L);profile.put("metadata","Original profile metadata");profile.put("parameters",PARAMETERS);db.insertOrThrow("model_profiles",null,profile);
                db.setTransactionSuccessful();
            }finally{db.endTransaction();}
        }
        File sentinel=new File(context.getFilesDir(),"models/upgrade-preservation.txt");sentinel.getParentFile().mkdirs();assertFalse("Do not overwrite an existing file",sentinel.exists());
        try(FileOutputStream output=new FileOutputStream(sentinel)){output.write("Original private model bytes.\n".getBytes(StandardCharsets.UTF_8));}
        assertTrue(context.getSharedPreferences("settings",0).edit().putString("v3_upgrade_fixture","preserve-20261031").putString("appearance","{\"mode\":\"Dark\",\"textScale\":1.15,\"navigation\":\"Notes,Planner,Assistant\"}").commit());
    }
}
