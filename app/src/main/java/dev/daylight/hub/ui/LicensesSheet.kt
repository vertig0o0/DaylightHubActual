package dev.daylight.hub.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable fun LicensesSheet(dismiss:()->Unit) {
    val context=LocalContext.current
    val files=remember { linkedMapOf("Overview" to "THIRD_PARTY_NOTICES.txt","llama.cpp" to "llama-LICENSE.txt","Reader JavaScript" to "reader/LICENSES.txt").apply {
        context.assets.list("licenses").orEmpty().sorted().forEach{put(it.removeSuffix(".txt"),"licenses/$it")}
    } }
    var selected by remember{mutableStateOf("Overview")};var expanded by remember{mutableStateOf(false)}
    val text by produceState("Loading…",selected){value=withContext(Dispatchers.IO){runCatching{context.assets.open(files.getValue(selected)).bufferedReader().use{it.readText()}}.getOrDefault("This notice could not be opened.")}}
    androidx.compose.ui.window.Dialog(onDismissRequest=dismiss,properties=androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth=false)) {
        Surface(Modifier.fillMaxWidth(.96f).fillMaxHeight(.92f),shape=MaterialTheme.shapes.large) {
            Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment=Alignment.CenterVertically){Text("Open source licenses",Modifier.weight(1f),style=MaterialTheme.typography.titleLarge);IconButton(onClick=dismiss){Icon(Icons.Default.Close,"Close licenses")}}
                Box { OutlinedButton(onClick={expanded=true}){Text(selected)};DropdownMenu(expanded,{expanded=false}){files.keys.forEach{name->DropdownMenuItem(text={Text(name)},onClick={selected=name;expanded=false})}} }
                key(selected){SelectionContainer(Modifier.weight(1f).verticalScroll(rememberScrollState())){Text(text,style=MaterialTheme.typography.bodySmall)}}
            }
        }
    }
}
