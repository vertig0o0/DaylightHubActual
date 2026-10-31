package dev.daylight.hub.data

import androidx.room.withTransaction
import dev.daylight.hub.tasks.TaskGraph
import org.json.JSONArray
import org.json.JSONObject

class WorkspaceRepository(private val db: HubDatabase, private val hub: HubRepository,
    private val now:()->java.time.ZonedDateTime={java.time.ZonedDateTime.now()}) {
    suspend fun moveTask(task:PlannerItem,neighbor:PlannerItem,before:Boolean)=db.withTransaction {
        val ordered=db.planner().all().filter{it.deletedAt==null}.sortedWith(compareBy<PlannerItem>{it.sortOrder}.thenBy{it.id}).toMutableList()
        val current=ordered.firstOrNull{it.id==task.id} ?: error("Task is unavailable.")
        val target=ordered.firstOrNull{it.id==neighbor.id} ?: error("The neighboring task is unavailable.")
        check(current.revision==task.revision && target.revision==neighbor.revision){"The task order changed. Try again from the refreshed list."}
        require(current.id!=target.id)
        ordered.removeAll{it.id==current.id}
        val index=ordered.indexOfFirst{it.id==target.id}+if(before)0 else 1
        ordered.add(index,current)
        ordered.forEachIndexed{position,item->val order=(position+1L)*1024L;if(item.sortOrder!=order)db.planner().update(item.copy(sortOrder=order,revision=item.revision+1))}
    }
    suspend fun addStartingSteps(taskId:Long,expected:Long,steps:List<String>)=db.withTransaction {
        require(steps.size in 1..3 && steps.all{it.isNotBlank() && it.length<=160}) { "Use one to three short steps." }
        val task=db.planner().get(taskId) ?: error("Task was removed.")
        check(task.revision==expected && task.deletedAt==null) { "Task changed. Request a fresh suggestion." }
        steps.forEach { hub.saveTask(PlannerItem(title=it,projectId=task.projectId,parentId=task.id)) }
        db.planner().update(task.copy(revision=task.revision+1))
    }
    suspend fun saveTask(task:PlannerItem, expected:Long?, prerequisites:Set<Long>):Long = db.withTransaction {
        val id=hub.saveTask(task,expected)
        setPrerequisites(id,prerequisites,db.planner().get(id)!!.revision)
        id
    }
    suspend fun saveProject(project: Project): Long = db.withTransaction {
        require(project.name.isNotBlank() && project.name.length<=100) { "Use a project name between 1 and 100 characters." }
        require(project.description.length <= 2000) { "Keep the project description brief." }
        val old = if (project.id == 0L) null else db.workspace().project(project.id) ?: error("Project was removed.")
        if (old != null) check(old.revision == project.revision) { "Project changed. Reopen it." }
        val saved = project.copy(name = project.name.trim(), updatedAt = System.currentTimeMillis(), revision = (old?.revision ?: -1) + 1)
        if (old == null) db.workspace().insertProject(saved) else { db.workspace().updateProject(saved); saved.id }
    }

    suspend fun removeProject(project: Project) = db.withTransaction {
        check(db.workspace().project(project.id)?.revision == project.revision) { "Project changed. Reopen it." }
        db.workspace().unassignTasks(project.id); db.workspace().unassignNotes(project.id)
        db.workspace().unassignConversations(project.id); db.workspace().deleteProject(project.id)
    }

    suspend fun setPrerequisites(taskId: Long, ids: Set<Long>, expected: Long) = db.withTransaction {
        val task = db.planner().get(taskId) ?: error("Task was removed.")
        check(task.revision == expected && task.deletedAt == null) { "Task changed. Review its prerequisites again." }
        val existing=db.workspace().allDependencies()
        val previous=existing.filter{it.taskId==taskId}.map{it.prerequisiteId}.toSet()
        ids.forEach { id -> val prerequisite=db.planner().get(id);require(prerequisite!=null && (prerequisite.deletedAt==null || id in previous)) { "A selected task is unavailable." } }
        TaskGraph.validateReplacement(taskId, ids, existing)
        db.workspace().clearDependencies(taskId)
        db.workspace().insertDependencies(ids.map { TaskDependency(taskId, it) })
        db.planner().update(task.copy(revision = task.revision + 1))
    }

    suspend fun addLabel(name: String): Long {
        val trimmed = name.trim()
        require(trimmed.isNotEmpty() && trimmed.length <= 40) { "Use a label between 1 and 40 characters." }
        return db.workspace().insertLabel(Label(name = trimmed))
    }

    suspend fun saveTemplate(project: Project, name: String): Long = db.withTransaction {
        require(name.isNotBlank()) { "Name this template." }
        check(db.workspace().project(project.id)?.revision==project.revision){"Project changed. Reopen it before saving a template."}
        require(name.length<=100){"Use a template name up to 100 characters."}
        require(db.workspace().templateTaskCount(project.id)<=1000 && db.workspace().templateTaskCharacters(project.id)+db.workspace().templateNoteCharacters(project.id)<=200000){"This project is too large for a template. Use up to 1,000 tasks and 200,000 text characters."}
        val tasks = db.workspace().templateTasks(project.id)
        val notes = db.workspace().templateNotes(project.id)
        require(notes.size<=200){"Use up to 200 notes in a template."}
        val ids = tasks.map { it.id }.toSet()
        val allEdges = db.workspace().templateEdges(project.id)
        require(allEdges.size<=5000){"Use up to 5,000 prerequisite links in a template."}
        val taskNames=allEdges.map{it.prerequisiteId}.filter{it !in ids}.distinct().associateWith{db.planner().get(it)?.title ?: "Deleted prerequisite"}
        val root = JSONObject().put("version", 2).put("description", project.description)
        val anchor=now()
        root.put("tasks", JSONArray().apply { tasks.forEach { task -> put(JSONObject()
            .put("key", task.id).put("title", task.title).put("details", task.details)
            .put("parent", task.parentId ?: JSONObject.NULL).put("priority", task.priority)
            .put("estimate", task.estimateMinutes ?: JSONObject.NULL)
            .put("dueDayOffset", task.dueAt?.let{java.time.temporal.ChronoUnit.DAYS.between(anchor.toLocalDate(),java.time.Instant.ofEpochMilli(it).atZone(anchor.zone).toLocalDate())} ?: JSONObject.NULL)
            .put("dueTime",task.dueAt?.let{java.time.Instant.ofEpochMilli(it).atZone(anchor.zone).toLocalTime().toString()} ?: JSONObject.NULL)
            .put("label",task.labelId ?: JSONObject.NULL)
            .put("blocker", task.manualBlocker)
            .put("external", allEdges.filter { it.taskId == task.id && it.prerequisiteId !in ids }.joinToString { edge -> taskNames[edge.prerequisiteId] ?: "Deleted prerequisite" })
            .put("prerequisites", JSONArray(allEdges.filter { it.taskId == task.id && it.prerequisiteId in ids }.map { it.prerequisiteId }))) } })
        root.put("notes", JSONArray().apply { notes.forEach { note -> put(JSONObject().put("key", note.id)
            .put("title", note.title).put("body", note.body).put("document", note.document).put("notebook", note.notebook).put("tags", note.tags).put("label",note.labelId ?: JSONObject.NULL)) } })
        val payload=root.toString()
        require(payload.toByteArray(Charsets.UTF_8).size<=1000000){"This template exceeds the 1 MB storage limit."}
        db.workspace().insertTemplate(ProjectTemplate(name = name.trim(), payload = payload))
    }

    suspend fun fromTemplate(templateId: Long, name: String): Long = db.withTransaction {
        val template = db.workspace().template(templateId) ?: error("Template was removed.")
        val root = JSONObject(template.payload)
        val version=root.getInt("version")
        require(version in 1..2) { "This template needs a newer app version." }
        val projectId = saveProject(Project(name = name, description = root.optString("description")))
        val inputTasks = root.getJSONArray("tasks")
        val inputNotes = root.getJSONArray("notes")
        require(inputTasks.length() <= 5000 && inputNotes.length() <= 1000) { "Template is too large." }
        val taskIds = mutableMapOf<Long, Long>(); val noteIds = mutableMapOf<Long, Long>()
        val orderBase=db.planner().lastOrder()
        val anchor=now()
        repeat(inputTasks.length()) { index ->
            val j = inputTasks.getJSONObject(index)
            val blocker = listOf(j.optString("blocker"), j.optString("external").takeIf { it.isNotBlank() }?.let { "External prerequisite not included: $it" }.orEmpty()).filter { it.isNotBlank() }.joinToString("\n")
            taskIds[j.getLong("key")] = hub.saveTask(PlannerItem(title = j.getString("title"), details = j.optString("details"),
                projectId = projectId, priority = j.getInt("priority"), estimateMinutes = if (j.isNull("estimate")) null else j.getInt("estimate"),
                labelId=if(j.isNull("label"))null else j.getLong("label").takeIf{db.workspace().label(it)!=null},
                dueAt = if(version==1){if(j.isNull("dueOffset"))null else anchor.toLocalDate().atStartOfDay(anchor.zone).toInstant().toEpochMilli()+j.getLong("dueOffset")}
                    else if(j.isNull("dueDayOffset"))null else anchor.toLocalDate().plusDays(j.getLong("dueDayOffset")).atTime(java.time.LocalTime.parse(j.getString("dueTime"))).atZone(anchor.zone).toInstant().toEpochMilli(),
                manualBlocker = blocker, sortOrder = Math.addExact(orderBase,(index+1L)*1024L)))
        }
        repeat(inputTasks.length()) { index ->
            val j = inputTasks.getJSONObject(index); val id = taskIds.getValue(j.getLong("key"))
            val task = db.planner().get(id)!!
            val parent = if (j.isNull("parent")) null else taskIds[j.getLong("parent")]
            hub.saveTask(task.copy(parentId = parent), task.revision)
            val deps = j.getJSONArray("prerequisites")
            val prerequisites = (0 until deps.length()).map { taskIds.getValue(deps.getLong(it)) }.toSet()
            setPrerequisites(id, prerequisites, db.planner().get(id)!!.revision)
        }
        repeat(inputNotes.length()) { index ->
            val j = inputNotes.getJSONObject(index)
            noteIds[j.getLong("key")] = hub.saveNote(Note(title = j.getString("title"), body = j.getString("body"),
                notebook = j.getString("notebook"), tags = j.getString("tags"), projectId = projectId,
                labelId=if(j.isNull("label"))null else j.getLong("label").takeIf{db.workspace().label(it)!=null}))
        }
        repeat(inputNotes.length()) { index ->
            val j = inputNotes.getJSONObject(index); val note = db.notes().get(noteIds.getValue(j.getLong("key")))!!
            fun remap(text:String) = Regex("daylighthub://note/(\\d+)").replace(text) { match ->
                noteIds[match.groupValues[1].toLong()]?.let { "daylighthub://note/$it" } ?: "template-reference-unavailable"
            }
            hub.saveNote(note.copy(document = remap(j.getString("document")),body=remap(j.getString("body"))), note.revision)
        }
        projectId
    }
}
