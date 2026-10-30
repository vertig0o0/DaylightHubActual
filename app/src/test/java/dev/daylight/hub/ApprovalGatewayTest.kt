package dev.daylight.hub

import dev.daylight.hub.ai.ApprovalGateway
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.async
import org.junit.Assert.*
import org.junit.Test

class ApprovalGatewayTest {
    @Test fun proposingDoesNotWriteAndApprovalIsSingleUse()=runTest {
        val gate=ApprovalGateway();var writes=0
        val proposal=gate.propose("Edit","old","new"){_,_->writes++}
        assertEquals(0,writes);gate.approve(proposal.id);assertEquals(1,writes)
        assertTrue(runCatching{gate.approve(proposal.id)}.isFailure);assertEquals(1,writes)
    }
    @Test fun rejectionAndRestartRevokeCapability()=runTest {
        val gate=ApprovalGateway();var writes=0
        val a=gate.propose("Delete","note","deleted"){_,_->writes++}
        gate.reject(a.id);assertTrue(runCatching{gate.approve(a.id)}.isFailure)
        val b=gate.propose("Edit","old","new"){_,_->writes++}
        gate.clear();assertTrue(runCatching{gate.approve(b.id)}.isFailure);assertEquals(0,writes)
    }
    @Test fun concurrentApprovalsCommitOnce()=runTest {
        val gate=ApprovalGateway();var writes=0
        val p=gate.propose("Complete","open","done"){_,_->writes++}
        val a=async{runCatching{gate.approve(p.id)}};val b=async{runCatching{gate.approve(p.id)}}
        assertEquals(1,listOf(a.await(),b.await()).count{it.isSuccess});assertEquals(1,writes)
    }
    @Test fun staleRevisionFailsAndConsumesApproval()=runTest {
        val gate=ApprovalGateway();var version=1;var writes=0
        val expected=version
        val p=gate.propose("Edit","old","new"){_,_->check(version==expected);writes++}
        version=2
        assertTrue(runCatching{gate.approve(p.id)}.isFailure);version=1
        assertTrue(runCatching{gate.approve(p.id)}.isFailure);assertEquals(0,writes)
    }
    @Test fun eventApprovalCarriesReviewedReminderOnly()=runTest {
        val gate=ApprovalGateway();var lead:Int?=null;var type=""
        val p=gate.propose("Event","new","event",true){n,m->lead=n;type=m}
        gate.approve(p.id,90,"ALARM");assertEquals(90,lead);assertEquals("ALARM",type)
        val invalid=gate.propose("Event","new","event",true){_,_->error("must not commit")}
        assertTrue(runCatching{gate.approve(invalid.id,15,"ALARM")}.isFailure)
    }
}
