package dev.daylight.hub

import dev.daylight.hub.data.*
import dev.daylight.hub.tasks.TaskGraph
import org.junit.Assert.*
import org.junit.Test

class TaskGraphTest {
    @Test fun selfAndTransitiveCyclesAreRejected() {
        assertTrue(runCatching { TaskGraph.validateReplacement(1, setOf(1), emptyList()) }.isFailure)
        val edges = listOf(TaskDependency(2, 3), TaskDependency(3, 1))
        assertTrue(runCatching { TaskGraph.validateReplacement(1, setOf(2), edges) }.isFailure)
        TaskGraph.validateReplacement(1, setOf(4, 5), edges)
    }
    @Test fun replacingAnOldEdgeDoesNotCreateAFalseCycle() {
        TaskGraph.validateReplacement(1, setOf(3), listOf(TaskDependency(1, 2), TaskDependency(2, 3)))
    }
    @Test fun reopenedDeletedAndMissingPrerequisitesBlock() {
        val task = PlannerItem(id = 1, title = "Work")
        val prerequisite = PlannerItem(id = 2, title = "Approval", isDone = true)
        val edges = listOf(TaskDependency(1, 2))
        assertTrue(TaskGraph.ready(task, mapOf(2L to prerequisite), edges))
        assertFalse(TaskGraph.ready(task, mapOf(2L to prerequisite.copy(isDone = false)), edges))
        assertFalse(TaskGraph.ready(task, mapOf(2L to prerequisite.copy(deletedAt = 1)), edges))
        assertFalse(TaskGraph.ready(task, emptyMap(), edges))
    }
    @Test fun manualBlockerAndCompletionAreIndependentOfDependencies() {
        val task = PlannerItem(id = 1, title = "Work", manualBlocker = "Waiting for input")
        assertFalse(TaskGraph.ready(task, emptyMap(), emptyList()))
        assertTrue(TaskGraph.ready(task.copy(manualBlocker = ""), emptyMap(), emptyList()))
        assertFalse(TaskGraph.ready(task.copy(manualBlocker = "", isDone = true), emptyMap(), emptyList()))
    }
    @Test fun aLongGraphIsCheckedWithoutRecursion() {
        val edges = (1L..10000L).map { TaskDependency(it, it + 1) }
        assertTrue(runCatching { TaskGraph.validateReplacement(10001, setOf(1), edges) }.isFailure)
    }
}
