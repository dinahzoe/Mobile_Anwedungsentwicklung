package com.example.aufgabe1

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity(), LocationListener {
    private var textLatLng: TextView? = null
    private var textAltitude: TextView? = null
    private var textSpeed: TextView? = null
    private var locationManager: LocationManager? = null
    private var currentProvider: String = LocationManager.GPS_PROVIDER

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textLatLng = findViewById(R.id.textLatLng)
        textAltitude = findViewById(R.id.textAltitude)
        textSpeed = findViewById(R.id.textSpeed)

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        // Prüfen, ob Berechtigung vorhanden ist
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            startLocationUpdates(currentProvider)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    // Hier setzen wir das Häkchen dynamisch vor jedem Anzeigen des Menüs
    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.let {
            for (i in 0 until menu.size()) {
                val item = menu.getItem(i)
                val isActive = (item.title.toString().contains("GPS") && currentProvider == LocationManager.GPS_PROVIDER) ||
                        (item.title.toString().contains("Network") && currentProvider == LocationManager.NETWORK_PROVIDER)

                // Zeige grünes Häkchen hinter dem aktiven Provider
                val baseTitle = if (item.title.toString().contains("✔")) {
                    item.title.toString().substringBefore("✔").trim()
                } else {
                    item.title.toString()
                }
                item.title = if (isActive) "$baseTitle ✔" else baseTitle
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when {
            item.title.toString().contains("GPS") -> {
                switchProvider(LocationManager.GPS_PROVIDER)
                true
            }
            item.title.toString().contains("Network") -> {
                switchProvider(LocationManager.NETWORK_PROVIDER)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun switchProvider(provider: String) {
        // Stoppe aktuelle Location-Updates
        try {
            locationManager?.removeUpdates(this)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }

        // Setze neuen Provider
        currentProvider = provider

        // Prüfe, ob Provider verfügbar ist
        val isEnabled = locationManager?.isProviderEnabled(provider) ?: false

        if (!isEnabled) {
            val providerName = if (provider == LocationManager.GPS_PROVIDER) "GPS" else "Network"
            Toast.makeText(
                this,
                "$providerName Provider ist nicht aktiviert!",
                Toast.LENGTH_LONG
            ).show()
        }

        // Zeige welcher Provider gewählt wurde
        val providerName = if (provider == LocationManager.GPS_PROVIDER) "GPS" else "Network"
        Toast.makeText(this, "$providerName Provider ausgewählt", Toast.LENGTH_SHORT).show()

        // Starte Location-Updates mit neuem Provider
        startLocationUpdates(provider)

        // Menü neu zeichnen, damit das Häkchen aktualisiert wird
        invalidateOptionsMenu()
    }

    private fun startLocationUpdates(provider: String) {
        try {
            // GPS Provider benötigt ACCESS_FINE_LOCATION
            // Network Provider benötigt ACCESS_COARSE_LOCATION (oder FINE)
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
            ) {
                locationManager?.requestLocationUpdates(provider, 1000, 0f, this)

                // Zeige letzte bekannte Position sofort an
                val lastLocation = locationManager?.getLastKnownLocation(provider)
                lastLocation?.let { onLocationChanged(it) }
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
            Toast.makeText(this, "Keine Berechtigung für Standortzugriff", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onLocationChanged(location: Location) {
        val latitude = location.latitude
        val longitude = location.longitude
        val altitude = location.altitude
        val speed = location.speed // m/s

        textLatLng?.text = "Breite: $latitude\nLänge: $longitude"
        textAltitude?.text = "Höhe: $altitude m"
        textSpeed?.text = "Geschwindigkeit: ${String.format("%.2f", speed * 3.6)} km/h"
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
            startLocationUpdates(currentProvider)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            locationManager?.removeUpdates(this)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}
