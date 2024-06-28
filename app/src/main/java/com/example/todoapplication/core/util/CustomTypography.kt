package com.example.todoapplication.core.util

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle

@Immutable
data class ExtendedTypography(
    val titleLarge: TextStyle = TextStyle.Default,
    val title: TextStyle = TextStyle.Default,
    val titleSmall: TextStyle = TextStyle.Default,
    val button: TextStyle = TextStyle.Default,
    val body: TextStyle = TextStyle.Default,
    val subhead: TextStyle = TextStyle.Default
)


val LocalExtendedTypography = staticCompositionLocalOf {
    ExtendedTypography()
}
@Composable
fun TypographyPreview() {
    Column {
        TypographyItem(name = "Large title — 32/38", style = ExtendedAppTypography.titleLarge)
        TypographyItem(name = "Title — 20/32", style = ExtendedAppTypography.title)
        TypographyItem(name = "BUTTON — 14/24", style = ExtendedAppTypography.button)
        TypographyItem(name = "Body — 16/20", style = ExtendedAppTypography.body)
        TypographyItem(name = "Subhead — 14/20", style = ExtendedAppTypography.subhead)
    }
}

@Composable
private fun TypographyItem(name: String, style: androidx.compose.ui.text.TextStyle) {
    Text(text = name, style = style)
}