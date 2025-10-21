package com.example.uebung3

import android.content.Context
import android.content.SharedPreferences

class PersistentStorage(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("hoehenmesser_prefs", Context.MODE_PRIVATE)

    fun saveReferencePressure(value: Float) {
        prefs.edit().putFloat("reference_pressure", value).apply()
    }

    fun loadReferencePressure(): Float? {
        return if (prefs.contains("reference_pressure")) {
            prefs.getFloat("reference_pressure", -1f).takeIf { it >= 0f }
        } else {
            null
        }
    }
}

