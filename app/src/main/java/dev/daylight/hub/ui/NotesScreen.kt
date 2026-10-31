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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.*
import com.mohamedrejeb.richeditor.model.*
import com.mohamedrejeb.richeditor.json.*
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import dev.daylight.hub.*
import dev.daylight.hub.data.Note
import dev.daylight.hub.data.NoteSummary
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.*
import dev.daylight.hub.ai.*
import kotlinx.coroutines.flow.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable fun NotesScreen(vm:HubViewModel,wide:Boolean) {
    val notes by vm.notes.collectAsStateWithLifecycle()
    val screen:NotesViewModel=viewModel()
    val query by screen.query.collectAsStateWithLifecycle()
    val notebook by screen.notebook.collectAsStateWithLifecycle()
    val sortTitle by screen.sortTitle.collectAsStateWithLifecycle()
    val matches=screen.notes.collectAsLazyPagingItems()
    val keyboardVisible=WindowInsets.ime.getBottom(LocalDensity.current)>0
    var deleting by remember { mutableStateOf<NoteSummary?>(null) }
    Row(Modifier.fillMaxSize(),horizontalArrangement=Arrangement.spacedBy(16.dp)) {
        if(wide || vm.editor==null) Column(if(wide) Modifier.width(290.dp) else Modifier.fillMaxSize()) {
            Row(Modifier.fillMaxWidth().padding(vertical=12.dp),verticalAlignment=Alignment.CenterVertically) {
                Text("Library",style=MaterialTheme.typography.titleLarge,modifier=Modifier.weight(1f))
                FilledTonalIconButton(onClick={vm.openNote()}) { Icon(Icons.Default.Add,"New note") }
            }
            OutlinedTextField(query,{screen.query.value=it},label={Text("Search notes and tags")},leadingIcon={Icon(Icons.Default.Search,null)},singleLine=true,modifier=Modifier.fillMaxWidth())
            if(!keyboardVisible)Row(Modifier.horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(8.dp)) {
                (listOf("")+notes.map { it.notebook }.distinct().sorted()).forEach { name->FilterChip(notebook==name,{screen.notebook.value=name},label={Text(name.ifBlank{"All notebooks"})}) }
            }
            if(!keyboardVisible)TextButton(onClick={screen.sortTitle.value=!sortTitle}) { Icon(Icons.Default.Sort,null); Text(if(sortTitle) "Title A–Z" else "Recently updated") }
            if(matches.itemCount==0 && matches.loadState.refresh is LoadState.NotLoading) EmptyState("No notes here","Create a note or change your search.")
            if(matches.loadState.refresh is LoadState.Loading)LinearProgressIndicator(Modifier.fillMaxWidth())
            if(matches.loadState.refresh is LoadState.Error)TextButton(onClick={matches.retry()}){Text("Retry loading notes")}
            LazyColumn(verticalArrangement=Arrangement.spacedBy(if(vm.appearance.compact) 6.dp else 12.dp),contentPadding=PaddingValues(bottom=16.dp)) {
                items(matches.itemCount,key=matches.itemKey{it.id}) { index->matches[index]?.let { note->
                    Card(onClick={vm.openNote(note.id)},modifier=animatedItem(),colors=CardDefaults.cardColors(containerColor=if(vm.editor?.saved?.id==note.id) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface)) {
                        Row(Modifier.fillMaxWidth().padding(if(vm.appearance.compact) 10.dp else 16.dp),verticalAlignment=Alignment.Top) {
                            Column(Modifier.weight(1f),verticalArrangement=Arrangement.spacedBy(5.dp)) {
                                Text(note.title.ifBlank { "Untitled note" },fontWeight=FontWeight.SemiBold)
                                Text(note.preview,maxLines=3,style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(note.notebook+(if(note.tags.isBlank()) "" else " · "+note.tags),style=MaterialTheme.typography.labelSmall)
                            }
                            IconButton(onClick={deleting=note}) { Icon(Icons.Default.DeleteOutline,"Delete note") }
                        }
                    }
                } }
            }
        }
        val draft=vm.editor
        if(draft!=null) key(draft) { NoteWritingPage(vm,draft,notes,Modifier.weight(1f)) }
        else if(wide) Box(Modifier.weight(1f).fillMaxHeight(),contentAlignment=Alignment.Center) { EmptyState("Your writing space","Choose a note, or start something new.") }
    }
    deleting?.let { note->AlertDialog(onDismissRequest={deleting=null},title={Text("Delete note?")},text={Text(note.title.ifBlank { "Untitled note" })},
        confirmButton={TextButton(onClick={vm.deleteNote(note); deleting=null}) { Text("Delete") }},dismissButton={TextButton(onClick={deleting=null}){Text("Keep note")}}) }
}
@Composable private fun NoteWritingPage(vm:HubViewModel,draft:EditorDraft,notes:List<NoteSummary>,modifier:Modifier) {
    val rich=draft.rich
    var linkPicker by remember { mutableStateOf(false) }
    var linkSearch by remember { mutableStateOf("") }
    var colorPicker by remember { mutableStateOf(false) }
    var highlight by remember { mutableStateOf(false) }
    var showMetadata by remember { mutableStateOf(false) }
    var skillMenu by remember { mutableStateOf(false) }
    var formattingMenu by remember { mutableStateOf(false) }
    var skillReview by remember(draft) { mutableStateOf<SkillReview?>(null) }
    var labelSuggestions by remember { mutableStateOf(false) }
    var labelPicker by remember { mutableStateOf(false) }
    var showProject by remember { mutableStateOf(false) }
    var titleFocused by remember(draft){mutableStateOf(false)}
    val keyboardVisible=WindowInsets.ime.getBottom(LocalDensity.current)>0
    val compactWriting=keyboardVisible && !titleFocused
    val projects by vm.projects.collectAsStateWithLifecycle()
    val labels by vm.labels.collectAsStateWithLifecycle()
    fun requestSkill(kind:SkillKind,allowed:Map<Long,String> = emptyMap()) {
        vm.action {
            vm.saveDraft(draft)
            val range=rich.selection
            val text=rich.annotatedString.text
            val start=minOf(range.start,range.end).coerceIn(0,text.length)
            val end=maxOf(range.start,range.end).coerceIn(0,text.length)
            val selected=if(start==end) "" else text.substring(start,end)
            val cleanup=kind in listOf(SkillKind.CLEARER,SkillKind.PUNCTUATION,SkillKind.SHORTER)
            require(!cleanup || selected.isNotBlank()) { "Select a short passage to edit first." }
            val source=selected.ifBlank{rich.toText()}
            require(source.isNotBlank() && source.length<=1600) { "Select an excerpt of up to 1,600 characters first." }
            val fingerprint=draft.snapshot();val revision=draft.saved.revision
            skillReview=SkillReview(when(kind){SkillKind.TITLE->"Suggest a note title";SkillKind.LABEL->"Suggest a label";else->"Edit selected text"},SkillRequest(kind,source,allowedLabels=allowed)) { suggestion ->
                check(vm.editor===draft && draft.snapshot()==fingerprint && draft.saved.revision==revision) { "The note changed. Request a fresh suggestion." }
                val previousTitle=draft.title
                when(kind) {
                    SkillKind.TITLE->draft.title=(suggestion as SkillResult.Text).value
                    SkillKind.LABEL->draft.labelId=(suggestion as SkillResult.Label).id
                    else->{check(rich.selection==range){"The selected passage changed. Select it again."};rich.replaceSelectedText((suggestion as SkillResult.Text).value)}
                }
                vm.saveDraft(draft)
                if(kind==SkillKind.TITLE) {
                    val savedRevision=draft.saved.revision
                    vm.undoAction=UndoAction("Title updated") { check(draft.saved.revision==savedRevision){"The note changed. Edit its title manually."};draft.title=previousTitle;vm.saveDraft(draft) }
                }
            }
        }
    }
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
        if(!compactWriting)OutlinedTextField(draft.title,{draft.title=it},placeholder={Text("Untitled note")},textStyle=MaterialTheme.typography.headlineSmall,singleLine=true,modifier=Modifier.fillMaxWidth().onFocusChanged{titleFocused=it.hasFocus}.semantics{contentDescription="Note title"})
        if(!keyboardVisible)TextButton(onClick={showProject=true}) { Text(projects.firstOrNull{it.id==draft.projectId}?.name ?: "Inbox · Assign project") }
        Surface(shape=MaterialTheme.shapes.medium,color=MaterialTheme.colorScheme.surfaceVariant) {
            Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),verticalAlignment=Alignment.CenterVertically) {
                IconButton(onClick={rich.toggleSpanStyle(SpanStyle(fontWeight=FontWeight.Bold))},modifier=Modifier.semantics{contentDescription="Bold"}) { Text("B",fontWeight=FontWeight.Bold) }
                IconButton(onClick={rich.toggleSpanStyle(SpanStyle(fontStyle=FontStyle.Italic))},modifier=Modifier.semantics{contentDescription="Italic"}) { Text("I",fontStyle=FontStyle.Italic) }
                IconButton(onClick={rich.toggleUnorderedList()}) { Icon(Icons.Default.FormatListBulleted,"Bullet list") }
                IconButton(onClick={rich.history.undo()},enabled=rich.history.canUndo) { Icon(Icons.Default.Undo,"Undo") }
                Box {
                    IconButton(onClick={skillMenu=true}) { Icon(Icons.Default.AutoAwesome,"AI writing actions") }
                    DropdownMenu(skillMenu,{skillMenu=false}) {
                        DropdownMenuItem(text={Text("Suggest title")},onClick={skillMenu=false;requestSkill(SkillKind.TITLE)})
                        DropdownMenuItem(text={Text("Suggest label")},onClick={skillMenu=false;labelSuggestions=true})
                        listOf(SkillKind.CLEARER to "Clearer wording",SkillKind.PUNCTUATION to "Fix punctuation",SkillKind.SHORTER to "Make shorter").forEach{(kind,label)->
                            DropdownMenuItem(text={Text(label)},enabled=!rich.selection.collapsed,onClick={skillMenu=false;requestSkill(kind)})
                        }
                    }
                }
                IconButton(onClick={formattingMenu=true}){Icon(Icons.Default.MoreHoriz,"More formatting")}
            }
        }
        if(formattingMenu) AlertDialog(onDismissRequest={formattingMenu=false},title={Text("Formatting")},text={
            Column(Modifier.verticalScroll(rememberScrollState())) {
                listOf("Underline","Strikethrough","Heading 1","Heading 2","Numbered list","Link a note","Text color","Highlight text","Redo","Continue here").forEach{label->
                    TextButton(enabled=label!="Redo" || rich.history.canRedo,onClick={
                        when(label) {
                            "Underline"->rich.toggleSpanStyle(SpanStyle(textDecoration=TextDecoration.Underline))
                            "Strikethrough"->rich.toggleSpanStyle(SpanStyle(textDecoration=TextDecoration.LineThrough))
                            "Heading 1"->rich.setHeadingStyle(if(rich.currentHeadingStyle==HeadingStyle.H1)HeadingStyle.Normal else HeadingStyle.H1)
                            "Heading 2"->rich.setHeadingStyle(if(rich.currentHeadingStyle==HeadingStyle.H2)HeadingStyle.Normal else HeadingStyle.H2)
                            "Numbered list"->rich.toggleOrderedList()
                            "Link a note"->linkPicker=true
                            "Text color"->{highlight=false;colorPicker=true}
                            "Highlight text"->{highlight=true;colorPicker=true}
                            "Redo"->rich.history.redo()
                            "Continue here"->vm.noteHandoff(draft)
                        };formattingMenu=false
                    }){Text(label)}
                }
            }
        },confirmButton={TextButton(onClick={formattingMenu=false}){Text("Done")}})
        if(bracket!=null) {
            val candidates=notes.filter { it.id!=draft.saved.id && it.title.contains(bracket.groupValues[1],true) }.take(5)
            candidates.forEach { target->TextButton(onClick={
                rich.selection=TextRange(bracket.range.first,rich.selection.end); rich.replaceSelectedText("")
                rich.addLink(target.title.ifBlank { "Untitled note" },"daylighthub://note/"+target.id)
            }) { Text("Link: "+target.title+" · "+target.notebook) } }
        }
        CompositionLocalProvider(LocalUriHandler provides uriHandler) {
            RichTextEditor(rich,Modifier.weight(1f).fillMaxWidth().semantics{contentDescription="Note content"},placeholder={Text("Start writing…")},
                textStyle=MaterialTheme.typography.bodyLarge.copy(color=MaterialTheme.colorScheme.onSurface,lineHeight=26.sp,
                    fontFamily=when(vm.appearance.editorFont) {"Serif"->FontFamily.Serif;"Mono"->FontFamily.Monospace;else->FontFamily.SansSerif}))
        }
        if(backlinks.isNotEmpty() && !keyboardVisible) {
            Text("Linked from",style=MaterialTheme.typography.labelMedium)
            Row(Modifier.horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(8.dp)) {
                backlinks.forEach { note->AssistChip(onClick={vm.openNote(note.id)},label={Text(note.title.ifBlank{"Untitled note"})}) }
            }
        }
    }
    if(showMetadata)EditorSheet("Note details",{showMetadata=false}) {
        OutlinedTextField(draft.notebook,{draft.notebook=it},label={Text("Notebook")},singleLine=true,modifier=Modifier.fillMaxWidth())
        OutlinedTextField(draft.tags,{draft.tags=it},label={Text("Tags, comma separated")},singleLine=true,modifier=Modifier.fillMaxWidth())
        TextButton(onClick={showMetadata=false;labelPicker=true}){Text("Label: "+(labels.firstOrNull{it.id==draft.labelId}?.name ?: "None"))}
        TextButton(onClick={showMetadata=false}){Text("Done")}
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
    if(showProject) ProjectPicker(projects,draft.projectId,{draft.projectId=it;showProject=false},{showProject=false})
    skillReview?.let{review->SkillDialog(vm,review,{skillReview=null})}
    if(labelSuggestions) LabelCatalogDialog(vm,{allowed->labelSuggestions=false;requestSkill(SkillKind.LABEL,allowed)},{labelSuggestions=false})
    if(labelPicker) EditorSheet("Note label",{labelPicker=false}) {
        TextButton(onClick={draft.labelId=null;labelPicker=false}) { Text("No label") }
        labels.forEach{label->TextButton(onClick={draft.labelId=label.id;labelPicker=false}) { Text(label.name) }}
    }
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
