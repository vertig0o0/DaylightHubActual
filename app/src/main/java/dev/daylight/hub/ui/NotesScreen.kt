@file:OptIn(com.mohamedrejeb.richeditor.annotation.ExperimentalRichTextApi::class,kotlinx.coroutines.FlowPreview::class)
package dev.daylight.hub.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.*
import com.mohamedrejeb.richeditor.model.*
import com.mohamedrejeb.richeditor.json.*
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import dev.daylight.hub.*
import dev.daylight.hub.data.Note
import kotlinx.coroutines.flow.*

@Composable fun NotesScreen(vm:HubViewModel,wide:Boolean) {
    val notes by vm.notes.collectAsState()
    var query by rememberSaveable { mutableStateOf("") }
    var notebook by rememberSaveable { mutableStateOf("All notebooks") }
    var sortTitle by rememberSaveable { mutableStateOf(false) }
    var deleting by remember { mutableStateOf<Note?>(null) }
    val matches=notes.filter { (notebook=="All notebooks" || it.notebook==notebook) && (it.title+" "+it.body+" "+it.tags).contains(query,true) }
        .let { if(sortTitle) it.sortedBy { n->n.title.lowercase() } else it }
    Row(Modifier.fillMaxSize(),horizontalArrangement=Arrangement.spacedBy(16.dp)) {
        if(wide || vm.editor==null) Column(if(wide) Modifier.width(290.dp) else Modifier.fillMaxSize()) {
            Row(Modifier.fillMaxWidth().padding(vertical=12.dp),verticalAlignment=Alignment.CenterVertically) {
                Text("Library",style=MaterialTheme.typography.titleLarge,modifier=Modifier.weight(1f))
                FilledTonalIconButton(onClick={vm.openNote()}) { Icon(Icons.Default.Add,"New note") }
            }
            OutlinedTextField(query,{query=it},label={Text("Search notes and tags")},leadingIcon={Icon(Icons.Default.Search,null)},singleLine=true,modifier=Modifier.fillMaxWidth())
            Row(Modifier.horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(8.dp)) {
                (listOf("All notebooks")+notes.map { it.notebook }.distinct().sorted()).forEach { name->FilterChip(notebook==name,{notebook=name},label={Text(name)}) }
            }
            TextButton(onClick={sortTitle=!sortTitle}) { Icon(Icons.Default.Sort,null); Text(if(sortTitle) "Title A–Z" else "Recently updated") }
            if(matches.isEmpty()) EmptyState("No notes here","Create a note or change your search.")
            LazyColumn(verticalArrangement=Arrangement.spacedBy(if(vm.appearance.compact) 6.dp else 12.dp),contentPadding=PaddingValues(bottom=16.dp)) {
                items(matches,key={it.id}) { note->
                    Card(onClick={vm.openNote(note.id)},colors=CardDefaults.cardColors(containerColor=if(vm.editor?.saved?.id==note.id) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface)) {
                        Row(Modifier.fillMaxWidth().padding(if(vm.appearance.compact) 10.dp else 16.dp),verticalAlignment=Alignment.Top) {
                            Column(Modifier.weight(1f),verticalArrangement=Arrangement.spacedBy(5.dp)) {
                                Text(note.title.ifBlank { "Untitled note" },fontWeight=FontWeight.SemiBold)
                                Text(note.body,maxLines=3,style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(note.notebook+(if(note.tags.isBlank()) "" else " · "+note.tags),style=MaterialTheme.typography.labelSmall)
                            }
                            IconButton(onClick={deleting=note}) { Icon(Icons.Default.DeleteOutline,"Delete note") }
                        }
                    }
                }
            }
        }
        val draft=vm.editor
        if(draft!=null) key(draft) { NoteWritingPage(vm,draft,notes,Modifier.weight(1f)) }
        else if(wide) Box(Modifier.weight(1f).fillMaxHeight(),contentAlignment=Alignment.Center) { EmptyState("Your writing space","Choose a note, or start something new.") }
    }
    deleting?.let { note->AlertDialog(onDismissRequest={deleting=null},title={Text("Delete note?")},text={Text(note.title.ifBlank { "Untitled note" })},
        confirmButton={TextButton(onClick={vm.deleteNote(note); deleting=null}) { Text("Delete") }},dismissButton={TextButton(onClick={deleting=null}){Text("Keep note")}}) }
}
@Composable private fun NoteWritingPage(vm:HubViewModel,draft:EditorDraft,notes:List<Note>,modifier:Modifier) {
    val rich=draft.rich
    var linkPicker by remember { mutableStateOf(false) }
    var linkSearch by remember { mutableStateOf("") }
    var colorPicker by remember { mutableStateOf(false) }
    var highlight by remember { mutableStateOf(false) }
    var showMetadata by remember { mutableStateOf(false) }
    val context=LocalContext.current
    val backlinks by remember(draft.saved.id) { vm.db.notes().backlinks(draft.saved.id) }.collectAsState(initial=emptyList())
    val uriHandler=remember { object:UriHandler { override fun openUri(uri:String) {
        val id=uri.removePrefix("daylighthub://note/").toLongOrNull()
        if(uri.startsWith("daylighthub://note/") && id!=null) vm.openNote(id)
        else vm.message="External links stay offline. You can select and copy their address."
    } } }
    LaunchedEffect(draft) {
        snapshotFlow { draft.snapshot() }.drop(1).debounce(600).collect { runCatching { vm.saveDraft(draft) } }
    }
    val beforeCaret=rich.annotatedString.text.take(rich.selection.end.coerceIn(0,rich.annotatedString.length))
    val bracket=Regex("\\[\\[([^]\\n]*)$").find(beforeCaret)
    Column(modifier.fillMaxHeight().padding(vertical=12.dp),verticalArrangement=Arrangement.spacedBy(8.dp)) {
        Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically) {
            Text(if(draft.status=="Saved" && draft.snapshot()!=draft.fingerprint) "Unsaved changes" else draft.status,style=MaterialTheme.typography.labelSmall,color=if(draft.status.startsWith("Save failed")) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,modifier=Modifier.weight(1f))
            TextButton(onClick={vm.action { vm.saveDraft(draft) }}) { Text("Save") }
            IconButton(onClick={showMetadata=!showMetadata}) { Icon(Icons.Default.Tune,"Notebook and tags") }
        }
        OutlinedTextField(draft.title,{draft.title=it},placeholder={Text("Untitled note")},textStyle=MaterialTheme.typography.headlineSmall,singleLine=true,modifier=Modifier.fillMaxWidth())
        if(showMetadata) Row(horizontalArrangement=Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(draft.notebook,{draft.notebook=it},label={Text("Notebook")},singleLine=true,modifier=Modifier.weight(1f))
            OutlinedTextField(draft.tags,{draft.tags=it},label={Text("Tags, comma separated")},singleLine=true,modifier=Modifier.weight(1f))
        }
        Surface(shape=MaterialTheme.shapes.medium,color=MaterialTheme.colorScheme.surfaceVariant) {
            Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),verticalAlignment=Alignment.CenterVertically) {
                TextButton(onClick={rich.toggleSpanStyle(SpanStyle(fontWeight=FontWeight.Bold))}) { Text("B",fontWeight=FontWeight.Bold) }
                TextButton(onClick={rich.toggleSpanStyle(SpanStyle(fontStyle=FontStyle.Italic))}) { Text("I",fontStyle=FontStyle.Italic) }
                TextButton(onClick={rich.toggleSpanStyle(SpanStyle(textDecoration=TextDecoration.Underline))}) { Text("U",textDecoration=TextDecoration.Underline) }
                TextButton(onClick={rich.toggleSpanStyle(SpanStyle(textDecoration=TextDecoration.LineThrough))}) { Text("S",textDecoration=TextDecoration.LineThrough) }
                TextButton(onClick={rich.setHeadingStyle(if(rich.currentHeadingStyle==HeadingStyle.H1) HeadingStyle.Normal else HeadingStyle.H1)}) { Text("H1") }
                TextButton(onClick={rich.setHeadingStyle(if(rich.currentHeadingStyle==HeadingStyle.H2) HeadingStyle.Normal else HeadingStyle.H2)}) { Text("H2") }
                IconButton(onClick={rich.toggleUnorderedList()}) { Icon(Icons.Default.FormatListBulleted,"Bullet list") }
                IconButton(onClick={rich.toggleOrderedList()}) { Icon(Icons.Default.FormatListNumbered,"Numbered list") }
                IconButton(onClick={highlight=false;colorPicker=true}) { Icon(Icons.Default.FormatColorText,"Text color") }
                IconButton(onClick={highlight=true;colorPicker=true}) { Icon(Icons.Default.BorderColor,"Highlight text") }
                IconButton(onClick={linkPicker=true}) { Icon(Icons.Default.Link,"Link a note") }
                IconButton(onClick={rich.history.undo()},enabled=rich.history.canUndo) { Icon(Icons.Default.Undo,"Undo") }
                IconButton(onClick={rich.history.redo()},enabled=rich.history.canRedo) { Icon(Icons.Default.Redo,"Redo") }
            }
        }
        if(bracket!=null) {
            val candidates=notes.filter { it.id!=draft.saved.id && it.title.contains(bracket.groupValues[1],true) }.take(5)
            candidates.forEach { target->TextButton(onClick={
                rich.selection=TextRange(bracket.range.first,rich.selection.end); rich.replaceSelectedText("")
                rich.addLink(target.title.ifBlank { "Untitled note" },"daylighthub://note/"+target.id)
            }) { Text("Link: "+target.title+" · "+target.notebook) } }
        }
        CompositionLocalProvider(LocalUriHandler provides uriHandler) {
            RichTextEditor(rich,Modifier.weight(1f).fillMaxWidth(),placeholder={Text("Start writing…")},
                textStyle=MaterialTheme.typography.bodyLarge.copy(color=MaterialTheme.colorScheme.onSurface,lineHeight=26.sp,
                    fontFamily=when(vm.appearance.editorFont) {"Serif"->FontFamily.Serif;"Mono"->FontFamily.Monospace;else->FontFamily.SansSerif}))
        }
        if(backlinks.isNotEmpty()) {
            Text("Linked from",style=MaterialTheme.typography.labelMedium)
            Row(Modifier.horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(8.dp)) {
                backlinks.forEach { note->AssistChip(onClick={vm.openNote(note.id)},label={Text(note.title.ifBlank{"Untitled note"})}) }
            }
        }
    }
    if(linkPicker) AlertDialog(onDismissRequest={linkPicker=false},title={Text("Link to a note")},text={
        Column { OutlinedTextField(linkSearch,{linkSearch=it},label={Text("Find a note")})
            LazyColumn(Modifier.heightIn(max=300.dp)) { items(notes.filter{it.id!=draft.saved.id && it.title.contains(linkSearch,true)},key={it.id}) { target->
                TextButton(onClick={
                    if(rich.selection.collapsed) rich.addLink(target.title.ifBlank{"Untitled note"},"daylighthub://note/"+target.id)
                    else rich.addLinkToSelection("daylighthub://note/"+target.id)
                    linkPicker=false
                }) { Text(target.title+" · "+target.notebook) }
            } }
        }
    },confirmButton={TextButton(onClick={linkPicker=false}){Text("Close")}})
    if(colorPicker) AlertDialog(onDismissRequest={colorPicker=false},title={Text(if(highlight) "Highlight color" else "Text color")},text={
        Column {
            listOf(listOf(0xFFB91C1C,0xFFE8590C,0xFFEAB308),listOf(0xFF15803D,0xFF2563EB,0xFF9333EA),
                listOf(0xFFFEF08A,0xFFF9A8D4,0xFF86EFAC)).forEach { row->
                Row(horizontalArrangement=Arrangement.spacedBy(16.dp),modifier=Modifier.padding(6.dp)) { row.forEach { color->
                    Surface(color=Color(color),shape=MaterialTheme.shapes.small,modifier=Modifier.size(48.dp).clickable {
                        rich.addSpanStyle(if(highlight) SpanStyle(background=Color(color)) else SpanStyle(color=Color(color))); colorPicker=false
                    }) {}
                } }
            }
        }
    },confirmButton={TextButton(onClick={ if(highlight) rich.removeSpanStyle(SpanStyle(background=rich.currentSpanStyle.background)) else rich.removeSpanStyle(SpanStyle(color=rich.currentSpanStyle.color)); colorPicker=false }){Text("Reset selected color")}},
        dismissButton={TextButton(onClick={colorPicker=false}){Text("Cancel")}})
}
