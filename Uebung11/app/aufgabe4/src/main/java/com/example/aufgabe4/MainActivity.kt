package com.example.aufgabe4

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
            CalculatorApp()
        }
    }
}

@Composable
fun CalculatorApp() {
    var display by remember { mutableStateOf("0") }
    var current by remember { mutableStateOf("") }
    var firstNumber by remember { mutableStateOf(0.0) }
    var operator by remember { mutableStateOf("") }

    val buttons = listOf(
        listOf("7","8","9","/"),
        listOf("4","5","6","*"),
        listOf("1","2","3","-"),
        listOf("0","C","=","+")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0DD400A3))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Taschenrechner",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 40.dp)
        )

        Text(
            text = display,
            fontSize = 32.sp,
            color = Color.Black,
            textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp)
        )

        Spacer(modifier = Modifier.height(50.dp))

        // Buttons
        buttons.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                row.forEach { symbol ->
                    Button(
                        onClick = {
                            when (symbol) {
                                in "0".."9" -> {
                                    current += symbol
                                    display = current
                                }
                                "+","-","*","/" -> {
                                    if (current.isNotEmpty()) {
                                        firstNumber = current.toDouble()
                                        operator = symbol
                                        current = ""
                                    }
                                }
                                "=" -> {
                                    if (current.isNotEmpty() && operator.isNotEmpty()) {
                                        val second = current.toDoubleOrNull() ?: 0.0
                                        val result = when(operator) {
                                            "+" -> firstNumber + second
                                            "-" -> firstNumber - second
                                            "*" -> firstNumber * second
                                            "/" -> if (second != 0.0) firstNumber / second else {
                                                display = "Error"
                                                current = ""
                                                operator = ""
                                                return@Button
                                            }
                                            else -> 0.0
                                        }
                                        display = if (result == result.toLong().toDouble())
                                            result.toLong().toString() else result.toString()
                                        current = result.toString()
                                        operator = ""
                                    }
                                }
                                "C" -> {
                                    current = ""
                                    firstNumber = 0.0
                                    operator = ""
                                    display = "0"
                                }
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(70.dp)
                    ) {
                        Text(text = symbol, fontSize = 24.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CalculatorPreview() {
    CalculatorApp()
}