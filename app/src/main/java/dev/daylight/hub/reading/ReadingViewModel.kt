package dev.daylight.hub.reading

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import androidx.room.withTransaction
import dev.daylight.hub.data.HubDatabase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.json.JSONObject
import java.time.LocalDate

data class OpenBook(val book:Book,val asset:ReflowAsset?=null,val pages:PageDocument?=null)

@OptIn(ExperimentalCoroutinesApi::class,FlowPreview::class)
class ReadingViewModel(app:Application,private val saved:SavedStateHandle):AndroidViewModel(app) {
    val db=HubDatabase.get(app)
    val repository=ReadingRepository(app,db)
    private val tracker=ReadingTracker(db)
    private val documentMutex=Mutex()
    private val prefs=app.getSharedPreferences("reading",0)
    val query=MutableStateFlow(saved.get<String>("query").orEmpty())
    val status=MutableStateFlow(saved.get<String>("status").orEmpty())
    val format=MutableStateFlow(saved.get<String>("format").orEmpty())
    val sort=MutableStateFlow(saved.get<String>("sort") ?: "RECENT")
    val library=combine(query.debounce(180),status,format,sort){q,s,f,o->listOf(q,s,f,o)}.flatMapLatest{filter->
        val pattern=withContext(Dispatchers.Default){dev.daylight.hub.data.CaseInsensitiveGlob.pattern(filter[0])}
        Pager(PagingConfig(pageSize=32,prefetchDistance=8,enablePlaceholders=false)){db.reading().library(filter[0],pattern,filter[1],filter[2],filter[3])}.flow
    }.cachedIn(viewModelScope)
    val recent=db.reading().recent().stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),emptyList())
    val current=db.reading().currentlyReading().stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),emptyList())
    val sessions=db.reading().sessions().stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),emptyList())
    val sessionHistory=Pager(PagingConfig(pageSize=40,enablePlaceholders=false)){db.reading().sessionHistory()}.flow.cachedIn(viewModelScope)
    val completionHistory=Pager(PagingConfig(pageSize=40,enablePlaceholders=false)){db.reading().completionHistory()}.flow.cachedIn(viewModelScope)
    val days=db.reading().dailyTime(LocalDate.now().minusDays(90).toString()).stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),emptyList())
    val completions=db.reading().completions().stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),emptyList())
    val goals=db.reading().goals().stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),emptyList())
    val imports=db.reading().imports().stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),emptyList())
    var tab by mutableStateOf(saved.get<String>("tab") ?: "Library")
    var opened by mutableStateOf<OpenBook?>(null);private set
    var position by mutableStateOf<BookPosition?>(null);private set
    var contents by mutableStateOf(emptyList<ReaderTocItem>());private set
    var fixedLayout by mutableStateOf(false);private set
    var fixedPages by mutableIntStateOf(0);private set
    var searchResults by mutableStateOf(emptyList<ReaderSearchResult>());private set
    var bookmarks by mutableStateOf(emptyList<Bookmark>());private set
    var bitmap by mutableStateOf<Bitmap?>(null);private set
    var loading by mutableStateOf(false);private set
    var pageLoading by mutableStateOf(false);private set
    var pageText by mutableStateOf("");private set
    var searching by mutableStateOf(false);private set
    var error by mutableStateOf<String?>(null);private set
    var message by mutableStateOf<String?>(null)
    var importProgress by mutableStateOf<BookImport?>(null);private set
    var importing by mutableStateOf(false);private set
    var undoBook by mutableStateOf<Book?>(null)
    var appearance by mutableStateOf(readAppearance());private set
    private var openJob:Job?=null
    private var pageJob:Job?=null
    private var searchJob:Job?=null
    private var importJob:Job?=null
    private var bookmarkJob:Job?=null
    private var positionJob:Job?=null
    private var foreground=false
    var ready by mutableStateOf(false);private set
    private var renderWidth=1080
    private val initialized=viewModelScope.async {
        tracker.recover();repository.recoverImports()
    }
    init {
        viewModelScope.launch { combine(query,status,format,sort){q,s,f,o->listOf(q,s,f,o)}.collect{v->saved["query"]=v[0];saved["status"]=v[1];saved["format"]=v[2];saved["sort"]=v[3]} }
        viewModelScope.launch { initialized.await();saved.get<Long>("bookId")?.takeIf{it>0}?.let{open(it)} }
        viewModelScope.launch { while(isActive) { delay(5000);if(foreground && ready)tracker.checkpoint() } }
    }
    fun action(block:suspend ()->Unit)=viewModelScope.launch {
        try { initialized.await();block() } catch(e:CancellationException){throw e} catch(e:Exception){message=e.message ?: "The reading action failed."}
    }
    fun selectTab(value:String){tab=value;saved["tab"]=value}
    fun import(uri:Uri) {
        if(importing){message="Finish or cancel the current import first.";return}
        importing=true;importProgress=null
        importJob=action {
            try {
                val book=repository.import(uri){progress->viewModelScope.launch{importProgress=progress}}
                message=if(importProgress?.message?.startsWith("Already")==true)"Already in your library. Position retained." else "Added ${book.title}."
                open(book.id)
            } finally { importing=false }
        }
    }
    fun cancelImport(){importJob?.cancel();importing=false;message="Import cancelled."}
    fun open(id:Long) {
        openJob?.cancel();pageJob?.cancel();searchJob?.cancel();bookmarkJob?.cancel()
        openJob=action {
            loading=true;error=null;ready=false;tracker.finish()
            try {
                documentMutex.withLock {
                    releaseDocument()
                    val book=db.reading().book(id)?.takeIf{it.deletedAt==null} ?: error("This book is unavailable.")
                    val source=repository.file(book.normalizedFile ?: book.fileName)
                    require(source.isFile){"The private copy is missing. Reimport the original file to repair it; your position is retained."}
                    var acquired:OpenBook?=null
                    val resource=try { withContext(NonCancellable+Dispatchers.IO) {
                        when(book.format) {
                            "PDF"->OpenBook(book,pages=PdfPages(getApplication(),source))
                            "CBZ"->OpenBook(book,pages=ComicPages(source))
                            else->OpenBook(book,asset=ReflowAsset(book,source))
                        }.also{acquired=it}
                    } } catch(error:Throwable) {
                        withContext(NonCancellable+Dispatchers.IO){acquired?.asset?.close();acquired?.pages?.close()};throw error
                    }
                    opened=resource;fixedLayout=false;fixedPages=0
                    position=db.reading().position(id);contents=resource.pages?.contents.orEmpty();searchResults=emptyList()
                    saved["bookId"]=id
                    if(book.status=="WANT_TO_READ")repository.status(id,book.revision,"READING")
                    val actual=db.reading().book(id)!!;opened=resource.copy(book=actual)
                    withContext(Dispatchers.IO){db.reading().updateProgress(id,book.progress,System.currentTimeMillis())}
                    resource.pages?.let { pages ->
                        val index=(position?.pageIndex ?: 0).coerceIn(0,pages.pageCount-1)
                        position=(position ?: BookPosition(id,kind="PAGE")).copy(pageIndex=index)
                        updateMetadata(pages.title,pages.author)
                        ready=true;if(foreground)tracker.start(actual)
                        showPage(index)
                    }
                    bookmarkJob=viewModelScope.launch{db.reading().bookmarks(id).collect{bookmarks=it}}
                }
            } catch(e:CancellationException){throw e} catch(e:Exception){error=e.message ?: "Could not open this book."}
            finally { loading=false }
        }
    }
    fun close() {
        openJob?.cancel();pageJob?.cancel();searchJob?.cancel();bookmarkJob?.cancel()
        action {
            positionJob?.join();position?.let{repository.savePosition(it)};tracker.finish();ready=false
            documentMutex.withLock{releaseDocument();position=null;contents=emptyList();bookmarks=emptyList();error=null;saved["bookId"]=0L}
        }
    }
    private suspend fun releaseDocument() {
        val old=opened;opened=null;bitmap=null;pageText=""
        withContext(NonCancellable+Dispatchers.IO){old?.asset?.close();old?.pages?.close()}
    }
    fun foreground(value:Boolean) {
        foreground=value
        action { if(foreground && ready)opened?.book?.let{tracker.start(it)} else tracker.finish() }
    }
    fun interacted()=tracker.interacted()
    fun event(event:ReaderEvent) {
        when(event) {
            is ReaderEvent.Location->savePosition(event.position)
            is ReaderEvent.Opened->{contents=event.contents;fixedLayout=event.fixedLayout;fixedPages=event.sections;ready=true;action{updateMetadata(event.title,event.author);if(foreground)opened?.book?.let{tracker.start(it)}}}
            is ReaderEvent.Search->{searchResults=event.results;searching=false}
            is ReaderEvent.Error->{error=event.message;ready=false;action{tracker.finish()}}
            is ReaderEvent.Interaction->interacted()
            is ReaderEvent.Selection->interacted()
        }
    }
    private fun savePosition(value:BookPosition) {
        if(value.bookId!=opened?.book?.id)return
        position=value;positionJob?.cancel()
        positionJob=action{delay(250);repository.savePosition(value)}
    }
    fun showPage(index:Int,width:Int=renderWidth) {
        val resource=opened ?: return;val pages=resource.pages ?: return
        val target=index.coerceIn(0,pages.pageCount-1);renderWidth=width
        pageJob?.cancel();pageLoading=true;interacted()
        pageJob=action {
            try {
                val image=documentMutex.withLock{withContext(Dispatchers.IO){pages.render(target,width)}}
                if(opened?.pages!==pages)return@action
                bitmap=image
                val old=position?.takeIf{it.pageIndex==target}
                savePosition(BookPosition(resource.book.id,kind="PAGE",pageIndex=target,chapter="Page ${target+1}",fraction=(target+1.0)/pages.pageCount,
                    offsetX=old?.offsetX ?: 0f,offsetY=old?.offsetY ?: 0f,zoom=old?.zoom ?: 1f))
            } finally { pageLoading=false }
        }
    }
    fun transform(zoom:Float,x:Float,y:Float) { position?.takeIf{it.kind=="PAGE"}?.let{savePosition(it.copy(zoom=zoom.coerceIn(1f,5f),offsetX=x.coerceIn(0f,1f),offsetY=y.coerceIn(0f,1f)))};interacted() }
    fun search(query:String) {
        val document=opened?.pages ?: return
        searchJob?.cancel();searching=true;searchResults=emptyList()
        searchJob=action { try { searchResults=documentMutex.withLock{withContext(Dispatchers.IO){document.search(query.take(200))}} } finally{searching=false} }
    }
    fun searching(){searching=true;searchResults=emptyList()}
    fun cancelSearch(){searchJob?.cancel();searching=false}
    fun readPageText()=action {
        val resource=opened ?: return@action
        val pdf=resource.pages as? PdfPages ?: return@action
        val index=position?.pageIndex ?: return@action
        val text=documentMutex.withLock{withContext(Dispatchers.IO){pdf.pageText(index)}}
        if(opened?.pages===pdf && position?.pageIndex==index)pageText=text.ifBlank{"This page has no embedded text. Scanned pages need OCR in another app."}
    }
    fun bookmark(label:String)=action { repository.bookmark(position ?: error("Wait for a reading position."),label.trim().take(200).ifBlank{"Bookmark"}) }
    fun deleteBookmark(id:Long)=action{db.reading().deleteBookmark(id)}
    fun status(book:Book,value:String)=action {
        repository.status(book.id,book.revision,value)
        if(opened?.book?.id==book.id)opened=opened?.copy(book=db.reading().book(book.id)!!)
    }
    fun remove(book:Book)=action{repository.remove(book);undoBook=book.copy(revision=book.revision+1);if(opened?.book?.id==book.id)close()}
    fun restore(book:Book)=action{repository.restore(book.id,book.revision)}
    fun metadata(book:Book,title:String,author:String)=action {
        require(title.isNotBlank() && title.length<=512 && author.length<=512)
        db.withTransaction {
            val current=db.reading().book(book.id) ?: error("Book unavailable.")
            check(current.revision==book.revision){"Book changed. Reopen its details."}
            db.reading().update(current.copy(title=title.trim(),author=author.trim(),metadataEdited=true,revision=current.revision+1))
        }
        if(opened?.book?.id==book.id)opened=opened?.copy(book=db.reading().book(book.id)!!)
    }
    private suspend fun updateMetadata(title:String,author:String) {
        val item=opened?.book ?: return
        db.withTransaction {
            val current=db.reading().book(item.id) ?: return@withTransaction
            if(!current.metadataEdited && (title.isNotBlank() || author.isNotBlank())) {
                val updated=current.copy(title=title.ifBlank{current.title}.take(512),author=author.ifBlank{current.author}.take(512))
                db.reading().update(updated);opened=opened?.takeIf{it.book.id==item.id}?.copy(book=updated)
            }
        }
    }
    fun goal(period:String,minutes:Int,enabled:Boolean)=action{require(period in listOf("DAILY","WEEKLY") && minutes in 1..10080);db.reading().saveGoal(ReadingGoal(period,minutes,enabled))}
    fun appearance(value:ReaderAppearance) { appearance=value;prefs.edit().putString("appearance",value.json().toString()).apply() }
    private fun readAppearance():ReaderAppearance=runCatching {
        val j=JSONObject(prefs.getString("appearance","{}")!!)
        ReaderAppearance(j.optString("theme","Light"),j.optString("font","serif"),j.optInt("fontSize",18).coerceIn(12,40),j.optDouble("lineHeight",1.6).toFloat().coerceIn(1.2f,2.2f),j.optBoolean("scrolling"),fixedZoom=j.optString("fixedZoom","fit-page"))
    }.getOrDefault(ReaderAppearance())
    override fun onCleared() {
        val resource=opened
        // Avoid blocking the main thread while PDFium releases native resources.
        CoroutineScope(SupervisorJob()+Dispatchers.IO).launch { documentMutex.withLock{resource?.asset?.close();resource?.pages?.close()};tracker.finish() }
        super.onCleared()
    }
}
