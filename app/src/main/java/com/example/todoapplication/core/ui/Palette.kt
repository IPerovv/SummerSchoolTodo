package com.example.todoapplication.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Immutable
data class ExtendedColors(
    val supportSeparator: Color = Color.Unspecified,
    val supportOverlay: Color = Color.Unspecified,
    val labelPrimary: Color = Color.Unspecified,
    val labelSecondary: Color = Color.Unspecified,
    val labelTertiary: Color = Color.Unspecified,
    val labelDisable: Color = Color.Unspecified,
    //val labelPrimaryReversed: Color = Color.Unspecified,
    val backPrimary: Color = Color.Unspecified,
    val backSecondary: Color = Color.Unspecified,
    val backElevated: Color = Color.Unspecified,
    val colorGrayLight: Color = Color.Unspecified,
    val colorRed: Color = red,
    val colorYellow: Color = yellow,
    val colorGreen: Color = green,
    val colorBlue: Color = blue,
    val colorGray: Color = gray,
    val colorWhite: Color = white,
    val paletteColor: Color = paletteBackColor

)

val lightExtendedColors = ExtendedColors(
    supportSeparator = supportSeparatorLight,
    supportOverlay = supportOverlayLight,
    labelPrimary = labelPrimaryLight,
    //labelPrimaryReversed = labelPrimary,
    labelSecondary = labelSecondaryLight,
    labelTertiary = labelTertiaryLight,
    labelDisable = labelDisableLight,
    backPrimary = backPrimaryLight,
    backSecondary = backSecondaryLight,
    backElevated = backElevatedLight,
    colorGrayLight = grayLightLight
)

val darkExtendedColors = ExtendedColors(
    supportSeparator = supportSeparatorDark,
    supportOverlay = supportOverlayDark,
    labelPrimary = labelPrimaryDark,
    //labelPrimaryReversed = labelPrimary,
    labelSecondary = labelSecondaryDark,
    labelTertiary = labelTertiaryDark,
    labelDisable = labelDisableDark,
    backPrimary = backPrimaryDark,
    backSecondary = backSecondaryDark,
    backElevated = backElevatedDark,
    colorGrayLight = grayLightDark
)

val LocalExtendedColors = staticCompositionLocalOf {
    ExtendedColors()
}

@Composable
fun PalettePreview(isDarkTheme: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(ExtendedTheme.colors.paletteColor)
    ) {
        Column {
            Text(
                text = if (isDarkTheme) "Палитра - темная тема" else "Палитра - светлая тема",
                fontSize = 24.sp,
            )
            ShowSpacer()
            Palette(isDarkTheme = isDarkTheme)
        }

    }

}

private fun Color.toHex(): String {
    return String.format("#%08X", (this.toArgb() and 0xFFFFFFFF.toInt()))
}

@Composable
private fun getColorName(color: Color): String {
    return when (color) {
        ExtendedTheme.colors.supportSeparator -> "Support Separator"
        ExtendedTheme.colors.supportOverlay -> "Support Overlay"
        ExtendedTheme.colors.labelPrimary -> "Label Primary"
        ExtendedTheme.colors.labelSecondary -> "Label Secondary"
        ExtendedTheme.colors.labelTertiary -> "Label Tertiary"
        ExtendedTheme.colors.labelDisable -> "Label Disable"
        ExtendedTheme.colors.backPrimary -> "Back Primary"
        ExtendedTheme.colors.backSecondary -> "Back Secondary"
        ExtendedTheme.colors.backElevated -> "Back Elevated"
        ExtendedTheme.colors.colorGrayLight -> "Gray Light"
        ExtendedTheme.colors.colorRed -> "Red"
        ExtendedTheme.colors.colorYellow -> "Yellow"
        ExtendedTheme.colors.colorGreen -> "Green"
        ExtendedTheme.colors.colorBlue -> "Blue"
        ExtendedTheme.colors.colorGray -> "Gray"
        else -> "Unknown Color"
    }
}

@Composable
private fun ShowSpacer() {
    Spacer(
        modifier = Modifier
            .height(20.dp)
            .fillMaxWidth()
            .background(ExtendedTheme.colors.paletteColor)
    )
}


@Composable
private fun Palette(isDarkTheme: Boolean) {
    AppTheme(darkTheme = isDarkTheme) {
        Column {
            Row {
                ColorPreviewItem(
                    color = ExtendedTheme.colors.supportSeparator,
                    Color.Black
                )
                ColorPreviewItem(
                    color = ExtendedTheme.colors.supportOverlay,
                    Color.Black
                )
            }
            ShowSpacer()
            Row {
                if (!isDarkTheme) {
                    ColorPreviewItem(
                        color = ExtendedTheme.colors.labelPrimary,
                        Color.White
                    )
                    ColorPreviewItem(
                        color = ExtendedTheme.colors.labelSecondary,
                        Color.White
                    )
                    ColorPreviewItem(
                        color = ExtendedTheme.colors.labelTertiary,
                        Color.White
                    )
                } else {
                    ColorPreviewItem(
                        color = ExtendedTheme.colors.labelPrimary,
                        Color.Black
                    )
                    ColorPreviewItem(
                        color = ExtendedTheme.colors.labelSecondary,
                        Color.Black
                    )
                    ColorPreviewItem(
                        color = ExtendedTheme.colors.labelTertiary,
                        Color.Black
                    )
                }

                ColorPreviewItem(
                    color = ExtendedTheme.colors.labelDisable,
                    Color.Black
                )
            }
            ShowSpacer()
            Row {
                ColorPreviewItem(
                    color = ExtendedTheme.colors.colorRed,
                    Color.White
                )
                ColorPreviewItem(
                    color = ExtendedTheme.colors.colorGreen,
                    Color.White
                )
                ColorPreviewItem(
                    color = ExtendedTheme.colors.colorBlue,
                    Color.White
                )
                ColorPreviewItem(
                    color = ExtendedTheme.colors.colorGray,
                    Color.White
                )
                if (!isDarkTheme) {
                    ColorPreviewItem(
                        color = ExtendedTheme.colors.colorGrayLight,
                        Color.Black
                    )
                } else {
                    ColorPreviewItem(
                        color = ExtendedTheme.colors.colorGrayLight,
                        Color.White
                    )
                }
                ColorPreviewItem(
                    color = ExtendedTheme.colors.colorWhite,
                    Color.Black,
                    name = "White"
                )
            }
            ShowSpacer()
            Row {
                if (!isDarkTheme) {
                    ColorPreviewItem(
                        color = ExtendedTheme.colors.backPrimary,
                        Color.Black
                    )
                    ColorPreviewItem(
                        color = ExtendedTheme.colors.backSecondary,
                        Color.Black
                    )
                    ColorPreviewItem(
                        color = ExtendedTheme.colors.backElevated,
                        Color.Black
                    )
                } else {
                    ColorPreviewItem(
                        color = ExtendedTheme.colors.backPrimary,
                        Color.White
                    )
                    ColorPreviewItem(
                        color = ExtendedTheme.colors.backSecondary,
                        Color.White
                    )
                    ColorPreviewItem(
                        color = ExtendedTheme.colors.backElevated,
                        Color.White
                    )
                }
            }
        }

    }
}

@Composable
private fun ColorPreviewItem(color: Color, textColor: Color) {
    Box(
        modifier = Modifier
            .width(240.dp)
            .height(100.dp)
            .background(color),
        contentAlignment = Alignment.BottomCenter
    ) {
        Text(
            text = getColorName(color = color) + " " + color.toHex(),
            color = textColor,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun ColorPreviewItem(color: Color, textColor: Color, name: String) {
    Box(
        modifier = Modifier
            .width(240.dp)
            .height(100.dp)
            .background(color),
        contentAlignment = Alignment.BottomCenter
    ) {
        Text(
            text = name + " " + color.toHex(),
            color = textColor,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}