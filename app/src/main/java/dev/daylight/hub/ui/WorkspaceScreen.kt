package dev.daylight.hub.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.daylight.hub.HubViewModel
import dev.daylight.hub.HandoffContext
import dev.daylight.hub.data.*
import dev.daylight.hub.tasks.TaskGraph
import dev.daylight.hub.ai.*
import org.json.JSONObject

@Composable fun PlannerHub(vm: HubViewModel) {
    val tasks by vm.tasks.collectAsStateWithLifecycle()
    val projects by vm.projects.collectAsStateWithLifecycle()
    val dependencies by vm.dependencies.collectAsStateWithLifecycle()
    val ready by vm.readyTasks.collectAsStateWithLifecycle()
    var query by rememberSaveable { mutableStateOf("") }
    var sort by rememberSaveable { mutableStateOf("Priority") }
    var minutes by rememberSaveable { mutableStateOf<Int?>(null) }
    var customTime by remember { mutableStateOf(false) }
    var timeMenu by remember { mutableStateOf(false) }
    var showUnestimated by rememberSaveable { mutableStateOf(false) }
    var projectEditor by remember { mutableStateOf<Project?>(null) }
    var projectPicker by remember { mutableStateOf(false) }
    var templatePicker by remember { mutableStateOf(false) }
    val activeProject = projects.firstOrNull { it.id == vm.selectedProject }
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Ready", "Tasks", "Calendar", "Projects").forEach { tab ->
                FilterChip(vm.plannerTab == tab, { vm.plannerTab = tab }, label = { Text(tab) })
            }
        }
        when (vm.plannerTab) {
            "Calendar" -> PlannerScreen(vm)
            "Projects" -> {
                if (activeProject != null) ProjectOverview(vm, activeProject, { projectEditor = it })
                else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Your projects", Modifier.weight(1f), style = MaterialTheme.typography.titleLarge)
                        TextButton(onClick = { templatePicker = true }) { Text("Templates") }
                        FilledTonalIconButton(onClick = { projectEditor = Project(name = "") }) { Icon(Icons.Default.Add, "New project") }
                    }
                    OutlinedTextField(query, { query = it }, Modifier.fillMaxWidth(), singleLine = true, placeholder = { Text("Find a project") }, leadingIcon = { Icon(Icons.Default.Search, null) })
                    val visible = projects.filter { it.name.contains(query, true) }
                    if (visible.isEmpty()) EmptyState("A place for related work", "Create a project to bring tasks, notes, and conversations together.")
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(bottom = 24.dp)) {
                        items(visible, key = { it.id }) { project ->
                            val projectTasks = tasks.filter { it.projectId == project.id }
                            Card(onClick = { vm.selectedProject = project.id }, modifier = animatedItem()) {
                                Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text(project.name, style = MaterialTheme.typography.titleMedium)
                                    if (project.description.isNotBlank()) Text(project.description, maxLines = 2, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text("${projectTasks.count { it.isDone }} of ${projectTasks.size} tasks complete" + if (project.archived) " · Archived" else "", style = MaterialTheme.typography.labelMedium)
                                    if (projectTasks.isNotEmpty()) LinearProgressIndicator(progress = { projectTasks.count { it.isDone }.toFloat() / projectTasks.size }, modifier = Modifier.fillMaxWidth())
                                }
                            }
                        }
                    }
                }
            }
            else -> {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(if (vm.plannerTab == "Ready") "Ready to work" else "All tasks", Modifier.weight(1f), style = MaterialTheme.typography.titleLarge)
                    FilledTonalIconButton(onClick = { vm.newTask(vm.selectedProject) }) { Icon(Icons.Default.Add, "New task") }
                }
                OutlinedTextField(query, { query = it }, Modifier.fillMaxWidth(), singleLine = true, placeholder = { Text("Find a task") }, leadingIcon = { Icon(Icons.Default.Search, null) })
                Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box {
                        FilterChip(minutes!=null,{timeMenu=true},label={Text(minutes?.let{"$it minutes"} ?: "I have time")},leadingIcon={Icon(Icons.Default.Schedule,null)})
                        DropdownMenu(timeMenu,{timeMenu=false}) {
                            DropdownMenuItem(text={Text("Any time")},onClick={minutes=null;showUnestimated=false;timeMenu=false})
                            listOf(5,15,30,60).forEach{value->DropdownMenuItem(text={Text("$value min")},onClick={minutes=value;showUnestimated=false;timeMenu=false})}
                            DropdownMenuItem(text={Text("Custom time")},onClick={timeMenu=false;customTime=true})
                        }
                    }
                    FilterChip(activeProject != null, { projectPicker = true }, label = { Text(activeProject?.name ?: "All projects") })
                    AssistChip(onClick = { sort = when (sort) { "Priority" -> "Deadline"; "Deadline" -> "Duration"; "Duration" -> "Manual";else -> "Priority" } }, label = { Text(sort) }, leadingIcon = { Icon(Icons.Default.Sort, null) })
                }
                val source = if (vm.plannerTab == "Ready") ready else tasks
                val filtered = source.filter { task ->
                    (vm.selectedProject == null || task.projectId == vm.selectedProject) &&
                        (task.projectId == vm.selectedProject || projects.none { it.id == task.projectId && it.archived }) &&
                        (task.title + " " + task.details).contains(query, true)
                }
                val unknown = if (minutes == null) 0 else filtered.count { it.estimateMinutes == null }
                val visible = filtered.filter { if(showUnestimated && minutes!=null) it.estimateMinutes==null else minutes == null || it.estimateMinutes?.let { estimate -> estimate <= minutes!! } == true }.let { list ->
                    when (sort) {
                        "Manual"->list.sortedWith(compareBy<PlannerItem>{it.sortOrder}.thenBy{it.id})
                        "Deadline" -> list.sortedWith(compareBy<PlannerItem> { it.isDone }.thenBy { it.dueAt ?: Long.MAX_VALUE }.thenByDescending { it.priority }.thenBy { it.id })
                        "Duration" -> list.sortedWith(compareBy<PlannerItem> { it.isDone }.thenBy { it.estimateMinutes ?: Int.MAX_VALUE }.thenBy { it.id })
                        else -> list.sortedWith(compareBy<PlannerItem> { it.isDone }.thenByDescending { it.priority }.thenBy { it.dueAt ?: Long.MAX_VALUE }.thenBy { it.sortOrder }.thenBy { it.id })
                    }
                }
                if (unknown > 0) TextButton(onClick={showUnestimated=!showUnestimated}) { Text(if(showUnestimated) "Show tasks that fit · estimates are required" else "No estimate · $unknown tasks") }
                if(showUnestimated && minutes!=null) Text("These tasks have no estimate. They may not fit your available time.",style=MaterialTheme.typography.bodySmall)
                if (visible.isEmpty()) EmptyState("Nothing matches right now", if (vm.plannerTab == "Ready") "Tasks appear here when their prerequisites and manual blockers are cleared. Try changing the filters." else "Add a task or change your filters.")
                LazyColumn(Modifier.weight(1f),verticalArrangement = Arrangement.spacedBy(8.dp), contentPadding = PaddingValues(bottom = 24.dp)) {
                    itemsIndexed(visible, key = { _,task -> task.id }) { index,task -> Column(animatedItem()) {
                        TaskRow(vm, task, tasks, dependencies, projects)
                        if(sort=="Manual")Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.End) {
                            TextButton(enabled=index>0 && !vm.reordering,onClick={vm.moveTask(task,visible[index-1],true)}){Icon(Icons.Default.ArrowUpward,null);Text("Earlier")}
                            TextButton(enabled=index<visible.lastIndex && !vm.reordering,onClick={vm.moveTask(task,visible[index+1],false)}){Icon(Icons.Default.ArrowDownward,null);Text("Later")}
                        }
                    } }
                }
            }
        }
    }
    if (vm.creatingTask || vm.selectedTask != null) {
        val task = tasks.firstOrNull { it.id == vm.selectedTask }
        if (vm.creatingTask || task != null) TaskDetails(vm, task, { vm.creatingTask = false; vm.selectedTask = null })
    }
    projectEditor?.let { project -> ProjectEditor(vm, project, { projectEditor = null }) }
    if (projectPicker) ProjectPicker(projects, vm.selectedProject, { vm.selectedProject = it; projectPicker = false }, { projectPicker = false }, includeAll = true)
    if (customTime) NumberDialog("Available minutes", minutes?.toString() ?: "15", { minutes = it;showUnestimated=false; customTime = false }, { customTime = false })
    if (templatePicker) TemplatePicker(vm, { templatePicker = false })
}

@Composable fun TaskRow(vm: HubViewModel, task: PlannerItem, tasks: List<PlannerItem>, dependencies: List<TaskDependency>, projects: List<Project>, modifier: Modifier = Modifier) {
    val blocked = task.manualBlocker.isNotBlank() || TaskGraph.blockedBy(task, tasks.associateBy { it.id }, dependencies).isNotEmpty()
    Card(onClick = { vm.openTask(task.id) }, modifier = modifier.fillMaxWidth()) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Checkbox(task.isDone, { if (!task.isDone && blocked) vm.openTask(task.id) else vm.action { vm.repo.saveTask(task.copy(isDone = !task.isDone), task.revision) } })
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(task.title, style = MaterialTheme.typography.titleSmall)
                Text(listOfNotNull(projects.firstOrNull { it.id == task.projectId }?.name, task.estimateMinutes?.let { "$it min" }, if (blocked && !task.isDone) "Blocked" else if (task.isDone) "Done" else if (task.workState == "IN_PROGRESS") "In progress" else null).joinToString(" · ").ifBlank { "Inbox" }, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                task.dueAt?.let { Text(formatTime(it), style = MaterialTheme.typography.labelSmall) }
            }
            if (blocked && !task.isDone) Icon(Icons.Default.Lock, "Task is blocked", tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable private fun ProjectOverview(vm: HubViewModel, project: Project, onEdit: (Project) -> Unit) {
    val tasks by vm.tasks.collectAsStateWithLifecycle(); val notes by vm.notes.collectAsStateWithLifecycle()
    val conversations by vm.conversations.collectAsStateWithLifecycle()
    val projectTasks = tasks.filter { it.projectId == project.id }
    val dependencies by vm.dependencies.collectAsStateWithLifecycle()
    val focusTime by remember(project.id){vm.db.focus().projectTotal(project.id)}.collectAsStateWithLifecycle(0L)
    var saveTemplate by remember { mutableStateOf(false) }
    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(bottom = 24.dp)) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { vm.selectedProject = null }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "All projects") }
                Text(project.name, Modifier.weight(1f), style = MaterialTheme.typography.headlineSmall)
                IconButton(onClick = { onEdit(project) }) { Icon(Icons.Default.Edit, "Edit project") }
            }
            if (project.description.isNotBlank()) Text(project.description)
            Text("${projectTasks.count { it.isDone }} of ${projectTasks.size} tasks complete", style = MaterialTheme.typography.labelLarge)
            if(projectTasks.isNotEmpty())SavedProgress(projectTasks.count{it.isDone}.toFloat()/projectTasks.size,Modifier.fillMaxWidth().padding(top=8.dp))
            Text("${focusTime/3600000} h ${(focusTime/60000)%60} min recorded focus",style=MaterialTheme.typography.bodySmall)
        }
        item { Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilledTonalButton(onClick = { vm.newTask(project.id) }) { Text("Add task") }
            OutlinedButton(onClick = { vm.openNote(projectId = project.id) }) { Text("Add note") }
            OutlinedButton(onClick = { saveTemplate = true }) { Text("Save template") }
            OutlinedButton(onClick={vm.openFocus(projectId=project.id)}){Text("Focus")}
            OutlinedButton(onClick={vm.handoffContext=HandoffContext(project.name,projectId=project.id)}){Text("Continue here")}
        } }
        item { Text("Tasks", style = MaterialTheme.typography.titleLarge) }
        items(projectTasks.take(12), key = { "task:${it.id}" }) { task ->
            Card(onClick = { vm.openTask(task.id) }) { Column(Modifier.fillMaxWidth().padding(16.dp)) {
                Text(task.title, fontWeight = FontWeight.Medium)
                if (task.manualBlocker.isNotBlank()) Text("Blocked: ${task.manualBlocker}", color = MaterialTheme.colorScheme.error)
                val blocked=TaskGraph.blockedBy(task,tasks.associateBy{it.id},dependencies)
                if(!task.isDone && blocked.isNotEmpty())Text("Needs ${blocked.size} prerequisite${if(blocked.size==1)"" else "s"}",color=MaterialTheme.colorScheme.error,style=MaterialTheme.typography.bodySmall)
            } }
        }
        if (projectTasks.size > 12) item { TextButton(onClick = { vm.plannerTab = "Tasks" }) { Text("See all tasks") } }
        item { Text("Notes", style = MaterialTheme.typography.titleLarge) }
        items(notes.filter { it.projectId == project.id }.take(8), key = { "note:${it.id}" }) { note ->
            Card(onClick = { vm.openNote(note.id) }) { Text(note.title.ifBlank { "Untitled note" }, Modifier.fillMaxWidth().padding(16.dp)) }
        }
        item { Text("Conversations", style = MaterialTheme.typography.titleLarge) }
        items(conversations.filter { it.projectId == project.id }.take(8), key = { "chat:${it.id}" }) { chat ->
            TextButton(onClick={vm.openConversation(chat.id)},modifier=Modifier.fillMaxWidth()) { Text(chat.title) }
        }
        item { TextButton(onClick={vm.newConversation(project.id)}) { Text("New project conversation") } }
        if (projectTasks.isEmpty() && notes.none { it.projectId == project.id }) item { EmptyState("Make this space yours", "Add a task or note, or assign existing items from their details.") }
    }
    if (saveTemplate) TextValueDialog("Save project template", project.name, { value -> vm.action { vm.workspace.saveTemplate(project, value); saveTemplate = false; vm.message = "Template saved. Copies will have independent items." } }, { saveTemplate = false })
}

@Composable fun EditorSheet(title: String, onClose: () -> Unit, content: @Composable ColumnScope.() -> Unit) {
    Dialog(onDismissRequest = onClose, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        val keyboard=LocalSoftwareKeyboardController.current
        val focus=LocalFocusManager.current
        DisposableEffect(Unit) { onDispose { focus.clearFocus();keyboard?.hide() } }
        Surface(Modifier.padding(12.dp).widthIn(max = 640.dp).fillMaxWidth().heightIn(max = 760.dp).imePadding(), shape = MaterialTheme.shapes.large) {
            Column(Modifier.padding(20.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(title, style = MaterialTheme.typography.headlineSmall)
                content()
            }
        }
    }
}

@Composable private fun TaskDetails(vm: HubViewModel, task: PlannerItem?, onClose: () -> Unit) {
    val projects by vm.projects.collectAsStateWithLifecycle(); val tasks by vm.tasks.collectAsStateWithLifecycle()
    val dependencies by vm.dependencies.collectAsStateWithLifecycle(); val labels by vm.labels.collectAsStateWithLifecycle()
    var title by rememberSaveable(task?.id) { mutableStateOf(task?.title ?: "") }
    var details by rememberSaveable(task?.id) { mutableStateOf(task?.details ?: "") }
    var projectId by rememberSaveable(task?.id) { mutableStateOf(if(task==null)vm.selectedProject else task.projectId) }
    var parentId by rememberSaveable(task?.id) { mutableStateOf(task?.parentId) }
    var priority by rememberSaveable(task?.id) { mutableIntStateOf(task?.priority ?: 1) }
    var estimate by rememberSaveable(task?.id) { mutableStateOf(task?.estimateMinutes?.toString() ?: "") }
    var blocker by rememberSaveable(task?.id) { mutableStateOf(task?.manualBlocker ?: "") }
    var due by rememberSaveable(task?.id) { mutableStateOf(task?.dueAt) }
    val baseRevision=rememberSaveable(task?.id){task?.revision ?: -1L}
    val stale=task!=null && task.revision!=baseRevision
    var showProject by remember { mutableStateOf(false) }; var showPrerequisites by remember { mutableStateOf(false) }
    var delete by remember { mutableStateOf(false) }; var complete by remember { mutableStateOf(false) }
    var more by remember { mutableStateOf(false) }; var error by remember { mutableStateOf("") }
    var review by remember { mutableStateOf<SkillReview?>(null) };var labelSuggestions by remember { mutableStateOf(false) }
    var prerequisites by rememberSaveable(task?.id) { mutableStateOf(dependencies.filter { it.taskId == task?.id }.map { it.prerequisiteId }.toSet()) }
    var prerequisitesLoaded by rememberSaveable(task?.id){mutableStateOf(task==null)}
    LaunchedEffect(task?.id){if(task!=null && !prerequisitesLoaded){prerequisites=vm.db.workspace().allDependencies().filter{it.taskId==task.id}.map{it.prerequisiteId}.toSet();prerequisitesLoaded=true}}
    var labelId by rememberSaveable(task?.id) { mutableStateOf(task?.labelId) }
    val unchanged=!stale && prerequisitesLoaded && task!=null && title==task.title && details==task.details && projectId==task.projectId && parentId==task.parentId && priority==task.priority && estimate==(task.estimateMinutes?.toString() ?: "") && blocker==task.manualBlocker && due==task.dueAt && labelId==task.labelId && prerequisites==dependencies.filter{it.taskId==task.id}.map{it.prerequisiteId}.toSet()
    fun suggest(kind:SkillKind,allowed:Map<Long,String> = emptyMap()) {
        val current=task ?: return
        val source=(current.title+"\n"+current.details).take(1600)
        val description=projects.firstOrNull{it.id==current.projectId}?.description.orEmpty().take(240)
        review=SkillReview(if(kind==SkillKind.STARTER) "Three small starting steps" else "Suggest a task label",SkillRequest(kind,source,description,allowed)) { result ->
            if(result is SkillResult.Steps) vm.workspace.addStartingSteps(current.id,current.revision,result.values)
            else vm.repo.saveTask(current.copy(labelId=(result as SkillResult.Label).id),current.revision)
            onClose()
        }
    }
    EditorSheet(if (task == null) "New task" else "Task details", onClose) {
        if(stale)Text("This task changed elsewhere. Close and reopen it to review the latest version.",color=MaterialTheme.colorScheme.error)
        if(!prerequisitesLoaded)LinearProgressIndicator(Modifier.fillMaxWidth())
        if(task!=null){val total by remember(task.id){vm.db.focus().taskTotal(task.id)}.collectAsStateWithLifecycle(0L);if(total>0)Text("${total/60000} min recorded focus",style=MaterialTheme.typography.bodySmall)}
        OutlinedTextField(title, { title = it }, Modifier.fillMaxWidth(), label = { Text("Task title") })
        OutlinedTextField(details, { details = it }, Modifier.fillMaxWidth(), label = { Text("Details") }, minLines = 2)
        parentId?.let{id->Row(verticalAlignment=Alignment.CenterVertically) { Text("Subtask of ${tasks.firstOrNull{it.id==id}?.title ?: "an unavailable task"}",Modifier.weight(1f),style=MaterialTheme.typography.bodySmall);TextButton(onClick={parentId=null}){Text("Detach")}}}
        TextButton(onClick = { showProject = true }) { Icon(Icons.Default.FolderOpen, null); Spacer(Modifier.width(8.dp)); Text(projects.firstOrNull { it.id == projectId }?.name ?: "Inbox · Assign project") }
        Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            listOf("Low", "Normal", "High", "Urgent").forEachIndexed { index, value -> FilterChip(priority == index, { priority = index }, label = { Text(value) }) }
        }
        OutlinedTextField(estimate, { estimate = it }, Modifier.fillMaxWidth(), label = { Text("Estimated minutes · optional") }, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
        Row(verticalAlignment = Alignment.CenterVertically) { Checkbox(due != null, { due = if (it) System.currentTimeMillis() else null }); Text("Has a deadline") }
        due?.let { DateTimeField("Deadline", it, onChange = { value -> due = value }) }
        OutlinedButton(onClick = { showPrerequisites = true }) { Text("Prerequisites · ${prerequisites.size}") }
        prerequisites.forEach { id -> Row(verticalAlignment=Alignment.CenterVertically){Text("Needs: " + (tasks.firstOrNull { it.id == id }?.title ?: "Deleted prerequisite · remove or restore"),Modifier.weight(1f), style = MaterialTheme.typography.bodySmall);IconButton(onClick={prerequisites=prerequisites-id}){Icon(Icons.Default.Close,"Remove prerequisite $id")}} }
        OutlinedTextField(blocker, { blocker = it }, Modifier.fillMaxWidth(), label = { Text("Manual blocker · leave blank to clear") })
        TextButton(onClick = { more = !more }) { Text(if (more) "Less" else "Status, labels, and subtasks") }
        if (more && task != null) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(enabled=unchanged,onClick = { vm.action { vm.repo.saveTask(task.copy(workState = if (task.workState == "IN_PROGRESS") "TODO" else "IN_PROGRESS"), task.revision); onClose() } }) { Text(if (task.workState == "IN_PROGRESS") "Set to do" else "Set in progress") }
                TextButton(enabled=unchanged,onClick = { vm.action { vm.repo.saveTask(PlannerItem(title = "New subtask", projectId = task.projectId, parentId = task.id)); onClose() } }) { Text("Add subtask") }
            }
        }
        if (more) {
            Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                FilterChip(labelId == null, { labelId = null }, label = { Text("No label") })
                labels.forEach { label -> FilterChip(labelId == label.id, { labelId = label.id }, label = { Text(label.name) }) }
            }
        }
        if(task!=null) {
            Row(Modifier.horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(8.dp)) {
                OutlinedButton(enabled=unchanged,onClick={vm.openFocus(task.id,task.projectId);onClose()}) { Text("Start focus") }
                OutlinedButton(enabled=unchanged && !task.isDone,onClick={suggest(SkillKind.STARTER)}) { Text("AI starting steps") }
                TextButton(enabled=unchanged,onClick={labelSuggestions=true}) { Text("Suggest label") }
                TextButton(enabled=unchanged,onClick={vm.handoffContext=HandoffContext(task.title,task.id,task.projectId);onClose()}){Text("Continue here")}
            }
            if(!unchanged) Text("Save your changes before starting focus or requesting AI help.",style=MaterialTheme.typography.bodySmall)
        }
        if (error.isNotBlank()) Text(error, color = MaterialTheme.colorScheme.error)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            if (task != null) IconButton(onClick = { delete = true }) { Icon(Icons.Default.DeleteOutline, "Delete task") }
            TextButton(onClick = onClose) { Text("Cancel") }
            Button(enabled = title.isNotBlank() && prerequisitesLoaded && !stale, onClick = {
                val parsed = estimate.toIntOrNull()
                if (estimate.isNotBlank() && (parsed == null || parsed <= 0)) error = "Enter a positive estimate or leave it empty."
                else vm.action {
                    val saved = (task ?: PlannerItem(title = title)).copy(title = title, details = details, projectId = projectId,
                        parentId = if (projectId != task?.projectId) null else parentId, priority = priority, estimateMinutes = parsed, dueAt = due, manualBlocker = blocker, labelId = labelId)
                    vm.workspace.saveTask(saved, if(task==null)null else baseRevision, prerequisites)
                    onClose()
                }
            }) { Text("Save task") }
        }
        if (task != null) OutlinedButton(enabled=unchanged,onClick = { complete = true }, modifier = Modifier.fillMaxWidth()) { Text(if (task.isDone) "Reopen task" else "Complete task") }
    }
    if (showProject) ProjectPicker(projects, projectId, { projectId = it; showProject = false }, { showProject = false })
    review?.let{SkillDialog(vm,it,{review=null})}
    if(labelSuggestions) LabelCatalogDialog(vm,{allowed->labelSuggestions=false;suggest(SkillKind.LABEL,allowed)},{labelSuggestions=false})
    if (showPrerequisites) {
        var search by remember { mutableStateOf("") }
        EditorSheet("Choose prerequisites", { showPrerequisites = false }) {
            OutlinedTextField(search, { search = it }, Modifier.fillMaxWidth(), label = { Text("Find a task") })
            LazyColumn(Modifier.heightIn(max = 320.dp)) {
                items(tasks.filter { it.id != task?.id && it.title.contains(search, true) }.take(100), key = { it.id }) { candidate ->
                    Row(Modifier.fillMaxWidth().clickable { prerequisites = if (candidate.id in prerequisites) prerequisites - candidate.id else prerequisites + candidate.id }, verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(candidate.id in prerequisites, null)
                        Column { Text(candidate.title); Text(projects.firstOrNull { it.id == candidate.projectId }?.name ?: "Inbox", style = MaterialTheme.typography.bodySmall) }
                    }
                }
            }
            Text("Self-dependencies and circular relationships are checked when saving.", style = MaterialTheme.typography.bodySmall)
            Button(onClick = { showPrerequisites = false }, modifier = Modifier.align(Alignment.End)) { Text("Done") }
        }
    }
    if (delete && task != null) AlertDialog(onDismissRequest = { delete = false }, title = { Text("Delete task?") }, text = { Text("Dependent tasks will show a missing prerequisite until it is restored or replaced.") }, confirmButton = { TextButton(onClick = { vm.deleteTask(task); onClose() }) { Text("Delete") } }, dismissButton = { TextButton(onClick = { delete = false }) { Text("Keep") } })
    if (complete && task != null) AlertDialog(onDismissRequest = { complete = false }, title = { Text(if (task.isDone) "Reopen task?" else "Complete task?") }, text = { Text(if (task.isDone) "Incomplete dependent tasks may become blocked again." else "This changes only this task. Prerequisites and subtasks keep their own status.") }, confirmButton = { TextButton(onClick = { vm.action { vm.repo.saveTask(task.copy(isDone = !task.isDone), task.revision); onClose() }; complete = false }) { Text("Confirm") } }, dismissButton = { TextButton(onClick = { complete = false }) { Text("Cancel") } })
}

@Composable private fun ProjectEditor(vm: HubViewModel, project: Project, onClose: () -> Unit) {
    var name by remember { mutableStateOf(project.name) }; var description by remember { mutableStateOf(project.description) }
    var pinned by remember { mutableStateOf(project.pinned) }; var archived by remember { mutableStateOf(project.archived) }
    var delete by remember { mutableStateOf(false) }
    EditorSheet(if (project.id == 0L) "New project" else "Edit project", onClose) {
        OutlinedTextField(name, { name = it }, Modifier.fillMaxWidth(), label = { Text("Name") })
        OutlinedTextField(description, { description = it }, Modifier.fillMaxWidth(), label = { Text("Short description") }, minLines = 2)
        Row(verticalAlignment = Alignment.CenterVertically) { Checkbox(pinned, { pinned = it }); Text("Pin project") }
        Row(verticalAlignment = Alignment.CenterVertically) { Checkbox(archived, { archived = it }); Text("Archive project") }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            if (project.id != 0L) TextButton(onClick = { delete = true }) { Text("Delete") }
            TextButton(onClick = onClose) { Text("Cancel") }
            Button(enabled = name.isNotBlank(), onClick = { vm.action { val id = vm.workspace.saveProject(project.copy(name = name, description = description, pinned = pinned, archived = archived)); vm.selectedProject = id; onClose() } }) { Text("Save project") }
        }
    }
    if (delete) AlertDialog(onDismissRequest = { delete = false }, title = { Text("Delete project?") }, text = { Text("Its tasks, notes, and conversations move to Inbox. Their content is kept.") }, confirmButton = { TextButton(onClick = { vm.action { vm.workspace.removeProject(project); vm.selectedProject = null; onClose() } }) { Text("Delete project") } }, dismissButton = { TextButton(onClick = { delete = false }) { Text("Keep") } })
}

@Composable fun ProjectPicker(projects: List<Project>, selected: Long?, onSelect: (Long?) -> Unit, onClose: () -> Unit, includeAll: Boolean = false) {
    EditorSheet("Choose project", onClose) {
        TextButton(onClick = { onSelect(null) }) { Text(if (includeAll) "All projects" else "Inbox · No project") }
        LazyColumn(Modifier.heightIn(max = 360.dp)) { items(projects, key = { it.id }) { project ->
            TextButton(onClick = { onSelect(project.id) }, modifier = Modifier.fillMaxWidth()) { Text(project.name + if (project.id == selected) " · Selected" else "") }
        } }
    }
}

@Composable fun TextValueDialog(title: String, initial: String = "", onSave: (String) -> Unit, onClose: () -> Unit) {
    var value by remember { mutableStateOf(initial) }
    AlertDialog(onDismissRequest = onClose, title = { Text(title) }, text = { OutlinedTextField(value, { value = it }, Modifier.fillMaxWidth(), singleLine = true) }, confirmButton = { TextButton(enabled = value.isNotBlank(), onClick = { onSave(value.trim()) }) { Text("Save") } }, dismissButton = { TextButton(onClick = onClose) { Text("Cancel") } })
}

@Composable fun NumberDialog(title: String, initial: String, onSave: (Int) -> Unit, onClose: () -> Unit) {
    var value by remember { mutableStateOf(initial) }
    AlertDialog(onDismissRequest = onClose, title = { Text(title) }, text = { OutlinedTextField(value, { value = it }, Modifier.fillMaxWidth(), singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), isError = value.toIntOrNull()?.let { it <= 0 } ?: true) }, confirmButton = { TextButton(enabled = (value.toIntOrNull() ?: 0) > 0, onClick = { onSave(value.toInt()) }) { Text("Apply") } }, dismissButton = { TextButton(onClick = onClose) { Text("Cancel") } })
}

@Composable private fun TemplatePicker(vm: HubViewModel, onClose: () -> Unit) {
    val templates by vm.templates.collectAsStateWithLifecycle()
    var selected by remember { mutableStateOf<ProjectTemplate?>(null) }; var name by remember { mutableStateOf("") }
    EditorSheet("Create from template", onClose) {
        Text("New tasks and notes are independent. Completion and session history are reset. Missing external prerequisites become blocker notes.")
        if (templates.isEmpty()) Text("Save a project as a template from its overview first.")
        templates.forEach { template -> TextButton(onClick = { selected = template; name = template.name }) { Text(template.name) } }
        selected?.let { template ->
            val data = remember(template.id) { JSONObject(template.payload) }
            Text("${data.getJSONArray("tasks").length()} tasks · ${data.getJSONArray("notes").length()} notes")
            OutlinedTextField(name, { name = it }, Modifier.fillMaxWidth(), label = { Text("New project name") })
            Button(enabled = name.isNotBlank(), onClick = { vm.action { vm.selectedProject = vm.workspace.fromTemplate(template.id, name); onClose() } }) { Text("Create independent project") }
        }
        TextButton(onClick = onClose) { Text("Close") }
    }
}
