package com.example.todoapplication.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val extendedColors =
        if (darkTheme) darkExtendedColors else lightExtendedColors
    val extendedTypography = ExtendedAppTypography
    val colorScheme = when {


        darkTheme -> darkColorScheme()
        else -> lightColorScheme()

    }



    CompositionLocalProvider(
        LocalExtendedColors provides extendedColors,
        LocalExtendedTypography provides extendedTypography
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }

}

object ExtendedTheme {
    val colors: ExtendedColors
        @Composable
        get() = LocalExtendedColors.current

    val typography: ExtendedTypography
        @Composable
        get() = LocalExtendedTypography.current
}


@Preview(showBackground = true, widthDp = 1440, heightDp = 1055)
@Composable
fun ShowPalettePreview() {
    Column {
        PalettePreview(isDarkTheme = false)
        Spacer(
            modifier = Modifier
                .height(40.dp)
                .fillMaxWidth()
                .background(ExtendedTheme.colors.paletteColor)
        )
        PalettePreview(isDarkTheme = true)
    }
}

@Preview(showBackground = true, widthDp = 250, heightDp = 115)
@Composable
fun ShowTypographyPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ExtendedTheme.colors.paletteColor)
    ) {
        TypographyPreview()
    }

}


