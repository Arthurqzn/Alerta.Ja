package br.com.alertaja.ui

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Bg = Color(0xFF090910)
val Surface = Color(0xFF191823)
val Purple = Color(0xFF9B6DFF)
val Red = Color(0xFFF43F5E)
val Muted = Color(0xFFB7B3C5)
private val Palette = darkColorScheme(
    primary = Purple, secondary = Red, background = Bg, surface = Surface,
    onBackground = Color.White, onSurface = Color.White, onPrimary = Color.White
)
@Composable fun AlertaTheme(content: @Composable () -> Unit) { MaterialTheme(colorScheme = Palette, content = content) }
