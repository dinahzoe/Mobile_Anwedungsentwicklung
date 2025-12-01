package com.example.uebung3

import android.content.Context
import android.util.Log
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*
import android.content.ContentValues
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.*
import java.io.OutputStream

class CSVWriterHelper(private val context: Context) {
    private val fileName = "gps_tracking.csv"

    fun writeData(latitude: Double, longitude: Double, altitude: Double) {
        val csvFile = File(context.filesDir, fileName)
        try {
            FileWriter(csvFile, true).use { writer ->
                val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                writer.append("$timestamp,$latitude,$longitude,$altitude\n")
                Log.d("CSV_DEBUG", "Schreibe CSV: $latitude, $longitude, $altitude")
                writer.flush()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun writeCSV(latitude: Double, longitude: Double, altitude: Double) {
        val csvContent = "${System.currentTimeMillis()};$latitude;$longitude;$altitude\n"
        val fileName = "gps_log.csv"

        val resolver = context.contentResolver

        // Funktioniert auf API 24–34
        val filesUri = MediaStore.Files.getContentUri("external")

        // Datei im MediaStore suchen
        val existingUri = resolver.query(
            filesUri,
            arrayOf(MediaStore.MediaColumns._ID, MediaStore.MediaColumns.RELATIVE_PATH),
            "${MediaStore.MediaColumns.DISPLAY_NAME}=?",
            arrayOf(fileName),
            null
        )?.use { cursor ->
            while (cursor.moveToNext()) {
                val path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.RELATIVE_PATH))
                if (path == Environment.DIRECTORY_DOWNLOADS + "/") {
                    val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                    return@use filesUri.buildUpon().appendPath(id.toString()).build()
                }
            }
            null
        }

        val uri = existingUri ?: run {
            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "text/csv")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }
            resolver.insert(filesUri, values)
        }

        uri?.let {
            resolver.openOutputStream(it, "wa")?.use { os ->
                os.write(csvContent.toByteArray())
            }
        }
    }
}

