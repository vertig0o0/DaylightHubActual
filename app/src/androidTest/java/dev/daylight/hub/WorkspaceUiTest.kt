package dev.daylight.hub

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Rule
import org.junit.Test

class WorkspaceUiTest {
    @get:Rule val compose=createAndroidComposeRule<MainActivity>()

    @Test fun projectTaskAndTimeFilterWorkWithoutAModel() {
        val suffix=System.currentTimeMillis().toString()
        val project="Workspace UI $suffix";val task="Seven minute task $suffix"
        compose.onNodeWithText("Planner").performClick()
        compose.onNodeWithText("Projects").performClick()
        compose.onNodeWithContentDescription("New project").performClick()
        compose.onNodeWithText("Name").performTextInput(project)
        compose.onNodeWithText("Save project").performScrollTo().performClick()
        compose.waitUntil(10000){compose.onAllNodesWithText(project).fetchSemanticsNodes().isNotEmpty()}
        compose.onNodeWithText("Add task").performClick()
        compose.onNodeWithText("Task title").performTextInput(task)
        compose.onNodeWithText("Estimated minutes · optional").performScrollTo().performTextInput("7")
        compose.onNodeWithText("Save task").performScrollTo().performClick()
        compose.waitUntil(10000){compose.onAllNodesWithText("Save task").fetchSemanticsNodes().isEmpty() && compose.onAllNodesWithText(task).fetchSemanticsNodes().isNotEmpty()}
        compose.onNodeWithText("Ready").performClick()
        compose.onNodeWithText("I have time").performClick()
        compose.onNodeWithText("5 min").performClick()
        compose.onNodeWithText(task).assertDoesNotExist()
        compose.onNodeWithText("5 minutes").performClick()
        compose.onNodeWithText("15 min").performClick()
        compose.onNodeWithText(task).performScrollTo().assertIsDisplayed()
    }
}
