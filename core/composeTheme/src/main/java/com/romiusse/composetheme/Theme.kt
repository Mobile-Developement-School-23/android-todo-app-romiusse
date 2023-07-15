package com.romiusse.composetheme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

private val LightColors = lightColorScheme(
    primary = label_primary,
    secondary = label_secondary,
    surface = back_primary
)


private val DarkColors = darkColorScheme(
    primary = dark_label_primary,
    secondary = dark_label_secondary,
    surface = dark_back_primary
)

@Preview
@Composable
fun AppTheme(
    type: Int? = 0,
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit = {}
) {

    val colors = when(type){
        0 -> if (!useDarkTheme) LightColors else DarkColors
        1 -> LightColors
        2 -> DarkColors
        else -> if (!useDarkTheme) LightColors else DarkColors
    }

    MaterialTheme(
        colorScheme = colors,
        typography = typography,
        content = content
    )
}