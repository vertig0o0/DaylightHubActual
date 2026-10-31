package dev.daylight.hub

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Rule
import org.junit.Test

class ConversationUiTest {
    @get:Rule val compose=createAndroidComposeRule<MainActivity>()

    @Test fun unloadedModelConversationSurvivesActivityRecreation() {
        val message="Keep this offline conversation ${System.currentTimeMillis()}"
        compose.onNodeWithText("AI").performClick()
        compose.onNodeWithContentDescription("New conversation").performClick()
        compose.onNodeWithText("Message your assistant…").performTextInput(message)
        compose.onNodeWithContentDescription("Send").performClick()
        compose.waitUntil(10000){compose.onAllNodes(hasText(message) and !hasClickAction()).fetchSemanticsNodes().isNotEmpty()}
        compose.activityRule.scenario.recreate()
        compose.onNodeWithText("AI").performClick()
        compose.waitUntil(10000){compose.onAllNodes(hasText(message) and !hasClickAction()).fetchSemanticsNodes().isNotEmpty()}
        compose.onNode(hasText(message) and !hasClickAction()).assertExists()
        compose.onNodeWithText("Message your assistant…").performTextInput("Unsent draft")
        compose.activityRule.scenario.recreate()
        compose.onNodeWithText("AI").performClick()
        compose.onNode(hasSetTextAction() and hasText("Unsent draft")).assertExists()
        compose.onNode(hasSetTextAction() and hasText("Unsent draft")).performTextClearance()
    }
}
