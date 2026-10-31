package dev.daylight.hub

import dev.daylight.hub.focus.FocusClock
import org.junit.Assert.*
import org.junit.Test

class FocusClockTest {
    @Test fun backgroundElapsedTimeCountsButCannotOverrunTheDuration() {
        assertEquals(4000L,FocusClock.read(10000,2000,1000,7000).remainingMs)
        val ended=FocusClock.read(10000,2000,1000,90000)
        assertTrue(ended.expired);assertEquals(12000L,ended.creditedMs)
    }
    @Test fun pausedAndRebootRecoveryNeverInventElapsedTime() {
        assertEquals(10000L,FocusClock.read(10000,2000,null,90000).remainingMs)
        assertEquals(10000L,FocusClock.read(10000,2000,90000,1000).remainingMs)
    }
    @Test fun checkpointAndResumePreserveExactlyOneCreditForEachInterval() {
        val first=FocusClock.read(10000,0,1000,4000)
        val second=FocusClock.read(first.remainingMs,first.creditedMs,4000,6000)
        assertEquals(5000L,second.remainingMs);assertEquals(5000L,second.creditedMs)
    }
}
