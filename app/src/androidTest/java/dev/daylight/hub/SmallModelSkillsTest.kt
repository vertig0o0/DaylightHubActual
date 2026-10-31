package dev.daylight.hub

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.daylight.hub.ai.*
import dev.daylight.hub.data.*
import kotlinx.coroutines.*
import org.junit.Assert.*
import org.junit.Assume
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class SmallModelSkillsTest {
    private fun verify(id:String)=runBlocking {
        val context=ApplicationProvider.getApplicationContext<Context>();val file=File(context.filesDir,"models/$id.gguf")
        Assume.assumeTrue("Provide the regression GGUF $id",file.isFile)
        val db=Room.inMemoryDatabaseBuilder(context,HubDatabase::class.java).build();val engine=InferenceEngine(context)
        try {
            val parameters=GenerationParameters(contextSize=1024,threads=2,temperature=0f,maxTokens=96,seed=0)
            val profile=ModelProfile("skills-$id","Skills $id",file.name,file.length(),GgufInspector.inspect(file),parameters.json())
            db.models().put(profile);withTimeout(90000){engine.load(profile)}
            val skills=SmallModelSkills(engine,db.models())
            val requests=listOf(
                SkillRequest(SkillKind.TITLE,"Pack a charger, passport, and headphones before leaving for the airport."),
                SkillRequest(SkillKind.LABEL,"The invoice is waiting for a supplier reply.",allowedLabels=mapOf(1L to "Waiting",2L to "Personal")),
                SkillRequest(SkillKind.STARTER,"Send the draft report to Alex."),
                SkillRequest(SkillKind.PUNCTUATION,"please bring milk bread and eggs tomorrow"),
                SkillRequest(SkillKind.HANDOFF,"Next open the budget worksheet and check the total before sending it.")
            )
            val report=StringBuilder()
            val errors=mutableListOf<String>()
            requests.forEach{request->
                try {
                val suggestion=skills.suggest(request)
                report.append(request.kind).append(": ").append(suggestion).append('\n')
                when(suggestion){is SkillResult.Text->assertTrue(suggestion.value.isNotBlank());is SkillResult.Steps->assertTrue(suggestion.values.size in 1..3);is SkillResult.Label->assertTrue(suggestion.id==null || suggestion.id in request.allowedLabels)}
                }catch(error:Exception){if(error is CancellationException)throw error;errors.add("${request.kind}: ${error.message}");report.append(errors.last()).append('\n')}
            }
            assertTrue("Suggestion generation must not write tasks",db.planner().all().isEmpty())
            assertTrue("Suggestion generation must not write notes",db.notes().all().isEmpty())
            File(context.filesDir,"v3-skills-$id-report.txt").writeText(report.toString())
            assertTrue(report.toString(),errors.isEmpty())
        }finally{engine.close();db.close()}
    }
    @Test fun skillsWorkWithThe230MModelWithoutAutomaticWrites()=verify("lfm230")
    @Test fun skillsWorkWithThe360MModelWithoutAutomaticWrites()=verify("smoke360")
}
