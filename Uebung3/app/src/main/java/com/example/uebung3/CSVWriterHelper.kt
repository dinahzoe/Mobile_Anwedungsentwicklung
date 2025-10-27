package com.example.uebung3

import android.content.Context
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

class CSVWriterHelper(private val context: Context) {
    private val fileName = "gps_tracking.csv"

    fun writeData(latitude: Double, longitude: Double, altitude: Double) {
        val csvFile = File(context.filesDir, fileName)
        try {
            FileWriter(csvFile, true).use { writer ->
                val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                writer.append("$timestamp,$latitude,$longitude,$altitude\n")
                writer.flush()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

