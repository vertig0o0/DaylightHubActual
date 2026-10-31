package dev.daylight.hub;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.test.platform.app.InstrumentationRegistry;
import org.junit.Assume;
import org.junit.Test;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import static org.junit.Assert.*;

/** Opt-in, read-only audit usable before and after an actual updater installation. */
public class ObtainiumUpgradeAuditTest {
    private void text(MessageDigest digest,String value) {
        byte[] bytes=value.getBytes(StandardCharsets.UTF_8);
        digest.update((bytes.length+":").getBytes(StandardCharsets.US_ASCII));digest.update(bytes);
    }
    private String hex(MessageDigest digest) {
        StringBuilder value=new StringBuilder();for(byte b:digest.digest())value.append(String.format("%02x",b&255));return value.toString();
    }
    private String data(Context context) throws Exception {
        MessageDigest digest=MessageDigest.getInstance("SHA-256");
        try(SQLiteDatabase db=SQLiteDatabase.openDatabase(context.getDatabasePath("daylight-hub.db").getPath(),null,SQLiteDatabase.OPEN_READONLY)) {
            // Runtime timer/checkpoint tables are intentionally excluded from this content audit.
            for(String table:new String[]{"notes","planner_items","calendar_events","event_exceptions","model_profiles","projects","task_dependencies","labels","project_templates","conversations","conversation_messages"}) {
                boolean exists;try(Cursor c=db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?",new String[]{table})){exists=c.moveToFirst();}
                if(!exists)continue;
                text(digest,table);
                String order=table.equals("task_dependencies")?"taskId,prerequisiteId":table.equals("event_exceptions")?"eventId,originalStart":"id";
                try(Cursor c=db.rawQuery("SELECT * FROM "+table+" ORDER BY "+order,null)) {
                    for(String column:c.getColumnNames())text(digest,column);
                    while(c.moveToNext())for(int i=0;i<c.getColumnCount();i++)text(digest,c.isNull(i)?"NULL":"VALUE:"+c.getString(i));
                }
            }
        }
        return hex(digest);
    }
    private String models(Context context) throws Exception {
        MessageDigest digest=MessageDigest.getInstance("SHA-256");File[] files=new File(context.getFilesDir(),"models").listFiles();
        if(files!=null){Arrays.sort(files,(a,b)->a.getName().compareTo(b.getName()));byte[] buffer=new byte[65536];
            for(File file:files)if(file.isFile()){text(digest,file.getName());text(digest,Long.toString(file.length()));try(FileInputStream input=new FileInputStream(file)){int count;while((count=input.read(buffer))!=-1)digest.update(buffer,0,count);}}
        }
        return hex(digest);
    }
    @Test public void preservesContentsAcrossActualObtainiumUpdate() throws Exception {
        String mode=InstrumentationRegistry.getArguments().getString("upgradeAudit");Assume.assumeTrue("Opt-in capture/verify around the updater install", "capture".equals(mode)||"verify".equals(mode));
        Context context=InstrumentationRegistry.getInstrumentation().getTargetContext();SharedPreferences audit=context.getSharedPreferences("obtainium_upgrade_audit",0);
        String data=data(context),models=models(context);String appearance=context.getSharedPreferences("settings",0).getString("appearance","");
        if(mode.equals("capture"))assertTrue(audit.edit().putString("data",data).putString("models",models).putString("appearance",appearance).commit());
        else {assertEquals("Saved content changed across update",audit.getString("data",null),data);assertEquals("Private model bytes changed",audit.getString("models",null),models);assertEquals("Appearance changed",audit.getString("appearance",null),appearance);
            assertEquals("v3.0.0",context.getPackageManager().getPackageInfo(context.getPackageName(),0).versionName);}
    }
}
