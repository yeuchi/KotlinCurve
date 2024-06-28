package com.ctyeung.kotlincurve.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.ctyeung.kotlincurve.data.BezierQuad
import com.ctyeung.kotlincurve.data.CubicSpline
import com.ctyeung.kotlincurve.data.Median

class MyPaperView(
    context: Context,
    attrs: AttributeSet
) :
    View(context, attrs) {
    private val dotColor = Color.BLUE
    private val lineColor = Color.GREEN
    private val bezierColor = Color.CYAN

    // defines paint and canvas
    private var _knots = arrayListOf<PointF>()

    private val cubicSpline = CubicSpline()
    private val bezierQuad = BezierQuad()
    var interpolation = InterpolationEnum.CubicSpline

    init {
        isFocusable = true
        isFocusableInTouchMode = true
    }

    override fun onDraw(canvas: Canvas) {
        drawKnots(canvas)

        when (interpolation) {
            InterpolationEnum.MedianFilter -> {
                val filtered = Median.filter(_knots)
                drawLine(canvas, filtered)
            }

            InterpolationEnum.Linear -> drawLine(canvas, _knots)

            InterpolationEnum.CubicSpline -> {
                cubicSpline.let { spline ->
                    spline.clear()
                    _knots.forEach {
                        spline.insert(it)
                    }
                    spline.formulate()
                    val points = spline.getPoints()
                    drawLine(canvas, points)
                }
            }

            InterpolationEnum.BezierQuad -> {
                bezierQuad.let { bezier ->
                    bezier.clear()

                    _knots.forEach {
                        bezier.insert(it)
                    }
                    val points = bezier.getPoints()
                    drawLine(canvas, points)
                }
            }

            InterpolationEnum.BezierCubic -> drawCubic(canvas)
        }
    }

    /**
     * Draw Circle for touch points
     */
    private fun drawKnots(canvas: Canvas) {
        Paint().let { paint ->
            paint.style = Paint.Style.FILL
            paint.color = dotColor
            _knots.apply {
                for (p in _knots) {
                    // highlight point
                    canvas.drawCircle(p.x, p.y, 15f, paint)
                }
            }
        }
    }

    private fun drawLine(canvas: Canvas, points: List<PointF>) {
        Paint().let { paint ->
            paint.isAntiAlias = true
            paint.strokeWidth = 3f
            paint.style = Paint.Style.STROKE
            paint.strokeJoin = Paint.Join.ROUND
            paint.strokeCap = Paint.Cap.ROUND
            paint.color = lineColor

            val size = points.size
            if (size > 1) {
                val path = Path()
                for (i in 0..size - 2) {
                    val p = points[i]
                    val pp = points[i + 1]
                    path.moveTo(p.x, p.y)
                    path.lineTo(pp.x, pp.y)
                }
                canvas.drawPath(path, paint)
            }
        }
    }

    /*
     * https://proandroiddev.com/drawing-bezier-curve-like-in-google-material-rally-e2b38053038c
     */
    private fun drawCubic(canvas: Canvas) {
        Paint().let { paint ->
            paint.isAntiAlias = true
            paint.strokeWidth = 3f
            paint.style = Paint.Style.STROKE
            paint.strokeJoin = Paint.Join.ROUND
            paint.strokeCap = Paint.Cap.ROUND
            paint.color = bezierColor
            paint.textSize = 65f

            val size = _knots.size
            if (size > 2) {
                val path = Path()
                val conPoint1 = ArrayList<PointF>()
                val conPoint2 = ArrayList<PointF>()
                for (i in 1 until size) {
                    val prev = _knots[i - 1]
                    val p = _knots[i]
                    conPoint1.add(PointF((p.x + prev.x) / 2, prev.y))
                    conPoint2.add(PointF((p.x + prev.x) / 2, p.y))
                }
                val first = _knots[0]
                first.apply {
                    path.reset()
                    path.moveTo(first.x, first.y)

                    for (i in 1..<size) {
                        val p = _knots[i]
                        path.cubicTo(
                            conPoint1[i - 1].x, conPoint1[i - 1].y,
                            conPoint2[i - 1].x, conPoint2[i - 1].y,
                            p.x, p.y
                        )
                    }
                    canvas.drawPath(path, paint)
                    // canvas.drawTextOnPath("Over the hill; The best of both worlds; You can’t judge a book by its cover; ", path, 1F, -10F, paint)
                }
            }
            else {
                drawLine(canvas, _knots)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //height = View.MeasureSpec.getSize(heightMeasureSpec)
        //width = View.MeasureSpec.getSize(widthMeasureSpec)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    fun clear() {
        _knots.clear()
        postInvalidate() // Indicate view should be redrawn
    }

    /**
     * Touch Event
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val point = PointF(event.x, event.y)

        // Checks for the event that occurs
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
            }

            /*case MotionEvent.ACTION_MOVE:
                path.lineTo(point.x, point.y);
                break;*/

            MotionEvent.ACTION_UP -> {
                _knots.add(point)
                _knots.sortBy { it.x }
                invalidate()
            }

            else -> return false
        }// Starts a new line in the path

        return true
    }
}

enum class InterpolationEnum {
    MedianFilter,
    Linear,
    BezierQuad,
    BezierCubic,
    CubicSpline
}