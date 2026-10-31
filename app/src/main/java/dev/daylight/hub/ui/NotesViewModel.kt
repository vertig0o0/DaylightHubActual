package dev.daylight.hub.ui

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.*
import dev.daylight.hub.data.HubDatabase
import dev.daylight.hub.data.CaseInsensitiveGlob
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

@OptIn(ExperimentalCoroutinesApi::class,FlowPreview::class)
class NotesViewModel(app:Application,private val saved:SavedStateHandle):AndroidViewModel(app) {
    val query=MutableStateFlow(saved.get<String>("query").orEmpty())
    val notebook=MutableStateFlow(saved.get<String>("notebook").orEmpty())
    val sortTitle=MutableStateFlow(saved.get<Boolean>("sortTitle") ?: false)
    val notes=combine(query.debounce(180),notebook,sortTitle){q,n,s->Triple(q,n,s)}.flatMapLatest{(q,n,s)->
        val pattern=withContext(Dispatchers.Default){CaseInsensitiveGlob.pattern(q)}
        Pager(PagingConfig(pageSize=32,prefetchDistance=8,enablePlaceholders=false)){HubDatabase.get(app).notes().page(q,pattern,n,s)}.flow
    }.cachedIn(viewModelScope)
    init { viewModelScope.launch{combine(query,notebook,sortTitle){q,n,s->Triple(q,n,s)}.collect{(q,n,s)->saved["query"]=q;saved["notebook"]=n;saved["sortTitle"]=s}} }
}
