package com.example.uebung3

import android.content.Context
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

data class GPXTrackPoint(
    val latitude: Double,
    val longitude: Double,
    val altitude: Double,
    val timestamp: String = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }.format(Date())
)

class GPXWriter(private val context: Context) {

    fun exportToGPX(trackPoints: List<GPXTrackPoint>, filename: String = "gps_track.gpx"): File {
        val gpxFile = File(context.getExternalFilesDir(null), filename)

        val gpxContent = buildGPXContent(trackPoints)
        gpxFile.writeText(gpxContent)

        return gpxFile
    }

    private fun buildGPXContent(trackPoints: List<GPXTrackPoint>): String {
        val sb = StringBuilder()

        // GPX Header
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
        sb.append("<gpx version=\"1.1\" creator=\"GPS Tracker App\" ")
        sb.append("xmlns=\"http://www.topografix.com/GPX/1/1\" ")
        sb.append("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ")
        sb.append("xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 ")
        sb.append("http://www.topografix.com/GPX/1/1/gpx.xsd\">\n")

        // Metadata
        sb.append("  <metadata>\n")
        sb.append("    <name>GPS Track</name>\n")
        sb.append("    <desc>Tracked route from GPS Tracker App</desc>\n")
        sb.append("    <time>${getCurrentTimestamp()}</time>\n")
        sb.append("  </metadata>\n")

        // Track
        sb.append("  <trk>\n")
        sb.append("    <name>Track 1</name>\n")
        sb.append("    <trkseg>\n")

        // Track Points
        for (point in trackPoints) {
            sb.append("      <trkpt lat=\"${point.latitude}\" lon=\"${point.longitude}\">\n")
            sb.append("        <ele>${point.altitude}</ele>\n")
            sb.append("        <time>${point.timestamp}</time>\n")
            sb.append("      </trkpt>\n")
        }

        sb.append("    </trkseg>\n")
        sb.append("  </trk>\n")
        sb.append("</gpx>")

        return sb.toString()
    }

    fun exportFromCSV(csvFilename: String = "gps_log.csv"): File? {
        val csvFile = File(context.filesDir, csvFilename)
        if (!csvFile.exists()) {
            return null
        }

        val trackPoints = mutableListOf<GPXTrackPoint>()

        csvFile.forEachLine { line ->
            val parts = line.split(";")
            if (parts.size >= 4) {
                try {
                    val timestamp = parts[0]
                    val lat = parts[1].toDouble()
                    val lon = parts[2].toDouble()
                    val alt = parts[3].toDouble()

                    trackPoints.add(GPXTrackPoint(lat, lon, alt, formatTimestamp(timestamp)))
                } catch (e: Exception) {
                    // Überspringe fehlerhafte Zeilen
                }
            }
        }

        return if (trackPoints.isNotEmpty()) {
            exportToGPX(trackPoints)
        } else {
            null
        }
    }

    private fun getCurrentTimestamp(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.format(Date())
    }

    private fun formatTimestamp(timestamp: String): String {
        // Konvertiere das Zeitformat aus der CSV in ISO 8601
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
            val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
            outputFormat.timeZone = TimeZone.getTimeZone("UTC")
            val date = inputFormat.parse(timestamp)
            outputFormat.format(date ?: Date())
        } catch (e: Exception) {
            getCurrentTimestamp()
        }
    }
}