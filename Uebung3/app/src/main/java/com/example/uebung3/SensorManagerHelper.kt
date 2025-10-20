package com.example.uebung3

import android.content.Context
import android.content.SharedPreferences

class PersistentStorage(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("hoehenmesser_prefs", Context.MODE_PRIVATE)

    fun saveReferenceAltitude(value: Float) {
        prefs.edit().putFloat("reference_altitude", value).apply()
    }

    fun loadReferenceAltitude(): Float {
        return prefs.getFloat("reference_altitude", -1f)
    }
}
