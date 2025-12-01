package com.example.uebung3

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View


class GraphView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val gridPaint = Paint().apply {
        color = Color.LTGRAY
        strokeWidth = 2f
    }

    private val linePaint = Paint().apply {
        color = Color.RED
        strokeWidth = 5f
        isAntiAlias = true
    }

    private val pointPaint = Paint().apply {
        color = Color.BLUE
        strokeWidth = 8f
        style = Paint.Style.FILL
        isAntiAlias = true
    }
    private val textPaint = Paint().apply {
        color = Color.DKGRAY
        textSize = 30f
        isAntiAlias = true
    }

    private var trackPoints: List<UTMPoint> = emptyList()

    fun setTrack(points: List<UTMPoint>) {
        trackPoints = points
        invalidate()  // View neu zeichnen
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (trackPoints.isEmpty()) {
            // Zeige Hinweistext wenn keine Daten vorhanden
            canvas.drawText(
                "Warte auf GPS-Daten...",
                width / 2f - 150f,
                height / 2f,
                textPaint
            )
            return
        }

        drawGrid(canvas)
        drawTrack(canvas)
    }

    private fun drawGrid(canvas: Canvas) {
        // Zeichne 5x5 Gitter (Meridiane in UTM)
        val stepX = width / 5f
        val stepY = height / 5f

        for (i in 0..5) {
            // Vertikale Linien
            canvas.drawLine(i * stepX, 0f, i * stepX, height.toFloat(), gridPaint)
            // Horizontale Linien
            canvas.drawLine(0f, i * stepY, width.toFloat(), i * stepY, gridPaint)
        }
    }

    private fun drawTrack(canvas: Canvas) {
        if (trackPoints.size < 2) {
            // Wenn nur ein Punkt vorhanden ist, zeichne nur den Punkt
            if (trackPoints.size == 1) {
                canvas.drawCircle(width / 2f, height / 2f, 10f, pointPaint)
            }
            return
        }

        // Finde Minimal- und Maximalwerte für Skalierung
        val minX = trackPoints.minOf { it.x }
        val maxX = trackPoints.maxOf { it.x }
        val minY = trackPoints.minOf { it.y }
        val maxY = trackPoints.maxOf { it.y }

        // Verhindere Division durch Null
        val rangeX = (maxX - minX).takeIf { it > 0 } ?: 1.0
        val rangeY = (maxY - minY).takeIf { it > 0 } ?: 1.0

        // Berechne Skalierung mit Padding
        val padding = 50f
        val scaleX = (width - 2 * padding) / rangeX
        val scaleY = (height - 2 * padding) / rangeY

        // Verwende die kleinere Skalierung für gleichmäßiges Verhältnis
        val scale = minOf(scaleX, scaleY)

        // Zeichne Linien zwischen den Punkten
        for (i in 0 until trackPoints.size - 1) {
            val p1 = trackPoints[i]
            val p2 = trackPoints[i + 1]

            val x1 = (padding + (p1.x - minX) * scale).toFloat()
            val y1 = (height - padding - (p1.y - minY) * scale).toFloat()
            val x2 = (padding + (p2.x - minX) * scale).toFloat()
            val y2 = (height - padding - (p2.y - minY) * scale).toFloat()

            canvas.drawLine(x1, y1, x2, y2, linePaint)
        }

        // Zeichne Punkte
        for (point in trackPoints) {
            val x = (padding + (point.x - minX) * scale).toFloat()
            val y = (height - padding - (point.y - minY) * scale).toFloat()
            canvas.drawCircle(x, y, 6f, pointPaint)
        }
    }
}