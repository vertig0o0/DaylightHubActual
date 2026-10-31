package dev.daylight.hub.focus

import android.Manifest
import android.app.*
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.SystemClock
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import dev.daylight.hub.MainActivity
import dev.daylight.hub.data.*
import kotlinx.coroutines.*

class FocusNotifications(private val context:Context) {
    private val manager=context.getSystemService(AlarmManager::class.java)
    private val prefs=context.getSharedPreferences("focus_alarms",0)
    fun permissionProblem():String? = when {
        Build.VERSION.SDK_INT>=33 && ContextCompat.checkSelfPermission(context,Manifest.permission.POST_NOTIFICATIONS)!=PackageManager.PERMISSION_GRANTED -> "Notifications are off. The timer still works; enable notifications for background controls and alerts."
        !NotificationManagerCompat.from(context).areNotificationsEnabled() -> "Focus notifications are disabled in Android settings."
        Build.VERSION.SDK_INT>=31 && !manager.canScheduleExactAlarms() -> "Precise alarms are off. Background completion alerts may be delayed."
        else -> null
    }
    private fun command(session:FocusSession,action:String):PendingIntent {
        val intent=Intent(context,FocusReceiver::class.java).setAction(action)
            .setData(Uri.parse("daylighthub://focus/${session.id}/${session.revision}/$action"))
            .putExtra("id",session.id).putExtra("revision",session.revision)
        return PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }
    fun refresh(session:FocusSession?) {
        val notifications=context.getSystemService(NotificationManager::class.java)
        notifications.createNotificationChannel(NotificationChannel("focus_active","Active focus session",NotificationManager.IMPORTANCE_LOW))
        notifications.createNotificationChannel(NotificationChannel("focus_finished","Focus completion",NotificationManager.IMPORTANCE_DEFAULT))
        prefs.getString("scheduled",null)?.let { old ->
            val intent=Intent(context,FocusReceiver::class.java).setAction("EXPIRE").setData(Uri.parse(old))
            PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE)?.let { manager.cancel(it);it.cancel() }
        }
        prefs.edit().remove("scheduled").apply()
        notifications.cancel(7101)
        if(session==null) return
        val canNotify=Build.VERSION.SDK_INT<33 || ContextCompat.checkSelfPermission(context,Manifest.permission.POST_NOTIFICATIONS)==PackageManager.PERMISSION_GRANTED
        val open=PendingIntent.getActivity(context,7101,Intent(context,MainActivity::class.java).putExtra("openFocus",true),PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        if(session.activeSlot==null) {
            if(canNotify && session.status=="COMPLETED") notifications.notify(7102,NotificationCompat.Builder(context,"focus_finished")
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm).setContentTitle(if(session.kind=="BREAK") "Break complete" else "Focus session complete")
                .setContentText(session.title).setContentIntent(open).setAutoCancel(true).setVisibility(NotificationCompat.VISIBILITY_PRIVATE).build())
            return
        }
        val remaining=FocusClock.read(session.remainingMs,session.creditedMs,session.sinceElapsed,SystemClock.elapsedRealtime()).remainingMs
        if(session.status=="RUNNING") {
            val pending=command(session,"EXPIRE")
            prefs.edit().putString("scheduled","daylighthub://focus/${session.id}/${session.revision}/EXPIRE").apply()
            val deadline=SystemClock.elapsedRealtime()+remaining
            try {
                if(Build.VERSION.SDK_INT<31 || manager.canScheduleExactAlarms()) manager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP,deadline,pending)
                else manager.setAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP,deadline,pending)
            } catch(_:SecurityException) { manager.setAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP,deadline,pending) }
        }
        if(canNotify) {
            val builder=NotificationCompat.Builder(context,"focus_active").setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setContentTitle(session.title).setContentText(if(session.status=="RUNNING") "Focus in progress" else "Focus paused")
                .setContentIntent(open).setOngoing(true).setOnlyAlertOnce(true).setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .addAction(0,if(session.status=="RUNNING") "Pause" else "Resume",command(session,if(session.status=="RUNNING") "PAUSE" else "RESUME"))
                .addAction(0,"End",command(session,"END"))
            if(session.status=="RUNNING") builder.setWhen(System.currentTimeMillis()+remaining).setUsesChronometer(true).setChronometerCountDown(true)
            notifications.notify(7101,builder.build())
        }
    }
}

class FocusReceiver:BroadcastReceiver() {
    override fun onReceive(context:Context,intent:Intent) {
        val pending=goAsync()
        CoroutineScope(SupervisorJob()+Dispatchers.IO).launch {
            try {
                val repository=FocusRepository(context.applicationContext,HubDatabase.get(context))
                when(intent.action) {
                    Intent.ACTION_BOOT_COMPLETED -> repository.reconcile(rebooted=true)
                    Intent.ACTION_MY_PACKAGE_REPLACED -> repository.reconcile()
                    "PAUSE","RESUME","END","EXPIRE" -> repository.command(intent.getLongExtra("id",-1),intent.getLongExtra("revision",-1),intent.action!!)
                }
            } catch(e:Exception) { android.util.Log.w("FocusReceiver","Could not reconcile focus state",e) }
            finally { pending.finish() }
        }
    }
}
