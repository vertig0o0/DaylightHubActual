package dev.daylight.hub.navigation

import org.json.JSONObject

enum class Destination(val title:String) {
    TODAY("Today"),PLANNER("Planner"),NOTES("Notes"),READING("Reading"),ASSISTANT("Assistant"),SETTINGS("Settings");
    companion object { fun from(value:String)=if(value=="Home")TODAY else entries.firstOrNull{it.title==value} ?: error("Unknown section.") }
}
enum class PlannerTab(val title:String) {
    READY("Ready"),TASKS("Tasks"),CALENDAR("Calendar"),PROJECTS("Projects");
    companion object { fun from(value:String)=entries.firstOrNull{it.title==value} ?: error("Unknown planner section.") }
}

/** Each primary section retains its child route; editor content stays in Room/recovery storage. */
data class NavigationState(val destination:Destination=Destination.TODAY,val plannerTab:PlannerTab=PlannerTab.READY,
    val projectId:Long?=null,val taskId:Long?=null,val creatingTask:Boolean=false,val noteId:Long?=null,val editingNote:Boolean=false) {
    fun json()=JSONObject().put("version",1).put("destination",destination.title).put("plannerTab",plannerTab.title)
        .put("projectId",projectId ?: JSONObject.NULL).put("taskId",taskId ?: JSONObject.NULL).put("creatingTask",creatingTask)
        .put("noteId",noteId ?: JSONObject.NULL).put("editingNote",editingNote).toString()
    companion object {
        fun read(text:String?):NavigationState=runCatching {
            val j=JSONObject(text ?: "{}");require(j.optInt("version",1)==1)
            fun id(key:String)=j.optLong(key,0).takeIf{it>0}
            val creating=j.optBoolean("creatingTask")
            NavigationState(Destination.from(j.optString("destination","Today")),PlannerTab.from(j.optString("plannerTab","Ready")),id("projectId"),if(creating)null else id("taskId"),creating,id("noteId"),j.optBoolean("editingNote"))
        }.getOrDefault(NavigationState())
    }
}
