package com.example.aufgabe3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TemperatureConverterApp()
        }
    }
}

@Composable
fun TemperatureConverterApp() {
    var input by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("Umgewandelter Wert") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Temperaturumrechner",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 40.dp)
        )

        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            placeholder = { Text("Wert hier bitte eingeben") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 40.dp)
        )

        Button(
            onClick = {
                val c = input.toDoubleOrNull()
                result = if (c != null) "Ergebnis: ${c * 9 / 5 + 32} °F" else "Ungültiger Wert"
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
                .height(75.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8BC34A)),
            shape = RoundedCornerShape(6.dp)
        ) {
            Text("Celsius in Fahrenheit", fontSize = 20.sp, color = Color.White)
        }

        Button(
            onClick = {
                val f = input.toDoubleOrNull()
                result = if (f != null) "Ergebnis: ${(f - 32) * 5 / 9} °C" else "Ungültiger Wert"
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 40.dp)
                .height(75.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF03A9F4)),
            shape = RoundedCornerShape(6.dp)
        ) {
            Text("Fahrenheit in Celsius", fontSize = 20.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = result,
            fontSize = 22.sp,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
                .padding(24.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TemperatureConverterPreview() {
    TemperatureConverterApp()
}
