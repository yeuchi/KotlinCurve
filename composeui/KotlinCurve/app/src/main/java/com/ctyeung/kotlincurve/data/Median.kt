package com.ctyeung.kotlincurve.data

import android.graphics.PointF


object Median {
    /*
     * TODO Add 3wide median filter
     */
    fun filter(knots:ArrayList<PointF>):ArrayList<PointF> {
        val filtered = ArrayList<PointF>()
        if(knots.size > 2) {
            filtered.add(knots[0])
            for (i in 1..knots.size - 2) {
                val p = knots[i - 1]
                val pp = knots[i]
                val ppp = knots[i + 1]
                val listY = arrayListOf<Float>(p.y, pp.y, ppp.y)
                listY.sort()
                filtered.add(PointF(pp.x, listY[1]))
            }
            filtered.add(knots[knots.size-1])
            return filtered
        }
        else {
            return knots
        }
    }
}