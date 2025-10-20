package com.example.uebung3und4

import android.app.Activity
import android.os.Bundle
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.widget.LinearLayout
import android.widget.TextView
import android.view.Gravity
import android.view.ViewGroup.LayoutParams

class MainActivity : Activity(), SensorEventListener {

    private lateinit var sensorVerwaltung: SensorManager
    private var beschleunigung: Sensor? = null
    private var magnetFeld: Sensor? = null

    private lateinit var rollAnzeige: TextView
    private lateinit var neigungAnzeige: TextView
    private lateinit var drehungAnzeige: TextView

    private var gDaten = FloatArray(3)
    private var mDaten = FloatArray(3)
    private var gVorhanden = false
    private var mVorhanden = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Layout direkt im Code erstellen (kein XML notwendig)
        val anzeigeLayout = LinearLayout(this)
        anzeigeLayout.orientation = LinearLayout.VERTICAL
        anzeigeLayout.gravity = Gravity.CENTER
        anzeigeLayout.setPadding(50, 50, 50, 50)

        rollAnzeige = TextView(this)
        neigungAnzeige = TextView(this)
        drehungAnzeige = TextView(this)

        rollAnzeige.textSize = 24f
        neigungAnzeige.textSize = 24f
        drehungAnzeige.textSize = 24f

        anzeigeLayout.addView(rollAnzeige, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
        anzeigeLayout.addView(neigungAnzeige, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
        anzeigeLayout.addView(drehungAnzeige, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))

        setContentView(anzeigeLayout)

        // Sensoren holen
        sensorVerwaltung = getSystemService(SENSOR_SERVICE) as SensorManager
        beschleunigung = sensorVerwaltung.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        magnetFeld = sensorVerwaltung.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    }

    override fun onResume() {
        super.onResume()
        beschleunigung?.let {
            sensorVerwaltung.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
        magnetFeld?.let {
            sensorVerwaltung.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorVerwaltung.unregisterListener(this)
    }

    override fun onSensorChanged(ereignis: SensorEvent) {
        when (ereignis.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                gDaten = ereignis.values.clone()
                gVorhanden = true
            }
            Sensor.TYPE_MAGNETIC_FIELD -> {
                mDaten = ereignis.values.clone()
                mVorhanden = true
            }
        }

        if (gVorhanden && mVorhanden) {
            val rotMatrix = FloatArray(9)
            val inklMatrix = FloatArray(9)
            val erfolgreich = SensorManager.getRotationMatrix(rotMatrix, inklMatrix, gDaten, mDaten)

            if (erfolgreich) {
                val lageWinkel = FloatArray(3)
                SensorManager.getOrientation(rotMatrix, lageWinkel)

                val drehWinkel = Math.toDegrees(lageWinkel[0].toDouble())  // Yaw
                val neigungsWinkel = Math.toDegrees(lageWinkel[1].toDouble()) // Pitch
                val rollWinkel = Math.toDegrees(lageWinkel[2].toDouble()) // Roll

                rollAnzeige.text = "Roll: %.1f°".format(rollWinkel)
                neigungAnzeige.text = "Neigung: %.1f°".format(neigungsWinkel)
                drehungAnzeige.text = "Drehung: %.1f°".format(drehWinkel)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, genauigkeit: Int) {
        // Genauigkeitsänderung wird hier nicht benötigt
    }
}
