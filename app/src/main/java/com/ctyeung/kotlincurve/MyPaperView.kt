package com.ctyeung.kotlincurve

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class MyPaperView(
    context: Context,
    attrs: AttributeSet
) :
    View(context, attrs) {
    val dotColor = Color.BLUE
    val lineColor = Color.GREEN
    val BezierColor = Color.CYAN

    // defines paint and canvas
    var drawPaint: Paint? = null
    var path: Path? = null
    var _knots: List<MyPoint>? = null
    var mListener: PaperEvent? = null


    fun setKnots(knots: List<MyPoint>) {
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
        val dotPaint = Paint()
        dotPaint.style = Paint.Style.FILL
        dotPaint.color = dotColor
        _knots?.apply {
            for (p in _knots!!) {
                // highlight point
                canvas.drawCircle(p.x.toFloat(), p.y.toFloat(), 5f, dotPaint)
            }
        }
    }

    /*
     * Draw tangent lines
     */
    private fun drawLine(canvas: Canvas) {
        drawPaint = Paint()
        drawPaint!!.isAntiAlias = true
        drawPaint!!.strokeWidth = 3f
        drawPaint!!.style = Paint.Style.STROKE
        drawPaint!!.strokeJoin = Paint.Join.ROUND
        drawPaint!!.strokeCap = Paint.Cap.ROUND
        drawPaint!!.color = lineColor

        val size = _knots?.size ?: 0
        if (size > 1) {
            val path = Path()
            for (i in 0..size - 2) {
                val p = _knots?.get(i)
                val pp = _knots?.get(i + 1)
                if (p != null && pp != null) {
                    path.moveTo(p.x.toFloat(), p.y.toFloat())
                    path.lineTo(pp.x.toFloat(), pp.y.toFloat())
                }
            }
            canvas.drawPath(path, drawPaint!!)
        }
    }

    /*
     * https://proandroiddev.com/drawing-bezier-curve-like-in-google-material-rally-e2b38053038c
     */
    private fun drawCubic(canvas: Canvas) {
        drawPaint = Paint()
        drawPaint!!.isAntiAlias = true
        drawPaint!!.strokeWidth = 3f
        drawPaint!!.style = Paint.Style.STROKE
        drawPaint!!.strokeJoin = Paint.Join.ROUND
        drawPaint!!.strokeCap = Paint.Cap.ROUND
        drawPaint!!.color = BezierColor

        val size = _knots?.size ?: 0
        if (size > 2) {
            val path = Path()
            val conPoint1 = ArrayList<MyPoint>()
            val conPoint2 = ArrayList<MyPoint>()
            for(i in 1 until size){
                val prev = _knots?.get(i-1)
                val p = _knots?.get(i)
                if(p!=null && prev!=null) {
                    conPoint1.add(MyPoint((p.x + prev.x) / 2, prev.y))
                    conPoint2.add(MyPoint((p.x + prev.x) / 2, p.y))
                }
            }
            val first = _knots?.get(0)
            first?.apply {
                path.reset()
                path.moveTo(first.x.toFloat(), first.y.toFloat())

                for (i in 1..size-1) {
                    val p = _knots?.get(i)
                    if(p != null) {
                        path.cubicTo(
                            conPoint1[i - 1].x.toFloat(), conPoint1[i - 1].y.toFloat(),
                            conPoint2[i - 1].x.toFloat(), conPoint2[i - 1].y.toFloat(),
                            p.x.toFloat(), p.y.toFloat()
                        )
                    }
                }
                canvas.drawPath(path, drawPaint!!)
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
        val point = MyPoint(event.x.toDouble(), event.y.toDouble())

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