package dev.daylight.hub.ui

import android.app.Activity
import android.graphics.BitmapFactory
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.*
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.daylight.hub.reading.ReadingViewModel
import dev.daylight.hub.HubViewModel
import dev.daylight.hub.R
import dev.daylight.hub.calendar.RecurrenceEngine
import dev.daylight.hub.tasks.TaskGraph
import java.io.File
import java.time.*
import java.time.format.DateTimeFormatter

private val mainDestinations=listOf("Today","Planner","Notes","Reading","Assistant")
private fun destinationIcon(value:String)=when(value) {
    "Today" -> Icons.Default.WbSunny
    "Planner" -> Icons.Default.Checklist
    "Notes" -> Icons.Default.Description
    "Reading" -> Icons.Default.MenuBook
    "Assistant" -> Icons.Default.ChatBubbleOutline
    else -> Icons.Default.Settings
}

@Composable fun V3App(vm:HubViewModel) {
    val reading:ReadingViewModel=viewModel()
    val appearance=vm.appearance
    HubTheme(appearance) {
        val density=LocalDensity.current
        CompositionLocalProvider(LocalDensity provides Density(density.density,density.fontScale*appearance.textScale)) {
            val context=LocalContext.current
            val screenState=rememberSaveableStateHolder()
            val section=if(vm.section=="Home") "Today" else vm.section
            val keyboardVisible=WindowInsets.ime.getBottom(LocalDensity.current)>0
            val readingOpen=section=="Reading" && (reading.opened!=null || reading.loading || reading.error!=null)
            var lastMain by rememberSaveable { mutableStateOf("Today") }
            var destinationMenu by remember { mutableStateOf(false) }
            LaunchedEffect(section) { if(section in mainDestinations) lastMain=section }
            val isDark=appearance.mode=="Dark" || appearance.mode=="System" && isSystemInDarkTheme()
            SideEffect { (context as? Activity)?.window?.let { window -> WindowCompat.getInsetsController(window,window.decorView).apply {
                isAppearanceLightStatusBars=!isDark;isAppearanceLightNavigationBars=!isDark
            } } }
            val snackbar=remember { SnackbarHostState() }
            LaunchedEffect(vm.message) {
                vm.message?.let { message -> snackbar.showSnackbar(message);if(vm.message==message) vm.message=null }
            }
            LaunchedEffect(vm.undoAction) {
                vm.undoAction?.let { undo ->
                    if(snackbar.showSnackbar(undo.message,"Undo",withDismissAction=true,duration=SnackbarDuration.Long)==SnackbarResult.ActionPerformed) vm.action { undo.restore() }
                    if(vm.undoAction===undo) vm.undoAction=null
                }
            }
            LaunchedEffect(reading.message){reading.message?.let{value->snackbar.showSnackbar(value);if(reading.message==value)reading.message=null}}
            LaunchedEffect(reading.undoBook){reading.undoBook?.let{book->if(snackbar.showSnackbar("Book removed","Undo",withDismissAction=true,duration=SnackbarDuration.Long)==SnackbarResult.ActionPerformed)reading.restore(book);if(reading.undoBook==book)reading.undoBook=null}}
            LaunchedEffect(vm.pendingBookUri){vm.pendingBookUri?.let{uri->reading.import(uri);vm.pendingBookUri=null}}
            BackHandler(enabled=section!="Today") {
                when {
                    section=="Planner" && (vm.selectedTask!=null || vm.creatingTask) -> { vm.selectedTask=null;vm.creatingTask=false }
                    section=="Notes" && vm.editor!=null -> vm.closeNote()
                    section=="Settings" -> vm.navigate(lastMain)
                    section=="Planner" && vm.selectedProject!=null -> vm.selectedProject=null
                    else -> vm.navigate("Today")
                }
            }
            BoxWithConstraints(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
                val rail=maxWidth>=600.dp;val twoPane=maxWidth>=840.dp
                val visible=Appearance.migrateNavigation(appearance.navigation,addReading=false).split(",").filter{it in mainDestinations}
                val background=if(appearance.background=="Gradient") Brush.verticalGradient(listOf(MaterialTheme.colorScheme.primary.copy(alpha=.06f),MaterialTheme.colorScheme.background)) else null
                if(background!=null) Box(Modifier.fillMaxSize().background(background))
                if(appearance.background=="Image" && appearance.image.isNotBlank()) {
                    val bitmap=remember(appearance.image) { runCatching { BitmapFactory.decodeFile(File(context.filesDir,appearance.image).absolutePath)?.asImageBitmap() }.getOrNull() }
                    bitmap?.let { Image(it,null,Modifier.fillMaxSize(),contentScale=androidx.compose.ui.layout.ContentScale.Crop,alpha=.12f) }
                }
                Row(Modifier.fillMaxSize().safeDrawingPadding().imePadding()) {
                    if(rail && !keyboardVisible) NavigationRail(containerColor=MaterialTheme.colorScheme.surface) {
                        Spacer(Modifier.height(16.dp));Icon(painterResource(R.drawable.ic_brand_mark),"ProSpaceHub",Modifier.size(36.dp),tint=MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.height(24.dp))
                        visible.forEach { item -> NavigationRailItem(section==item,{vm.navigate(item)},icon={Icon(destinationIcon(item),item)},label={Text(item)}) }
                        Spacer(Modifier.weight(1f))
                        NavigationRailItem(section=="Settings",{vm.navigate("Settings")},icon={Icon(Icons.Default.Settings,"Settings")},label={Text("Settings")})
                    }
                    Scaffold(containerColor=Color.Transparent,snackbarHost={SnackbarHost(snackbar)},topBar={if(!readingOpen){
                        Surface(color=MaterialTheme.colorScheme.surface.copy(alpha=.97f)) {
                            Row(Modifier.fillMaxWidth().padding(horizontal=12.dp,vertical=8.dp),verticalAlignment=Alignment.CenterVertically) {
                                if(section=="Settings" || section=="Notes" && vm.editor!=null && !twoPane) IconButton(onClick={if(section=="Settings") vm.navigate(lastMain) else vm.closeNote()}) { Icon(Icons.AutoMirrored.Filled.ArrowBack,if(section=="Notes") "Notes menu" else "Back") }
                                Box(Modifier.weight(1f)) {
                                    TextButton(onClick={destinationMenu=true}) {
                                        Text(if(section=="Today") "ProSpaceHub" else section,style=MaterialTheme.typography.headlineSmall)
                                        Icon(Icons.Default.ExpandMore,"Open any section",Modifier.padding(start=4.dp))
                                    }
                                    DropdownMenu(expanded=destinationMenu,onDismissRequest={destinationMenu=false}) {
                                        mainDestinations.forEach { item -> DropdownMenuItem(text={Text(item)},onClick={destinationMenu=false;vm.navigate(item)},leadingIcon={Icon(destinationIcon(item),null)}) }
                                    }
                                }
                                if(section=="Notes" && vm.editor!=null) IconButton(onClick={vm.openFocus()}) { Icon(Icons.Default.Timer,"Focus timer") }
                                if(section!="Settings") IconButton(onClick={vm.navigate("Settings")}) { Icon(Icons.Default.Settings,"Settings") }
                            }
                        }
                    }},bottomBar={
                        if(!rail && !keyboardVisible && visible.isNotEmpty() && !readingOpen && !(section=="Notes" && vm.editor!=null)) NavigationBar {
                            visible.forEach { item -> NavigationBarItem(section==item,{vm.navigate(item)},icon={Icon(destinationIcon(item),item)},label={Text(if(item=="Assistant") "AI" else item,maxLines=2)}) }
                        }
                    }) { padding ->
                        Column(Modifier.fillMaxSize().padding(padding).padding(horizontal=if(rail) 24.dp else 16.dp)) {
                            if(section!="Today" && !readingOpen && !(section=="Notes" && vm.editor!=null)) { ActiveFocusCard(vm);Spacer(Modifier.height(8.dp)) }
                            Box(Modifier.weight(1f)) {
                                screenState.SaveableStateProvider(section) { when(section) {
                                    "Today" -> TodayScreen(vm,reading)
                                    "Planner" -> PlannerHub(vm)
                                    "Notes" -> NotesScreen(vm,twoPane)
                                    "Reading" -> ReadingScreen(reading)
                                    "Assistant" -> AssistantScreen(vm)
                                    else -> SettingsScreen(vm)
                                } }
                            }
                        }
                    }
                }
            }
            FocusDialogs(vm)
            vm.pending?.let { ApprovalDialog(vm,it) }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable private fun TodayScreen(vm:HubViewModel,reading:ReadingViewModel) {
    val tasks by vm.tasks.collectAsStateWithLifecycle();val ready by vm.readyTasks.collectAsStateWithLifecycle()
    val projects by vm.projects.collectAsStateWithLifecycle();val dependencies by vm.dependencies.collectAsStateWithLifecycle()
    val events by vm.events.collectAsStateWithLifecycle();val exceptions by vm.exceptions.collectAsStateWithLifecycle()
    val markers by vm.resumeMarkers.collectAsStateWithLifecycle()
    val notes by vm.notes.collectAsStateWithLifecycle()
    val recentBooks by reading.recent.collectAsStateWithLifecycle()
    val today=LocalDate.now();val zone=ZoneId.systemDefault()
    val start=today.atStartOfDay(zone).toInstant().toEpochMilli();val end=today.plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli()
    val occurrences=remember(events,exceptions,today) { events.flatMap{RecurrenceEngine.between(it,exceptions,start,end)}.sortedBy{it.startAt}.take(5) }
    LazyColumn(Modifier.fillMaxSize(),verticalArrangement=Arrangement.spacedBy(14.dp),contentPadding=PaddingValues(top=12.dp,bottom=24.dp)) {
        item { Text(today.format(DateTimeFormatter.ofPattern("EEEE, d MMMM")),style=MaterialTheme.typography.labelLarge,color=MaterialTheme.colorScheme.onSurfaceVariant) }
        item { Text("Today",style=MaterialTheme.typography.headlineMedium) }
        item { FlowRow(horizontalArrangement=Arrangement.spacedBy(8.dp),verticalArrangement=Arrangement.spacedBy(8.dp)) {
            FilledTonalButton(onClick={vm.newTask()}) { Icon(Icons.Default.Add,null);Spacer(Modifier.width(4.dp));Text("New plan") }
            OutlinedButton(onClick={vm.openNote()}) { Text("Add note") }
            OutlinedButton(onClick={vm.openFocus()}) { Text("Focus") }
        } }
        item { ActiveFocusCard(vm) }
        recentBooks.firstOrNull()?.let{book->item {
            Card(onClick={reading.open(book.id);vm.navigate("Reading")},modifier=Modifier.fillMaxWidth()) {
                Row(Modifier.padding(16.dp),verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(12.dp)) {
                    Icon(Icons.Default.MenuBook,null)
                    Column(Modifier.weight(1f)) { Text("Resume reading",style=MaterialTheme.typography.labelMedium);Text(book.title,style=MaterialTheme.typography.titleSmall,maxLines=1) }
                    Text("${(book.progress*100).toInt()}%",style=MaterialTheme.typography.labelMedium)
                }
            }
        }}
        if(markers.isNotEmpty()) item { Text("Continue here",style=MaterialTheme.typography.titleLarge) }
        items(markers.take(2),key={"resume:${it.id}"}) { marker -> Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(8.dp)) {
                Text(notes.firstOrNull{it.id==marker.noteId}?.title ?: tasks.firstOrNull{it.id==marker.taskId}?.title ?: projects.firstOrNull{it.id==marker.projectId}?.name ?: "Saved next step",style=MaterialTheme.typography.titleSmall)
                Text(marker.nextStep)
                Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.End) {
                    TextButton(onClick={vm.action{vm.db.focus().removeMarker(marker.id)}}) { Text("Dismiss") }
                    Button(onClick={ if(marker.noteId!=null)vm.openNote(marker.noteId) else if(marker.taskId!=null && tasks.any{it.id==marker.taskId}) vm.openTask(marker.taskId) else if(projects.any{it.id==marker.projectId}) { vm.selectedProject=marker.projectId;vm.plannerTab="Projects";vm.navigate("Planner") } else vm.message="The linked item is unavailable. Your next step is still saved." }) { Text("Resume") }
                }
            }
        } }
        item { Row(verticalAlignment=Alignment.CenterVertically) { Text("Ready to work",Modifier.weight(1f),style=MaterialTheme.typography.titleLarge);TextButton(onClick={vm.plannerTab="Ready";vm.navigate("Planner")}) { Text("See all") } } }
        val available=ready.filter { task->projects.none{it.id==task.projectId && it.archived} }.take(3)
        if(available.isEmpty()) item { EmptyState("Room for your next step","Create a task, or clear a blocker to bring work into this queue.") }
        items(available,key={"ready:${it.id}"}) { task -> TaskRow(vm,task,tasks,dependencies,projects,animatedItem()) }
        item { Text("Today’s agenda",style=MaterialTheme.typography.titleLarge) }
        if(occurrences.isEmpty()) item { Text("No events today.",color=MaterialTheme.colorScheme.onSurfaceVariant) }
        items(occurrences,key={"event:${it.event.id}:${it.originalStart}"}) { occurrence ->
            Card(onClick={vm.plannerTab="Calendar";vm.navigate("Planner")}) { Column(Modifier.fillMaxWidth().padding(16.dp)) {
                Text(occurrence.event.title,style=MaterialTheme.typography.titleSmall)
                Text(if(occurrence.event.allDay) "All day" else formatTime(occurrence.startAt,occurrence.event.zoneId),style=MaterialTheme.typography.bodySmall)
            } }
        }
    }
}
