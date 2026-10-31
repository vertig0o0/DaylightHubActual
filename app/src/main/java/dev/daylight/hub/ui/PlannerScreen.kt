package dev.daylight.hub.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.*
import androidx.compose.ui.text.font.FontWeight
import dev.daylight.hub.HubViewModel
import dev.daylight.hub.calendar.*
import dev.daylight.hub.data.*
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.Locale

@Composable fun PlannerScreen(vm:HubViewModel) {
    val events by vm.events.collectAsState(); val tasks by vm.tasks.collectAsState(); val exceptions by vm.exceptions.collectAsState()
    var selectedEpoch by rememberSaveable { mutableLongStateOf(LocalDate.now().toEpochDay()) }
    val selected=LocalDate.ofEpochDay(selectedEpoch)
    var mode by rememberSaveable { mutableStateOf("Month") }
    var editing by remember { mutableStateOf<CalendarEvent?>(null) }
    var original by remember { mutableStateOf<Long?>(null) }
    var showEditor by remember { mutableStateOf(false) }
    var scopeChoice by remember { mutableStateOf<Occurrence?>(null) }
    var taskEdit by remember { mutableStateOf<PlannerItem?>(null) }
    var showTask by remember { mutableStateOf(false) }
    val zone=ZoneId.systemDefault()
    val locale=androidx.compose.ui.platform.LocalConfiguration.current.locales[0]
    val firstWeekDay=WeekFields.of(locale).firstDayOfWeek
    val monthStart=selected.withDayOfMonth(1)
    val gridStart=monthStart.minusDays(((monthStart.dayOfWeek.value-firstWeekDay.value+7)%7).toLong())
    val from=if(mode=="Month") gridStart else if(mode=="Week") selected.minusDays(((selected.dayOfWeek.value-firstWeekDay.value+7)%7).toLong()) else selected
    val until=when(mode) { "Month"->from.plusDays(42); "Week"->from.plusDays(7); "Agenda"->from.plusDays(31); else->from.plusDays(1) }
    val occurrences=remember(events,exceptions,from,until) { events.flatMap { RecurrenceEngine.between(it,exceptions,from.atStartOfDay(zone).toInstant().toEpochMilli(),until.atStartOfDay(zone).toInstant().toEpochMilli()) }.sortedBy { it.startAt } }
    fun newEvent(date:LocalDate,hour:Int=9,minute:Int=0) {
        val start=date.atTime(hour,minute).atZone(zone).toInstant().toEpochMilli()
        editing=CalendarEvent(title="",startAt=start,endAt=start+3600000); original=null; showEditor=true
    }
    fun edit(item:Occurrence) {
        if(item.event.recurrence.isNotBlank()) scopeChoice=item else { editing=item.event; original=null; showEditor=true }
    }
    LaunchedEffect(vm.plannerRequest) {
        val request=vm.plannerRequest ?: return@LaunchedEffect
        request.task?.let{task->
            selectedEpoch=Instant.ofEpochMilli(task.dueAt ?: System.currentTimeMillis()).atZone(zone).toLocalDate().toEpochDay()
            taskEdit=task;showTask=true
        }
        request.occurrence?.let{item->
            selectedEpoch=Instant.ofEpochMilli(item.startAt).atZone(zone).toLocalDate().toEpochDay()
            edit(item)
        }
        vm.plannerRequest=null
    }
    Column(Modifier.fillMaxSize(),verticalArrangement=Arrangement.spacedBy(8.dp)) {
        Row(Modifier.fillMaxWidth().padding(top=8.dp),verticalAlignment=Alignment.CenterVertically) {
            IconButton(onClick={ selectedEpoch=(if(mode=="Month") selected.minusMonths(1) else selected.minusDays(if(mode=="Week") 7 else 1)).toEpochDay() }) { Icon(Icons.Default.ChevronLeft,"Previous") }
            Text(selected.format(DateTimeFormatter.ofPattern("MMMM yyyy")),style=MaterialTheme.typography.titleMedium,modifier=Modifier.weight(1f))
            IconButton(onClick={ selectedEpoch=(if(mode=="Month") selected.plusMonths(1) else selected.plusDays(if(mode=="Week") 7 else 1)).toEpochDay() }) { Icon(Icons.Default.ChevronRight,"Next") }
            TextButton(onClick={selectedEpoch=LocalDate.now().toEpochDay()}) { Text("Today") }
            FilledTonalIconButton(onClick={newEvent(selected)}) { Icon(Icons.Default.Add,"New event") }
        }
        Row(Modifier.horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(8.dp)) {
            listOf("Month","Week","Day","Agenda").forEach { m->FilterChip(mode==m,{mode=m},label={Text(m)}) }
            AssistChip(onClick={taskEdit=null;showTask=true},label={Text("New task")},leadingIcon={Icon(Icons.Default.Checklist,null)})
        }
        val alarmContext=LocalContext.current
        val alarmProblem=events.filter{it.reminderMinutes!=null}.firstNotNullOfOrNull{AlarmScheduler(alarmContext).permissionProblem(it.reminderMode)}
        if(events.any{it.reminderMinutes!=null} && alarmProblem!=null)
            Text(alarmProblem,style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.error)
        if(mode=="Month") {
            Column {
                Row {
                    repeat(7) { offset-> val day=firstWeekDay.plus(offset.toLong())
                        Text(day.getDisplayName(java.time.format.TextStyle.NARROW,locale),Modifier.weight(1f),textAlign=androidx.compose.ui.text.style.TextAlign.Center,style=MaterialTheme.typography.labelSmall)
                    }
                }
                repeat(6) { week->
                    Row {
                        repeat(7) { col->
                            val date=gridStart.plusDays((week*7+col).toLong())
                            val dayStart=date.atStartOfDay(zone).toInstant().toEpochMilli(); val dayEnd=date.plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli()
                            val dayEvents=occurrences.filter { it.startAt<dayEnd && it.endAt>dayStart }
                            Column(Modifier.weight(1f).height(if(vm.appearance.compact) 44.dp else 52.dp)
                                .padding(2.dp).background(if(date==selected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface,MaterialTheme.shapes.small)
                                .clickable { if(date==selected) newEvent(date) else selectedEpoch=date.toEpochDay() }.padding(4.dp)) {
                                Row(verticalAlignment=Alignment.CenterVertically) {
                                    Text(date.dayOfMonth.toString(),style=MaterialTheme.typography.labelMedium,fontWeight=if(date==LocalDate.now()) FontWeight.Bold else FontWeight.Normal,
                                        color=if(date.month==selected.month) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha=.6f),modifier=Modifier.weight(1f))
                                    val extra=if(dayEvents.size>1) "+"+(dayEvents.size-1) else ""
                                    val taskDot=if(tasks.any { (it.dueAt ?: Long.MIN_VALUE) in dayStart until dayEnd && !it.isDone }) "•" else ""
                                    Text(extra+taskDot,style=MaterialTheme.typography.labelSmall,maxLines=1)
                                }
                                dayEvents.take(1).forEach { e->Text(e.event.title,maxLines=1,overflow=androidx.compose.ui.text.style.TextOverflow.Ellipsis,style=MaterialTheme.typography.labelSmall,color=Color(e.event.color)) }
                            }
                        }
                    }
                }
            }
        }
        if(mode=="Day" || mode=="Week") {
            Timeline(occurrences,from,if(mode=="Week") 7 else 1,Modifier.weight(1f),::edit,::newEvent)
        } else {
            val agendaFrom=if(mode=="Month") selected else from
            val agendaUntil=if(mode=="Month") selected.plusDays(1) else until
            val low=agendaFrom.atStartOfDay(zone).toInstant().toEpochMilli(); val high=agendaUntil.atStartOfDay(zone).toInstant().toEpochMilli()
            val shown=occurrences.filter { it.startAt<high && it.endAt>low }
            LazyColumn(Modifier.weight(1f),verticalArrangement=Arrangement.spacedBy(8.dp),contentPadding=PaddingValues(bottom=16.dp)) {
                item { Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically) {
                    Text(if(mode=="Month") selected.format(DateTimeFormatter.ofPattern("EEE, d MMM")) else "Upcoming",style=MaterialTheme.typography.titleMedium,modifier=Modifier.weight(1f))
                    TextButton(onClick={newEvent(selected)}) { Text("Add event") }
                } }
                if(shown.isEmpty() && tasks.none{(it.dueAt ?: Long.MIN_VALUE) in low until high}) item { EmptyState("No events","Select a date or tap + to plan something.") }
                items(shown,key={it.event.id.toString()+":"+it.originalStart}) { item->
                    Card(onClick={edit(item)}) { Row(Modifier.fillMaxWidth().padding(14.dp),horizontalArrangement=Arrangement.spacedBy(12.dp)) {
                        Box(Modifier.width(4.dp).height(44.dp).background(Color(item.event.color),MaterialTheme.shapes.small))
                        Column { Text(item.event.title,fontWeight=FontWeight.SemiBold); Text(if(item.event.allDay) "All day" else formatTime(item.startAt,item.event.zoneId),style=MaterialTheme.typography.bodySmall)
                            if(item.event.recurrence.isNotBlank()) Text("Repeats "+item.event.recurrence.removePrefix("FREQ=").lowercase(),style=MaterialTheme.typography.labelSmall)
                        }
                    } }
                }
                items(tasks.filter { (it.dueAt ?: Long.MIN_VALUE) in low until high },key={"task:"+it.id}) { task->
                    Card(onClick={taskEdit=task;showTask=true}) { Row(Modifier.fillMaxWidth().padding(10.dp),verticalAlignment=Alignment.CenterVertically) {
                        Checkbox(task.isDone,{checked->vm.action {vm.repo.saveTask(task.copy(isDone=checked),task.revision)}})
                        Column(Modifier.weight(1f)) { Text(task.title); Text("Task · "+formatTime(task.dueAt),style=MaterialTheme.typography.bodySmall) }
                    } }
                }
            }
        }
    }
    scopeChoice?.let { item->AlertDialog(onDismissRequest={scopeChoice=null},title={Text("Edit repeating event")},text={Text("Choose this occurrence or the entire series.")},
        confirmButton={TextButton(onClick={
            val parent=events.first { it.id==item.event.id }
            editing=item.event.copy(startAt=item.startAt,endAt=item.endAt,revision=parent.revision)
            original=item.originalStart; showEditor=true;scopeChoice=null
        }){Text("This occurrence")}},dismissButton={TextButton(onClick={
            editing=events.first { it.id==item.event.id }; original=null;showEditor=true;scopeChoice=null
        }){Text("Entire series")}}) }
    if(showEditor) editing?.let { event->
        EventEditor(event,original!=null,{showEditor=false},onSave={updated->
            vm.action {
                if(original!=null) vm.repo.saveOccurrence(event.id,original!!,updated,event.revision)
                else vm.repo.saveEvent(updated,if(event.id==0L) null else event.revision)
                showEditor=false
            }
        },onDelete=if(event.id==0L) null else { {
            vm.action {
                if(original!=null) vm.repo.saveOccurrence(event.id,original!!,null,event.revision)
                else vm.repo.deleteEvent(event.id,event.revision)
                showEditor=false
            }
        } })
    }
    if(showTask) TaskEditor(taskEdit,{showTask=false},{ task->vm.action { vm.repo.saveTask(task,taskEdit?.revision); showTask=false } },
        { task->vm.action { vm.repo.deleteTask(task.id,task.revision); showTask=false } })
}
private data class Positioned(val item:Occurrence,val column:Int,val columns:Int)
private fun position(items:List<Occurrence>): List<Positioned> {
    val out=mutableListOf<Positioned>(); var group=mutableListOf<Pair<Occurrence,Int>>(); val active=mutableListOf<Pair<Long,Int>>(); var width=0
    fun finish() { group.forEach { out.add(Positioned(it.first,it.second,width.coerceAtLeast(1))) }; group=mutableListOf(); width=0 }
    for(item in items.sortedBy { it.startAt }) {
        active.removeAll { it.first<=item.startAt }
        if(active.isEmpty() && group.isNotEmpty()) finish()
        var column=0; while(active.any { it.second==column }) column++
        active.add(item.endAt to column); width=maxOf(width,column+1); group.add(item to column)
    }; finish(); return out
}
@Composable private fun Timeline(items:List<Occurrence>,start:LocalDate,days:Int,modifier:Modifier,onEdit:(Occurrence)->Unit,onNew:(LocalDate,Int,Int)->Unit) {
    val zone=ZoneId.systemDefault()
    BoxWithConstraints(modifier) {
        val dayWidth=if(days==1) maxWidth-44.dp else maxOf(112.dp,(maxWidth-44.dp)/7)
        Column(Modifier.fillMaxSize().horizontalScroll(rememberScrollState())) {
            Row { Spacer(Modifier.width(44.dp)); repeat(days) { offset->
                val day=start.plusDays(offset.toLong())
                Column(Modifier.width(dayWidth).padding(4.dp)) {
                    Text(day.format(DateTimeFormatter.ofPattern("EEE d")),style=MaterialTheme.typography.labelMedium)
                    val low=day.atStartOfDay(zone).toInstant().toEpochMilli();val high=day.plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli()
                    items.filter { it.event.allDay && it.startAt<high && it.endAt>low }.forEach { item->
                        Text(item.event.title,Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.secondaryContainer).clickable{onEdit(item)}.padding(4.dp),maxLines=1,style=MaterialTheme.typography.labelSmall)
                    }
                }
            } }
            Row(Modifier.weight(1f).verticalScroll(rememberScrollState())) {
                Column(Modifier.width(44.dp)) { repeat(24) { Text("%02d:00".format(it),Modifier.height(60.dp),style=MaterialTheme.typography.labelSmall) } }
                repeat(days) { offset->
                    val day=start.plusDays(offset.toLong()); val low=day.atStartOfDay(zone).toInstant().toEpochMilli(); val high=day.plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli()
                    Box(Modifier.width(dayWidth).height(1440.dp)) {
                        Column { repeat(48) { slot->Box(Modifier.fillMaxWidth().height(30.dp).border(.25.dp,MaterialTheme.colorScheme.outlineVariant)
                            .clickable { onNew(day,slot/2,if(slot%2==0) 0 else 30) }) } }
                        val positioned=position(items.filter { !it.event.allDay && it.startAt<high && it.endAt>low })
                        positioned.forEach { pos->
                            val item=pos.item
                            val localStart=Instant.ofEpochMilli(maxOf(item.startAt,low)).atZone(zone)
                            val minutes=if(item.startAt<low) 0 else localStart.hour*60+localStart.minute
                            val localEnd=Instant.ofEpochMilli(minOf(item.endAt,high)).atZone(zone)
                            val endMinutes=if(item.endAt>=high) 1440 else localEnd.hour*60+localEnd.minute
                            val width=dayWidth/pos.columns
                            Surface(color=Color(item.event.color),contentColor=foreground(Color(item.event.color)),shape=MaterialTheme.shapes.small,
                                modifier=Modifier.offset(x=width*pos.column,y=minutes.dp).width(width-2.dp).height((endMinutes-minutes).coerceAtLeast(24).dp).clickable { onEdit(item) }) {
                                Column(Modifier.padding(4.dp)) { Text(item.event.title,style=MaterialTheme.typography.labelSmall,maxLines=2); if(endMinutes-minutes>40) Text("%02d:%02d".format(localStart.hour,localStart.minute),style=MaterialTheme.typography.labelSmall) }
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable fun DateTimeField(label:String,millis:Long,zoneId:String=ZoneId.systemDefault().id,dateOnly:Boolean=false,onChange:(Long)->Unit) {
    val context=LocalContext.current; val zone=runCatching {ZoneId.of(zoneId)}.getOrDefault(ZoneId.systemDefault())
    val date=Instant.ofEpochMilli(millis).atZone(zone)
    Column {
        Text(label,style=MaterialTheme.typography.labelMedium)
        Row {
            TextButton(onClick={ DatePickerDialog(context,{_,y,m,d->onChange(LocalDate.of(y,m+1,d).atTime(date.toLocalTime()).atZone(zone).toInstant().toEpochMilli())},date.year,date.monthValue-1,date.dayOfMonth).show() }) { Icon(Icons.Default.CalendarToday,null); Text(date.format(DateTimeFormatter.ofPattern("d MMM yyyy"))) }
            if(!dateOnly) TextButton(onClick={ TimePickerDialog(context,{_,h,m->onChange(date.withHour(h).withMinute(m).withSecond(0).toInstant().toEpochMilli())},date.hour,date.minute,true).show() }) { Icon(Icons.Default.Schedule,null);Text(date.format(DateTimeFormatter.ofPattern("HH:mm"))) }
        }
    }
}
@Composable fun ReminderPicker(minutes:Int?,mode:String,confirmed:Boolean,onChange:(Int?,String)->Unit,onTimingConfirmed:(Boolean)->Unit={}) {
    var custom by remember(minutes) { mutableStateOf(minutes?.toString() ?: "30") }
    Text("How early should the reminder go off?",style=MaterialTheme.typography.titleSmall)
    Row(Modifier.horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(8.dp)) {
        listOf<Int?>(null,0,30,60,90,120,180,360,720,1440).forEach { value->
            FilterChip(minutes==value && confirmed,{custom=(value ?: 30).toString();onChange(value,mode);onTimingConfirmed(true)},label={Text(when(value){null->"Off";0->"At start";else->"$value min before"})})
        }
    }
    OutlinedTextField(custom,{custom=it;val valid=it.toIntOrNull()?.takeIf{n->n>=0 && n%30==0};onTimingConfirmed(valid!=null);valid?.let{n->onChange(n,mode)}},
        label={Text("Custom minutes before (multiples of 30)")},singleLine=true,keyboardOptions=androidx.compose.foundation.text.KeyboardOptions(keyboardType=androidx.compose.ui.text.input.KeyboardType.Number),modifier=Modifier.fillMaxWidth())
    Row(horizontalArrangement=Arrangement.spacedBy(8.dp)) {
        FilterChip(mode=="NOTIFICATION",{onChange(minutes,"NOTIFICATION")},label={Text("Notification")})
        FilterChip(mode=="ALARM",{onChange(minutes,"ALARM")},label={Text("Ringing alarm")})
    }
    if(!confirmed) Text("Choose a timing option to confirm. Suggested: "+(minutes?.let{if(it==0) "At start" else "$it minutes before"} ?: "Off")+".",style=MaterialTheme.typography.bodySmall,color=MaterialTheme.colorScheme.primary)
}
@Composable private fun EventEditor(event:CalendarEvent,oneOccurrence:Boolean,onDismiss:()->Unit,onSave:(CalendarEvent)->Unit,onDelete:(()->Unit)?) {
    var title by remember {mutableStateOf(event.title)}; var description by remember{mutableStateOf(event.description)}
    var start by remember{mutableLongStateOf(event.startAt)};var end by remember{mutableLongStateOf(event.endAt)}
    var zone by remember{mutableStateOf(event.zoneId)};var allDay by remember{mutableStateOf(event.allDay)}
    var rule by remember{mutableStateOf(event.recurrence)};var until by remember{mutableStateOf(event.repeatUntil)}
    var lead by remember{mutableStateOf(event.reminderMinutes)};var alarmMode by remember{mutableStateOf(event.reminderMode)}
    var confirmed by remember{mutableStateOf(event.id!=0L)};var color by remember{mutableLongStateOf(event.color)}
    var error by remember{mutableStateOf("")};var deleteConfirm by remember{mutableStateOf(false)}
    androidx.compose.ui.window.Dialog(onDismissRequest=onDismiss,properties=androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth=false)) {
        Surface(Modifier.fillMaxWidth(.96f).fillMaxHeight(.94f),shape=MaterialTheme.shapes.large) {
            Column(Modifier.padding(20.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
                Text(if(event.id==0L) "New event" else if(oneOccurrence) "Edit occurrence" else "Edit event",style=MaterialTheme.typography.headlineSmall)
                Column(Modifier.weight(1f).verticalScroll(rememberScrollState()),verticalArrangement=Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(title,{title=it},label={Text("Event title")},modifier=Modifier.fillMaxWidth())
                    OutlinedTextField(description,{description=it},label={Text("Description")},modifier=Modifier.fillMaxWidth(),minLines=2)
                    Row(verticalAlignment=Alignment.CenterVertically) { Text("All day",Modifier.weight(1f));Switch(allDay,{value->
                        allDay=value
                        if(value) {val z=runCatching{ZoneId.of(zone)}.getOrDefault(ZoneId.systemDefault());start=Instant.ofEpochMilli(start).atZone(z).toLocalDate().atStartOfDay(z).toInstant().toEpochMilli();end=Instant.ofEpochMilli(start).atZone(z).toLocalDate().plusDays(1).atStartOfDay(z).toInstant().toEpochMilli()}
                    }) }
                    val editorZone=runCatching{ZoneId.of(zone)}.getOrDefault(ZoneId.systemDefault())
                    DateTimeField("Starts",start,zone,allDay){next->
                        if(allDay){
                            val days=java.time.temporal.ChronoUnit.DAYS.between(Instant.ofEpochMilli(start).atZone(editorZone).toLocalDate(),Instant.ofEpochMilli(end).atZone(editorZone).toLocalDate()).coerceAtLeast(1)
                            start=next;end=Instant.ofEpochMilli(next).atZone(editorZone).toLocalDate().plusDays(days).atStartOfDay(editorZone).toInstant().toEpochMilli()
                        }else{val duration=end-start;start=next;end=next+duration}
                    }
                    val shownEnd=if(allDay) Instant.ofEpochMilli(end).atZone(editorZone).minusDays(1).toInstant().toEpochMilli() else end
                    DateTimeField(if(allDay) "Last day" else "Ends",shownEnd,zone,allDay){value->end=if(allDay) Instant.ofEpochMilli(value).atZone(editorZone).toLocalDate().plusDays(1).atStartOfDay(editorZone).toInstant().toEpochMilli() else value}
                    OutlinedTextField(zone,{zone=it},label={Text("Time zone")},singleLine=true,modifier=Modifier.fillMaxWidth())
                    if(!oneOccurrence) {
                        Text("Repeat",style=MaterialTheme.typography.titleSmall)
                        Row(Modifier.horizontalScroll(rememberScrollState()),horizontalArrangement=Arrangement.spacedBy(6.dp)) {
                            listOf("" to "Never","FREQ=DAILY" to "Daily","FREQ=WEEKLY" to "Weekly","FREQ=MONTHLY" to "Monthly").forEach{(value,label)->FilterChip(rule==value,{rule=value},label={Text(label)})}
                        }
                        if(rule.isNotBlank()) {
                            Row(verticalAlignment=Alignment.CenterVertically){Checkbox(until!=null,{until=if(it) start+31L*86400000 else null});Text("End repeating on a date")}
                            until?.let{DateTimeField("Repeat through",it,zone,true){date->val z=ZoneId.of(zone);until=Instant.ofEpochMilli(date).atZone(z).toLocalDate().atTime(23,59,59).atZone(z).toInstant().toEpochMilli()}}
                        }
                    }
                    ReminderPicker(lead,alarmMode,confirmed,onChange={minutes,type->lead=minutes;alarmMode=type},onTimingConfirmed={confirmed=it})
                    Text("Event color",style=MaterialTheme.typography.titleSmall)
                    Row(horizontalArrangement=Arrangement.spacedBy(12.dp)) { listOf(0xFF52796F,0xFF3F64B5,0xFF8E54B1,0xFFB95A38,0xFFAA3D64,0xFF746029).forEach{c->
                        Surface(color=Color(c),shape=MaterialTheme.shapes.small,border=if(color==c) BorderStroke(3.dp,MaterialTheme.colorScheme.onSurface) else null,modifier=Modifier.size(36.dp).clickable{color=c}){}
                    } }
                    if(error.isNotBlank()) Text(error,color=MaterialTheme.colorScheme.error)
                }
                Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween) {
                    if(onDelete!=null) TextButton(onClick={deleteConfirm=true}){Text("Delete",color=MaterialTheme.colorScheme.error)}
                    TextButton(onClick=onDismiss){Text("Cancel")}
                    Button(onClick={
                        try {
                            require(confirmed){"Choose how early the reminder should go off."}
                            val result=event.copy(title=title.trim(),description=description,startAt=start,endAt=end,zoneId=zone,allDay=allDay,recurrence=rule,repeatUntil=until,reminderMinutes=lead,reminderMode=alarmMode,color=color)
                            validateEvent(result);onSave(result)
                        } catch(e:Exception){error=e.message ?: "Check event details."}
                    }){Text("Save event")}
                }
            }
        }
    }
    if(deleteConfirm) AlertDialog(onDismissRequest={deleteConfirm=false},title={Text(if(oneOccurrence) "Delete this occurrence?" else "Delete event and its repeats?")},
        text={Text(title)},confirmButton={TextButton(onClick={onDelete?.invoke();deleteConfirm=false}){Text("Delete")}},dismissButton={TextButton(onClick={deleteConfirm=false}){Text("Cancel")}})
}
@Composable private fun TaskEditor(task:PlannerItem?,onDismiss:()->Unit,onSave:(PlannerItem)->Unit,onDelete:(PlannerItem)->Unit) {
    var title by remember{mutableStateOf(task?.title ?: "")};var details by remember{mutableStateOf(task?.details ?: "")}
    var due by remember{mutableLongStateOf(task?.dueAt ?: System.currentTimeMillis()+3600000)}
    var deleting by remember{mutableStateOf(false)}
    AlertDialog(onDismissRequest=onDismiss,title={Text(if(task==null) "New task" else "Edit task")},text={
        Column(verticalArrangement=Arrangement.spacedBy(10.dp)){
            OutlinedTextField(title,{title=it},label={Text("Title")});OutlinedTextField(details,{details=it},label={Text("Details")})
            DateTimeField("Due",due){due=it}
            if(task!=null) TextButton(onClick={deleting=true}){Text("Delete task",color=MaterialTheme.colorScheme.error)}
        }
    },confirmButton={TextButton(enabled=title.isNotBlank(),onClick={onSave((task ?: PlannerItem(title=title,dueAt=due)).copy(title=title,details=details,dueAt=due))}){Text("Save")}},
        dismissButton={TextButton(onClick=onDismiss){Text("Cancel")}})
    if(deleting && task!=null) AlertDialog(onDismissRequest={deleting=false},title={Text("Delete task?")},text={Text(title)},
        confirmButton={TextButton(onClick={onDelete(task)}){Text("Delete")}},dismissButton={TextButton(onClick={deleting=false}){Text("Cancel")}})
}
