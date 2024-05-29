package com.ctyeung.kotlincurve.data

import android.graphics.PointF

class LinearSpline {

    var knots = ArrayList<PointF>()

    fun insert(knot:PointF) {
        knots.add(knot)
        knots.sortBy { it.x }
    }

    fun clear() {
        knots.clear()
    }
}