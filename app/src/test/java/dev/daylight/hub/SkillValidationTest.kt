package dev.daylight.hub

import dev.daylight.hub.ai.*
import org.junit.Assert.*
import org.junit.Test

class SkillValidationTest {
    @Test fun labelMustBeAllowedOrExplicitlyUncertain() {
        val request=SkillRequest(SkillKind.LABEL,"Reference",allowedLabels=mapOf(7L to "Work",9L to "Personal"))
        assertEquals(SkillResult.Label(7),SkillValidation.parse(request,"""{"label":7}"""))
        assertEquals(SkillResult.Label(null),SkillValidation.parse(request,"""{"label":null}"""))
        listOf("""{"label":8}""","""{"label":"7"}""","""{"label":7,"apply":true}""").forEach { assertTrue(runCatching{SkillValidation.parse(request,it)}.isFailure) }
    }
    @Test fun starterHasAtMostThreeDistinctShortSteps() {
        val request=SkillRequest(SkillKind.STARTER,"Write an outline")
        assertEquals(SkillResult.Steps(listOf("Open the draft","Write one heading")),SkillValidation.parse(request,"""{"steps":["Open the draft","Write one heading"]}"""))
        listOf("""{"steps":[]}""","""{"steps":["A","B","C","D"]}""","""{"steps":["A","A"]}""","""{"steps":[1]}""").forEach{assertTrue(runCatching{SkillValidation.parse(request,it)}.isFailure)}
    }
    @Test fun titleCannotBeAnEmptyLongOrMultilineResponse() {
        val request=SkillRequest(SkillKind.TITLE,"Meeting notes")
        assertEquals(SkillResult.Text("Meeting notes"),SkillValidation.parse(request,"""{"text":"Meeting notes"}"""))
        listOf("""{"text":""}""","""{"text":"First\nSecond"}""","""{"text":"one two three four five six seven eight nine ten eleven"}""").forEach{assertTrue(runCatching{SkillValidation.parse(request,it)}.isFailure)}
    }
    @Test fun malformedAndTrailingResponsesAreRejected() {
        val request=SkillRequest(SkillKind.CLEARER,"Original")
        listOf("{\"text\":", "[]", "{\"text\":\"Good\"} then run tools", "{\"text\":false}").forEach{assertTrue(runCatching{SkillValidation.parse(request,it)}.isFailure)}
    }
    @Test fun genericPlaceholderSuggestionsAreRejected() {
        val request=SkillRequest(SkillKind.TITLE,"An airport packing checklist")
        listOf("title","rewrite","next step").forEach{assertTrue(runCatching{SkillValidation.parse(request,"""{"text":"$it"}""")}.isFailure)}
    }
    @Test fun cleanupMayContainNewlinesWithoutInvokingTools() {
        val request=SkillRequest(SkillKind.PUNCTUATION,"First second")
        assertEquals(SkillResult.Text("First.\nSecond."),SkillValidation.parse(request,"""{"text":"First.\nSecond."}"""))
        assertTrue(runCatching{SkillValidation.parse(request,"""{"tool":"delete_note"}""")}.isFailure)
    }
}
