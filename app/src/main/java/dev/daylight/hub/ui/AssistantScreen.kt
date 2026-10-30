@file:OptIn(com.mohamedrejeb.richeditor.annotation.ExperimentalRichTextApi::class)
package dev.daylight.hub.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.*
import androidx.compose.ui.text.font.FontWeight
import dev.daylight.hub.HubViewModel
import dev.daylight.hub.ai.ApprovalProposal
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.json.*
import com.mohamedrejeb.richeditor.ui.material3.RichText

@Composable fun AssistantScreen(vm:HubViewModel) {
    val engine by vm.engine.state.collectAsState()
    val scroll=rememberLazyListState()
    LaunchedEffect(vm.chat.size,vm.preview){ if(vm.chat.size>0) scroll.animateScrollToItem(vm.chat.size.coerceAtMost(scroll.layoutInfo.totalItemsCount-1).coerceAtLeast(0)) }
    Column(Modifier.fillMaxSize().padding(vertical=12.dp),verticalArrangement=Arrangement.spacedBy(10.dp)) {
        Surface(color=MaterialTheme.colorScheme.secondaryContainer,shape=MaterialTheme.shapes.medium) {
            Row(Modifier.fillMaxWidth().padding(12.dp),verticalAlignment=Alignment.CenterVertically) {
                Icon(Icons.Default.Lock,null,Modifier.size(18.dp))
                Text(engine.error ?: if(engine.loadedId!=null) engine.description else "Offline AI · Load a local GGUF in Settings",Modifier.weight(1f).padding(start=8.dp),style=MaterialTheme.typography.labelMedium,maxLines=2)
            }
        }
        Row(verticalAlignment=Alignment.CenterVertically) {
            Text("Allow app tools",Modifier.weight(1f),style=MaterialTheme.typography.bodySmall)
            Switch(vm.useTools,{vm.useTools=it},enabled=!vm.generating)
        }
        LazyColumn(Modifier.weight(1f).fillMaxWidth(),state=scroll,verticalArrangement=Arrangement.spacedBy(12.dp),contentPadding=PaddingValues(vertical=8.dp)) {
            if(vm.chat.isEmpty()) item {
                EmptyState("A private place to think","Ask a question, find a note, or plan your day. Every proposed change needs your approval.")
                Row(horizontalArrangement=Arrangement.spacedBy(8.dp)) {
                    SuggestionChip(onClick={vm.chatInput="What is on my calendar today?"},label={Text("Today's agenda")})
                    SuggestionChip(onClick={vm.chatInput="Create a note about "},label={Text("Capture an idea")})
                }
            }
            itemsIndexed(vm.chat) { _,line->
                Row(Modifier.fillMaxWidth(),horizontalArrangement=if(line.user) Arrangement.End else Arrangement.Start) {
                    Surface(color=if(line.user) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                        shape=MaterialTheme.shapes.large,modifier=Modifier.widthIn(max=640.dp)) {
                        androidx.compose.foundation.text.selection.SelectionContainer { Text(line.text,Modifier.padding(16.dp)) }
                    }
                }
            }
            if(vm.generating) item {
                Surface(shape=MaterialTheme.shapes.large) { Column(Modifier.padding(16.dp)) {
                    if(vm.preview.isBlank()) { LinearProgressIndicator(Modifier.width(140.dp));Text("Thinking on your device…",style=MaterialTheme.typography.labelSmall) }
                    else Text(vm.preview)
                } }
            }
        }
        vm.pending?.let { proposal->FilledTonalButton(onClick={/* review is presented globally */},modifier=Modifier.fillMaxWidth()) { Text("Review pending: "+proposal.title) } }
        Row(verticalAlignment=Alignment.Bottom,horizontalArrangement=Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(vm.chatInput,{vm.chatInput=it},placeholder={Text("Message your assistant…")},modifier=Modifier.weight(1f),maxLines=5,enabled=vm.pending==null)
            if(vm.generating) FilledIconButton(onClick={vm.stop()}) { Icon(Icons.Default.Stop,"Stop generation") }
            else FilledIconButton(onClick={vm.send()},enabled=vm.chatInput.isNotBlank() && vm.pending==null && !engine.busy) { Icon(Icons.AutoMirrored.Filled.Send,"Send") }
        }
    }
}
@Composable fun ApprovalDialog(vm:HubViewModel,proposal:ApprovalProposal) {
    var lead by remember(proposal.id){mutableStateOf(proposal.suggestedMinutes)}
    var mode by remember(proposal.id){mutableStateOf(proposal.suggestedMode)}
    var confirmed by remember(proposal.id){mutableStateOf(!proposal.needsReminder)}
    androidx.compose.ui.window.Dialog(onDismissRequest={vm.reject()},properties=androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth=false)) {
        Surface(Modifier.fillMaxWidth(.95f).heightIn(max=700.dp),shape=MaterialTheme.shapes.large) {
            Column(Modifier.padding(20.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
                Text("Review AI proposal",style=MaterialTheme.typography.labelLarge,color=MaterialTheme.colorScheme.primary)
                Text(proposal.title,style=MaterialTheme.typography.headlineSmall)
                Column(Modifier.weight(1f,false).verticalScroll(rememberScrollState()),verticalArrangement=Arrangement.spacedBy(12.dp)) {
                    Text("Current",fontWeight=FontWeight.SemiBold);Text(proposal.before)
                    HorizontalDivider();Text("Proposed",fontWeight=FontWeight.SemiBold);Text(proposal.after)
                    proposal.richPreview?.let { document->
                        val state=remember(proposal.id){RichTextState().setJson(document)}
                        CompositionLocalProvider(androidx.compose.ui.platform.LocalUriHandler provides object:androidx.compose.ui.platform.UriHandler{override fun openUri(uri:String){}}) {
                            RichText(state=state,modifier=Modifier.fillMaxWidth())
                        }
                    }
                    if(proposal.needsReminder) {
                        HorizontalDivider()
                        ReminderPicker(lead,mode,confirmed,onChange={minutes,type->lead=minutes;mode=type},onTimingConfirmed={confirmed=it})
                    }
                }
                Text("Approval applies once to this exact change. Nothing is saved until you approve.",style=MaterialTheme.typography.bodySmall)
                Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.End) {
                    TextButton(onClick={vm.reject()}){Text("Reject")}
                    Spacer(Modifier.width(12.dp))
                    Button(enabled=confirmed,onClick={vm.approve(lead,mode)}){Text("Approve and save")}
                }
            }
        }
    }
}
