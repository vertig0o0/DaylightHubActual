package dev.daylight.hub.ui

import android.content.SharedPreferences
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import org.json.JSONObject

data class Appearance(
    val mode: String="System", val accent: Long=0xFF366A59, val compact: Boolean=false,
    val textScale: Float=1f, val editorFont: String="Sans", val background: String="Solid",
    val image: String="", val lightBackground: Long=0xFFF6F7F4, val darkBackground: Long=0xFF101715,
    val lightSurface: Long=0xFFFFFFFF, val darkSurface: Long=0xFF1C2621,
    val darkAccent: Long=0xFF91D5B6, val navigation: String="Home,Planner,Notes,Assistant"
) {
    fun json() = JSONObject().put("mode",mode).put("accent",accent).put("compact",compact).put("textScale",textScale)
        .put("editorFont",editorFont).put("background",background).put("image",image)
        .put("lightBackground",lightBackground).put("darkBackground",darkBackground).put("lightSurface",lightSurface)
        .put("darkSurface",darkSurface).put("darkAccent",darkAccent).put("navigation",navigation).toString()
    fun describe()="Theme: "+mode+"\nLight accent: #"+"%06X".format(accent and 0xFFFFFF)+" · Dark accent: #"+"%06X".format(darkAccent and 0xFFFFFF)+"\nSpacing: "+(if(compact) "Compact" else "Comfortable")+"\nText size: "+(textScale*100).toInt()+"% · Editor font: "+editorFont
    companion object {
        fun read(p: SharedPreferences): Appearance {
            val presets=listOf(0xFF366A59,0xFF4E6298,0xFF9A5A40,0xFF72518F)
            val fallback=Appearance(accent=presets[p.getInt("accent",0).coerceIn(presets.indices)], compact=p.getBoolean("compact",false))
            return runCatching { parse(p.getString("appearance",null) ?: return fallback) }.getOrDefault(fallback)
        }
        fun parse(text: String): Appearance {
            val j=JSONObject(text); val d=Appearance()
            return Appearance(j.optString("mode",d.mode),j.optLong("accent",d.accent),j.optBoolean("compact",false),
                j.optDouble("textScale",1.0).toFloat(),j.optString("editorFont","Sans"),j.optString("background","Solid"),
                j.optString("image",""),j.optLong("lightBackground",d.lightBackground),j.optLong("darkBackground",d.darkBackground),
                j.optLong("lightSurface",d.lightSurface),j.optLong("darkSurface",d.darkSurface),j.optLong("darkAccent",d.darkAccent),
                j.optString("navigation",d.navigation)).also { require(it.mode in listOf("System","Light","Dark")); require(it.textScale.isFinite() && it.textScale in .8f..1.5f) }
        }
    }
}
fun foreground(background: Color): Color = if(background.luminance()>.179f) Color.Black else Color.White
@Composable fun HubTheme(appearance: Appearance, content: @Composable ()->Unit) {
    val dark=appearance.mode=="Dark" || appearance.mode=="System" && isSystemInDarkTheme()
    val base=if(dark) darkColorScheme() else lightColorScheme()
    val background=Color(if(dark) appearance.darkBackground else appearance.lightBackground)
    val surface=Color(if(dark) appearance.darkSurface else appearance.lightSurface)
    val primary=Color(if(dark) appearance.darkAccent else appearance.accent)
    val scheme=base.copy(primary=primary,onPrimary=foreground(primary),secondary=primary,onSecondary=foreground(primary),
        background=background,onBackground=foreground(background),surface=surface,onSurface=foreground(surface),
        surfaceVariant=surface,onSurfaceVariant=foreground(surface),surfaceContainerLowest=surface,surfaceContainerLow=surface,
        surfaceContainer=surface,surfaceContainerHigh=surface,surfaceContainerHighest=surface)
    MaterialTheme(colorScheme=scheme) {
        CompositionLocalProvider(LocalContentColor provides scheme.onBackground) { content() }
    }
}
