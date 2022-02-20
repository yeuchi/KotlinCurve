package com.ctyeung.kotlincurve

import android.graphics.Point

class MyPoint : Comparable<MyPoint> {
    var x: Double = 0.toDouble()
    var y: Double = 0.toDouble()

    constructor(x: Double, y: Double) {
        this.x = x
        this.y = y
    }

    constructor() {
        x = 0.0
        y = 0.0
    }

    protected fun slopeFromPoint(p: Point): Double {
        return (p.y - y) / (p.x - x)
    }

    override fun compareTo(o: MyPoint): Int {
        return Comparators.CompareX.compare(this, o)
    }

    object Comparators {

        var CompareX: java.util.Comparator<MyPoint> = Comparator { o1, o2 -> (o1.x - o2.x).toInt() }
    }
}