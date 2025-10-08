package com.example.aufgabe1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Aufgabe1UI()
        }
    }
}

@Composable
fun Aufgabe1UI() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Lorem ipsum dolor sit amet")
        Text("Lorem ipsum dolor sit amet", color = Color.Red)
        Text("Lorem ipsum dolor sit amet", color = Color.Blue)
        Text(
            "Lorem ipsum dolor sit amet",
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic,
            fontFamily = FontFamily.Monospace
        )
        Text("Großer Text", fontSize = 25.sp)
        Text("Kleiner Text", fontSize = 10.sp)
        Text("Eigene Schriftart", fontFamily = FontFamily.Cursive)
        ClickableText(
            buildAnnotatedString {
                pushStyle(SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline))
                append("http://youtube.com")
            },
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun Aufgabe1UIPreview() {
    Aufgabe1UI()
}
