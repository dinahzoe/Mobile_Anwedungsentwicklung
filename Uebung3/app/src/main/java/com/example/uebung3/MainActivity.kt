package com.example.uebung3

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions

class MainActivity : AppCompatActivity(), LocationListener, SensorEventListener, OnMapReadyCallback {

    private lateinit var textLatLng: TextView
    private lateinit var textAltitude: TextView
    private lateinit var textSpeed: TextView
    private lateinit var btnSetReference: Button
    private lateinit var mapView: MapView

    private lateinit var locationManager: LocationManager
    private lateinit var sensorManager: SensorManager
    private var pressureSensor: Sensor? = null
    private var lastPressure: Float? = null

    private lateinit var storage: PersistentStorage
    private var referenceAltitude: Float? = null

    private lateinit var csvWriter: CSVWriterHelper

    private var googleMap: GoogleMap? = null
    private val routePoints = mutableListOf<LatLng>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textLatLng = findViewById(R.id.textLatLng)
        textAltitude = findViewById(R.id.textAltitude)
        textSpeed = findViewById(R.id.textSpeed)
        btnSetReference = findViewById(R.id.btnSetReference)
        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        storage = PersistentStorage(this)
        csvWriter = CSVWriterHelper(this)
        referenceAltitude = storage.loadReferenceAltitude()

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        } else {
            startGPS()
        }

        btnSetReference.setOnClickListener {
            lastPressure?.let {
                val alt = AltitudeCalculator.calculateAltitudeFromPressure(it)
                referenceAltitude = alt.toFloat()
                storage.saveReferenceAltitude(referenceAltitude!!)
                Toast.makeText(this, "Referenzhöhe gespeichert: $alt m", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startGPS() {
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0f, this)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
        pressureSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onLocationChanged(location: Location) {
        val latitude = location.latitude
        val longitude = location.longitude
        val speedKmh = location.speed * 3.6
        val altitude = lastPressure?.let { AltitudeCalculator.calculateAltitudeFromPressure(it, referenceAltitude) } ?: 0.0

        // UI
        textLatLng.text = "Breite: $latitude\nLänge: $longitude"
        textAltitude.text = "Höhe: %.1f m".format(altitude)
        textSpeed.text = "Geschwindigkeit: %.2f km/h".format(speedKmh)

        // CSV speichern
        csvWriter.writeData(latitude, longitude, altitude)

        // Route auf der Map zeichnen
        val latLng = LatLng(latitude, longitude)
        routePoints.add(latLng)
        googleMap?.clear()
        googleMap?.addPolyline(PolylineOptions().addAll(routePoints).width(5f).color(0xFF0000FF.toInt()))
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_PRESSURE) {
            lastPressure = event.values[0]
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap?.uiSettings?.isZoomControlsEnabled = true
    }
}
