package dev.daylight.hub.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.daylight.hub.HubViewModel
import dev.daylight.hub.ai.*
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject

data class SkillReview(val title:String,val request:SkillRequest,val apply:suspend(SkillResult)->Unit)

@Composable fun SkillDialog(vm:HubViewModel,review:SkillReview,onClose:()->Unit) {
    ReviewedSkillDialog(vm,review,onClose){SmallModelSkills(vm.engine,vm.db.models()).suggest(it)}
}

@Composable internal fun ReviewedSkillDialog(vm:HubViewModel,review:SkillReview,onClose:()->Unit,suggest:suspend(SkillRequest)->SkillResult) {
    var attempt by remember { mutableIntStateOf(0) };var loading by remember { mutableStateOf(true) }
    var result by remember { mutableStateOf<SkillResult?>(null) };var editable by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) };var applying by remember { mutableStateOf(false) }
    var generation by remember { mutableStateOf<Job?>(null) };val scope=rememberCoroutineScope()
    LaunchedEffect(review,attempt) {
        loading=true;error=null;result=null;generation=currentCoroutineContext()[Job]
        try {
            val suggestion=suggest(review.request)
            result=suggestion;editable=when(suggestion) { is SkillResult.Text->suggestion.value;is SkillResult.Steps->suggestion.values.joinToString("\n");is SkillResult.Label->suggestion.id?.let{review.request.allowedLabels[it]}.orEmpty() }
        } catch(e:CancellationException) { error="Stopped. You can retry or keep editing manually.";throw e }
        catch(e:Exception) { error=e.message ?: "The suggestion could not be generated. Nothing changed." }
        finally { loading=false;generation=null }
    }
    EditorSheet(review.title,{if(!applying) onClose()}) {
        Text("Source excerpt",style=MaterialTheme.typography.labelLarge)
        Text(review.request.source,style=MaterialTheme.typography.bodyMedium)
        HorizontalDivider()
        if(loading) {
            LinearProgressIndicator(Modifier.fillMaxWidth())
            Text("Creating a short suggestion on your device…")
            TextButton(onClick={generation?.cancel()}) { Text("Stop") }
        }
        val suggestion=result
        if(suggestion is SkillResult.Label) {
            Text(if(suggestion.id==null) "No suitable label. Your current label is kept." else "Suggested label: $editable",style=MaterialTheme.typography.titleMedium)
        } else if(suggestion!=null) {
            OutlinedTextField(editable,{editable=it},Modifier.fillMaxWidth(),label={Text(if(suggestion is SkillResult.Steps) "Edit steps · one per line, up to three" else "Edit suggestion")},minLines=2)
        }
        error?.let{Text(it,color=MaterialTheme.colorScheme.error)}
        Text("Nothing changes until you apply this suggestion.",style=MaterialTheme.typography.bodySmall)
        Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.End) {
            TextButton(enabled=!applying,onClick=onClose) { Text("Dismiss") }
            if(!loading) TextButton(enabled=!applying,onClick={attempt++}) { Text("Retry") }
            Button(enabled=!loading && !applying && suggestion!=null && !(suggestion is SkillResult.Label && suggestion.id==null),onClick={
                applying=true;error=null
                scope.launch {
                    try {
                        val payload=when(suggestion) {
                            is SkillResult.Text->JSONObject().put("text",editable)
                            is SkillResult.Steps->JSONObject().put("steps",JSONArray(editable.lines().map{it.trim()}.filter{it.isNotEmpty()}))
                            is SkillResult.Label->JSONObject().put("label",suggestion.id ?: JSONObject.NULL)
                            else->throw IllegalStateException("Generate a suggestion first.")
                        }
                        val validated=SkillValidation.parse(review.request,payload.toString())
                        val proposal=vm.approvals.propose(review.title,review.request.source,payload.toString()) { _,_->review.apply(validated) }
                        vm.approvals.approve(proposal.id);onClose()
                    } catch(e:CancellationException){throw e}
                    catch(e:Exception){error=e.message ?: "Could not apply the suggestion. Your source was kept."}
                    finally { applying=false }
                }
            }) { Text(if(applying) "Saving…" else if(suggestion is SkillResult.Steps) "Save as subtasks" else "Apply") }
        }
    }
}

@Composable fun LabelCatalogDialog(vm:HubViewModel,onSelect:(Map<Long,String>)->Unit,onClose:()->Unit) {
    val labels by vm.labels.collectAsStateWithLifecycle()
    var selected by remember { mutableStateOf(emptySet<Long>()) };var name by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) };val scope=rememberCoroutineScope()
    EditorSheet("Choose allowed labels",onClose) {
        Text("Select one to eight labels. The model can suggest one of these or say that none fits.")
        labels.forEach { label -> FilterChip(selected=label.id in selected,onClick={selected=if(label.id in selected) selected-label.id else if(selected.size<8) selected+label.id else selected},label={Text(label.name)}) }
        OutlinedTextField(name,{name=it},Modifier.fillMaxWidth(),label={Text("New label")},singleLine=true)
        TextButton(enabled=name.isNotBlank(),onClick={scope.launch { try { val id=vm.workspace.addLabel(name);name="";if(selected.size<8) selected=selected+id;error=null }catch(e:Exception){error=e.message} }}) { Text("Add label") }
        error?.let{Text(it,color=MaterialTheme.colorScheme.error)}
        Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.End) {
            TextButton(onClick=onClose) { Text("Cancel") }
            Button(enabled=selected.size in 1..8,onClick={onSelect(labels.filter{it.id in selected}.associate{it.id to it.name})}) { Text("Suggest a label") }
        }
    }
}
