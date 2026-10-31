package dev.daylight.hub.ui

import android.os.SystemClock
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.daylight.hub.HubViewModel
import dev.daylight.hub.data.FocusSession
import dev.daylight.hub.focus.*
import dev.daylight.hub.ai.*
import kotlinx.coroutines.delay
import androidx.paging.*
import androidx.paging.compose.*
import java.time.*
import java.time.format.DateTimeFormatter

@Composable fun FocusDialogs(vm:HubViewModel) {
    if(vm.showFocusHistory)FocusHistory(vm)
    vm.handoffContext?.let{context->
        var next by androidx.compose.runtime.saveable.rememberSaveable(context.taskId,context.projectId,context.noteId){mutableStateOf("")}
        var review by remember{mutableStateOf<SkillReview?>(null)}
        LaunchedEffect(context){val key=context.taskId?.let{"task:$it"} ?: context.noteId?.let{"note:$it"} ?: "project:${context.projectId}";vm.db.focus().marker(key)?.let{if(next.isEmpty())next=it.nextStep}}
        EditorSheet("Continue here",{vm.handoffContext=null}) {
            Text(context.title,style=MaterialTheme.typography.titleMedium)
            OutlinedTextField(next,{next=it.take(1000)},Modifier.fillMaxWidth(),label={Text("Where you stopped and your next step")},minLines=3)
            TextButton(enabled=next.isNotBlank(),onClick={val source=next;review=SkillReview("Shorten next step",SkillRequest(SkillKind.HANDOFF,source)){result->check(next==source){"Your next step changed. Request a fresh suggestion."};next=(result as SkillResult.Text).value}}){Text("Shorten with AI")}
            Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.End){TextButton(onClick={vm.handoffContext=null}){Text("Cancel")};Button(enabled=next.isNotBlank(),onClick={vm.action{vm.focus.handoff(context.taskId,context.projectId,next,context.noteId);vm.handoffContext=null}}){Text("Save next step")}}
        }
        review?.let{SkillDialog(vm,it,{review=null})}
    }
    val active by vm.activeFocus.collectAsStateWithLifecycle()
    if(vm.showFocus) {
        val session=active
        if(session!=null) ActiveFocus(vm,session)
        else FocusSetup(vm)
    }
    vm.endedFocus?.let { session ->
        var next by remember(session.id) { mutableStateOf("") }
        var review by remember { mutableStateOf<SkillReview?>(null) }
        EditorSheet("Session saved",{vm.endedFocus=null}) {
            Text("${session.creditedMs/60000} minutes · ${session.title}")
            OutlinedTextField(next,{next=it},Modifier.fillMaxWidth(),label={Text("Continue here · your next step")},minLines=2)
            TextButton(enabled=next.isNotBlank(),onClick={val original=next;review=SkillReview("Shorten next step",SkillRequest(SkillKind.HANDOFF,original)){result->check(next==original){"Your next step changed. Request a fresh suggestion."};next=(result as SkillResult.Text).value}}) { Text("Shorten with AI") }
            Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.End) {
                TextButton(onClick={vm.endedFocus=null}) { Text("Done") }
                Button(enabled=next.isNotBlank(),onClick={vm.action { vm.focus.handoff(session.taskId,session.projectId,next);vm.endedFocus=null }}) { Text("Save next step") }
            }
            Row(horizontalArrangement=Arrangement.spacedBy(8.dp)) {
                listOf(5,15).forEach { minutes -> OutlinedButton(onClick={vm.action { vm.focus.start(session.taskId,session.projectId,minutes,"BREAK");vm.endedFocus=null;vm.showFocus=true }}) { Text("$minutes min break") } }
            }
        }
        review?.let{SkillDialog(vm,it,{review=null})}
    }
}

@Composable private fun FocusSetup(vm:HubViewModel) {
    val tasks by vm.tasks.collectAsStateWithLifecycle();val projects by vm.projects.collectAsStateWithLifecycle()
    val history by vm.focusHistory.collectAsStateWithLifecycle()
    var taskId by remember { mutableStateOf(vm.focusTaskId) };var projectId by remember { mutableStateOf(vm.focusProjectId) }
    var duration by remember { mutableIntStateOf(25) };var custom by remember { mutableStateOf(false) }
    var picker by remember { mutableStateOf(false) };var showTasks by remember { mutableStateOf(false) }
    EditorSheet("Start a focus session",{vm.showFocus=false}) {
        Text("Choose one task or project. Your timer continues when the screen is locked or you leave the app.")
        OutlinedButton(onClick={showTasks=!showTasks}) { Text(tasks.firstOrNull{it.id==taskId}?.title ?: "Choose task") }
        if(showTasks) {
            tasks.filter{!it.isDone}.take(30).forEach { task -> TextButton(onClick={taskId=task.id;projectId=task.projectId;showTasks=false}) { Text(task.title) } }
        }
        TextButton(onClick={picker=true}) { Text(projects.firstOrNull{it.id==projectId}?.name ?: "Or choose project") }
        Row(Modifier.horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(8.dp)) {
            listOf(15,25,45,60).forEach { minutes -> FilterChip(duration==minutes,{duration=minutes},label={Text("$minutes min")}) }
            AssistChip(onClick={custom=true},label={Text("Custom")})
        }
        Text("Selected duration: $duration minutes",style=MaterialTheme.typography.labelLarge)
        val problem=FocusNotifications(LocalContext.current).permissionProblem()
        problem?.let { Text(it,style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.onSurfaceVariant) }
        Button(enabled=taskId!=null || projectId!=null,onClick={vm.action { vm.focus.start(taskId,projectId,duration) }},modifier=Modifier.fillMaxWidth()) { Text("Start focus") }
        if(history.isNotEmpty()) {
            HorizontalDivider();Text("Recent sessions",style=MaterialTheme.typography.titleMedium)
            history.filter{it.activeSlot==null}.take(5).forEach { item -> Text("${item.title} · ${item.creditedMs/60000} min${if(item.kind=="BREAK") " break" else ""}",style=MaterialTheme.typography.bodyMedium) }
        }
        TextButton(onClick={vm.showFocus=false;vm.showFocusHistory=true}){Text("All sessions and totals")}
        TextButton(onClick={vm.showFocus=false}) { Text("Close") }
    }
    if(picker) ProjectPicker(projects,projectId,{projectId=it;taskId=null;picker=false},{picker=false})
    if(custom) NumberDialog("Focus duration · 1–180 minutes",duration.toString(),{if(it<=180){duration=it;custom=false}else vm.message="Choose up to 180 minutes."},{custom=false})
}

@Composable private fun FocusHistory(vm:HubViewModel) {
    val pager=remember{Pager(PagingConfig(32,enablePlaceholders=false)){vm.db.focus().pagedHistory()}.flow}
    val sessions=pager.collectAsLazyPagingItems()
    val total by remember{vm.db.focus().total()}.collectAsStateWithLifecycle(0L)
    EditorSheet("Focus history",{vm.showFocusHistory=false}) {
        Text("${total/3600000} h ${(total/60000)%60} min of recorded focus",style=MaterialTheme.typography.titleMedium)
        Text("Breaks are recorded separately and excluded from this total.",style=MaterialTheme.typography.bodySmall)
        LazyColumn(Modifier.fillMaxWidth().heightIn(min=160.dp,max=480.dp),verticalArrangement=Arrangement.spacedBy(8.dp)) {
            if(sessions.itemCount==0)item{Text("Your sessions will appear here.")}
            items(sessions.itemCount,key=sessions.itemKey{it.id}){index->sessions[index]?.let{session->
                ListItem(headlineContent={Text(session.title)},supportingContent={Text("${Instant.ofEpochMilli(session.startedAt).atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("d MMM · HH:mm"))} · ${session.creditedMs/60000} min · ${if(session.kind=="BREAK")"Break" else "Focus"} · ${session.status.lowercase()}")})
            }}
        }
        TextButton(onClick={vm.showFocusHistory=false}){Text("Done")}
    }
}

@Composable private fun ActiveFocus(vm:HubViewModel,session:FocusSession) {
    var now by remember { mutableLongStateOf(SystemClock.elapsedRealtime()) }
    LaunchedEffect(session.id,session.revision) { while(true) { now=SystemClock.elapsedRealtime();delay(1000) } }
    LaunchedEffect(session.id) { while(true) { delay(15000);vm.db.focus().current()?.let { vm.focus.command(it.id,it.revision,"CHECKPOINT") } } }
    val reading=FocusClock.read(session.remainingMs,session.creditedMs,session.sinceElapsed,now)
    EditorSheet(if(session.kind=="BREAK") "Take a break" else "Focus",{vm.showFocus=false}) {
        Text(session.title,style=MaterialTheme.typography.titleLarge)
        Text("%02d:%02d".format(reading.remainingMs/60000,(reading.remainingMs/1000)%60),style=MaterialTheme.typography.displayLarge,modifier=Modifier.align(Alignment.CenterHorizontally))
        LinearProgressIndicator(progress={1f-reading.remainingMs.toFloat()/session.durationMs},modifier=Modifier.fillMaxWidth())
        Text(if(session.status=="PAUSED") "Paused" else "One thing at a time.",modifier=Modifier.align(Alignment.CenterHorizontally))
        Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(12.dp)) {
            Button(onClick={vm.focusCommand(session,if(session.status=="RUNNING") "PAUSE" else "RESUME")},modifier=Modifier.weight(1f)) { Text(if(session.status=="RUNNING") "Pause" else "Resume") }
            OutlinedButton(onClick={vm.focusCommand(session,"END")},modifier=Modifier.weight(1f)) { Text("End session") }
        }
        TextButton(onClick={vm.showFocus=false},modifier=Modifier.align(Alignment.CenterHorizontally)) { Text("Keep working in the app") }
    }
}

@Composable fun ActiveFocusCard(vm:HubViewModel) {
    val session by vm.activeFocus.collectAsStateWithLifecycle()
    session?.let { item -> Card(onClick={vm.showFocus=true},modifier=Modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth().padding(16.dp),verticalAlignment=Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) { Text(if(item.status=="PAUSED") "Focus paused" else if(item.kind=="BREAK") "Break in progress" else "Focus in progress",style=MaterialTheme.typography.labelMedium);Text(item.title,style=MaterialTheme.typography.titleSmall) }
            Text("Open",color=MaterialTheme.colorScheme.primary)
        }
    } }
}
