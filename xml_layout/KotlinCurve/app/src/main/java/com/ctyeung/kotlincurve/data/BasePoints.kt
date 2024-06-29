package com.ctyeung.kotlincurve.data

import android.graphics.PointF

/**
 * Class to manage knots or control points
 */
open class BasePoints {
    val knots = arrayListOf<PointF>()

     fun getNumKnots(): Int {
        return knots.size
    }

    fun setKnots(points:ArrayList<PointF>) {
        knots.clear()
        knots.addAll(points.filterNotNull())
    }

    /**
     * bisection insert O(log n) performance
     */
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

    /**
     * bisection search to locate x-axis values for input
     * O(log n) performance
     */
     fun bisection(ab: Float): Int {                                               // x-axis value
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
}