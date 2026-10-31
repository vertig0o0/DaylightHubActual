package dev.daylight.hub.ui

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.NavigateBefore
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.*
import androidx.lifecycle.compose.*
import androidx.paging.LoadState
import androidx.paging.compose.*
import dev.daylight.hub.reading.*
import kotlinx.coroutines.*
import java.time.*
import java.time.format.DateTimeFormatter

private val readingStatuses=listOf("WANT_TO_READ","READING","PAUSED","FINISHED")
private fun readingStatus(value:String)=when(value){"WANT_TO_READ"->"Want to read";"READING"->"Reading";"PAUSED"->"Paused";"FINISHED"->"Finished";else->value}

@Composable fun ReadingScreen(vm:ReadingViewModel) {
    val context=LocalContext.current
    val lifecycle=androidx.lifecycle.compose.LocalLifecycleOwner.current.lifecycle
    val view=LocalView.current
    val inReader=vm.opened!=null || vm.loading || vm.error!=null
    DisposableEffect(lifecycle,inReader,view) {
        fun visible()=vm.foreground(inReader && view.hasWindowFocus() && lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED))
        val observer=LifecycleEventObserver { _,event ->
            if(event==Lifecycle.Event.ON_RESUME)visible()
            if(event==Lifecycle.Event.ON_PAUSE)vm.foreground(false)
        }
        val focus=android.view.ViewTreeObserver.OnWindowFocusChangeListener{visible()}
        lifecycle.addObserver(observer);view.viewTreeObserver.addOnWindowFocusChangeListener(focus);visible()
        onDispose { lifecycle.removeObserver(observer);if(view.viewTreeObserver.isAlive)view.viewTreeObserver.removeOnWindowFocusChangeListener(focus);vm.foreground(false) }
    }
    BackHandler(inReader){vm.close()}
    val picker=rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()){uri->uri?.let{
        runCatching{context.contentResolver.takePersistableUriPermission(it,Intent.FLAG_GRANT_READ_URI_PERMISSION)}
        vm.import(it)
    }}
    var details by remember{mutableStateOf<Book?>(null)}
    if(inReader) ReaderScreen(vm) else Column(Modifier.fillMaxSize(),verticalArrangement=Arrangement.spacedBy(12.dp)) {
        Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically) {
            Row(Modifier.weight(1f),horizontalArrangement=Arrangement.spacedBy(8.dp)) {
                listOf("Library","Journey").forEach{tab->FilterChip(vm.tab==tab,{vm.selectTab(tab)},label={Text(tab)})}
            }
            FilledTonalButton(onClick={picker.launch(arrayOf("*/*"))},enabled=!vm.importing){Icon(Icons.Default.Add,null);Spacer(Modifier.width(4.dp));Text("Import")}
        }
        if(vm.importing) vm.importProgress?.let{job->Card {
            Column(Modifier.fillMaxWidth().padding(12.dp)) {
                Text("${job.stage.lowercase().replaceFirstChar{it.uppercase()}} · ${job.originalName}",maxLines=2)
                val expected=job.expectedBytes
                if(expected!=null && expected>0)LinearProgressIndicator(progress={(job.bytesCopied.toDouble()/expected).toFloat().coerceIn(0f,1f)},modifier=Modifier.fillMaxWidth()) else LinearProgressIndicator(Modifier.fillMaxWidth())
                TextButton(onClick=vm::cancelImport){Text("Cancel import")}
            }
        }}
        if(vm.tab=="Journey") ReadingJourney(vm) else ReadingLibrary(vm,onDetails={details=it})
    }
    details?.let{book->BookDetails(vm,book,{details=null})}
}

@Composable private fun ReadingLibrary(vm:ReadingViewModel,onDetails:(Book)->Unit) {
    val query by vm.query.collectAsStateWithLifecycle()
    val status by vm.status.collectAsStateWithLifecycle()
    val sort by vm.sort.collectAsStateWithLifecycle()
    val format by vm.format.collectAsStateWithLifecycle()
    val recent by vm.recent.collectAsStateWithLifecycle()
    val imports by vm.imports.collectAsStateWithLifecycle()
    val books=vm.library.collectAsLazyPagingItems()
    var filterMenu by remember{mutableStateOf(false)}
    var sortMenu by remember{mutableStateOf(false)}
    Column(Modifier.fillMaxSize(),verticalArrangement=Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(query,{vm.query.value=it.take(200)},Modifier.fillMaxWidth(),placeholder={Text("Search title or author")},leadingIcon={Icon(Icons.Default.Search,null)},singleLine=true,
            trailingIcon={if(query.isNotEmpty())IconButton(onClick={vm.query.value=""}){Icon(Icons.Default.Close,"Clear book search")}})
        Row(horizontalArrangement=Arrangement.spacedBy(8.dp)) {
            Box { OutlinedButton(onClick={filterMenu=true}){Icon(Icons.Default.FilterList,null);Spacer(Modifier.width(4.dp));Text(if(status.isEmpty() && format.isEmpty())"Filter" else "Filtered")}
                DropdownMenu(filterMenu,{filterMenu=false}) {
                    DropdownMenuItem(text={Text("All statuses")},onClick={vm.status.value="";filterMenu=false})
                    readingStatuses.forEach{value->DropdownMenuItem(text={Text(readingStatus(value))},onClick={vm.status.value=value;filterMenu=false})}
                    HorizontalDivider()
                    DropdownMenuItem(text={Text("All formats")},onClick={vm.format.value="";filterMenu=false})
                    listOf("EPUB","PDF","MOBI","FB2","FB2_ZIP","TXT","HTML","RTF","CBZ","CBR").forEach{value->DropdownMenuItem(text={Text(value.replace('_',' '))},onClick={vm.format.value=value;filterMenu=false})}
                }
            }
            Box { TextButton(onClick={sortMenu=true}){Text("Sort: ${sort.lowercase().replaceFirstChar{it.uppercase()}}")}
                DropdownMenu(sortMenu,{sortMenu=false}) { listOf("RECENT","TITLE","AUTHOR","PROGRESS","IMPORTED").forEach{value->DropdownMenuItem(text={Text(value.lowercase().replaceFirstChar{it.uppercase()})},onClick={vm.sort.value=value;sortMenu=false})} }
            }
        }
        LazyColumn(Modifier.weight(1f),verticalArrangement=Arrangement.spacedBy(10.dp),contentPadding=PaddingValues(bottom=24.dp)) {
            if(query.isBlank() && status.isEmpty() && format.isEmpty() && recent.isNotEmpty()) item {
                Text("Continue reading",style=MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp));BookRow(vm,recent.first(),onDetails,compact=true)
                Spacer(Modifier.height(12.dp));Text("Your library",style=MaterialTheme.typography.titleMedium)
            }
            imports.filter{it.stage in listOf("FAILED","INTERRUPTED")}.take(2).forEach{job->item(key="import:${job.id}") {
                Card { Column(Modifier.fillMaxWidth().padding(12.dp)) { Text(job.originalName,style=MaterialTheme.typography.titleSmall);Text(job.message);TextButton(onClick={vm.action{vm.db.reading().dismissImport(job.id)}}){Text("Dismiss")}} }
            }}
            if(books.loadState.refresh is LoadState.Loading)item{Box(Modifier.fillMaxWidth().padding(32.dp),contentAlignment=Alignment.Center){CircularProgressIndicator()}}
            if(books.loadState.refresh is LoadState.Error)item{Text("The library could not be loaded.");TextButton(onClick={books.retry()}){Text("Retry")}}
            if(books.itemCount==0 && books.loadState.refresh is LoadState.NotLoading)item { EmptyState(if(query.isEmpty() && status.isEmpty() && format.isEmpty())"Your next chapter starts here" else "No matching books","Import a DRM-free book or PDF. A private copy stays available when the original moves.") }
            items(books.itemCount,key=books.itemKey{it.id}){index->books[index]?.let{book->BookRow(vm,book,onDetails)}}
            if(books.loadState.append is LoadState.Loading)item{LinearProgressIndicator(Modifier.fillMaxWidth())}
            if(books.loadState.append is LoadState.Error)item{TextButton(onClick={books.retry()}){Text("Retry loading more books")}}
        }
    }
}

@Composable internal fun BookRow(vm:ReadingViewModel,book:Book,onDetails:(Book)->Unit,compact:Boolean=false) {
    Card(onClick={vm.open(book.id)},modifier=Modifier.fillMaxWidth()) {
        Row(Modifier.padding(12.dp),verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(12.dp)) {
            BookCover(vm,book,Modifier.size(if(compact)48.dp else 56.dp,if(compact)64.dp else 76.dp))
            Column(Modifier.weight(1f),verticalArrangement=Arrangement.spacedBy(4.dp)) {
                Text(book.title,style=MaterialTheme.typography.titleSmall,maxLines=2)
                if(book.author.isNotBlank())Text(book.author,style=MaterialTheme.typography.bodySmall,maxLines=1)
                Text("${book.originalFormat.replace('_',' ')} · ${readingStatus(book.status)} · ${(book.progress*100).toInt()}%",style=MaterialTheme.typography.labelSmall,color=MaterialTheme.colorScheme.onSurfaceVariant)
                SavedProgress(book.progress.toFloat(),Modifier.fillMaxWidth())
            }
            IconButton(onClick={onDetails(book)}){Icon(Icons.Default.MoreVert,"Details for ${book.title}")}
        }
    }
}

@Composable private fun BookCover(vm:ReadingViewModel,book:Book,modifier:Modifier) {
    val image by produceState<android.graphics.Bitmap?>(null,book.coverFile) {
        value=withContext(Dispatchers.IO){book.coverFile?.let{runCatching{BitmapFactory.decodeFile(vm.repository.file(it).absolutePath)}.getOrNull()}}
    }
    Box(modifier.clip(RoundedCornerShape(8.dp)).background(MaterialTheme.colorScheme.secondaryContainer),contentAlignment=Alignment.Center) {
        if(image!=null)Image(image!!.asImageBitmap(),null,Modifier.fillMaxSize(),contentScale=ContentScale.Crop)
        else Icon(if(book.format=="PDF")Icons.Default.PictureAsPdf else Icons.Default.MenuBook,null,tint=MaterialTheme.colorScheme.onSecondaryContainer)
    }
}

@Composable private fun BookDetails(vm:ReadingViewModel,book:Book,dismiss:()->Unit) {
    var title by remember(book.id){mutableStateOf(book.title)};var author by remember(book.id){mutableStateOf(book.author)}
    var status by remember(book.id){mutableStateOf(book.status)}
    AlertDialog(onDismissRequest=dismiss,title={Text("Book details")},text={Column(Modifier.verticalScroll(rememberScrollState()),verticalArrangement=Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(title,{title=it},label={Text("Title")},singleLine=true)
        OutlinedTextField(author,{author=it},label={Text("Author")},singleLine=true)
        Text("${book.originalFormat.replace('_',' ')} · ${book.bytes/(1024*1024)} MB · ${(book.progress*100).toInt()}%",style=MaterialTheme.typography.bodySmall)
        readingStatuses.forEach{value->Row(Modifier.fillMaxWidth().clickable{status=value},verticalAlignment=Alignment.CenterVertically){RadioButton(status==value,{status=value});Text(readingStatus(value))}}
        if(book.originalFormat in listOf("TXT","RTF","HTML"))Text("An offline reading copy is converted to EPUB. RTF uses extracted text; HTML uses readable text. The original is retained.",style=MaterialTheme.typography.bodySmall)
        TextButton(onClick={vm.remove(book);dismiss()}){Icon(Icons.Default.DeleteOutline,null);Text("Remove from library")}
    }},confirmButton={TextButton(enabled=title.isNotBlank() && title.length<=512 && author.length<=512,onClick={vm.action {
        // One transaction prevents the metadata and status controls racing each other.
        vm.repository.editDetails(book,title,author,status)
    };dismiss()}){Text("Save")}},dismissButton={TextButton(onClick=dismiss){Text("Cancel")}})
}

@Composable private fun ReaderScreen(vm:ReadingViewModel) {
    val motion=LocalHubMotion.current
    val readerAppearance=vm.appearance.copy(reducedMotion=!motion.placement)
    val resource=vm.opened
    var controls by rememberSaveable(resource?.book?.id){mutableStateOf(true)}
    var panel by rememberSaveable(resource?.book?.id){mutableStateOf("")}
    var web by remember{mutableStateOf<ReflowReader?>(null)}
    var search by rememberSaveable{mutableStateOf("")}
    var bookmarkLabel by rememberSaveable{mutableStateOf("")}
    var bookmarkDialog by remember{mutableStateOf(false)}
    fun closePanel(){vm.cancelSearch();web?.command("cancelSearch");panel=""}
    fun go(locator:String) { if(resource?.pages!=null)locator.toIntOrNull()?.let{vm.showPage(it)} else web?.command("goTo",locator=locator);closePanel() }
    Column(Modifier.fillMaxSize()) {
        if(controls)Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically) {
            IconButton(onClick=vm::close){Icon(Icons.AutoMirrored.Filled.ArrowBack,"Back to library")}
            Column(Modifier.weight(1f)) { Text(resource?.book?.title ?: "Opening book",style=MaterialTheme.typography.titleSmall,maxLines=1);vm.position?.let{Text(if(it.kind=="PAGE")"Page ${it.pageIndex+1} of ${resource?.pages?.pageCount}" else if(vm.fixedLayout)"Page ${it.section+1} of ${vm.fixedPages}" else "${(it.fraction*100).toInt()}% · ${it.chapter}",style=MaterialTheme.typography.bodySmall,maxLines=1)} }
            IconButton(enabled=vm.ready,onClick={panel="Contents"}){Icon(Icons.Default.Toc,"Contents and bookmarks")}
            IconButton(enabled=vm.ready,onClick={panel="Tools"}){Icon(Icons.Default.MoreVert,"Reading tools")}
        }
        if(vm.loading)Box(Modifier.weight(1f).fillMaxWidth(),contentAlignment=Alignment.Center){CircularProgressIndicator()}
        else if(vm.error!=null)Column(Modifier.weight(1f).fillMaxWidth().padding(24.dp),verticalArrangement=Arrangement.Center) {
            Text("Could not open this book",style=MaterialTheme.typography.titleLarge);Text(vm.error!!)
            Row { TextButton(onClick=vm::close){Text("Library")};resource?.let{TextButton(onClick={vm.open(it.book.id)}){Text("Retry")}} }
        } else Box(Modifier.weight(1f).fillMaxWidth()) {
            resource?.asset?.let{asset->key(asset.token) {
                AndroidView(factory={context->ReflowReader(context,asset,vm.position,readerAppearance,{event->if(vm.opened?.asset===asset)vm.event(event)}).also{web=it}},update={it.appearance(readerAppearance)},modifier=Modifier.fillMaxSize(),onRelease={it.release();if(web===it)web=null})
            }}
            if(resource?.pages!=null) PageImage(vm)
            if(!controls)FilledTonalIconButton(onClick={controls=true;vm.interacted()},modifier=Modifier.align(Alignment.TopEnd).padding(8.dp)){Icon(Icons.Default.MoreHoriz,"Show reading controls")}
            if(vm.pageLoading)LinearProgressIndicator(Modifier.fillMaxWidth().align(Alignment.TopCenter))
        }
        if(controls && resource!=null)Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.SpaceBetween) {
            IconButton(onClick={if(resource.pages!=null)vm.showPage((vm.position?.pageIndex ?: 0)-1) else web?.command("previous")},enabled=resource.pages==null || (vm.position?.pageIndex ?: 0)>0){Icon(Icons.AutoMirrored.Filled.NavigateBefore,"Previous page")}
            TextButton(onClick={bookmarkLabel=vm.position?.chapter.orEmpty().ifBlank{"Bookmark"};bookmarkDialog=true}){Icon(Icons.Default.BookmarkAdd,null);Spacer(Modifier.width(4.dp));Text("Bookmark")}
            IconButton(onClick={controls=false;vm.interacted()}){Icon(Icons.Default.Fullscreen,"Hide reading controls")}
            IconButton(onClick={if(resource.pages!=null)vm.showPage((vm.position?.pageIndex ?: 0)+1) else web?.command("next")},enabled=resource.pages==null || (vm.position?.pageIndex ?: 0)<resource.pages.pageCount-1){Icon(Icons.AutoMirrored.Filled.NavigateNext,"Next page")}
        }
    }
    if(bookmarkDialog)AlertDialog(onDismissRequest={bookmarkDialog=false},title={Text("Add bookmark")},text={OutlinedTextField(bookmarkLabel,{bookmarkLabel=it.take(200)},label={Text("Label")})},confirmButton={TextButton(onClick={vm.bookmark(bookmarkLabel);bookmarkDialog=false}){Text("Save")}},dismissButton={TextButton(onClick={bookmarkDialog=false}){Text("Cancel")}})
    if(panel.isNotEmpty())AlertDialog(onDismissRequest={closePanel()},title={Text(panel)},text={
        when(panel) {
            "Contents"->LazyColumn(Modifier.heightIn(max=480.dp)) {
                if(vm.contents.isNotEmpty())item{Text("Contents",style=MaterialTheme.typography.titleSmall)}
                items(vm.contents){item->TextButton(onClick={go(item.locator)},modifier=Modifier.padding(start=(item.depth.coerceAtMost(5)*12).dp)){Text(item.title.ifBlank{"Untitled section"})}}
                item{Text("Bookmarks",style=MaterialTheme.typography.titleSmall)}
                if(vm.bookmarks.isEmpty())item{Text("Save a bookmark to find this place again.")}
                items(vm.bookmarks,key={it.id}){item->Row(verticalAlignment=Alignment.CenterVertically){TextButton(onClick={runCatching{BookPosition.parse(item.bookId,item.position)}.onSuccess{position->if(position.kind=="PAGE")go(position.pageIndex.toString()) else go(position.locator)}},modifier=Modifier.weight(1f)){Text(item.label)};IconButton(onClick={vm.deleteBookmark(item.id)}){Icon(Icons.Default.DeleteOutline,"Delete bookmark ${item.label}")}}}
            }
            "Search"->Column {
                OutlinedTextField(search,{search=it.take(200)},label={Text("Find in book")},singleLine=true)
                Row { TextButton(onClick={if(resource?.pages!=null)vm.search(search) else {vm.searching();web?.command("search",query=search)}}){Text("Find")};if(vm.searching)TextButton(onClick={vm.cancelSearch();web?.command("cancelSearch")}){Text("Stop")}}
                if(vm.searching)LinearProgressIndicator(Modifier.fillMaxWidth())
                LazyColumn(Modifier.heightIn(max=360.dp)){items(vm.searchResults){result->TextButton(onClick={go(result.locator)}){Column{Text(result.title);Text(result.excerpt,style=MaterialTheme.typography.bodySmall)}}}}
                if(resource?.book?.format=="CBZ")Text("Image comics do not contain searchable text.")
            }
            "Appearance"->ReaderAppearanceControls(vm)
            "Page text"->SelectionContainer { Text(vm.pageText,Modifier.heightIn(max=480.dp).verticalScroll(rememberScrollState())) }
            "Go to page"->{var number by remember{mutableStateOf(((vm.position?.pageIndex ?: 0)+1).toString())};Column { OutlinedTextField(number,{number=it.filter(Char::isDigit).take(6)},label={Text("Page 1–${resource?.pages?.pageCount}")},singleLine=true);TextButton(onClick={number.toIntOrNull()?.takeIf{it in 1..(resource?.pages?.pageCount ?: 0)}?.let{go((it-1).toString())}}){Text("Go")}}}
            else->Column {
                TextButton(onClick={panel="Search"}){Icon(Icons.Default.Search,null);Text("Search this book")}
                if(resource?.asset!=null)TextButton(onClick={panel="Appearance"}){Icon(Icons.Default.TextFields,null);Text("Text and theme")}
                if(resource?.pages!=null)TextButton(onClick={panel="Go to page"}){Text("Go to page")}
                if(resource?.pages is PdfPages)TextButton(onClick={vm.readPageText();panel="Page text"}){Text("Read page text")}
                resource?.book?.let{book->readingStatuses.forEach{value->TextButton(onClick={vm.status(book,value);panel=""}){Text(if(value=="FINISHED")"Mark finished" else readingStatus(value))}}}
            }
        }
    },confirmButton={TextButton(onClick={closePanel()}){Text("Done")}})
}

@Composable private fun PageImage(vm:ReadingViewModel) {
    val image=vm.bitmap ?: return
    val position=vm.position ?: return
    var zoom by remember(position.bookId,position.pageIndex){mutableFloatStateOf(position.zoom)}
    var x by remember(position.bookId,position.pageIndex){mutableFloatStateOf(if(position.zoom==1f).5f else position.offsetX)}
    var y by remember(position.bookId,position.pageIndex){mutableFloatStateOf(if(position.zoom==1f).5f else position.offsetY)}
    BoxWithConstraints(Modifier.fillMaxSize().clip(RoundedCornerShape(0.dp)).background(MaterialTheme.colorScheme.surfaceContainer)) {
        val width=constraints.maxWidth.toFloat().coerceAtLeast(1f);val height=constraints.maxHeight.toFloat().coerceAtLeast(1f)
        Image(image.asImageBitmap(),"Page ${position.pageIndex+1}",Modifier.fillMaxSize().graphicsLayer{scaleX=zoom;scaleY=zoom;translationX=(.5f-x)*width*(zoom-1);translationY=(.5f-y)*height*(zoom-1)}
            .pointerInput(position.bookId,position.pageIndex){detectTransformGestures{_,pan,change,_->
                zoom=(zoom*change).coerceIn(1f,5f)
                if(zoom>1){x=(x-pan.x/(width*(zoom-1))).coerceIn(0f,1f);y=(y-pan.y/(height*(zoom-1))).coerceIn(0f,1f)}else{x=.5f;y=.5f}
                vm.transform(zoom,x,y)
            }},contentScale=ContentScale.Fit)
        Row(Modifier.align(Alignment.BottomEnd).padding(8.dp)) {
            FilledTonalIconButton(onClick={zoom=(zoom/1.5f).coerceAtLeast(1f);if(zoom==1f){x=.5f;y=.5f};vm.transform(zoom,x,y)}){Icon(Icons.Default.ZoomOut,"Zoom out")}
            FilledTonalIconButton(onClick={zoom=(zoom*1.5f).coerceAtMost(5f);vm.transform(zoom,x,y)}){Icon(Icons.Default.ZoomIn,"Zoom in")}
        }
    }
}

@Composable private fun ReaderAppearanceControls(vm:ReadingViewModel) {
    val value=vm.appearance
    Column(Modifier.verticalScroll(rememberScrollState())) {
        Row { listOf("Light","Dark","Sepia").forEach{theme->FilterChip(value.theme==theme,{vm.appearance(value.copy(theme=theme))},label={Text(theme)},modifier=Modifier.padding(end=4.dp))} }
        if(vm.fixedLayout) {
            Text("This book has a fixed page design. Zoom keeps its original layout.")
            listOf("fit-page" to "Fit page","fit-width" to "Fit width","1" to "100%","1.5" to "150%","2" to "200%").forEach{(zoom,label)->FilterChip(value.fixedZoom==zoom,{vm.appearance(value.copy(fixedZoom=zoom))},label={Text(label)})}
            return@Column
        }
        Text("Text size: ${value.fontSize}");Slider(value.fontSize.toFloat(),{vm.appearance(value.copy(fontSize=it.toInt()))},valueRange=12f..40f,modifier=Modifier.semantics{contentDescription="Reader text size"})
        Text("Line spacing");Slider(value.lineHeight,{vm.appearance(value.copy(lineHeight=it))},valueRange=1.2f..2.2f,modifier=Modifier.semantics{contentDescription="Reader line spacing"})
        listOf("serif","sans-serif","monospace").forEach{font->Row(Modifier.clickable{vm.appearance(value.copy(font=font))},verticalAlignment=Alignment.CenterVertically){RadioButton(value.font==font,{vm.appearance(value.copy(font=font))});Text(font)}}
        Row(verticalAlignment=Alignment.CenterVertically){Text("Continuous scrolling",Modifier.weight(1f));Switch(value.scrolling,{vm.appearance(value.copy(scrolling=it))})}
    }
}

@Composable private fun ReadingJourney(vm:ReadingViewModel) {
    val current by vm.current.collectAsStateWithLifecycle();val sessions by vm.sessions.collectAsStateWithLifecycle()
    val days by vm.days.collectAsStateWithLifecycle();val completions by vm.completions.collectAsStateWithLifecycle();val goals by vm.goals.collectAsStateWithLifecycle()
    var goalDialog by remember{mutableStateOf(false)}
    var history by rememberSaveable{mutableStateOf("")}
    val today=LocalDate.now();val weekStart=today.with(java.time.DayOfWeek.MONDAY)
    LazyColumn(Modifier.fillMaxSize(),verticalArrangement=Arrangement.spacedBy(12.dp),contentPadding=PaddingValues(bottom=24.dp)) {
        item{Row(verticalAlignment=Alignment.CenterVertically){Text("Your reading journey",Modifier.weight(1f),style=MaterialTheme.typography.titleLarge);TextButton(onClick={goalDialog=true}){Text("Goals")}}}
        goals.filter{it.enabled}.forEach{goal->item {
            val start=if(goal.period=="DAILY")today else weekStart
            val minutes=days.filter{it.localDate>=start.toString() && it.localDate<=today.toString()}.sumOf{it.milliseconds}/60000
            Card { Column(Modifier.fillMaxWidth().padding(16.dp)){Text("${if(goal.period=="DAILY")"Today" else "This week"}: $minutes / ${goal.minutes} minutes");LinearProgressIndicator(progress={(minutes.toFloat()/goal.minutes).coerceIn(0f,1f)},modifier=Modifier.fillMaxWidth().padding(top=8.dp))} }
        }}
        item{Text("Activity · last 12 weeks",style=MaterialTheme.typography.titleMedium);Spacer(Modifier.height(8.dp));ReadingHeatmap(days,today)}
        item{Text("Time counts while this reader is visible. It pauses in the background and after two minutes without interaction. Tap a control or turn a page to continue.",style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.onSurfaceVariant)}
        if(current.isNotEmpty())item{Text("Currently reading",style=MaterialTheme.typography.titleMedium)}
        items(current,key={"current:${it.id}"}){book->BookRow(vm,book,onDetails={vm.open(it.id)},compact=true)}
        item{Row(verticalAlignment=Alignment.CenterVertically){Text("Recent sessions",Modifier.weight(1f),style=MaterialTheme.typography.titleMedium);TextButton(onClick={history="Sessions"}){Text("All history")}}}
        if(sessions.isEmpty())item{Text("Open a book to start your first reading session.")}
        items(sessions.take(20),key={"session:${it.id}"}){session->ListItem(headlineContent={Text(session.title)},supportingContent={Text("${session.localDate} · ${session.creditedMs/60000} min ${session.creditedMs/1000%60} sec")})}
        item{Row(verticalAlignment=Alignment.CenterVertically){Text("Finished books",Modifier.weight(1f),style=MaterialTheme.typography.titleMedium);TextButton(onClick={history="Finished books"}){Text("All finished")}}}
        if(completions.isEmpty())item{Text("Mark a book finished in its reading tools to add it here.")}
        items(completions,key={"finished:${it.id}"}){item->ListItem(headlineContent={Text(item.title)},supportingContent={Text(Instant.ofEpochMilli(item.changedAt).atZone(ZoneId.systemDefault()).toLocalDate().format(DateTimeFormatter.ofPattern("d MMM yyyy")))},leadingContent={Icon(Icons.Default.CheckCircleOutline,null)})}
    }
    if(history.isNotEmpty())ReadingHistory(vm,history){history=""}
    if(goalDialog) {
        var period by remember{mutableStateOf("DAILY")};var minutes by remember{mutableStateOf("20")};var enabled by remember{mutableStateOf(true)}
        AlertDialog(onDismissRequest={goalDialog=false},title={Text("Optional reading goal")},text={Column {
            Row { listOf("DAILY","WEEKLY").forEach{item->FilterChip(period==item,{period=item;goals.firstOrNull{it.period==item}?.let{minutes=it.minutes.toString();enabled=it.enabled}},label={Text(item.lowercase().replaceFirstChar{it.uppercase()})})} }
            OutlinedTextField(minutes,{minutes=it.filter(Char::isDigit).take(5)},label={Text("Minutes")},singleLine=true)
            Row(verticalAlignment=Alignment.CenterVertically){Checkbox(enabled,{enabled=it});Text("Enable goal")}
        }},confirmButton={TextButton(enabled=minutes.toIntOrNull() in 1..10080,onClick={vm.goal(period,minutes.toInt(),enabled);goalDialog=false}){Text("Save")}},dismissButton={TextButton(onClick={goalDialog=false}){Text("Cancel")}})
    }
}

@Composable private fun ReadingHistory(vm:ReadingViewModel,kind:String,dismiss:()->Unit) {
    androidx.compose.ui.window.Dialog(onDismissRequest=dismiss,properties=androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth=false)) {
        Surface(Modifier.fillMaxWidth(.95f).fillMaxHeight(.9f),shape=MaterialTheme.shapes.large) {
            Column(Modifier.padding(16.dp)) {
                Row(verticalAlignment=Alignment.CenterVertically){Text(kind,Modifier.weight(1f),style=MaterialTheme.typography.titleLarge);IconButton(onClick=dismiss){Icon(Icons.Default.Close,"Close history")}}
                if(kind=="Sessions") {
                    val items=vm.sessionHistory.collectAsLazyPagingItems()
                    LazyColumn(Modifier.weight(1f)) {
                        items(items.itemCount,key=items.itemKey{it.id}){index->items[index]?.let{session->ListItem(headlineContent={Text(session.title)},supportingContent={Text("${session.localDate} · ${session.creditedMs/60000} min ${session.creditedMs/1000%60} sec")})}}
                        if(items.loadState.append is LoadState.Loading || items.loadState.refresh is LoadState.Loading)item{LinearProgressIndicator(Modifier.fillMaxWidth())}
                    }
                } else {
                    val items=vm.completionHistory.collectAsLazyPagingItems()
                    LazyColumn(Modifier.weight(1f)) {
                        items(items.itemCount,key=items.itemKey{it.id}){index->items[index]?.let{entry->ListItem(headlineContent={Text(entry.title)},supportingContent={Text(Instant.ofEpochMilli(entry.changedAt).atZone(ZoneId.systemDefault()).toLocalDate().toString())})}}
                        if(items.loadState.append is LoadState.Loading || items.loadState.refresh is LoadState.Loading)item{LinearProgressIndicator(Modifier.fillMaxWidth())}
                    }
                }
            }
        }
    }
}

@Composable private fun ReadingHeatmap(days:List<ReadingDay>,today:LocalDate) {
    val values=remember(days){days.associate{it.localDate to it.milliseconds}}
    Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(4.dp)) {
        repeat(12){week->Column(verticalArrangement=Arrangement.spacedBy(4.dp)) { repeat(7){day->
            val date=today.minusDays(83L-week*7-day);val minutes=(values[date.toString()] ?: 0)/60000
            val color=when {minutes>=30->MaterialTheme.colorScheme.primary;minutes>=10->MaterialTheme.colorScheme.primary.copy(alpha=.65f);minutes>0->MaterialTheme.colorScheme.primary.copy(alpha=.35f);else->MaterialTheme.colorScheme.surfaceContainerHighest}
            Box(Modifier.size(20.dp).clip(RoundedCornerShape(4.dp)).background(color).semantics{contentDescription="$date: $minutes reading minutes"})
        } }}
    }
}
