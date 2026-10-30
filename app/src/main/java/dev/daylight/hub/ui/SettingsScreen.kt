package dev.daylight.hub.ui

import android.Manifest
import android.app.AlarmManager
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import dev.daylight.hub.HubViewModel
import dev.daylight.hub.ai.*
import dev.daylight.hub.calendar.AlarmScheduler
import dev.daylight.hub.data.ModelProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

@Composable fun SettingsScreen(vm:HubViewModel) {
    val context=LocalContext.current
    val models by vm.models.collectAsState()
    val engine by vm.engine.state.collectAsState()
    val a=vm.appearance
    var editing by remember { mutableStateOf<ModelProfile?>(null) }
    var deleting by remember { mutableStateOf<ModelProfile?>(null) }
    var reset by remember { mutableStateOf(false) }
    val modelPicker=rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result->
        result.data?.data?.let(vm::importModel)
    }
    val imagePicker=rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result->
        result.data?.data?.let { uri->vm.action {
            val dest=withContext(Dispatchers.IO) {
                // Decode and resize locally rather than holding an unbounded full-resolution image.
                val bytes=context.contentResolver.openInputStream(uri)?.use { input->
                    val buffer=java.io.ByteArrayOutputStream(); val chunk=ByteArray(8192);var total=0
                    while(true){val n=input.read(chunk);if(n<0)break;total+=n;require(total<=20*1024*1024){"Choose an image smaller than 20 MB."};buffer.write(chunk,0,n)}
                    buffer.toByteArray()
                } ?: error("Cannot read image.")
                val options=android.graphics.BitmapFactory.Options().apply{inJustDecodeBounds=true}
                android.graphics.BitmapFactory.decodeByteArray(bytes,0,bytes.size,options)
                require(options.outWidth>0 && options.outHeight>0){"Invalid image."}
                var sample=1;while(maxOf(options.outWidth,options.outHeight)/sample>1600)sample*=2
                val bitmap=android.graphics.BitmapFactory.decodeByteArray(bytes,0,bytes.size,android.graphics.BitmapFactory.Options().apply{inSampleSize=sample}) ?: error("Cannot decode image.")
                File(context.filesDir,"appearance").mkdirs()
                File(context.filesDir,"appearance/"+java.util.UUID.randomUUID()+".jpg").also { file->file.outputStream().use{bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG,85,it)};bitmap.recycle() }
            }
            vm.saveAppearance(vm.appearance.copy(background="Image",image="appearance/"+dest.name))
            if(a.image.startsWith("appearance/")) File(context.filesDir,a.image).delete()
            vm.message="Background imported."
        } }
    }
    val notifications=rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { AlarmScheduler.request(context) }
    fun localPicker(type:String)=Intent(Intent.ACTION_OPEN_DOCUMENT).setType(type).addCategory(Intent.CATEGORY_OPENABLE).putExtra(Intent.EXTRA_LOCAL_ONLY,true)
    LazyColumn(Modifier.fillMaxSize(),verticalArrangement=Arrangement.spacedBy(16.dp),contentPadding=PaddingValues(vertical=16.dp)) {
        item { HubCard("Local models","Import a GGUF from device storage. For an 8 GB phone, start with a small 1–3B quantized chat model.") {
            Text(engine.error ?: engine.description,style=MaterialTheme.typography.bodySmall)
            if(engine.loading){
                Text("Elapsed: "+engine.elapsedSeconds+" s",style=MaterialTheme.typography.labelSmall)
                val percent=engine.progress
                if(percent==null)LinearProgressIndicator(Modifier.fillMaxWidth())
                else {LinearProgressIndicator(progress={percent/100f},modifier=Modifier.fillMaxWidth());Text("Weights mapped: "+percent+"%",style=MaterialTheme.typography.labelSmall)}
                TextButton(enabled=!engine.cancelling,onClick={vm.engine.cancel()}){Text(if(engine.cancelling)"Cancelling…" else "Cancel loading")}
            }
            vm.importStatus?.let { Text(it);LinearProgressIndicator(Modifier.fillMaxWidth()) }
            Row(horizontalArrangement=Arrangement.spacedBy(8.dp)) {
                Button(enabled=vm.importStatus==null && !engine.busy,onClick={modelPicker.launch(localPicker("*/*"))}){Icon(Icons.Default.FileOpen,null);Spacer(Modifier.width(8.dp));Text("Import GGUF")}
                OutlinedButton(enabled=engine.loadedId!=null && !engine.busy,onClick={vm.action{vm.engine.unload()}}){Text("Unload")}
            }
            models.forEach { profile->
                HorizontalDivider()
                Text(profile.name,fontWeight=FontWeight.SemiBold)
                Text("%.2f GB".format(profile.bytes.toDouble()/(1024*1024*1024))+" · "+profile.metadata,style=MaterialTheme.typography.bodySmall)
                Row(horizontalArrangement=Arrangement.spacedBy(6.dp)) {
                    FilledTonalButton(enabled=!engine.busy,onClick={vm.loadModel(profile)}){Text(if(engine.loadedId==profile.id)"Reload" else "Load")}
                    TextButton(onClick={editing=profile}){Text("Parameters")}
                    IconButton(enabled=!engine.busy,onClick={deleting=profile}){Icon(Icons.Default.DeleteOutline,"Remove model")}
                }
            }
        } }
        item { HubCard("Appearance","Preview changes immediately. Settings remains accessible even when sections are hidden.") {
            Row(Modifier.horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(8.dp)) {
                listOf("System","Light","Dark").forEach { value->FilterChip(a.mode==value,{vm.saveAppearance(a.copy(mode=value))},label={Text(value)}) }
            }
            Text("Accent presets",style=MaterialTheme.typography.labelLarge)
            Row(horizontalArrangement=Arrangement.spacedBy(16.dp)) {
                listOf(0xFF366A59 to 0xFF91D5B6,0xFF4E6298 to 0xFFB2C7FF,0xFF9A5A40 to 0xFFFFB59A,0xFF72518F to 0xFFDDB8FF).forEach{(light,dark)->
                    Surface(color=Color(light),shape=MaterialTheme.shapes.small,modifier=Modifier.size(40.dp).clickable{vm.saveAppearance(a.copy(accent=light,darkAccent=dark))},
                        border=if(a.accent==light) BorderStroke(3.dp,MaterialTheme.colorScheme.onSurface) else null){}
                }
            }
            ColorSetting("Light accent",a.accent){vm.saveAppearance(a.copy(accent=it))}
            ColorSetting("Dark accent",a.darkAccent){vm.saveAppearance(a.copy(darkAccent=it))}
            ColorSetting("Light background",a.lightBackground){vm.saveAppearance(a.copy(lightBackground=it))}
            ColorSetting("Dark background",a.darkBackground){vm.saveAppearance(a.copy(darkBackground=it))}
            ColorSetting("Light surfaces",a.lightSurface){vm.saveAppearance(a.copy(lightSurface=it))}
            ColorSetting("Dark surfaces",a.darkSurface){vm.saveAppearance(a.copy(darkSurface=it))}
            Row(verticalAlignment=Alignment.CenterVertically){Text("Compact spacing",Modifier.weight(1f));Switch(a.compact,{vm.saveAppearance(a.copy(compact=it))})}
            Text("Text size: "+(a.textScale*100).toInt()+"%")
            Slider(a.textScale,{vm.saveAppearance(a.copy(textScale=it))},valueRange=.8f..1.5f,steps=6)
            Text("Editor font",style=MaterialTheme.typography.labelLarge)
            Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){listOf("Sans","Serif","Mono").forEach{font->FilterChip(a.editorFont==font,{vm.saveAppearance(a.copy(editorFont=font))},label={Text(font)})}}
            Text("Background",style=MaterialTheme.typography.labelLarge)
            Row(horizontalArrangement=Arrangement.spacedBy(8.dp)){listOf("Solid","Gradient","Image").forEach{value->FilterChip(a.background==value,{
                if(value=="Image") imagePicker.launch(localPicker("image/*")) else vm.saveAppearance(a.copy(background=value))
            },label={Text(value)})}}
            TextButton(onClick={reset=true}){Text("Reset appearance")}
        } }
        item { HubCard("Navigation","Choose the sections shown in the bottom bar or tablet rail.") {
            val nav=a.navigation.split(",").filter{it.isNotBlank()}.toMutableList()
            listOf("Home","Planner","Notes","Assistant").forEach { section->
                Row(verticalAlignment=Alignment.CenterVertically) {
                    Checkbox(section in nav,{checked->val next=nav.toMutableList();if(checked)next.add(section)else next.remove(section);vm.saveAppearance(a.copy(navigation=next.joinToString(",")))})
                    Text(section,Modifier.weight(1f))
                    val index=nav.indexOf(section)
                    IconButton(enabled=index>0,onClick={val next=nav.toMutableList();java.util.Collections.swap(next,index,index-1);vm.saveAppearance(a.copy(navigation=next.joinToString(",")))}){Icon(Icons.Default.ArrowUpward,"Move "+section+" earlier")}
                    IconButton(enabled=index>=0 && index<nav.lastIndex,onClick={val next=nav.toMutableList();java.util.Collections.swap(next,index,index+1);vm.saveAppearance(a.copy(navigation=next.joinToString(",")))}){Icon(Icons.Default.ArrowDownward,"Move "+section+" later")}
                }
            }
            Text("Open any section",style=MaterialTheme.typography.labelSmall)
            Row(Modifier.horizontalScroll(rememberScrollState())){listOf("Home","Planner","Notes","Assistant").forEach{section->TextButton(onClick={vm.navigate(section)}){Text(section)}}}
        } }
        item { HubCard("Alarms & reminders","Calendar alarms are scheduled by Android and do not need the AI to stay loaded.") {
            Text(AlarmScheduler(context).permissionProblem() ?: "Reminder permissions are enabled.",style=MaterialTheme.typography.bodySmall)
            if(Build.VERSION.SDK_INT>=33) OutlinedButton(onClick={notifications.launch(Manifest.permission.POST_NOTIFICATIONS)}){Text("Allow notifications")}
            if(Build.VERSION.SDK_INT>=31) OutlinedButton(onClick={context.startActivity(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM,Uri.parse("package:"+context.packageName)))}){Text("Allow alarms & reminders")}
            TextButton(onClick={context.startActivity(Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).putExtra(Settings.EXTRA_APP_PACKAGE,context.packageName))}){Text("Sound and notification settings")}
            Text("Android cannot deliver alarms while the phone is powered off or the app is force-stopped. Reopening the app rebuilds schedules.",style=MaterialTheme.typography.bodySmall)
        } }
        item { HubCard("Private by design","Models, notes, plans, and settings stay in app-private storage.") {
            Text("No network permission, accounts, sync, analytics, or cloud backup. You approve every AI write.",style=MaterialTheme.typography.bodySmall)
            Text("ProSpaceHub · 1.1.0",style=MaterialTheme.typography.labelSmall)
        } }
    }
    editing?.let { profile->ParametersDialog(profile,{editing=null}){parameters->vm.action{
        vm.repo.saveModelParameters(profile,parameters.json());editing=null
        vm.message=if(engine.loadedId==profile.id) "Parameters saved. Reload this model to apply context/thread changes." else "Parameters saved."
    }} }
    deleting?.let { profile->AlertDialog(onDismissRequest={deleting=null},title={Text("Remove imported model?")},text={Text(profile.name+"\nThis removes the app's copy of the GGUF.")},
        confirmButton={TextButton(onClick={vm.removeModel(profile);deleting=null}){Text("Remove")}},dismissButton={TextButton(onClick={deleting=null}){Text("Cancel")}}) }
    if(reset) AlertDialog(onDismissRequest={reset=false},title={Text("Reset appearance?")},text={Text("Restore default theme, colors, spacing, and navigation.")},
        confirmButton={TextButton(onClick={vm.saveAppearance(Appearance());reset=false}){Text("Reset")}},dismissButton={TextButton(onClick={reset=false}){Text("Cancel")}})
}
@Composable private fun ColorSetting(label:String,color:Long,onChange:(Long)->Unit) {
    var input by remember(color){mutableStateOf("%06X".format(color and 0xFFFFFF))}
    Row(verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(10.dp)){
        Surface(color=Color(color),modifier=Modifier.size(26.dp),shape=MaterialTheme.shapes.small,border=BorderStroke(1.dp,MaterialTheme.colorScheme.outline)){}
        OutlinedTextField(input,{text->input=text.removePrefix("#");if(input.matches(Regex("[0-9a-fA-F]{6}")))onChange(0xFF000000 or input.toLong(16))},label={Text(label+" · hex")},singleLine=true,modifier=Modifier.weight(1f))
    }
}
@Composable private fun ParametersDialog(profile:ModelProfile,onDismiss:()->Unit,onSave:(GenerationParameters)->Unit) {
    val initial=remember{GenerationParameters.parse(profile.parameters)}
    val values=remember{mutableStateMapOf("Temperature" to initial.temperature.toString(),"Top-p" to initial.topP.toString(),"Top-k" to initial.topK.toString(),
        "Min-p" to initial.minP.toString(),"Repetition penalty" to initial.repetitionPenalty.toString(),"Seed (-1 random)" to initial.seed.toString(),
        "Maximum reply tokens" to initial.maxTokens.toString(),"Context tokens" to initial.contextSize.toString(),"CPU threads" to initial.threads.toString())}
    var system by remember{mutableStateOf(initial.systemPrompt)};var error by remember{mutableStateOf("")}
    androidx.compose.ui.window.Dialog(onDismissRequest=onDismiss,properties=androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth=false)) {
        Surface(Modifier.fillMaxWidth(.95f).fillMaxHeight(.92f),shape=MaterialTheme.shapes.large) {
            Column(Modifier.padding(20.dp),verticalArrangement=Arrangement.spacedBy(12.dp)){
                Text("Model parameters",style=MaterialTheme.typography.headlineSmall)
                Text("Context and CPU thread changes require reloading. Sampling changes apply to the next reply.",style=MaterialTheme.typography.bodySmall)
                Column(Modifier.weight(1f).verticalScroll(rememberScrollState()),verticalArrangement=Arrangement.spacedBy(10.dp)){
                    values.keys.forEach{key->OutlinedTextField(values[key] ?: "",{values[key]=it},label={Text(key)},singleLine=true,modifier=Modifier.fillMaxWidth())}
                    OutlinedTextField(system,{system=it},label={Text("System prompt")},modifier=Modifier.fillMaxWidth(),minLines=3)
                    if(error.isNotBlank())Text(error,color=MaterialTheme.colorScheme.error)
                }
                Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.End){
                    TextButton(onClick=onDismiss){Text("Cancel")}
                    Button(onClick={try{
                        val result=GenerationParameters(values.getValue("Temperature").toFloat(),values.getValue("Top-p").toFloat(),values.getValue("Top-k").toInt(),
                            values.getValue("Min-p").toFloat(),values.getValue("Repetition penalty").toFloat(),values.getValue("Seed (-1 random)").toLong(),
                            values.getValue("Maximum reply tokens").toInt(),values.getValue("Context tokens").toInt(),values.getValue("CPU threads").toInt(),system)
                        result.validate();onSave(result)
                    }catch(e:Exception){error=e.message ?: "Check parameter values."}}){Text("Save profile")}
                }
            }
        }
    }
}
