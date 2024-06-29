package com.ctyeung.kotlincurve.data

import android.graphics.PointF
import android.util.Log

class BezierQuad {

    private var knots = arrayListOf<PointF>()
    fun clear() {
        knots.clear()
    }

    private fun getNumKnots(): Int {
        return knots.size
    }

    fun insert(p: PointF) {
        when (getNumKnots()) {
            0 -> {
                knots.add(p)
            }

            1 -> {
                if (p.x < knots[0].x) {
                    knots.add(0, p)
                } else {
                    knots.add(p)
                }
            }

            else -> {
                val index = bisection(p.x) + 1
                knots.add(index, p)
            }
        }
    }

    /*
 * bisection search to locate x-axis values for input
 * - intended as a private method
 */
    private fun bisection(ab: Float): Int {                                               // x-axis value
        var ju = knots.size                                                // upper limit
        var jl = 0                                                                // lower limit
        var jm: Int?                                                            // midpoint

        while (ju - jl > 1) {
            jm = (ju + jl) / 2                                    // midpoint formula

            if (ab > knots[jm].x)
                jl = jm
            else
                ju = jm
        }
        return jl
    }

    private fun requant(i: Int): Int {
        return when {
            i < 1 -> 1
            i > knots.size - 2 -> knots.size - 2
            else -> i
        }
    }

    fun getPoints(): ArrayList<PointF> {
        val numKnots = getNumKnots()
        return when (numKnots) {
            0,
            1,
            2 -> knots

            else -> interpolateAll()
        }
    }

    /**
     * Assumption: 3 or more points
     */
    private fun interpolateAll(): ArrayList<PointF> {
        val listPoints = arrayListOf(knots[0])
        val start = knots[0].x.toInt() + 1
        val end = knots.last().x.toInt() - 1

        /* calculate all points */
        for (i in start..end) {

            /* find nearest knot */
            val index = requant(bisection(i.toFloat()))
            val p0 = knots[index - 1]

            /* calculate control point */
            val p1 = calculateControlPoint(index)
            val p2 = knots[index + 1]

            val y = doBezier(i.toFloat(), p0, p1, p2)
            listPoints.add(PointF(i.toFloat(), y))
        }
        return listPoints
    }

    /**
     * Calculate point on bezier curve
     */
    private fun doBezier(x: Float, p0: PointF, p1: PointF, p2: PointF): Float {
        /* x: 0 to 1 range */
        val t = (x - p0.x) / (p2.x - p0.x)
        // val x = (1 - t) * (1 - t) * knots[0].x + 2 * (1 - t) * t * knots[1].x + t * t * knots[2].x
        val y = (1 - t) * (1 - t) * p0.y + 2 * (1 - t) * t * p1.y + t * t * p2.y
        return y
    }

    /**
     * Calculate Control Point from knots i-1, i, i+1
     * So line will go through all knots
     */
    private fun calculateControlPoint(index: Int): PointF {
        /* index-1, index, index+1 must exist */
        val t = (knots[index].x - knots[index - 1].x) / (knots[index + 1].x - knots[index - 1].x)
        // val x = (1 - t) * (1 - t) * knots[0].x + 2 * (1 - t) * t * knots[1].x + t * t * knots[2].x
        //val y = (1 - t) * (1 - t) * knots[index-1].y + 2 * (1 - t) * t * knots[index].y + t * t * knots[index+1].y
        val controlY = (knots[index].y - ((1 - t) * (1 - t) * knots[index-1].y) - (t * t * knots[index+1].y)) / (2 * (1 - t) * t)
        return PointF(knots[index].x, controlY)
    }
}