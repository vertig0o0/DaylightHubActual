package dev.daylight.hub.calendar

import android.app.*
import android.content.Intent
import android.media.*
import android.os.*
import androidx.core.app.NotificationManagerCompat
import dev.daylight.hub.data.CalendarEvent

/** Concurrent alarms share audio but retain every title and snooze identity. */
class RingingAlarmService : Service() {
    private data class Active(val eventId:Long,val original:Long,val revision:Long,val title:String)
    private val alarms=mutableListOf<Active>()
    private var player:MediaPlayer?=null
    private val handler=Handler(Looper.getMainLooper())
    private fun stopSound(){handler.removeCallbacksAndMessages(null);player?.release();player=null}
    private fun finish(){stopSound();alarms.clear();stopForeground(STOP_FOREGROUND_REMOVE);stopSelf()}
    private fun updateNotification(){
        fun action(name:String)=PendingIntent.getService(this,name.hashCode(),Intent(this,RingingAlarmService::class.java).setAction(name),PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val plural=alarms.size>1
        val title=if(plural) alarms.size.toString()+" event alarms" else alarms.firstOrNull()?.title ?: "Event alarm"
        val notice=AlarmNotifications.notification(this,title,alarms.joinToString(" · "){it.title},true)
            .addAction(0,if(plural) "Dismiss all" else "Dismiss",action("DISMISS"))
            .addAction(0,if(plural) "Snooze all 30 min" else "Snooze 30 min",action("SNOOZE")).setOngoing(true).build()
        startForeground(9001,notice)
    }
    override fun onStartCommand(intent:Intent?,flags:Int,startId:Int):Int {
        instance=this
        if(intent==null){finish();return START_NOT_STICKY}
        if(intent.action=="DISMISS" || intent.action=="SNOOZE"){
            if(intent.action=="SNOOZE"){
                alarms.forEach{Snoozes.put(this,Snooze(it.eventId,it.original,System.currentTimeMillis()+30*60000,it.revision))}
                AlarmScheduler.request(this)
            }
            finish();return START_NOT_STICKY
        }
        val item=Active(intent.getLongExtra("eventId",0),intent.getLongExtra("original",0),intent.getLongExtra("revision",0),intent.getStringExtra("title") ?: "Event")
        if(alarms.none{it.eventId==item.eventId && it.original==item.original})alarms.add(item)
        updateNotification()
        if(player==null){
            try{player=MediaPlayer().apply{
                setAudioAttributes(AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build())
                setDataSource(this@RingingAlarmService,RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));isLooping=true;prepare();start()
            }}catch(_:Exception){}
            handler.postDelayed({
                val missed=alarms.toList()
                finish()
                missed.forEach{AlarmNotifications.post(this,(it.eventId+100000).toInt(),AlarmNotifications.notification(this,"Missed alarm: "+it.title,"Open calendar to view the event").build())}
            },10*60*1000L)
        }
        return START_NOT_STICKY
    }
    override fun onBind(intent:Intent?)=null
    override fun onDestroy(){stopSound();if(instance===this)instance=null;super.onDestroy()}
    companion object{
        @Volatile private var instance:RingingAlarmService?=null
        fun reconcile(events:List<CalendarEvent>){
            val running=instance ?: return
            running.handler.post{
                running.alarms.removeAll{active->events.none{it.id==active.eventId && it.revision==active.revision}}
                if(running.alarms.isEmpty())running.finish() else running.updateNotification()
            }
        }
    }
}
