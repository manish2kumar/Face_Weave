package com.example.faceweave

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.google.mlkit.vision.facemesh.FaceMesh

class FaceMeshOverlay @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private var faceMeshes: List<FaceMesh> = emptyList()

    private val pointPaint = Paint().apply {
        color = Color.GREEN
        style = Paint.Style.FILL
        strokeWidth = 6f
        isAntiAlias = true
    }

    private val linePaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 2f
        isAntiAlias = true
    }

    private var imageWidth: Int = 0
    private var imageHeight: Int = 0

    fun setFaceMeshes(meshes: List<FaceMesh>?, imgWidth: Int, imgHeight: Int) {
        faceMeshes = meshes ?: emptyList()
        imageWidth = imgWidth
        imageHeight = imgHeight
        postInvalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (imageWidth == 0 || imageHeight == 0) return

        val scaleX = width.toFloat() / imageWidth.toFloat()
        val scaleY = height.toFloat() / imageHeight.toFloat()

        faceMeshes.forEach { mesh ->
            // Draw triangle connections
            mesh.allTriangles.forEach { triangle ->
                val points = triangle.allPoints
                if (points.size == 3) {
                    val p1 = points[0].position
                    val p2 = points[1].position
                    val p3 = points[2].position

                    canvas.drawLine(p1.x * scaleX, p1.y * scaleY, p2.x * scaleX, p2.y * scaleY, linePaint)
                    canvas.drawLine(p2.x * scaleX, p2.y * scaleY, p3.x * scaleX, p3.y * scaleY, linePaint)
                    canvas.drawLine(p3.x * scaleX, p3.y * scaleY, p1.x * scaleX, p1.y * scaleY, linePaint)
                }
            }

            // Draw facial mesh points
            mesh.allPoints.forEach { point ->
                canvas.drawCircle(point.position.x * scaleX, point.position.y * scaleY, 3f, pointPaint)
            }
        }
    }
}
