package com.example.aufgabe1;

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity(), LocationListener {
    private var textLatLng: TextView? = null
    private var textAltitude: TextView? = null
    private var textSpeed: TextView? = null
    private var locationManager: LocationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textLatLng = findViewById<TextView?>(R.id.textLatLng)
        textAltitude = findViewById<TextView?>(R.id.textAltitude)
        textSpeed = findViewById<TextView?>(R.id.textSpeed)

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        // Prüfen, ob Berechtigung vorhanden ist
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            startGPS()
        }
    }

    private fun startGPS() {
        try {
            locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0f, this)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    override fun onLocationChanged(location: Location) {
        val latitude = location.getLatitude()
        val longitude = location.getLongitude()
        val altitude = location.getAltitude()
        val speed = location.getSpeed() // m/s

        textLatLng!!.setText("Breite: " + latitude + "\nLänge: " + longitude)
        textAltitude!!.setText("Höhe: " + altitude + " m")
        textSpeed!!.setText("Geschwindigkeit: " + String.format("%.2f km/h", speed * 3.6))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            startGPS()
        }
    }


    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}