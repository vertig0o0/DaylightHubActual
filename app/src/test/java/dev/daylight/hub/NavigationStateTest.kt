package dev.daylight.hub

import dev.daylight.hub.navigation.*
import org.junit.Assert.*
import org.junit.Test

class NavigationStateTest {
    @Test fun childRoutesSurviveASectionChangeAndSerialization() {
        val state=NavigationState(Destination.NOTES,PlannerTab.TASKS,projectId=18,taskId=4,noteId=9,editingNote=true)
        val restored=NavigationState.read(state.copy(destination=Destination.PLANNER).json())
        assertEquals(4L,restored.taskId);assertEquals(9L,restored.noteId);assertTrue(restored.editingNote);assertEquals(PlannerTab.TASKS,restored.plannerTab)
    }
    @Test fun invalidSavedRoutesFallBackWithoutInventingItemIds() {
        assertEquals(NavigationState(),NavigationState.read("bad JSON"))
        assertEquals(NavigationState(),NavigationState.read("""{"version":99}"""))
        val restored=NavigationState.read("""{"destination":"Home","taskId":-2,"projectId":0,"creatingTask":true}""")
        assertEquals(Destination.TODAY,restored.destination);assertNull(restored.taskId);assertNull(restored.projectId)
    }
}
