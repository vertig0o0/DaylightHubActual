package dev.daylight.hub

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.daylight.hub.data.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WorkspaceIntegrationTest {
    private val context=ApplicationProvider.getApplicationContext<Context>()
    private fun database()=Room.inMemoryDatabaseBuilder(context,HubDatabase::class.java).build()
    @Test fun pagedNoteSearchPreservesUnicodeCaseAndLiteralWildcards()=runBlocking {
        val db=database()
        try {
            val hub=HubRepository(db)
            val target=hub.saveNote(Note(title="Çalışma Σ ẞ",body="Keep [literal]*_? characters"))
            hub.saveNote(Note(title="Unrelated",body="Keep literalX characters"))
            suspend fun matches(query:String):List<Long> {
                val result=db.notes().page(query,CaseInsensitiveGlob.pattern(query),"",false).load(androidx.paging.PagingSource.LoadParams.Refresh(null,32,false))
                return (result as androidx.paging.PagingSource.LoadResult.Page).data.map{it.id}
            }
            for(query in listOf("çalışma","ς","ß","[literal]*_?"))assertEquals(query,listOf(target),matches(query))
        }finally{db.close()}
    }
    @Test fun removedRelationshipsRemainEditableAndCanBeDetachedExplicitly()=runBlocking {
        val db=database()
        try {
            val hub=HubRepository(db);val work=WorkspaceRepository(db,hub)
            val parent=hub.saveTask(PlannerItem(title="Parent"));val child=hub.saveTask(PlannerItem(title="Child",parentId=parent))
            val dependent=hub.saveTask(PlannerItem(title="Dependent"));work.setPrerequisites(dependent,setOf(parent),0)
            hub.deleteTask(parent,0)
            hub.saveTask(db.planner().get(child)!!.copy(details="Still editable"),0)
            val current=db.planner().get(dependent)!!;work.saveTask(current.copy(details="Preserve the missing prerequisite"),current.revision,setOf(parent))
            assertEquals(parent,db.planner().get(child)!!.parentId);assertEquals(listOf(TaskDependency(dependent,parent)),db.workspace().allDependencies())
            assertTrue(db.workspace().readyTasks().first().none{it.id==dependent})
            assertTrue(runCatching{hub.saveTask(PlannerItem(title="Invalid new child",parentId=parent))}.isFailure)
            val edited=db.planner().get(child)!!;hub.saveTask(edited.copy(parentId=null),edited.revision)
            assertNull(db.planner().get(child)!!.parentId)
        }finally{db.close()}
    }
    @Test fun manualReorderingIsAtomicAndKeepsDependenciesAndContent()=runBlocking {
        val db=database()
        try {
            val hub=HubRepository(db);val work=WorkspaceRepository(db,hub)
            val a=hub.saveTask(PlannerItem(title="A",details="Keep A"));val b=hub.saveTask(PlannerItem(title="B"));val c=hub.saveTask(PlannerItem(title="C"))
            work.setPrerequisites(b,setOf(a),0)
            val oldC=db.planner().get(c)!!;work.moveTask(oldC,db.planner().get(a)!!,true)
            fun List<PlannerItem>.ordered()=sortedWith(compareBy<PlannerItem>{it.sortOrder}.thenBy{it.id}).map{it.id}
            assertEquals(listOf(c,a,b),db.planner().all().ordered());assertEquals("Keep A",db.planner().get(a)!!.details)
            assertEquals(listOf(TaskDependency(b,a)),db.workspace().allDependencies())
            assertTrue(runCatching{work.moveTask(oldC,db.planner().get(b)!!,false)}.isFailure)
            assertEquals(listOf(c,a,b),db.planner().all().ordered())
        }finally{db.close()}
    }
    @Test fun reviewedStartingStepsApplyOnlyOnceToTheExpectedRevision()=runBlocking {
        val db=database()
        try {
            val hub=HubRepository(db);val work=WorkspaceRepository(db,hub);val parent=hub.saveTask(PlannerItem(title="Report"))
            work.addStartingSteps(parent,0,listOf("Open the draft","Check the totals"))
            assertEquals(2,db.planner().all().count{it.parentId==parent})
            assertTrue(runCatching{work.addStartingSteps(parent,0,listOf("Duplicate retry"))}.isFailure)
            assertEquals(3,db.planner().all().size)
        }finally{db.close()}
    }

    @Test fun readyQueueTracksReopenDeletionRestorationAndManualBlockers()=runBlocking {
        val db=database()
        try {
            val hub=HubRepository(db);val work=WorkspaceRepository(db,hub)
            val a=hub.saveTask(PlannerItem(title="Approval"));val b=hub.saveTask(PlannerItem(title="Publish"))
            work.setPrerequisites(b,setOf(a),0)
            assertEquals(listOf(a),db.workspace().readyTasks().first().map{it.id})
            hub.saveTask(db.planner().get(a)!!.copy(isDone=true),0)
            assertEquals(listOf(b),db.workspace().readyTasks().first().map{it.id})
            hub.deleteTask(a,1);assertTrue(db.workspace().readyTasks().first().isEmpty())
            hub.restoreTask(a,2);assertEquals(listOf(b),db.workspace().readyTasks().first().map{it.id})
            hub.saveTask(db.planner().get(b)!!.copy(manualBlocker="Waiting"),1)
            assertTrue(db.workspace().readyTasks().first().isEmpty())
            hub.saveTask(db.planner().get(a)!!.copy(isDone=false),3)
            hub.saveTask(db.planner().get(b)!!.copy(manualBlocker=""),2)
            assertEquals(listOf(a),db.workspace().readyTasks().first().map{it.id})
        } finally { db.close() }
    }
    @Test fun cyclicEditRollsBackTheWholeTaskSave()=runBlocking {
        val db=database()
        try {
            val hub=HubRepository(db);val work=WorkspaceRepository(db,hub)
            val a=hub.saveTask(PlannerItem(title="Original A"));val b=hub.saveTask(PlannerItem(title="B"))
            work.setPrerequisites(a,setOf(b),0)
            val old=db.planner().get(b)!!
            assertTrue(runCatching{work.saveTask(old.copy(title="Must roll back"),old.revision,setOf(a))}.isFailure)
            assertEquals("B",db.planner().get(b)!!.title)
            assertEquals(listOf(TaskDependency(a,b)),db.workspace().allDependencies())
        } finally { db.close() }
    }
    @Test fun opposingConcurrentEdgesCannotCommitACycle()=runBlocking {
        val db=database()
        try {
            val hub=HubRepository(db);val work=WorkspaceRepository(db,hub)
            val a=hub.saveTask(PlannerItem(title="A"));val b=hub.saveTask(PlannerItem(title="B"))
            val outcomes=awaitAll(async(Dispatchers.Default){runCatching{work.setPrerequisites(a,setOf(b),0)}},async(Dispatchers.Default){runCatching{work.setPrerequisites(b,setOf(a),0)}})
            assertEquals(1,outcomes.count{it.isSuccess});assertEquals(1,db.workspace().allDependencies().size)
        } finally { db.close() }
    }
    @Test fun projectDeletionAndParentMoveKeepItemsAndStableIds()=runBlocking {
        val db=database()
        try {
            val hub=HubRepository(db);val work=WorkspaceRepository(db,hub)
            val p=work.saveProject(Project(name="A"));val q=work.saveProject(Project(name="B"))
            val parent=hub.saveTask(PlannerItem(title="Parent",projectId=p))
            val child=hub.saveTask(PlannerItem(title="Child",projectId=p,parentId=parent))
            val note=hub.saveNote(Note(title="Note",body="Keep this",projectId=q))
            hub.saveTask(db.planner().get(parent)!!.copy(projectId=q),0)
            assertEquals(q,db.planner().get(child)!!.projectId)
            work.removeProject(db.workspace().project(q)!!)
            assertNull(db.planner().get(parent)!!.projectId);assertNull(db.planner().get(child)!!.projectId)
            assertNull(db.notes().get(note)!!.projectId);assertEquals("Keep this",db.notes().get(note)!!.body)
        } finally { db.close() }
    }
    @Test fun templateClonesTaskEdgesAndRemapsNoteLinksIndependently()=runBlocking {
        val db=database()
        try {
            val hub=HubRepository(db);val work=WorkspaceRepository(db,hub)
            val p=work.saveProject(Project(name="Source"))
            val a=hub.saveTask(PlannerItem(title="A",projectId=p,isDone=true))
            val b=hub.saveTask(PlannerItem(title="B",projectId=p,parentId=a))
            work.setPrerequisites(b,setOf(a),0)
            val target=hub.saveNote(Note(title="Target",body="Text",projectId=p))
            hub.saveNote(Note(title="Source",body="Link",document="""{"url":"daylighthub://note/$target"}""",projectId=p))
            val template=work.saveTemplate(db.workspace().project(p)!!,"Reusable")
            val first=work.fromTemplate(template,"First");val second=work.fromTemplate(template,"Second")
            val firstTasks=db.planner().all().filter{it.projectId==first};val secondTasks=db.planner().all().filter{it.projectId==second}
            assertTrue(firstTasks.none{it.isDone});assertTrue(firstTasks.map{it.id}.intersect(secondTasks.map{it.id}.toSet()).isEmpty())
            val firstA=firstTasks.single{it.title=="A"};val firstB=firstTasks.single{it.title=="B"}
            assertEquals(firstA.id,firstB.parentId);assertTrue(TaskDependency(firstB.id,firstA.id) in db.workspace().allDependencies())
            val copiedNotes=db.notes().all().filter{it.projectId==first}
            assertTrue(copiedNotes.single{it.title=="Source"}.document.contains("daylighthub://note/${copiedNotes.single{it.title=="Target"}.id}"))
            hub.saveTask(firstA.copy(title="Changed"),firstA.revision)
            assertTrue(secondTasks.any{it.title=="A"})
        } finally { db.close() }
    }
    @Test fun onlyOneFocusSessionCanOccupyTheActiveSlot()=runBlocking {
        val db=database()
        try {
            val sample=FocusSession(title="Focus",durationMs=60000,remainingMs=60000)
            val id=db.focus().insert(sample)
            assertTrue(runCatching{db.focus().insert(sample)}.isFailure)
            db.focus().update(sample.copy(id=id,activeSlot=null,status="ENDED"))
            assertTrue(db.focus().insert(sample)>id)
        } finally { db.close() }
    }
    @Test fun templatesKeepLocalDeadlineTimeAcrossDaylightSavingChanges()=runBlocking {
        val db=database()
        val zone=java.time.ZoneId.of("Europe/Berlin")
        var date=java.time.ZonedDateTime.of(2026,3,28,12,0,0,0,zone)
        try {
            val hub=HubRepository(db);val work=WorkspaceRepository(db,hub){date}
            val project=work.saveProject(Project(name="Calendar template"))
            val label=work.addLabel("Template label")
            val deadline=java.time.ZonedDateTime.of(2026,3,29,9,30,0,0,zone)
            hub.saveTask(PlannerItem(title="At 9:30 tomorrow",projectId=project,dueAt=deadline.toInstant().toEpochMilli(),labelId=label))
            val template=work.saveTemplate(db.workspace().project(project)!!,"DST-safe")
            date=java.time.ZonedDateTime.of(2026,10,24,12,0,0,0,zone)
            val copy=work.fromTemplate(template,"October")
            val cloned=db.workspace().templateTasks(copy).single()
            val due=java.time.Instant.ofEpochMilli(cloned.dueAt!!).atZone(zone)
            assertEquals(java.time.LocalDate.of(2026,10,25),due.toLocalDate())
            assertEquals(java.time.LocalTime.of(9,30),due.toLocalTime());assertEquals(label,cloned.labelId)
            hub.saveNote(Note(title="Large note",body="x".repeat(200001),projectId=project))
            assertTrue(runCatching{work.saveTemplate(db.workspace().project(project)!!,"Too large")}.isFailure)
        }finally{db.close()}
    }
}
