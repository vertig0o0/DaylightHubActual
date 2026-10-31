package dev.daylight.hub

import androidx.compose.ui.graphics.Color
import dev.daylight.hub.ui.*
import org.junit.Assert.*
import org.junit.Test

class ThemeContrastTest {
    @Test fun customAccentsRemainReadableOnLightAndDarkSurfaces() {
        listOf(Color(0xFFF6F7F4) to Color.White,Color(0xFF101715) to Color(0xFF1C2621)).forEach{(background,surface)->
            listOf(Color.White,Color.Black,Color.Yellow,Color.Red,Color(0xFF366A59),Color(0xFF91D5B6)).forEach{accent->
                val adjusted=readableAccent(accent,surface,background)
                assertTrue(contrastRatio(adjusted,surface)>=4.5f);assertTrue(contrastRatio(adjusted,background)>=4.5f)
                assertTrue(contrastRatio(foreground(adjusted),adjusted)>=4.5f)
            }
        }
    }
}
