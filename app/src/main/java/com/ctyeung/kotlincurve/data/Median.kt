package com.ctyeung.kotlincurve.data

import com.ctyeung.kotlincurve.MyPoint

object Median {
    /*
     * TODO Add 3wide median filter
     */
    fun filter(knots:ArrayList<MyPoint>):ArrayList<MyPoint> {
        var filtered = ArrayList<MyPoint>()
        if(knots.size > 2) {
            filtered.add(knots[0])
            for (i in 1..knots.size - 2) {
                val p = knots[i - 1]
                val pp = knots[i]
                val ppp = knots[i + 1]
                val listY = arrayListOf<Double>(p.y, pp.y, ppp.y)
                listY.sort()
                filtered.add(MyPoint(pp.x, listY[1]))
            }
            filtered.add(knots[knots.size-1])
            return filtered
        }
        else {
            return knots
        }
    }
}