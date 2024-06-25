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

class MyPaperView(
    context: Context,
    attrs: AttributeSet
) :
    View(context, attrs) {
    private val dotColor = Color.BLUE
    private val lineColor = Color.GREEN
    private val bezierColor = Color.CYAN

    // defines paint and canvas
    private var path: Path? = null
    private var _knots: List<PointF>? = null
    private var mListener: PaperEvent? = null


    fun setKnots(knots: List<PointF>) {
        _knots = knots
        invalidate()
    }

    init {
        isFocusable = true
        isFocusableInTouchMode = true
        path = Path()
    }

    fun setListener(listener: PaperEvent) {
        mListener = listener
    }

    override fun onDraw(canvas: Canvas) {
        drawKnots(canvas)
        drawLine(canvas)
        drawCubic(canvas)
    }

    /*
     * Draw touch points
     */
    private fun drawKnots(canvas: Canvas) {
        Paint().let { paint ->
            paint.style = Paint.Style.FILL
            paint.color = dotColor
            _knots?.apply {
                for (p in _knots!!) {
                    // highlight point
                    canvas.drawCircle(p.x, p.y, 5f, paint)
                }
            }
        }
    }

    /*
     * Draw tangent lines
     */
    private fun drawLine(canvas: Canvas) {
        Paint().let { paint ->
            paint.isAntiAlias = true
            paint.strokeWidth = 3f
            paint.style = Paint.Style.STROKE
            paint.strokeJoin = Paint.Join.ROUND
            paint.strokeCap = Paint.Cap.ROUND
            paint.color = lineColor

            val size = _knots?.size ?: 0
            if (size > 1) {
                val path = Path()
                for (i in 0..size - 2) {
                    val p = _knots?.get(i)
                    val pp = _knots?.get(i + 1)
                    if (p != null && pp != null) {
                        path.moveTo(p.x, p.y)
                        path.lineTo(pp.x, pp.y)
                    }
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

            val size = _knots?.size ?: 0
            if (size > 2) {
                val path = Path()
                val conPoint1 = ArrayList<PointF>()
                val conPoint2 = ArrayList<PointF>()
                for (i in 1 until size) {
                    val prev = _knots?.get(i - 1)
                    val p = _knots?.get(i)
                    if (p != null && prev != null) {
                        conPoint1.add(PointF((p.x + prev.x) / 2, prev.y))
                        conPoint2.add(PointF((p.x + prev.x) / 2, p.y))
                    }
                }
                val first = _knots?.get(0)
                first?.apply {
                    path.reset()
                    path.moveTo(first.x, first.y)

                    for (i in 1..<size) {
                        val p = _knots?.get(i)
                        if (p != null) {
                            path.cubicTo(
                                conPoint1[i - 1].x, conPoint1[i - 1].y,
                                conPoint2[i - 1].x, conPoint2[i - 1].y,
                                p.x, p.y
                            )
                        }
                    }
                    canvas.drawPath(path, paint)
                    // canvas.drawTextOnPath("Over the hill; The best of both worlds; You can’t judge a book by its cover; ", path, 1F, -10F, paint)
                }
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //height = View.MeasureSpec.getSize(heightMeasureSpec)
        //width = View.MeasureSpec.getSize(widthMeasureSpec)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    fun clear() {
        _knots = null
        path = Path()
        postInvalidate() // Indicate view should be redrawn
    }

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
                mListener!!.onActionUp(point)
            }

            else -> return false
        }// Starts a new line in the path

        return true
    }
}