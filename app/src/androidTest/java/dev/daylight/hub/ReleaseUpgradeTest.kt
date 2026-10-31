package dev.daylight.hub

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.daylight.hub.data.HubDatabase
import dev.daylight.hub.ui.Appearance
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Assume
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class ReleaseUpgradeTest {
    @Test fun originalPublicV2UpgradeRetainsDataFilesProfilesAndPreferences()=runBlocking {
        val context=ApplicationProvider.getApplicationContext<Context>();val preferences=context.getSharedPreferences("settings",0)
        Assume.assumeTrue("Seed the original v2 APK using LegacyUpgradeSeedTest first",preferences.getString("v3_upgrade_fixture",null)=="preserve-20261031")
        val info=context.packageManager.getPackageInfo(context.packageName,0)
        @Suppress("DEPRECATION") val version=if(android.os.Build.VERSION.SDK_INT>=28)info.longVersionCode else info.versionCode.toLong()
        assertEquals(5L,version)
        val db=HubDatabase.get(context)
        val note=db.notes().get(LegacyUpgradeSeedTest.NOTE_ID)!!
        assertEquals("Upgrade note",note.title);assertEquals(LegacyUpgradeSeedTest.BODY,note.body);assertEquals("Preserved notebook",note.notebook);assertEquals("upgrade,keep",note.tags);assertEquals(7L,note.revision);assertNull(note.projectId)
        val task=db.planner().get(LegacyUpgradeSeedTest.TASK_ID)!!
        assertEquals("Keep this detail",task.details);assertEquals(LegacyUpgradeSeedTest.DUE_AT,task.dueAt);assertTrue(task.isDone);assertEquals(3L,task.revision)
        val profile=db.models().get("upgrade-preservation")!!
        assertEquals(LegacyUpgradeSeedTest.PARAMETERS,profile.parameters);assertEquals("Original profile metadata",profile.metadata)
        assertEquals("Original private model bytes.\n",File(context.filesDir,"models/upgrade-preservation.txt").readText())
        val appearance=Appearance.read(preferences)
        assertEquals("Dark",appearance.mode);assertEquals(1.15f,appearance.textScale,0f);assertEquals("Notes,Planner,Reading,Assistant",appearance.navigation)
    }
}
