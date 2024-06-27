package com.ctyeung.kotlincurve.data

import android.graphics.PointF

class BezierQuad {

    private var knots = arrayListOf<PointF>()
    fun clear() {
        knots.clear()
    }

    private fun getNumKnots(): Int {
        return knots.size
    }

    fun insert (p:PointF) {
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

    fun getPoints(): ArrayList<PointF> {
        val numKnots = getNumKnots()
        return when (numKnots) {
            0,
            1,
            2 -> knots
            else -> interpolateAll()
        }
    }

    fun interpolateY(x: Float): Float {
        val index = bisection(x)
        return if (knots[index].x == x) {
            knots[index].y
        } else {
            doBezier(x, index)
        }
    }

    /**
     * https://stackoverflow.com/questions/5634460/quadratic-b%C3%A9zier-curve-calculate-points
     *
     * requirement: 3 knots
     */
    fun doBezier(x:Float, index:Int):Float {
        /* x: 0 to 1 range */
        val t = (x - knots[0].x) / (knots.last().x - knots[0].x)
        val x = (1 - t) * (1 - t) * knots[0].x + 2 * (1 - t) * t * knots[1].x + t * t * knots[2].x;
        val y = (1 - t) * (1 - t) * knots[0].y + 2 * (1 - t) * t * knots[1].y + t * t * knots[2].y;
        return y
    }

    private fun interpolateAll(): ArrayList<PointF> {
        val listPoints = ArrayList<PointF>()
        val start = knots[0].x.toInt() + 1
        val end = knots.get(getNumKnots() - 1).x.toInt() - 1
        for (i in start..end) {
            val y = interpolateY(i.toFloat())
            listPoints.add(PointF(i.toFloat(), y))
        }
        return listPoints
    }
}