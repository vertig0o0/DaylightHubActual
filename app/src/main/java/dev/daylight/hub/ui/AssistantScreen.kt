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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.compose.ui.text.font.FontWeight
import dev.daylight.hub.HubViewModel
import dev.daylight.hub.ai.ApprovalProposal
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.json.*
import com.mohamedrejeb.richeditor.ui.material3.RichText
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable fun AssistantScreen(vm:HubViewModel) {
    val engine by vm.engine.state.collectAsState()
    val scroll=rememberLazyListState()
    val conversations by vm.conversations.collectAsStateWithLifecycle()
    val projects by vm.projects.collectAsStateWithLifecycle()
    var showConversations by remember { mutableStateOf(false) }
    var showProject by remember { mutableStateOf(false) }
    var showOptions by remember{mutableStateOf(false)}
    val keyboardVisible=WindowInsets.ime.getBottom(LocalDensity.current)>0
    val keyboard=LocalSoftwareKeyboardController.current
    val focus=LocalFocusManager.current
    LaunchedEffect(vm.chat.size){ if(vm.chat.size>0 && !scroll.isScrollInProgress) scroll.scrollToItem(vm.chat.size.coerceAtMost(scroll.layoutInfo.totalItemsCount-1).coerceAtLeast(0)) }
    Column(Modifier.fillMaxSize().padding(vertical=12.dp),verticalArrangement=Arrangement.spacedBy(10.dp)) {
        Row(verticalAlignment=Alignment.CenterVertically) {
            TextButton(enabled=!vm.generating && vm.pending==null,onClick={showConversations=true},modifier=Modifier.weight(1f)) { Column { Text(conversations.firstOrNull{it.id==vm.conversationId}?.title ?: "New conversation",maxLines=1,overflow=TextOverflow.Ellipsis);Text((projects.firstOrNull{it.id==vm.conversationProjectId}?.name ?: "Inbox")+(if(vm.useTools)" · Tools on" else ""),style=MaterialTheme.typography.labelSmall,maxLines=1) } }
            IconButton(enabled=!vm.generating && vm.pending==null,onClick={vm.newConversation()}) { Icon(Icons.Default.Add,"New conversation") }
            IconButton(onClick={showOptions=true}){Icon(Icons.Default.MoreVert,"Conversation options")}
        }
        if(!keyboardVisible)Surface(color=MaterialTheme.colorScheme.secondaryContainer,shape=MaterialTheme.shapes.medium) {
            Row(Modifier.fillMaxWidth().padding(12.dp),verticalAlignment=Alignment.CenterVertically) {
                Icon(Icons.Default.Lock,null,Modifier.size(18.dp))
                Text(engine.error ?: if(engine.loading) engine.description+" · "+engine.elapsedSeconds+" s" else if(engine.loadedId!=null) engine.description else "Offline AI · Load a local GGUF in Settings",Modifier.weight(1f).padding(start=8.dp),style=MaterialTheme.typography.labelMedium,maxLines=1,overflow=TextOverflow.Ellipsis)
                if(engine.loading)TextButton(enabled=!engine.cancelling,onClick={vm.engine.cancel()}){Text("Cancel load")}
            }
        }
        LazyColumn(Modifier.weight(1f).fillMaxWidth(),state=scroll,verticalArrangement=Arrangement.spacedBy(12.dp),contentPadding=PaddingValues(vertical=8.dp)) {
            if(vm.olderMessagesAvailable || vm.showingEarlierMessages)item {
                Row { if(vm.olderMessagesAvailable)TextButton(enabled=!vm.generating && !vm.conversationLoading,onClick=vm::earlierMessages){Text("Earlier messages")};if(vm.showingEarlierMessages)TextButton(enabled=!vm.generating && !vm.conversationLoading,onClick=vm::latestMessages){Text("Latest messages")} }
            }
            if(vm.chat.isEmpty()) item {
                EmptyState("A private place to think","Ask a question, find a note, or plan your day. Every proposed change needs your approval.")
                Row(horizontalArrangement=Arrangement.spacedBy(8.dp)) {
                    SuggestionChip(onClick={vm.chatInput="What is on my calendar today?"},label={Text("Today's agenda")})
                    SuggestionChip(onClick={vm.chatInput="Create a note about "},label={Text("Capture an idea")})
                }
            }
            items(vm.chat,key={it.id}) { line->
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
            else FilledIconButton(onClick={vm.send();focus.clearFocus();keyboard?.hide()},enabled=vm.chatInput.isNotBlank() && !vm.conversationLoading && vm.pending==null && !engine.busy) { Icon(Icons.AutoMirrored.Filled.Send,"Send") }
        }
    }
    if(showOptions)EditorSheet("Conversation options",{showOptions=false}) {
        TextButton(enabled=!vm.generating && vm.pending==null,onClick={showOptions=false;showProject=true}){Text(projects.firstOrNull{it.id==vm.conversationProjectId}?.name ?: "Inbox · Assign project")}
        Row(verticalAlignment=Alignment.CenterVertically){Text("Allow app tools",Modifier.weight(1f));Switch(vm.useTools,{vm.useTools=it},enabled=!vm.generating)}
        Text("Every proposed write still needs your explicit approval.",style=MaterialTheme.typography.bodySmall)
        Text(engine.error ?: engine.description.ifBlank{"Load a local GGUF in Settings to generate replies."},style=MaterialTheme.typography.bodySmall)
        TextButton(onClick={showOptions=false}){Text("Done")}
    }
    if(showProject) ProjectPicker(projects,vm.conversationProjectId,{vm.assignConversation(it);showProject=false},{showProject=false})
    if(showConversations) EditorSheet("Conversations",{showConversations=false}) {
        if(conversations.isEmpty()) Text("Your conversations are saved on this device when you send a message.")
        LazyColumn(Modifier.heightIn(max=400.dp)) { items(conversations,key={it.id}) { conversation ->
            TextButton(onClick={vm.openConversation(conversation.id);showConversations=false},modifier=Modifier.fillMaxWidth()) { Text(conversation.title) }
        } }
        TextButton(onClick={vm.newConversation();showConversations=false}) { Text("New conversation") }
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
