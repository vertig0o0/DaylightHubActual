package dev.daylight.hub.tasks

import dev.daylight.hub.data.PlannerItem
import dev.daylight.hub.data.TaskDependency

/** Pure, iterative graph operations; never run model inference to calculate readiness. */
object TaskGraph {
    fun validateReplacement(taskId: Long, prerequisiteIds: Set<Long>, edges: List<TaskDependency>) {
        require(taskId !in prerequisiteIds) { "A task cannot depend on itself." }
        val graph = edges.filter { it.taskId != taskId }.groupBy({ it.taskId }, { it.prerequisiteId })
        for (candidate in prerequisiteIds) {
            val pending = ArrayDeque<Long>(); pending.add(candidate)
            val seen = mutableSetOf<Long>()
            while (pending.isNotEmpty()) {
                val next = pending.removeLast()
                require(next != taskId) { "This prerequisite would create a circular dependency." }
                if (seen.add(next)) graph[next].orEmpty().forEach(pending::add)
            }
        }
    }

    fun blockedBy(task: PlannerItem, tasks: Map<Long, PlannerItem>, edges: List<TaskDependency>): List<Long> =
        edges.asSequence().filter { it.taskId == task.id }.map { it.prerequisiteId }
            .filter { id -> tasks[id]?.let { !it.isDone || it.deletedAt != null } ?: true }.toList()

    fun ready(task: PlannerItem, tasks: Map<Long, PlannerItem>, edges: List<TaskDependency>): Boolean =
        !task.isDone && task.deletedAt == null && task.manualBlocker.isBlank() && blockedBy(task, tasks, edges).isEmpty()
}
