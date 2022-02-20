package com.ctyeung.kotlincurve

class LinearSpline {

    var knots = ArrayList<MyPoint>()

    fun insert(knot:MyPoint) {
        knots.add(knot)
        knots.sortBy { it.x }
    }

    fun clear() {
        knots.clear()
    }
}