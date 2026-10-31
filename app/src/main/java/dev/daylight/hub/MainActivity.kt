package dev.daylight.hub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import dev.daylight.hub.calendar.AlarmScheduler
import dev.daylight.hub.ui.V3App

class MainActivity : ComponentActivity() {
    private lateinit var model: HubViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        model=ViewModelProvider(this)[HubViewModel::class.java]
        if(intent.getBooleanExtra("openCalendar",false)) { model.section="Planner";model.plannerTab="Calendar" }
        if(intent.getBooleanExtra("openFocus",false)) model.showFocus=true
        handleBookIntent(intent)
        setContent { V3App(model) }
    }
    override fun onResume() { super.onResume(); AlarmScheduler.request(this);if(::model.isInitialized) model.action { model.focus.reconcile() } }
    override fun onNewIntent(intent: android.content.Intent) {
        super.onNewIntent(intent); setIntent(intent)
        if(intent.getBooleanExtra("openCalendar",false)) { model.plannerTab="Calendar";model.navigate("Planner") }
        if(intent.getBooleanExtra("openFocus",false)) model.showFocus=true
        handleBookIntent(intent)
    }
    private fun handleBookIntent(value:android.content.Intent) {
        if(value.action==android.content.Intent.ACTION_VIEW && value.data?.scheme=="content") {
            model.pendingBookUri=value.data;model.navigate("Reading")
            // A configuration change must not import the same intent a second time.
            value.action=null;value.data=null
        }
    }
    override fun onStop() { if(::model.isInitialized) { model.editor?.let { d -> model.action { model.saveDraft(d) } }; if(!isChangingConfigurations) model.stop() }; super.onStop() }
}
