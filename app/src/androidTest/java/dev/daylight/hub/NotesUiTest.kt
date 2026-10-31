package dev.daylight.hub

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NotesUiTest {
    @get:Rule val rule=createAndroidComposeRule<MainActivity>()
    @Test fun writingAreaAndSaveStayVisibleWithTheKeyboard() {
        rule.onNodeWithText("Notes",useUnmergedTree=true).performClick()
        rule.onNodeWithContentDescription("New note").performClick()
        rule.onNodeWithContentDescription("Note content").performClick()
        rule.waitUntil(10000) {
            var visible=false
            rule.runOnIdle{visible=androidx.core.view.ViewCompat.getRootWindowInsets(rule.activity.window.decorView)?.isVisible(androidx.core.view.WindowInsetsCompat.Type.ime())==true}
            visible
        }
        rule.onNodeWithText("Save",substring=false).assertIsDisplayed()
        rule.onNodeWithContentDescription("Note content").assertIsDisplayed().assertHeightIsAtLeast(96.dp)
        rule.onNodeWithContentDescription("Note content").performTextReplacement("A visible writing area")
        rule.onNodeWithContentDescription("Note content").assertTextContains("A visible writing area",substring=true)
    }
    @Test fun formattedNoteSavesAndReopens() {
        val title="UI acceptance "+System.currentTimeMillis()
        rule.onNodeWithText("Notes",useUnmergedTree=true).performClick()
        rule.onNodeWithContentDescription("New note").performClick()
        rule.onNodeWithContentDescription("Note title").performTextInput(title)
        rule.onNodeWithText("B",substring=false).performClick()
        rule.onNodeWithContentDescription("Note content").performTextInput("A private thought")
        rule.onNodeWithText("Save",substring=false).performClick()
        rule.waitUntil(10000){rule.onAllNodesWithText("Saved").fetchSemanticsNodes().isNotEmpty()}
        rule.onNodeWithContentDescription("Notes menu").performClick()
        rule.waitUntil(10000){rule.onAllNodesWithContentDescription("New note").fetchSemanticsNodes().isNotEmpty()}
        rule.onNodeWithText(title,substring=false).performClick()
        rule.onNodeWithText("A private thought",substring=true).assertExists()
    }
    @Test fun darkCalendarSupportsLargerTextAndDayView() {
        val vm=androidx.lifecycle.ViewModelProvider(rule.activity)[HubViewModel::class.java]
        val before=vm.appearance
        try {
            rule.runOnIdle { vm.saveAppearance(before.copy(mode="Dark",textScale=1.3f));vm.plannerTab="Calendar";vm.section="Planner" }
            rule.onNodeWithText("Month",substring=false).assertIsSelected()
            rule.waitForIdle()
            val image=androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().uiAutomation.takeScreenshot()
            java.io.File(rule.activity.filesDir,"qa-calendar-dark.png").outputStream().use { image.compress(android.graphics.Bitmap.CompressFormat.PNG,100,it) }
            image.recycle()
            rule.onNodeWithText("Day",substring=false).performClick()
            rule.onNodeWithText("Day",substring=false).assertIsSelected()
            rule.onNodeWithText("12:00",substring=false).assertExists()
        } finally { rule.runOnIdle { vm.saveAppearance(before) } }
    }
}
