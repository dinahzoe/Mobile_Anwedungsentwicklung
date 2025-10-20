package com.example.hoehenmesser

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hoehenmesser.ui.theme.HoehenmesserTheme
import kotlinx.coroutines.delay
import kotlin.math.pow

class MainActivity : ComponentActivity(), SensorEventListener {

    private val p0 = 1013.25f // Standard-Luftdruck auf Meereshöhe

    private lateinit var sensorManager: SensorManager
    private var pressureSensor: Sensor? = null

    private val pressureState = mutableStateOf<Float?>(null)
    private val altitudeState = mutableStateOf<Double?>(null)
    private var manualPressure: Float? = null // Druck für manuelle Höhe

    private var lastPressure: Float? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)

        setContent {
            HoehenmesserTheme {
                Surface(modifier = Modifier.fillMaxSize()) {

                    // UI-Update alle 200ms
                    LaunchedEffect(Unit) {
                        while (true) {
                            val currentPressure = manualPressure ?: lastPressure
                            currentPressure?.takeIf { it > 0 }?.let { p ->
                                val altitudeValue =
                                    44330.0 * (1 - (p / p0).toDouble().pow(1 / 5.255))
                                altitudeState.value = altitudeValue
                                pressureState.value = p
                            }
                            delay(200L)
                        }
                    }

                    HoehenmesserView(
                        pressure = pressureState.value,
                        altitude = altitudeState.value,
                        onSetKnownHeight = { knownHeight ->
                            // Höhe exakt übernehmen
                            altitudeState.value = knownHeight
                            // Luftdruck passend zur eingegebenen Höhe berechnen
                            manualPressure = (p0 * (1 - (knownHeight / 44330.0)).pow(5.255)).toFloat()
                        }
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        pressureSensor?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_PRESSURE) {
            lastPressure = event.values[0]
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}

@Composable
fun HoehenmesserView(
    pressure: Float?,
    altitude: Double?,
    onSetKnownHeight: (Double) -> Unit
) {
    var inputText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly // gleichmäßige Verteilung
    ) {
        // Titel
        Text(
            text = "Luftdruck berechnen",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        // Eingabefeld für Höhe
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Höhe eingeben (m)",
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = { Text("z.B. 250") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.width(200.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = {
                    inputText.toDoubleOrNull()?.let {
                        onSetKnownHeight(it)
                        inputText = ""
                    }
                },
                enabled = inputText.isNotBlank()
            ) {
                Text("Höhe übernehmen")
            }
        }

        // Ergebnis: Luftdruck
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (altitude != null) "Höhe: %.1f m".format(altitude) else "Höhe: –",
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (pressure != null) "Luftdruck: %.2f hPa".format(pressure) else "Luftdruck: –",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HoehenmesserPreview() {
    HoehenmesserTheme {
        HoehenmesserView(
            pressure = 1005.3f,
            altitude = 250.0,
            onSetKnownHeight = {}
        )
    }
}
