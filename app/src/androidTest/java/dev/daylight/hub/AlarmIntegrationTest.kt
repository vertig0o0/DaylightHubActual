package dev.daylight.hub

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import dev.daylight.hub.calendar.*
import dev.daylight.hub.data.*
import kotlinx.coroutines.*
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AlarmIntegrationTest {
    @Test fun exactNotificationAndGroupedRingingCanBeSnoozedAndCancelled()=runBlocking {
        val context=ApplicationProvider.getApplicationContext<Context>()
        val ui=InstrumentationRegistry.getInstrumentation().uiAutomation
        ui.executeShellCommand("pm grant "+context.packageName+" android.permission.POST_NOTIFICATIONS").close()
        ui.executeShellCommand("appops set "+context.packageName+" SCHEDULE_EXACT_ALARM allow").close()
        val scheduler=AlarmScheduler(context)
        withTimeout(15000){while(scheduler.permissionProblem()!=null)delay(100)}
        AlarmNotifications.channels(context)
        val repo=HubRepository(HubDatabase.get(context))
        val notifications=context.getSystemService(NotificationManager::class.java)
        val ids=mutableListOf<Long>()
        try {
            val whenAt=System.currentTimeMillis()+7000
            val id=repo.saveEvent(CalendarEvent(title="Alarm acceptance notification",startAt=whenAt,endAt=whenAt+60000,reminderMinutes=0))
            ids.add(id);scheduler.rebuild()
            withTimeout(30000){while(notifications.activeNotifications.none{it.id==id.toInt()})delay(200)}
            notifications.cancel(id.toInt())
            val ringAt=System.currentTimeMillis()+7000
            repeat(2){index->ids.add(repo.saveEvent(CalendarEvent(title="Alarm acceptance ring "+index,startAt=ringAt,endAt=ringAt+60000,reminderMinutes=0,reminderMode="ALARM")))}
            scheduler.rebuild()
            withTimeout(30000){while(notifications.activeNotifications.none{it.id==9001 && it.notification.extras.getString("android.title")=="2 event alarms"})delay(200)}
            context.startService(Intent(context,RingingAlarmService::class.java).setAction("SNOOZE"))
            withTimeout(15000){while(Snoozes.all(context).count{it.eventId in ids}<2)delay(100)}
            assertTrue(Snoozes.all(context).filter{it.eventId in ids}.all{it.trigger>System.currentTimeMillis()+29*60000})
        } finally {
            ids.forEach{id->repo.db.calendar().get(id)?.let{repo.deleteEvent(id,it.revision)}}
            scheduler.rebuild()
            assertTrue(Snoozes.all(context).none{it.eventId in ids})
        }
    }
}
