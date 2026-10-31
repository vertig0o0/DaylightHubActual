package dev.daylight.hub.ui

import android.database.ContentObserver
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import androidx.compose.animation.core.*
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

data class HubMotion(val enabled:Boolean=true,val placement:Boolean=true)
val LocalHubMotion=staticCompositionLocalOf{HubMotion()}

@Composable fun motionPolicy(preference:String):HubMotion {
    val resolver=LocalContext.current.contentResolver
    val scale by produceState(1f,resolver) {
        fun update(){value=Settings.Global.getFloat(resolver,Settings.Global.ANIMATOR_DURATION_SCALE,1f)}
        val observer=object:ContentObserver(Handler(Looper.getMainLooper())){override fun onChange(selfChange:Boolean)=update()}
        resolver.registerContentObserver(Settings.Global.getUriFor(Settings.Global.ANIMATOR_DURATION_SCALE),false,observer)
        update();awaitDispose{resolver.unregisterContentObserver(observer)}
    }
    return HubMotion(preference!="Off" && scale>0,preference=="System" && scale>0)
}

@Composable fun LazyItemScope.animatedItem(modifier:Modifier=Modifier):Modifier {
    val motion=LocalHubMotion.current
    return if(!motion.enabled)modifier else modifier.animateItem(fadeInSpec=tween(120),placementSpec=if(motion.placement)tween(180)else null,fadeOutSpec=tween(100))
}

@Composable fun SavedProgress(value:Float,modifier:Modifier=Modifier) {
    val motion=LocalHubMotion.current
    val progress by animateFloatAsState(value.coerceIn(0f,1f),if(motion.enabled)tween(180)else snap(),label="Saved progress")
    LinearProgressIndicator(progress={progress},modifier=modifier)
}
