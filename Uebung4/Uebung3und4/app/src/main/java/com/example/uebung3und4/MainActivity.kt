package com.example.uebung3und4

import android.app.Activity
import android.os.Bundle
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.widget.TextView
import android.view.View
import android.widget.LinearLayout

class MainActivity : Activity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var magnetometer: Sensor? = null

    private lateinit var rollText: TextView
    private lateinit var pitchText: TextView
    private lateinit var yawText: TextView

    private lateinit var wasserwaage: LinearLayout
    private lateinit var blase: View

    private var gravDaten = FloatArray(3)
    private var magnetDaten = FloatArray(3)
    private var gravVorhanden = false
    private var magnetVorhanden = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rollText = findViewById(R.id.textRoll)
        pitchText = findViewById(R.id.textPitch)
        yawText = findViewById(R.id.textYaw)

        wasserwaage = findViewById(R.id.wasserwaageLayout)
        blase = findViewById(R.id.blase)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    }

    override fun onResume() {
        super.onResume()
        accelerometer?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI) }
        magnetometer?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI) }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        when(event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                gravDaten = event.values.clone()
                gravVorhanden = true
            }
            Sensor.TYPE_MAGNETIC_FIELD -> {
                magnetDaten = event.values.clone()
                magnetVorhanden = true
            }
        }

        if (gravVorhanden && magnetVorhanden) {
            val rotMatrix = FloatArray(9)
            val inklMatrix = FloatArray(9)
            val success = SensorManager.getRotationMatrix(rotMatrix, inklMatrix, gravDaten, magnetDaten)
            if (success) {
                val orientation = FloatArray(3)
                SensorManager.getOrientation(rotMatrix, orientation)

                val yaw = Math.toDegrees(orientation[0].toDouble())
                val pitch = Math.toDegrees(orientation[1].toDouble())
                val roll = Math.toDegrees(orientation[2].toDouble())

                rollText.text = "Roll: %.1f°".format(roll)
                pitchText.text = "Pitch: %.1f°".format(pitch)
                yawText.text = "Yaw: %.1f°".format(yaw)

                val maxVerschiebung = wasserwaage.width - blase.width
                val prozent = (roll / 90.0).coerceIn(-1.0, 1.0)
                blase.translationX = (prozent * maxVerschiebung / 2).toFloat()

            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
