package com.example.uebung3

import kotlin.math.pow

object AltitudeCalculator {
    private const val P0 = 1013.25f

    fun calculateAltitudeFromPressure(pressure: Float, referencePressure: Float? = null): Double {
        val p = referencePressure ?: pressure
        return 44330.0 * (1 - (p / P0).toDouble().pow(1 / 5.255))
    }
}
