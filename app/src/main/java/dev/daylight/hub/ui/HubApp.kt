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
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.text.font.FontWeight
import androidx.core.view.WindowCompat
import dev.daylight.hub.*
import java.io.File
import java.time.*
import java.time.format.DateTimeFormatter

private val sections=listOf("Home","Planner","Notes","Assistant")
private fun icon(section:String)=when(section) { "Home"->Icons.Default.Home; "Planner"->Icons.Default.CalendarMonth; "Notes"->Icons.Default.Description; "Assistant"->Icons.Default.AutoAwesome; else->Icons.Default.Settings }
@Composable fun HubApp(vm: HubViewModel) {
    val a=vm.appearance
    HubTheme(a) {
        val density=LocalDensity.current
        CompositionLocalProvider(LocalDensity provides Density(density.density,density.fontScale*a.textScale)) {
            val context=LocalContext.current
            val isDark=a.mode=="Dark" || a.mode=="System" && isSystemInDarkTheme()
            SideEffect { (context as? Activity)?.window?.let { WindowCompat.getInsetsController(it,it.decorView).apply {
                isAppearanceLightStatusBars=!isDark; isAppearanceLightNavigationBars=!isDark
            } } }
            val snackbar=remember { SnackbarHostState() }
            LaunchedEffect(vm.message) { vm.message?.let { snackbar.showSnackbar(it); vm.message=null } }
            val nav=a.navigation.split(",").filter { it in sections }.distinct()
            BackHandler(enabled=vm.section!="Home") {
                if(vm.section=="Notes" && vm.editor!=null) vm.closeNote() else vm.navigate("Home")
            }
            BoxWithConstraints(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
                val wide=maxWidth>=700.dp
                val bg=if(a.background=="Gradient") Brush.verticalGradient(listOf(MaterialTheme.colorScheme.primary.copy(alpha=.16f),MaterialTheme.colorScheme.background)) else Brush.verticalGradient(listOf(MaterialTheme.colorScheme.background,MaterialTheme.colorScheme.background))
                Box(Modifier.fillMaxSize().background(bg)) {
                    if(a.background=="Image" && a.image.isNotBlank()) {
                        val bitmap=remember(a.image) { runCatching { BitmapFactory.decodeFile(File(context.filesDir,a.image).absolutePath)?.asImageBitmap() }.getOrNull() }
                        bitmap?.let { Image(it,null,Modifier.fillMaxSize(),contentScale=androidx.compose.ui.layout.ContentScale.Crop,alpha=.22f) }
                    }
                }
                Row(Modifier.fillMaxSize().safeDrawingPadding().imePadding()) {
                    if(wide) NavigationRail(containerColor=MaterialTheme.colorScheme.surface.copy(alpha=.94f)) {
                        Spacer(Modifier.height(20.dp)); Icon(Icons.Default.WbSunny,"ProSpaceHub",tint=MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.height(24.dp))
                        (nav+"Settings").forEach { s->NavigationRailItem(vm.section==s,{vm.navigate(s)},icon={Icon(icon(s),s)},label={Text(s)}) }
                    }
                    Scaffold(containerColor=Color.Transparent,contentColor=MaterialTheme.colorScheme.onBackground,
                        topBar={ Surface(color=MaterialTheme.colorScheme.surface.copy(alpha=.95f),contentColor=MaterialTheme.colorScheme.onSurface) {
                            Row(Modifier.fillMaxWidth().padding(horizontal=16.dp,vertical=12.dp),verticalAlignment=Alignment.CenterVertically) {
                                if(vm.section=="Notes" && vm.editor!=null && !wide) IconButton(onClick={vm.closeNote()}) { Icon(Icons.AutoMirrored.Filled.ArrowBack,"Notes menu") }
                                Column(Modifier.weight(1f)) {
                                    Text("PROSPACEHUB",style=MaterialTheme.typography.labelSmall,color=MaterialTheme.colorScheme.primary,letterSpacing=2.sp)
                                    Text(if(vm.section=="Notes" && vm.editor!=null) "Writing space" else vm.section,style=MaterialTheme.typography.headlineSmall,fontWeight=FontWeight.SemiBold)
                                }
                                IconButton(onClick={vm.navigate("Settings")}) { Icon(Icons.Default.Settings,"Settings") }
                            }
                        } },
                        bottomBar={ if(!wide && !(vm.section=="Notes" && vm.editor!=null)) NavigationBar {
                            nav.forEach { s->NavigationBarItem(vm.section==s,{vm.navigate(s)},icon={Icon(icon(s),s)},label={Text(s)}) }
                        } },snackbarHost={SnackbarHost(snackbar)}
                    ) { padding ->
                        Box(Modifier.fillMaxSize().padding(padding).padding(horizontal=if(wide) 24.dp else 12.dp)) {
                            when(vm.section) {
                                "Home"->HomeScreen(vm)
                                "Notes"->NotesScreen(vm,wide)
                                "Planner"->PlannerScreen(vm)
                                "Assistant"->AssistantScreen(vm)
                                else->SettingsScreen(vm)
                            }
                        }
                    }
                }
            }
            vm.pending?.let { ApprovalDialog(vm,it) }
        }
    }
}
@Composable fun HubCard(title:String, subtitle:String?=null, content:@Composable ColumnScope.()->Unit) {
    Card(Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surface.copy(alpha=.97f),contentColor=MaterialTheme.colorScheme.onSurface)) {
        Column(Modifier.padding(18.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
            Text(title,style=MaterialTheme.typography.titleMedium,fontWeight=FontWeight.SemiBold)
            subtitle?.let { Text(it,style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.onSurfaceVariant) }
            content()
        }
    }
}
@Composable fun EmptyState(title:String,body:String) {
    Column(Modifier.fillMaxWidth().padding(24.dp),verticalArrangement=Arrangement.spacedBy(8.dp)) {
        Text(title,style=MaterialTheme.typography.titleLarge); Text(body,color=MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
fun formatTime(ms:Long?,zone:String=ZoneId.systemDefault().id):String = ms?.let { Instant.ofEpochMilli(it).atZone(ZoneId.of(zone)).format(DateTimeFormatter.ofPattern("EEE, d MMM · HH:mm")) } ?: "No deadline"
@Composable private fun HomeScreen(vm:HubViewModel) {
    val notes by vm.notes.collectAsState(); val tasks by vm.tasks.collectAsState(); val events by vm.events.collectAsState(); val exceptions by vm.exceptions.collectAsState()
    val today=LocalDate.now(); val zone=ZoneId.systemDefault()
    val start=today.atStartOfDay(zone).toInstant().toEpochMilli(); val end=today.plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli()
    val occurrences=remember(events,exceptions,today) { events.flatMap { dev.daylight.hub.calendar.RecurrenceEngine.between(it,exceptions,start,end) }.sortedBy { it.startAt } }
    val engine by vm.engine.state.collectAsState()
    LazyColumn(Modifier.fillMaxSize(),verticalArrangement=Arrangement.spacedBy(16.dp),contentPadding=PaddingValues(vertical=16.dp)) {
        item {
            Card(colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.primaryContainer)) {
                Column(Modifier.fillMaxWidth().padding(24.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
                    Text(today.format(DateTimeFormatter.ofPattern("EEEE, d MMMM")),style=MaterialTheme.typography.labelLarge)
                    Text("Make room for what matters.",style=MaterialTheme.typography.headlineMedium,fontWeight=FontWeight.SemiBold)
                    Text("Your notes, calendar, and AI. Entirely on your device.")
                    FilledTonalButton(onClick={vm.navigate("Assistant")}) { Icon(Icons.Default.AutoAwesome,null); Spacer(Modifier.width(8.dp)); Text("Ask your assistant") }
                }
            }
        }
        item { Row(horizontalArrangement=Arrangement.spacedBy(12.dp)) {
            Card(onClick={vm.navigate("Planner")},modifier=Modifier.weight(1f)) { Column(Modifier.padding(18.dp)) { Icon(Icons.Default.CalendarToday,null); Text(occurrences.size.toString(),style=MaterialTheme.typography.headlineMedium); Text("Events today") } }
            Card(onClick={vm.navigate("Notes")},modifier=Modifier.weight(1f)) { Column(Modifier.padding(18.dp)) { Icon(Icons.Default.Description,null); Text(notes.size.toString(),style=MaterialTheme.typography.headlineMedium); Text("Notes saved") } }
        } }
        item { Text("Today's agenda",style=MaterialTheme.typography.titleLarge) }
        if(occurrences.isEmpty() && tasks.none { (it.dueAt ?: Long.MIN_VALUE) in start until end && !it.isDone }) item { EmptyState("A little breathing room","Add an event in Planner or capture an idea in Notes.") }
        items(occurrences.take(8),key={"event-"+it.event.id+"-"+it.originalStart}) { item ->
            Card(onClick={vm.navigate("Planner")}) { Column(Modifier.fillMaxWidth().padding(16.dp)) { Text(item.event.title,fontWeight=FontWeight.SemiBold); Text(if(item.event.allDay) "All day" else formatTime(item.startAt,item.event.zoneId),style=MaterialTheme.typography.bodySmall) } }
        }
        items(tasks.filter { (it.dueAt ?: Long.MIN_VALUE) in start until end && !it.isDone },key={"task-"+it.id}) { task->
            Card { Row(Modifier.fillMaxWidth().padding(12.dp),verticalAlignment=Alignment.CenterVertically) {
                Checkbox(task.isDone,{vm.action { vm.repo.saveTask(task.copy(isDone=it),task.revision) }})
                Column { Text(task.title); Text(formatTime(task.dueAt),style=MaterialTheme.typography.bodySmall) }
            } }
        }
        item { HubCard("On-device AI",engine.error ?: engine.description) { Text(if(engine.loadedId==null) "Import a GGUF in Settings → Models to begin." else "Ready for private, offline conversations.",style=MaterialTheme.typography.bodySmall) } }
    }
}
