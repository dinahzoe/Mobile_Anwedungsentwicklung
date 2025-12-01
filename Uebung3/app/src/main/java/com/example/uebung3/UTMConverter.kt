package com.example.uebung3
import kotlin.math.*

data class UTMPoint(val x: Double, val y: Double)

class UTMConverter {

    // WGS84 Parameter
    private val a = 6378137.0  // Äquatorradius in Metern
    private val f = 1.0 / 298.257223563  // Abplattung
    private val e = sqrt(2 * f - f * f)  // erste numerische Exzentrizität
    private val e2 = e * e

    // UTM Parameter
    private val k0 = 0.9996  // Maßstabsfaktor
    private val E0 = 500000.0  // False Easting
    private val N0 = 0.0  // False Northing für nördliche Hemisphäre

    fun toUTM(lat: Double, lon: Double): UTMPoint {
        // Konvertierung zu Radiant
        val latRad = lat * PI / 180.0
        val lonRad = lon * PI / 180.0

        // UTM Zone berechnen
        val zone = floor((lon + 180.0) / 6.0).toInt() + 1

        // Zentralmeridian der Zone
        val lon0 = ((zone - 1) * 6.0 - 180.0 + 3.0) * PI / 180.0

        val N = a / sqrt(1 - e2 * sin(latRad).pow(2))
        val T = tan(latRad).pow(2)
        val C = e2 * cos(latRad).pow(2) / (1 - e2)
        val A = (lonRad - lon0) * cos(latRad)

        val M = a * (
                (1 - e2/4 - 3*e2*e2/64 - 5*e2*e2*e2/256) * latRad -
                        (3*e2/8 + 3*e2*e2/32 + 45*e2*e2*e2/1024) * sin(2*latRad) +
                        (15*e2*e2/256 + 45*e2*e2*e2/1024) * sin(4*latRad) -
                        (35*e2*e2*e2/3072) * sin(6*latRad)
                )

        val x = E0 + k0 * N * (
                A + (1 - T + C) * A.pow(3) / 6 +
                        (5 - 18*T + T*T + 72*C - 58*e2/(1-e2)) * A.pow(5) / 120
                )

        val y = N0 + k0 * (
                M + N * tan(latRad) * (
                        A*A/2 + (5 - T + 9*C + 4*C*C) * A.pow(4) / 24 +
                                (61 - 58*T + T*T + 600*C - 330*e2/(1-e2)) * A.pow(6) / 720
                        )
                )

        return UTMPoint(x, y)
    }
}
