package dev.daylight.hub.calendar

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

data class Snooze(val eventId:Long,val original:Long,val trigger:Long,val revision:Long)
object Snoozes {
    private val lock=Any()
    fun all(context:Context):List<Snooze> = synchronized(lock) {
        val array=JSONArray(context.getSharedPreferences("alarm_registry",0).getString("snoozes","[]"))
        List(array.length()){i->val j=array.getJSONObject(i);Snooze(j.getLong("id"),j.getLong("original"),j.getLong("trigger"),j.getLong("revision"))}
    }
    fun replace(context:Context,items:List<Snooze>) = synchronized(lock) {
        val array=JSONArray();items.forEach{array.put(JSONObject().put("id",it.eventId).put("original",it.original).put("trigger",it.trigger).put("revision",it.revision))}
        context.getSharedPreferences("alarm_registry",0).edit().putString("snoozes",array.toString()).apply()
    }
    fun put(context:Context,item:Snooze)=synchronized(lock) { replace(context,all(context).filterNot{it.eventId==item.eventId && it.original==item.original}+item) }
    fun remove(context:Context,id:Long,original:Long)=synchronized(lock){replace(context,all(context).filterNot{it.eventId==id && it.original==original})}
}
