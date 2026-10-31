package dev.daylight.hub.data

/** List/link metadata deliberately excludes the complete rich document and body. */
data class NoteSummary(val id:Long,val title:String,val preview:String,val notebook:String,val tags:String,
    val projectId:Long?,val labelId:Long?,val updatedAt:Long,val revision:Long)
