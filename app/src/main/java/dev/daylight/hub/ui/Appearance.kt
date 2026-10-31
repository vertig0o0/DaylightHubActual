package dev.daylight.hub.ui

import android.content.SharedPreferences
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.lerp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.json.JSONObject

data class Appearance(
    val mode: String="System", val accent: Long=0xFF366A59, val compact: Boolean=false,
    val textScale: Float=1f, val editorFont: String="Sans", val background: String="Solid",
    val image: String="", val lightBackground: Long=0xFFF6F7F4, val darkBackground: Long=0xFF101715,
    val lightSurface: Long=0xFFFFFFFF, val darkSurface: Long=0xFF1C2621,
    val darkAccent: Long=0xFF91D5B6, val navigation: String="Today,Planner,Notes,Reading,Assistant",
    val motion:String="System"
) {
    fun json() = JSONObject().put("mode",mode).put("accent",accent).put("compact",compact).put("textScale",textScale)
        .put("editorFont",editorFont).put("background",background).put("image",image)
        .put("lightBackground",lightBackground).put("darkBackground",darkBackground).put("lightSurface",lightSurface)
        .put("darkSurface",darkSurface).put("darkAccent",darkAccent).put("navigation",navigation).put("navigationVersion",2).put("motion",motion).toString()
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
                migrateNavigation(j.optString("navigation",d.navigation),j.optInt("navigationVersion",0)<2),j.optString("motion","System")).also { require(it.mode in listOf("System","Light","Dark")); require(it.motion in listOf("System","Reduced","Off")); require(it.textScale.isFinite() && it.textScale in .8f..1.5f) }
        }
        fun migrateNavigation(value:String,addReading:Boolean=true):String {
            val items=value.split(",").map { if(it=="Home") "Today" else it }.filter{it in listOf("Today","Planner","Notes","Reading","Assistant")}.distinct().toMutableList()
            if(addReading && "Reading" !in items) items.add(items.indexOf("Assistant").takeIf{it>=0} ?: items.size,"Reading")
            return items.joinToString(",")
        }
    }
}
fun foreground(background: Color): Color = if(background.luminance()>.179f) Color.Black else Color.White
fun contrastRatio(first:Color,second:Color):Float {
    val a=first.luminance();val b=second.luminance()
    return (maxOf(a,b)+.05f)/(minOf(a,b)+.05f)
}
fun readableAccent(requested:Color,surface:Color,background:Color):Color {
    val opaque=requested.copy(alpha=1f)
    if(contrastRatio(opaque,surface)>=4.5f && contrastRatio(opaque,background)>=4.5f)return opaque
    var best=foreground(surface);var score=0f
    for(step in 1..100)for(target in listOf(Color.Black,Color.White)) {
        val candidate=lerp(opaque,target,step/100f)
        val contrast=minOf(contrastRatio(candidate,surface),contrastRatio(candidate,background))
        if(contrast>=4.5f)return candidate
        if(contrast>score){score=contrast;best=candidate}
    }
    return best
}
@Composable fun HubTheme(appearance: Appearance, content: @Composable ()->Unit) {
    val motion=motionPolicy(appearance.motion)
    val dark=appearance.mode=="Dark" || appearance.mode=="System" && isSystemInDarkTheme()
    val base=if(dark) darkColorScheme() else lightColorScheme()
    val background=Color(if(dark) appearance.darkBackground else appearance.lightBackground).copy(alpha=1f)
    val surface=Color(if(dark) appearance.darkSurface else appearance.lightSurface).copy(alpha=1f)
    val primary=remember(appearance.accent,appearance.darkAccent,dark,background,surface){readableAccent(Color(if(dark) appearance.darkAccent else appearance.accent),surface,background)}
    val tonal=lerp(surface,primary,if(dark) .15f else .10f)
    val secondaryText=lerp(foreground(surface),surface,.2f).takeIf{contrastRatio(it,surface)>=4.5f && contrastRatio(it,tonal)>=4.5f} ?: foreground(surface)
    val scheme=base.copy(primary=primary,onPrimary=foreground(primary),secondary=primary,onSecondary=foreground(primary),
        primaryContainer=tonal,onPrimaryContainer=foreground(tonal),secondaryContainer=tonal,onSecondaryContainer=foreground(tonal),
        background=background,onBackground=foreground(background),surface=surface,onSurface=foreground(surface),
        surfaceVariant=lerp(surface,primary,.06f),onSurfaceVariant=secondaryText,surfaceContainerLowest=background,surfaceContainerLow=surface,
        surfaceContainer=surface,surfaceContainerHigh=lerp(surface,primary,.06f),surfaceContainerHighest=tonal,
        outline=lerp(foreground(surface),surface,.45f),outlineVariant=lerp(foreground(surface),surface,.82f))
    val typography=Typography(
        headlineMedium=TextStyle(fontSize=28.sp,lineHeight=34.sp,fontWeight=FontWeight.SemiBold),
        headlineSmall=TextStyle(fontSize=24.sp,lineHeight=32.sp,fontWeight=FontWeight.SemiBold),
        titleLarge=TextStyle(fontSize=20.sp,lineHeight=28.sp,fontWeight=FontWeight.SemiBold),
        titleMedium=TextStyle(fontSize=16.sp,lineHeight=24.sp,fontWeight=FontWeight.SemiBold),
        bodyLarge=TextStyle(fontSize=16.sp,lineHeight=24.sp),bodyMedium=TextStyle(fontSize=14.sp,lineHeight=20.sp),
        labelSmall=TextStyle(fontSize=12.sp,lineHeight=16.sp))
    MaterialTheme(colorScheme=scheme,typography=typography,shapes=Shapes(small=RoundedCornerShape(8.dp),medium=RoundedCornerShape(12.dp),large=RoundedCornerShape(20.dp))) {
        CompositionLocalProvider(LocalContentColor provides scheme.onBackground,LocalHubMotion provides motion) { content() }
    }
}
