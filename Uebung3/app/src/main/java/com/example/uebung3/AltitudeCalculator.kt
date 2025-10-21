package com.example.uebung3

import kotlin.math.pow

object AltitudeCalculator {
    private const val P0 = 1013.25 // hPa sea level standard

    fun calculateAltitude(pressure: Float, referencePressure: Float? = null): Double {
        val p0 = referencePressure ?: P0.toFloat()
        val exponent = 1.0 / 5.255
        return 44330.0 * (1.0 - (pressure.toDouble() / p0.toDouble()).pow(exponent))
    }
}

