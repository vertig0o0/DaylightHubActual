package dev.daylight.hub.calendar

import android.Manifest
import android.app.*
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import dev.daylight.hub.data.*
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.json.JSONArray

class AlarmScheduler(private val context: Context) {
    private val manager=context.getSystemService(AlarmManager::class.java)
    private val prefs=context.getSharedPreferences("alarm_registry",0)
    fun permissionProblem(mode:String?=null): String? = when {
        Build.VERSION.SDK_INT >= 33 && ContextCompat.checkSelfPermission(context,Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED -> "Notifications are disabled. Reminders are inactive."
        !NotificationManagerCompat.from(context).areNotificationsEnabled() -> "Notifications are disabled. Reminders are inactive."
        Build.VERSION.SDK_INT >= 31 && !manager.canScheduleExactAlarms() -> "Allow Alarms & reminders in Android settings to activate reminders."
        mode!=null && context.getSystemService(NotificationManager::class.java).getNotificationChannel(if(mode=="ALARM") AlarmNotifications.RINGING else AlarmNotifications.REMINDERS)?.importance==NotificationManager.IMPORTANCE_NONE -> "This reminder channel is disabled in Android notification settings."
        else -> null
    }
    suspend fun rebuild() = rebuildMutex.withLock {
        val old=JSONArray(prefs.getString("pending","[]"))
        repeat(old.length()) { index ->
            val intent=Intent(context,AlarmReceiver::class.java).setData(android.net.Uri.parse(old.getString(index)))
            PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE)?.let { manager.cancel(it); it.cancel() }
        }
        prefs.edit().putString("pending","[]").apply()
        val db=HubDatabase.get(context); val now=System.currentTimeMillis(); val registry=JSONArray()
        val events=db.calendar().all()
        RingingAlarmService.reconcile(events)
        if(permissionProblem()!=null) return@withLock
        for(event in events) {
            if(permissionProblem(event.reminderMode)!=null) continue
            val lead=(event.reminderMinutes ?: continue).toLong()*60000
            val end=if(event.recurrence.isBlank()) maxOf(now+366L*86400000,event.endAt+86400000) else maxOf(now,event.startAt)+5L*366*86400000
            val next=RecurrenceEngine.between(event,db.calendar().exceptions(event.id),now-86400000+lead,end)
                .mapNotNull { item -> RecurrenceEngine.reminderAt(item)?.takeIf { it > now }?.let { it to item } }.minByOrNull { it.first } ?: continue
            val uri="daylighthub://alarm/" + event.id + "/" + next.second.originalStart
            val intent=Intent(context,AlarmReceiver::class.java).setData(android.net.Uri.parse(uri))
                .putExtra("eventId",event.id).putExtra("original",next.second.originalStart).putExtra("start",next.second.startAt)
            val pending=PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            try {
                if(event.reminderMode=="ALARM") {
                    val show=PendingIntent.getActivity(context,0,Intent(context,dev.daylight.hub.MainActivity::class.java).putExtra("openCalendar",true),PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
                    manager.setAlarmClock(AlarmManager.AlarmClockInfo(next.first,show),pending)
                } else manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,next.first,pending)
                registry.put(uri)
            } catch(_: SecurityException) { /* A permission may have been revoked during this rebuild. */ }
        }
        val validSnoozes=Snoozes.all(context).filter { snooze->snooze.trigger>now && events.any{it.id==snooze.eventId && it.revision==snooze.revision && it.reminderMinutes!=null} }
        Snoozes.replace(context,validSnoozes)
        for(snooze in validSnoozes) {
            val uri="daylighthub://snooze/"+snooze.eventId+"/"+snooze.original
            val intent=Intent(context,AlarmReceiver::class.java).setData(android.net.Uri.parse(uri)).putExtra("eventId",snooze.eventId).putExtra("original",snooze.original).putExtra("snoozed",true)
            val pending=PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            try { manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,snooze.trigger,pending);registry.put(uri) } catch(_:SecurityException){}
        }
        prefs.edit().putString("pending",registry.toString()).apply()
    }
    companion object {
        private val rebuildMutex=Mutex()
        private val scope=CoroutineScope(SupervisorJob()+Dispatchers.IO)
        fun request(context: Context) { scope.launch { runCatching { AlarmScheduler(context.applicationContext).rebuild() } } }
    }
}
