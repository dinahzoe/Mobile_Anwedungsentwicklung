package com.example.aufgabe2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FarbbalkenTheme {
                FarbbalkenApp()
            }
        }
    }
}

private val LightColors = lightColorScheme()

@Composable
fun FarbbalkenTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        content = content
    )
}

@Composable
fun FarbbalkenApp() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
    ) {
        val colors = listOf(
            Color(0xFFFF5722),
            Color(0xFF009688),
            Color(0xFF673AB7),
            Color(0xFF3F51B5),
            Color(0xFF9C27B0),
            Color(0xFFE91E63),
            Color(0xFFFF9800),
            Color(0xFF8BC34A)
        )

        colors.forEach { color ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(color)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FarbbalkenPreview() {
    FarbbalkenTheme {
        FarbbalkenApp()
    }
}
