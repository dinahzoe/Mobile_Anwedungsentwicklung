package com.example.aufgabe3

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val rootLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setBackgroundColor(Color.parseColor("#FAFAFA"))
            setPadding(24)
        }

        val title = TextView(this).apply {
            text = "Temperaturumrechner"
            textSize = 28f
            setTextColor(Color.BLACK)
            setPadding(0, 0, 0, 32)
            setTypeface(null, android.graphics.Typeface.BOLD)
        }
        rootLayout.addView(title)

        val inputLayout = LinearLayout(this).apply {
            setPadding(12)
            setBackgroundColor(Color.WHITE)
            setMargins(0, 0, 0, 32)
        }

        val inputValue = EditText(this).apply {
            hint = "Wert hier bitte eingeben"
            textSize = 20f
            setTypeface(null, android.graphics.Typeface.BOLD)
            setPadding(12)
        }

        inputLayout.addView(inputValue)
        rootLayout.addView(inputLayout)

        val btnCtoF = Button(this).apply {
            text = "Celsius → Fahrenheit"
            setBackgroundColor(Color.parseColor("#FF9800"))
            setTextColor(Color.WHITE)
            textSize = 18f
            setPadding(0,16,0,16)
        }
        val btnFtoC = Button(this).apply {
            text = "Fahrenheit → Celsius"
            setBackgroundColor(Color.parseColor("#03A9F4"))
            setTextColor(Color.WHITE)
            textSize = 18f
            setPadding(0,16,0,16)
        }

        rootLayout.addView(btnCtoF)
        rootLayout.addView(btnFtoC)

        val spacer = View(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                100
            )
        }
        rootLayout.addView(spacer)

        val resultLayout = LinearLayout(this).apply {
            setBackgroundColor(Color.parseColor("#E0E0E0"))
            gravity = Gravity.CENTER
            orientation = LinearLayout.VERTICAL
            setPadding(20)
        }

        val result = TextView(this).apply {
            text = "Umgewandelter Wert"
            textSize = 22f
            setTextColor(Color.BLACK)
        }

        resultLayout.addView(result)
        rootLayout.addView(resultLayout)

        setContentView(rootLayout)

        btnCtoF.setOnClickListener {
            val input = inputValue.text.toString().trim()
            if (input.isEmpty()) {
                Toast.makeText(this, "Bitte geben Sie einen Wert ein", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            try {
                val c = input.toDouble()
                val f = c * 9 / 5 + 32
                result.text = "Ergebnis: $f °F"
            } catch (e: NumberFormatException) {
                Toast.makeText(this, "Ungültiger Wert", Toast.LENGTH_SHORT).show()
            }
        }

        btnFtoC.setOnClickListener {
            val input = inputValue.text.toString().trim()
            if (input.isEmpty()) {
                Toast.makeText(this, "Bitte geben Sie einen Wert ein", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            try {
                val f = input.toDouble()
                val c = (f - 32) * 5 / 9
                result.text = "Ergebnis: $c °C"
            } catch (e: NumberFormatException) {
                Toast.makeText(this, "Ungültiger Wert", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun LinearLayout.setMargins(left: Int, top: Int, right: Int, bottom: Int) {
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(left, top, right, bottom)
        layoutParams = params
    }
}
