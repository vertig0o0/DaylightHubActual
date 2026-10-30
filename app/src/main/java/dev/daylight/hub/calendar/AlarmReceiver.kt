package dev.daylight.hub.calendar

import android.app.*
import android.content.*
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dev.daylight.hub.MainActivity
import dev.daylight.hub.R
import dev.daylight.hub.data.*
import kotlinx.coroutines.*

object AlarmNotifications {
    const val REMINDERS="calendar_reminders"
    const val RINGING="calendar_ringing"
    fun post(context:Context,id:Int,notification:android.app.Notification) {
        if(Build.VERSION.SDK_INT>=33 && androidx.core.content.ContextCompat.checkSelfPermission(context,android.Manifest.permission.POST_NOTIFICATIONS)!=android.content.pm.PackageManager.PERMISSION_GRANTED) return
        try { NotificationManagerCompat.from(context).notify(id,notification) } catch(_:SecurityException) { /* Permission revoked concurrently. */ }
    }
    fun channels(context: Context) {
        val manager=context.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(NotificationChannel(REMINDERS,"Event reminders",NotificationManager.IMPORTANCE_HIGH))
        manager.createNotificationChannel(NotificationChannel(RINGING,"Ringing alarms",NotificationManager.IMPORTANCE_HIGH).apply { setSound(null,null) })
    }
    fun notification(context: Context, title: String, details: String, ringing: Boolean=false): NotificationCompat.Builder {
        channels(context)
        val open=PendingIntent.getActivity(context,0,Intent(context,MainActivity::class.java).putExtra("openCalendar",true),PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        return NotificationCompat.Builder(context,if(ringing) RINGING else REMINDERS).setSmallIcon(R.drawable.ic_brand_mark)
            .setContentTitle(title).setContentText(details).setContentIntent(open).setAutoCancel(!ringing)
            .setCategory(if(ringing) NotificationCompat.CATEGORY_ALARM else NotificationCompat.CATEGORY_EVENT)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
    }
}
class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val pending=goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                if(intent.action in listOf(Intent.ACTION_BOOT_COMPLETED,Intent.ACTION_TIME_CHANGED,Intent.ACTION_TIMEZONE_CHANGED,Intent.ACTION_MY_PACKAGE_REPLACED,AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED)) {
                    AlarmScheduler(context).rebuild(); return@launch
                }
                val db=HubDatabase.get(context)
                val event=db.calendar().get(intent.getLongExtra("eventId",-1)) ?: return@launch
                val original=intent.getLongExtra("original",event.startAt)
                if(intent.getBooleanExtra("snoozed",false)) {
                    val snooze=Snoozes.all(context).firstOrNull{it.eventId==event.id && it.original==original} ?: return@launch
                    Snoozes.remove(context,event.id,original)
                    if(snooze.revision!=event.revision) return@launch
                }
                val ex=db.calendar().exceptions(event.id).firstOrNull { it.originalStart==original }
                if(ex?.cancelled==true) return@launch
                val current=ex?.replacementJson?.takeIf { it.isNotBlank() }?.let { eventFromJson(org.json.JSONObject(it)) } ?: event
                if(current.reminderMinutes==null) return@launch
                if(AlarmScheduler(context).permissionProblem(current.reminderMode)!=null) return@launch
                if(current.reminderMode=="ALARM") {
                    val service=Intent(context,RingingAlarmService::class.java).putExtra("title",current.title)
                        .putExtra("eventId",event.id).putExtra("original",original).putExtra("revision",event.revision)
                    context.startForegroundService(service)
                } else if(AlarmScheduler(context).permissionProblem()==null) {
                    AlarmNotifications.post(context,event.id.toInt(),AlarmNotifications.notification(context,current.title,"Your event is coming up").build())
                }
                AlarmScheduler(context).rebuild()
            } finally { pending.finish() }
        }
    }
}
