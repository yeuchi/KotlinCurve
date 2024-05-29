package com.ctyeung.kotlincurve.views

import android.graphics.Point
import android.graphics.PointF

fun PointF.slopeFromPoint(p: Point): Float {
    return (p.y - y) / (p.x - x)
}

fun PointF.compareTo(o: PointF): Int {
    return Comparators.CompareX.compare(this, o)
}
object Comparators {
    var CompareX: java.util.Comparator<PointF> = Comparator { o1, o2 -> (o1.x - o2.x).toInt() }
}