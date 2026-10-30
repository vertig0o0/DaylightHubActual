package dev.daylight.hub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import dev.daylight.hub.calendar.AlarmScheduler
import dev.daylight.hub.ui.HubApp

class MainActivity : ComponentActivity() {
    private lateinit var model: HubViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        model=ViewModelProvider(this)[HubViewModel::class.java]
        if(intent.getBooleanExtra("openCalendar",false)) model.section="Planner"
        setContent { HubApp(model) }
    }
    override fun onResume() { super.onResume(); AlarmScheduler.request(this) }
    override fun onNewIntent(intent: android.content.Intent) {
        super.onNewIntent(intent); setIntent(intent)
        if(intent.getBooleanExtra("openCalendar",false)) model.navigate("Planner")
    }
    override fun onStop() { if(::model.isInitialized) { model.editor?.let { d -> model.action { model.saveDraft(d) } }; model.stop() }; super.onStop() }
}
