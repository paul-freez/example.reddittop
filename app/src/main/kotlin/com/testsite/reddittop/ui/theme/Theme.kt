package com.testsite.reddittop.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

private val LightColorScheme = lightColorScheme(
    primary = OrangePrimary,
    onPrimary = White,
    secondary = TextSecondary,
    onSecondary = White,
    background = White,
    surface = White,
    onSurface = TextTitle,
    onSurfaceVariant = TextPrimary,
    outline = GrayAccent
)

// Defaulting to light mode colors for now as requested, but keeping the structure
private val DarkColorScheme = darkColorScheme(
    primary = OrangePrimary,
    onPrimary = White,
    secondary = TextSecondary,
    onSecondary = White,
    background = DarkBackground,
    surface = DarkBackground,
    onSurface = White,
    onSurfaceVariant = GrayAccent,
    outline = GrayAccent
)

@Composable
fun RedditTopTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun RedditTopThemePreview() {
    RedditTopTheme(darkTheme = false) {
        ThemePreviewContent()
    }
}

@Preview(showBackground = true, name = "Dark Mode")
@Composable
fun RedditTopThemePreviewDark() {
    RedditTopTheme(darkTheme = true) {
        ThemePreviewContent()
    }
}

@Composable
private fun ThemePreviewContent() {
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "RedditTop Theme Preview",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            ColorSample(name = "Primary", color = MaterialTheme.colorScheme.primary)
            ColorSample(name = "Secondary", color = MaterialTheme.colorScheme.secondary)
            ColorSample(name = "Background", color = MaterialTheme.colorScheme.background)
            ColorSample(name = "On Surface (Title)", color = MaterialTheme.colorScheme.onSurface)
            ColorSample(name = "On Surface Variant (Primary Text)", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun ColorSample(name: String, color: Color) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(40.dp),
            color = color,
            shadowElevation = 2.dp,
            shape = MaterialTheme.shapes.small
        ) {}
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = name, style = MaterialTheme.typography.bodyLarge)
    }
}
