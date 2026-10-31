package dev.daylight.hub

import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.lifecycle.ViewModelProvider
import dev.daylight.hub.ai.*
import dev.daylight.hub.ui.*
import kotlinx.coroutines.awaitCancellation
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.atomic.AtomicBoolean

class SkillReviewUiTest {
    @get:Rule val compose=createAndroidComposeRule<MainActivity>()

    @Test fun aSuggestionCanBeEditedAndOnlyAppliesAfterReview() {
        var applied="";var count=0
        compose.activityRule.scenario.onActivity{activity->
            val vm=ViewModelProvider(activity)[HubViewModel::class.java]
            activity.setContent {
                var show by remember{mutableStateOf(true)}
                HubTheme(Appearance()){if(show)ReviewedSkillDialog(vm,SkillReview("Review title",SkillRequest(SkillKind.TITLE,"Original source")){result->applied=(result as SkillResult.Text).value;count++},{show=false}){SkillResult.Text("Generated title")}}
            }
        }
        compose.onNode(hasSetTextAction() and hasText("Generated title")).assertExists()
        compose.runOnIdle{assertEquals(0,count);assertEquals("",applied)}
        compose.onNode(hasSetTextAction() and hasText("Generated title")).performTextReplacement("My reviewed title")
        compose.onNodeWithText("Apply").performClick()
        compose.waitUntil{count==1}
        compose.runOnIdle{assertEquals("My reviewed title",applied);assertEquals(1,count)}
        compose.onNodeWithText("Apply").assertDoesNotExist()
    }

    @Test fun dismissingGenerationCancelsItWithoutApplying() {
        val started=AtomicBoolean();val cancelled=AtomicBoolean();var count=0
        compose.activityRule.scenario.onActivity{activity->
            val vm=ViewModelProvider(activity)[HubViewModel::class.java]
            activity.setContent {
                var show by remember{mutableStateOf(true)}
                HubTheme(Appearance()){if(show)ReviewedSkillDialog(vm,SkillReview("Pending",SkillRequest(SkillKind.TITLE,"Keep this source")){count++},{show=false}){
                    started.set(true);try{awaitCancellation()}finally{cancelled.set(true)}
                }}
            }
        }
        compose.waitUntil{started.get()};compose.onNodeWithText("Dismiss").performClick();compose.waitUntil{cancelled.get()}
        compose.runOnIdle{assertEquals(0,count)}
    }

    @Test fun staleContentKeepsTheSource() {
        var source="Original";var revision=1L
        compose.activityRule.scenario.onActivity{activity->
            val vm=ViewModelProvider(activity)[HubViewModel::class.java]
            val captured=revision
            activity.setContent {
                HubTheme(Appearance()){ReviewedSkillDialog(vm,SkillReview("Stale review",SkillRequest(SkillKind.TITLE,source)){
                    check(revision==captured){"The note changed. Request a fresh suggestion."};source=(it as SkillResult.Text).value
                },{}){SkillResult.Text("A title")}}
            }
        }
        compose.onNodeWithText("Apply").assertIsEnabled()
        compose.runOnIdle{revision++}
        compose.onNodeWithText("Apply").performClick()
        compose.onNodeWithText("The note changed. Request a fresh suggestion.").assertExists()
        compose.runOnIdle{assertEquals("Original",source)}
    }
}
