package dev.daylight.hub.ai

import kotlinx.coroutines.channels.ReceiveChannel

internal object InferenceProtocol {
    const val LOAD=1; const val UNLOAD=2; const val GENERATE=3; const val CANCEL=4; const val RESET=5
    const val LOADED=11; const val UNLOADED=12; const val TOKEN=13; const val DONE=14; const val ERROR=15
    const val PROGRESS=16; const val CANCELLED=17
}
internal data class InferenceRequest(
    val id:Int, val kind:Int, val file:String="", val parameters:String="",
    val roles:List<String> = emptyList(), val contents:List<String> = emptyList(), val grammar:String="",val inputTokenLimit:Int=0
)
internal data class InferenceReply(val id:Int,val kind:Int,val text:String="",val percent:Int = -1)
internal interface InferenceTransport {
    val replies:ReceiveChannel<InferenceReply>
    var onDisconnect:((String)->Unit)?
    suspend fun send(request:InferenceRequest)
    fun cancel(id:Int)
    fun reset()
    fun close()
}
