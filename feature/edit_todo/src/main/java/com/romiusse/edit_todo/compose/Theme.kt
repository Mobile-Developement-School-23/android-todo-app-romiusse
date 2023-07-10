package com.romiusse.edit_todo.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

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


@Composable
fun AppTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (!useDarkTheme) {
        LightColors
    } else {
        DarkColors
    }

    MaterialTheme(
        colorScheme = colors,
        typography = typography,
        content = content
    )
}