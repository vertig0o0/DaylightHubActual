package dev.daylight.hub.ai

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.UUID

data class ApprovalProposal(val id: String, val title: String, val before: String, val after: String, val needsReminder: Boolean=false,val richPreview:String?=null,val suggestedMinutes:Int?=30,val suggestedMode:String="NOTIFICATION")
/** Holds executable callbacks only in app memory; the model receives no approval capability. */
class ApprovalGateway {
    private data class Pending(val view: ApprovalProposal, val commit: suspend (Int?,String)->Unit)
    private val pending=mutableMapOf<String,Pending>()
    private val mutex=Mutex()
    fun propose(title: String, before: String, after: String, needsReminder: Boolean=false,richPreview:String?=null,suggestedMinutes:Int?=30,suggestedMode:String="NOTIFICATION",
        commit: suspend (Int?,String)->Unit): ApprovalProposal {
        val view=ApprovalProposal(UUID.randomUUID().toString(),title,before,after,needsReminder,richPreview,suggestedMinutes,suggestedMode)
        synchronized(pending) { pending[view.id]=Pending(view,commit) }; return view
    }
    fun reject(id: String) { synchronized(pending) { pending.remove(id) } }
    suspend fun approve(id: String, minutes: Int?=null, mode: String="NOTIFICATION") = mutex.withLock {
        val proposal=synchronized(pending) { pending.remove(id) } ?: error("This approval expired or was already used.")
        if(proposal.view.needsReminder) {
            require(minutes==null || minutes>=0 && minutes%30==0) { "Choose a 30-minute reminder increment." }
            require(mode in listOf("ALARM","NOTIFICATION"))
        }
        // Consumption precedes commit: failures/stale versions require a new review.
        proposal.commit(minutes,mode)
    }
    fun clear() { synchronized(pending) { pending.clear() } }
}
