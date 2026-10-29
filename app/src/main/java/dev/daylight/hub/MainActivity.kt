package dev.daylight.hub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.filled.StickyNote2
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.core.content.edit
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import dev.daylight.hub.ai.LocalChatClient
import dev.daylight.hub.data.*
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.OffsetDateTime
import java.time.format.DateTimeParseException
import java.util.Locale

private enum class Section(val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Home("Home", Icons.Default.Home), Planner("Planner", Icons.Default.CalendarMonth),
    Notes("Notes", Icons.Default.Description), Assistant("Assistant", Icons.Default.AutoAwesome),
    Settings("Settings", Icons.Default.Settings)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = HubDatabase.get(this)
        setContent { DaylightApp(db) }
    }
}

@Composable
private fun DaylightApp(db: HubDatabase) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("settings", 0) }
    var backStack by rememberSaveable { mutableStateOf(listOf(Section.Home.name)) }
    val section = Section.valueOf(backStack.last())
    val navigate: (Section) -> Unit = { destination ->
        if (destination == Section.Home) {
            backStack = listOf(Section.Home.name)
        } else if (destination != section) {
            backStack = backStack + destination.name
        }
    }
    val goBack: () -> Unit = {
        if (backStack.size > 1) backStack = backStack.dropLast(1)
    }
    val backState = rememberNavigationEventState(currentInfo = NavigationEventInfo.None)
    // Keep Home open at the root. Editor dialogs handle Back in their own window first.
    NavigationBackHandler(state = backState, onBackCompleted = goBack)
    val notes by db.notes().observeAll().collectAsState(initial = emptyList())
    val planner by db.planner().observeAll().collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()
    var accent by remember { mutableIntStateOf(prefs.getInt("accent", 0)) }
    var compact by remember { mutableStateOf(prefs.getBoolean("compact", false)) }
    val colors = listOf(Color(0xFF366A59), Color(0xFF4E6298), Color(0xFF9A5A40), Color(0xFF72518F))
    val accentColor = colors[accent.coerceIn(colors.indices)]
    MaterialTheme(colorScheme = lightColorScheme(primary = accentColor, secondary = accentColor, background = Color(0xFFF6F7F4), surface = Color.White)) {
        val windowWidth = with(LocalDensity.current) { LocalWindowInfo.current.containerSize.width.toDp() }
        val wide = windowWidth >= 700.dp
        Row(Modifier.fillMaxSize().safeDrawingPadding().imePadding().background(Color(0xFFF6F7F4))) {
            if (wide) NavigationRail(Modifier.fillMaxHeight().background(Color.White), containerColor = Color.White) {
                Spacer(Modifier.height(22.dp))
                Text("ProSpaceHub", Modifier.padding(horizontal = 16.dp, vertical = 14.dp), color = accentColor, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.6.sp)
                Section.entries.forEach { item -> NavigationRailItem(selected = section == item, onClick = { navigate(item) }, icon = { Icon(item.icon, null) }, label = { Text(item.label) }, alwaysShowLabel = true) }
            }
            Column(Modifier.fillMaxSize().weight(1f)) {
                Row(Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 18.dp), verticalAlignment = Alignment.CenterVertically) {
                    if (section != Section.Home) {
                        IconButton(onClick = goBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                        Spacer(Modifier.width(8.dp))
                    }
                    Column(Modifier.weight(1f)) {
                        Text("Your space", color = Color(0xFF748078), fontSize = 13.sp)
                        Text(section.label, fontSize = 26.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF202A25))
                    }
                    Surface(shape = CircleShape, color = Color(0xFF3B1764)) {
                        Icon(androidx.compose.ui.res.painterResource(R.drawable.ic_brand_mark), "ProSpaceHub logo", Modifier.padding(11.dp).size(24.dp), tint = Color.White)
                    }
                }
                Box(Modifier.weight(1f).fillMaxWidth()) {
                    when (section) {
                        Section.Home -> HomeScreen(notes, planner, compact, accentColor, navigate, scope, db)
                        Section.Planner -> PlannerScreen(planner, compact, scope, db)
                        Section.Notes -> NotesScreen(notes, compact, scope, db)
                        Section.Assistant -> AssistantScreen(db, notes, planner, prefs)
                        Section.Settings -> SettingsScreen(prefs, accent, compact, { accent = it }, { compact = it })
                    }
                }
                if (!wide) NavigationBar(containerColor = Color.White) {
                    Section.entries.forEach { item -> NavigationBarItem(selected = section == item, onClick = { navigate(item) }, icon = { Icon(item.icon, null) }, label = { Text(item.label, fontSize = 10.sp) }, alwaysShowLabel = true) }
                }
            }
        }
    }
}

@Composable
private fun HomeScreen(notes: List<Note>, planner: List<PlannerItem>, compact: Boolean, accent: Color, go: (Section) -> Unit, scope: kotlinx.coroutines.CoroutineScope, db: HubDatabase) {
    val todayStart = LocalDateTime.now().toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    val todayEnd = todayStart + 24 * 60 * 60 * 1000
    val todayItems = planner.filter { it.dueAt in todayStart until todayEnd && !it.isDone }
    LazyColumn(contentPadding = PaddingValues(horizontal = 20.dp, vertical = 4.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Surface(shape = RoundedCornerShape(24.dp), color = accent, modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(24.dp)) {
                    Text(greeting(), color = Color.White.copy(alpha = .78f), fontSize = 14.sp)
                    Text("Make room for what matters.", color = Color.White, fontSize = 25.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 6.dp))
                    Text("Your plans, thoughts and local AI in one private space.", color = Color.White.copy(alpha = .85f), fontSize = 14.sp, modifier = Modifier.padding(top = 8.dp))
                }
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SummaryCard("Today", "${todayItems.size} planned", Icons.Default.CalendarToday, Modifier.weight(1f)) { go(Section.Planner) }
                SummaryCard("Notes", "${notes.size} saved", Icons.AutoMirrored.Filled.StickyNote2, Modifier.weight(1f)) { go(Section.Notes) }
            }
        }
        item { Text("Up next", fontSize = 17.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 4.dp)) }
        if (todayItems.isEmpty()) item { EmptyCard("Nothing scheduled for today", "Add a task in Planner or ask your assistant to plan it.") }
        items(todayItems.take(4), key = { "home-${it.id}" }) { item -> PlannerRow(item, compact,
            onToggle = { scope.launch { db.planner().update(item.copy(isDone = !item.isDone)) } },
            onEdit = { go(Section.Planner) }, onDelete = { scope.launch { db.planner().delete(item) } }) }
        item { Button(onClick = { go(Section.Assistant) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)) { Icon(Icons.Default.AutoAwesome, null); Spacer(Modifier.width(8.dp)); Text("Ask your assistant") } }
    }
}

@Composable
private fun SummaryCard(title: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier, onClick: () -> Unit) {
    Surface(modifier.clickable(onClick = onClick), shape = RoundedCornerShape(18.dp), color = Color.White) {
        Column(Modifier.padding(17.dp)) {
            Icon(icon, null, tint = Color(0xFF527665))
            Text(value, fontWeight = FontWeight.SemiBold, fontSize = 18.sp, modifier = Modifier.padding(top = 14.dp))
            Text(title, color = Color(0xFF7C8780), fontSize = 13.sp)
        }
    }
}

@Composable
private fun PlannerScreen(items: List<PlannerItem>, compact: Boolean, scope: kotlinx.coroutines.CoroutineScope, db: HubDatabase) {
    var editing by remember { mutableStateOf<PlannerItem?>(null) }
    var showEditor by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) { Text("A clear view of what’s ahead", color = Color(0xFF78837C), fontSize = 13.sp); Text("${items.count { !it.isDone }} open · ${items.count { it.isDone }} done", fontSize = 14.sp) }
            Button(onClick = { editing = null; showEditor = true }, shape = RoundedCornerShape(14.dp)) { Icon(Icons.Default.Add, null); Text("New") }
        }
        if (items.isEmpty()) EmptyCard("Your planner is clear", "Create a task or ask the assistant to add one.", Modifier.padding(20.dp))
        else LazyColumn(contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(items, key = { it.id }) { item -> PlannerRow(item, compact,
                onToggle = { scope.launch { db.planner().update(item.copy(isDone = !item.isDone)) } },
                onEdit = { editing = item; showEditor = true },
                onDelete = { scope.launch { db.planner().delete(item) } }) }
        }
    }
    if (showEditor) PlannerEditor(editing, onDismiss = { showEditor = false }, onSave = { title, details, due ->
        scope.launch { if (editing == null) db.planner().insert(PlannerItem(title = title, details = details, dueAt = due)) else db.planner().update(editing!!.copy(title = title, details = details, dueAt = due)) }
        showEditor = false
    })
}

@Composable
private fun PlannerRow(item: PlannerItem, compact: Boolean, onToggle: () -> Unit, onEdit: () -> Unit, onDelete: () -> Unit) {
    Surface(shape = RoundedCornerShape(16.dp), color = Color.White) {
        Row(Modifier.fillMaxWidth().clickable(onClick = onEdit).padding(horizontal = 12.dp, vertical = if (compact) 8.dp else 13.dp), verticalAlignment = Alignment.CenterVertically) {
            Checkbox(item.isDone, onCheckedChange = { onToggle() })
            Column(Modifier.weight(1f).padding(start = 6.dp)) {
                Text(item.title, fontWeight = FontWeight.Medium, color = if (item.isDone) Color.Gray else Color(0xFF25302A))
                Text(formatDate(item.dueAt) + if (item.details.isNotBlank()) " · ${item.details}" else "", color = Color(0xFF7A857E), fontSize = 12.sp, maxLines = 1)
            }
            IconButton(onClick = onDelete) { Icon(Icons.Default.DeleteOutline, "Delete", tint = Color(0xFF8B9690)) }
        }
    }
}

@Composable
private fun PlannerEditor(item: PlannerItem?, onDismiss: () -> Unit, onSave: (String, String, Long) -> Unit) {
    var title by remember(item) { mutableStateOf(item?.title ?: "") }
    var details by remember(item) { mutableStateOf(item?.details ?: "") }
    var due by remember(item) { mutableStateOf(formatInput(item?.dueAt ?: (System.currentTimeMillis() + 60 * 60 * 1000))) }
    var error by remember { mutableStateOf("") }
    AlertDialog(onDismissRequest = onDismiss, title = { Text(if (item == null) "New planner item" else "Edit planner item") },
        text = { Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedTextField(title, { title = it }, label = { Text("Title") }, singleLine = true)
            OutlinedTextField(details, { details = it }, label = { Text("Details") })
            OutlinedTextField(due, { due = it }, label = { Text("Date and time") }, supportingText = { Text("Format: yyyy-MM-dd HH:mm") })
            if (error.isNotBlank()) Text(error, color = MaterialTheme.colorScheme.error)
        } }, confirmButton = { TextButton(onClick = {
            val parsed = parseInput(due)
            if (title.isBlank()) error = "Add a title." else if (parsed == null) error = "Enter a valid date and time." else onSave(title.trim(), details.trim(), parsed)
        }) { Text("Save") } }, dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } })
}

@Composable
private fun NotesScreen(notes: List<Note>, compact: Boolean, scope: kotlinx.coroutines.CoroutineScope, db: HubDatabase) {
    var editing by remember { mutableStateOf<Note?>(null) }
    var showEditor by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("Your ideas stay yours", color = Color(0xFF78837C), fontSize = 13.sp, modifier = Modifier.weight(1f))
            Button(onClick = { editing = null; showEditor = true }, shape = RoundedCornerShape(14.dp)) { Icon(Icons.Default.Add, null); Text("New note") }
        }
        if (notes.isEmpty()) EmptyCard("A place for every thought", "Write something here or ask the assistant to take a note.", Modifier.padding(20.dp))
        else LazyColumn(contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(notes, key = { it.id }) { note ->
                Surface(shape = RoundedCornerShape(16.dp), color = Color.White, modifier = Modifier.fillMaxWidth().clickable { editing = note; showEditor = true }) {
                    Row(Modifier.padding(if (compact) 13.dp else 17.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(note.title.ifBlank { "Untitled note" }, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                            Text(note.body, maxLines = 3, color = Color(0xFF738078), fontSize = 13.sp, modifier = Modifier.padding(top = 5.dp))
                            Text("Updated ${formatDate(note.updatedAt)}", color = Color(0xFF9AA39D), fontSize = 11.sp, modifier = Modifier.padding(top = 8.dp))
                        }
                        IconButton(onClick = { scope.launch { db.notes().delete(note) } }) { Icon(Icons.Default.DeleteOutline, "Delete note", tint = Color(0xFF8B9690)) }
                    }
                }
            }
        }
    }
    if (showEditor) NoteEditor(editing, onDismiss = { showEditor = false }, onSave = { title, body ->
        scope.launch { if (editing == null) db.notes().insert(Note(title = title, body = body)) else db.notes().update(editing!!.copy(title = title, body = body, updatedAt = System.currentTimeMillis())) }
        showEditor = false
    })
}

@Composable
private fun NoteEditor(note: Note?, onDismiss: () -> Unit, onSave: (String, String) -> Unit) {
    var title by remember(note) { mutableStateOf(note?.title ?: "") }
    var body by remember(note) { mutableStateOf(note?.body ?: "") }
    AlertDialog(onDismissRequest = onDismiss, title = { Text(if (note == null) "New note" else "Edit note") },
        text = { Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedTextField(title, { title = it }, label = { Text("Title") }, singleLine = true)
            OutlinedTextField(body, { body = it }, label = { Text("Note") }, minLines = 5)
        } }, confirmButton = { TextButton(onClick = { onSave(title.trim(), body.trim()) }) { Text("Save") } }, dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } })
}

private data class ChatLine(val text: String, val fromUser: Boolean)

@Composable
private fun AssistantScreen(db: HubDatabase, notes: List<Note>, planner: List<PlannerItem>, prefs: android.content.SharedPreferences) {
    val scope = rememberCoroutineScope()
    val client = remember { LocalChatClient() }
    val lines = remember { mutableStateListOf<ChatLine>() }
    val messages = remember { JSONArray() }
    var input by remember { mutableStateOf("") }
    var busy by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }
    val endpoint = prefs.getString("endpoint", "http://127.0.0.1:8080/v1") ?: "http://127.0.0.1:8080/v1"
    val model = prefs.getString("model", "local-model") ?: "local-model"
    LaunchedEffect(Unit) {
        messages.put(JSONObject().put("role", "system").put("content", "You are the private assistant inside ProSpaceHub. The user's notes and planner are private local data. Only read or change them using tools when the user asks. Before creating notes or planner items, infer reasonable titles and dates from their request; if a date is ambiguous, ask a question instead. Current date/time: ${OffsetDateTime.now()}."))
        if (lines.isEmpty()) lines.add(ChatLine("Hi! I can help you think, capture a note, or put something on your planner. Your request is sent only to the local model address in Settings.", false))
    }
    Column(Modifier.fillMaxSize().padding(horizontal = 18.dp)) {
        Surface(color = Color(0xFFEAF0EA), shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
            Row(Modifier.padding(13.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Lock, null, tint = Color(0xFF527665), modifier = Modifier.size(18.dp))
                Text("Local model · $model", fontSize = 12.sp, color = Color(0xFF526158), modifier = Modifier.padding(start = 8.dp))
            }
        }
        if (lines.size == 1) Row(Modifier.fillMaxWidth().padding(top = 12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SuggestionChip(onClick = { input = "What is on my planner today?" }, label = { Text("What’s on today?") })
            SuggestionChip(onClick = { input = "Take a note about " }, label = { Text("Take a note") })
        }
        LazyColumn(Modifier.weight(1f).fillMaxWidth(), contentPadding = PaddingValues(vertical = 16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(lines) { line ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = if (line.fromUser) Arrangement.End else Arrangement.Start) {
                    Surface(color = if (line.fromUser) Color(0xFFDFEAE3) else Color.White, shape = RoundedCornerShape(16.dp), modifier = Modifier.widthIn(max = 640.dp)) {
                        Text(line.text, Modifier.padding(horizontal = 15.dp, vertical = 12.dp), color = Color(0xFF26312B), fontSize = 14.sp)
                    }
                }
            }
            if (busy) item { Text("Thinking locally…", color = Color(0xFF77827B), fontSize = 13.sp) }
        }
        if (error.isNotBlank()) Text(error, color = MaterialTheme.colorScheme.error, fontSize = 12.sp, modifier = Modifier.padding(bottom = 6.dp))
        Row(Modifier.fillMaxWidth().padding(bottom = 12.dp), verticalAlignment = Alignment.Bottom) {
            OutlinedTextField(input, { input = it }, modifier = Modifier.weight(1f), placeholder = { Text("Message your assistant…") }, maxLines = 4, shape = RoundedCornerShape(18.dp))
            Spacer(Modifier.width(8.dp))
            FilledIconButton(enabled = !busy && input.isNotBlank(), onClick = {
                val prompt = input.trim(); input = ""; error = ""; busy = true
                lines.add(ChatLine(prompt, true)); messages.put(JSONObject().put("role", "user").put("content", prompt))
                scope.launch {
                    try {
                        val answer = client.send(endpoint, model, messages) { name, args ->
                            when (name) {
                                "create_note" -> {
                                    val id = db.notes().insert(Note(title = args.optString("title", "Note"), body = args.optString("body", "")))
                                    "Saved note #$id."
                                }
                                "create_planner_item" -> {
                                    val due = parseIso(args.optString("due_at")) ?: throw IllegalArgumentException("The date could not be read; ask the user to clarify the date and time.")
                                    val id = db.planner().insert(PlannerItem(title = args.optString("title", "Task"), details = args.optString("details", ""), dueAt = due))
                                    "Added planner item #$id."
                                }
                                "list_notes" -> notes.take(30).joinToString("\n") { "${it.id}: ${it.title} — ${it.body.take(160)}" }.ifBlank { "No notes yet." }
                                "list_planner" -> planner.take(40).joinToString("\n") { "${it.id}: ${it.title}, ${formatDate(it.dueAt)}, ${if (it.isDone) "done" else "open"}" }.ifBlank { "Planner is empty." }
                                else -> "Unknown action."
                            }
                        }
                        messages.put(JSONObject().put("role", "assistant").put("content", answer))
                        lines.add(ChatLine(answer, false))
                    } catch (e: Exception) { error = e.message ?: "Could not reach the local model."; lines.add(ChatLine("I couldn't complete that request. Check your local model connection in Settings.", false)) }
                    busy = false
                }
            }, shape = CircleShape, modifier = Modifier.size(52.dp)) { Icon(Icons.AutoMirrored.Filled.Send, "Send") }
        }
    }
}

@Composable
private fun SettingsScreen(prefs: android.content.SharedPreferences, accent: Int, compact: Boolean, onAccent: (Int) -> Unit, onCompact: (Boolean) -> Unit) {
    var endpoint by remember { mutableStateOf(prefs.getString("endpoint", "http://127.0.0.1:8080/v1") ?: "http://127.0.0.1:8080/v1") }
    var model by remember { mutableStateOf(prefs.getString("model", "local-model") ?: "local-model") }
    var saved by remember { mutableStateOf(false) }
    LazyColumn(contentPadding = PaddingValues(horizontal = 20.dp, vertical = 4.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item { SettingsGroup("Local AI", "Connect to a model server running on this device or your private network.") {
            OutlinedTextField(endpoint, { endpoint = it; saved = false }, label = { Text("Local server address") }, modifier = Modifier.fillMaxWidth(), singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri))
            OutlinedTextField(model, { model = it; saved = false }, label = { Text("Model name") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            Button(onClick = { prefs.edit { putString("endpoint", endpoint.trim()); putString("model", model.trim()) }; saved = true }, modifier = Modifier.fillMaxWidth()) { Text(if (saved) "Saved" else "Save model settings") }
            Text("Only loopback and private IPv4 addresses are accepted. No cloud API is used. For an on-device llama.cpp server, start it on 127.0.0.1:8080.", color = Color(0xFF77827B), fontSize = 12.sp)
        } }
        item { SettingsGroup("Appearance", "Choose an accent and a denser layout.") {
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp), verticalAlignment = Alignment.CenterVertically) {
                listOf(Color(0xFF366A59), Color(0xFF4E6298), Color(0xFF9A5A40), Color(0xFF72518F)).forEachIndexed { i, c ->
                    Surface(shape = CircleShape, color = c, modifier = Modifier.size(38.dp).clickable { prefs.edit { putInt("accent", i) }; onAccent(i) }, border = if (accent == i) BorderStroke(3.dp, Color(0xFF26312B)) else null) {}
                }
            }
            HorizontalDivider()
            Row(verticalAlignment = Alignment.CenterVertically) { Text("Compact lists", Modifier.weight(1f)); Switch(compact, { prefs.edit { putBoolean("compact", it) }; onCompact(it) }) }
        } }
        item { SettingsGroup("Your data", "Planner and notes are stored in the app's on-device database.") {
            Text("You can open, edit and delete each item from its section. The assistant can add notes and planner items only through the local model connection.", color = Color(0xFF69756D), fontSize = 13.sp)
            Text("Export and import are planned for a future version.", color = Color(0xFF9AA39D), fontSize = 12.sp)
        } }
        item { Text("ProSpaceHub · 1.0.0", color = Color(0xFF9AA39D), fontSize = 12.sp, modifier = Modifier.padding(bottom = 14.dp)) }
    }
}

@Composable
private fun SettingsGroup(title: String, subtitle: String, content: @Composable ColumnScope.() -> Unit) {
    Surface(shape = RoundedCornerShape(18.dp), color = Color.White, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(title, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
            Text(subtitle, color = Color(0xFF77827B), fontSize = 13.sp)
            content()
        }
    }
}

@Composable
private fun EmptyCard(title: String, body: String, modifier: Modifier = Modifier) {
    Surface(modifier.fillMaxWidth(), color = Color.White, shape = RoundedCornerShape(18.dp)) {
        Column(Modifier.padding(20.dp)) { Text(title, fontWeight = FontWeight.SemiBold, fontSize = 16.sp); Text(body, color = Color(0xFF7B867F), fontSize = 13.sp, modifier = Modifier.padding(top = 5.dp)) }
    }
}

private fun greeting() = when (java.time.LocalTime.now().hour) { in 5..11 -> "Good morning"; in 12..17 -> "Good afternoon"; else -> "Good evening" }
private fun formatDate(time: Long): String = Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("EEE, d MMM · HH:mm", Locale.getDefault()))
private fun formatInput(time: Long): String = Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.US))
private fun parseInput(value: String): Long? = try { LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() } catch (_: Exception) { null }
private fun parseIso(value: String): Long? = try { OffsetDateTime.parse(value).toInstant().toEpochMilli() } catch (_: DateTimeParseException) { try { Instant.parse(value).toEpochMilli() } catch (_: Exception) { null } }
