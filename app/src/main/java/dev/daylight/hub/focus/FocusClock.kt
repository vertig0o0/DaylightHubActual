package dev.daylight.hub.focus

data class FocusReading(val remainingMs:Long,val creditedMs:Long,val expired:Boolean)

/** A countdown is a deadline, not a background loop. Wall-clock changes never change credit. */
object FocusClock {
    fun read(remainingMs:Long,creditedMs:Long,sinceElapsed:Long?,nowElapsed:Long):FocusReading {
        val delta=if(sinceElapsed==null) 0 else (nowElapsed-sinceElapsed).coerceAtLeast(0).coerceAtMost(remainingMs)
        val remaining=(remainingMs-delta).coerceAtLeast(0)
        return FocusReading(remaining,creditedMs+delta,remaining==0L)
    }
}
