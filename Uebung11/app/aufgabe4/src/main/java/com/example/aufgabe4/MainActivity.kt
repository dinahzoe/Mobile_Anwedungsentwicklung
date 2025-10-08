package com.example.aufgabe4

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding

class MainActivity : AppCompatActivity() {

    private lateinit var display: TextView
    private var current = ""
    private var firstNumber = 0.0
    private var operator = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.parseColor("#0DD400A3"))
            setPadding(24)
            gravity = Gravity.CENTER_HORIZONTAL
        }

        val title = TextView(this).apply {
            text = "Taschenrechner"
            textSize = 28f
            setTextColor(Color.BLACK)
            setPadding(0, 0, 0, 40)
            setTypeface(typeface, android.graphics.Typeface.BOLD)
        }
        root.addView(title)

        display = TextView(this).apply {
            text = "0"
            textSize = 32f
            setTextColor(Color.BLACK)
            setBackgroundColor(Color.parseColor("#E0E0E0"))
            setPadding(24)
            gravity = Gravity.END
        }
        val displayParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            bottomMargin = 40
        }
        root.addView(display, displayParams)

        val grid = GridLayout(this).apply {
            columnCount = 4
            rowCount = 4
        }

        val buttonTexts = arrayOf(
            "7","8","9","/",
            "4","5","6","*",
            "1","2","3","-",
            "0","C","=","+"
        )

        val buttons = mutableListOf<Button>()

        buttonTexts.forEach { text ->
            val btn = Button(this).apply {
                this.text = text
                layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    height = 140
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED,1f)
                    setMargins(8,8,8,8)
                }
            }
            buttons.add(btn)
            grid.addView(btn)
        }

        root.addView(grid)

        setContentView(root)

        val numberButtons = buttons.filter { it.text.toString().matches(Regex("\\d")) }
        numberButtons.forEach { btn ->
            btn.setOnClickListener {
                current += btn.text.toString()
                display.text = current
            }
        }

        buttons.find { it.text == "+" }?.setOnClickListener { setOperator("+") }
        buttons.find { it.text == "-" }?.setOnClickListener { setOperator("-") }
        buttons.find { it.text == "*" }?.setOnClickListener { setOperator("*") }
        buttons.find { it.text == "/" }?.setOnClickListener { setOperator("/") }

        buttons.find { it.text == "C" }?.setOnClickListener { clear() }

        buttons.find { it.text == "=" }?.setOnClickListener { calculate() }
    }

    private fun setOperator(op: String) {
        if (current.isEmpty()) return
        firstNumber = current.toDoubleOrNull() ?: return
        operator = op
        current = ""
    }

    private fun clear() {
        current = ""
        firstNumber = 0.0
        operator = ""
        display.text = "0"
    }

    private fun calculate() {
        if (current.isEmpty() || operator.isEmpty()) return
        val second = current.toDoubleOrNull() ?: return
        val result = when(operator) {
            "+" -> firstNumber + second
            "-" -> firstNumber - second
            "*" -> firstNumber * second
            "/" -> if (second != 0.0) firstNumber / second else {
                display.text = "Error"
                current = ""
                operator = ""
                return
            }
            else -> 0.0
        }

        display.text = if (result == result.toLong().toDouble()) {
            result.toLong().toString()
        } else result.toString()
        current = result.toString()
        operator = ""
    }
}
