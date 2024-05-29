package com.ctyeung.kotlincurve.views

import android.graphics.PointF

open class Line {

    var p0: PointF
    var p1: PointF

    /*
     * default constructor
     */
    constructor(p0: PointF, p1: PointF) {
        this.p0 = p0
        this.p1 = p1
    }

    /*
     * construct from 1 point and slope
     */
    constructor(p0: PointF, slope: Float) {
        this.p0 = p0

        // horionzontal line
        if (0F == slope) {
            this.p1 = PointF(p0.x + 5, p0.y)
        } else {
            // default as yIntercept
            val y = p0.y - slope * p0.x
            this.p1 = PointF(0F, y)
        }
    }

    /*
     * Find slope of this line
     * return: Double.NAN if vertical
     */
    open val slope: Float
        get() = if (isVertical) {
            java.lang.Float.NaN
        } else (p1.y - p0.y) / (p1.x - p0.x)

    /*
     * Find Y-Intercept or b
     * - x point where y=0
     *
     * return: x if line is not vertical or horizontal
     */
    open val yIntercept: Float
        get() {
            if (isHorizontal) {
                return java.lang.Float.NaN
            } else {
                val m = slope
                return if (m == java.lang.Float.NaN) java.lang.Float.NaN else p1.y - p1.x * m

            }
        }

    /*
     * Is this a vertical line ?
     */
    open val isVertical: Boolean
        get() = if (p0.x == p1.x) true else false

    /*
     * Is this a horizontal line ?
     */
    open val isHorizontal: Boolean
        get() = if (p0.y == p1.y) true else false

    /*
     * Find tangent line input point
     */
    fun findNormalLineFrom(p: PointF): Line {
        // if vertical line
        return if (isVertical) {
            Line(p, PointF(p0.x, p.y))
        } else if (isHorizontal) {
            Line(p, PointF(p.x, p0.y))
        } else {
            // slope is inverse of this line
            Line(p, -1 / slope)
        }// tangent line
        // if horizontal line
    }

    /*
     * Find intersection point from a given line
     */
    open fun findIntersectionFrom(line: Line): PointF? {
        // parallel lines
        if (this.isVertical && line.isVertical || this.isHorizontal && line.isHorizontal)
            return null

        if (isVertical) {
            val x = this.p0.x
            val y = line.findY(x)
            return PointF(x, y)
        } else if (isHorizontal) {
            val y = this.p0.y
            val x = line.findX(y)
            return PointF(x, y)
        } else {
            val m = this.slope
            val b = this.yIntercept
            val x = (line.yIntercept - b) / (m - line.slope)
            val y = findY(x)
            return PointF(x, y)

            /*
             * alternative method
             * https://math.stackexchange.com/questions/27388/intersection-of-2-lines-in-2d-via-general-form-linear-equations
             */
        }
    }

    open fun findY(x: Float): Float {
        return if (isVertical) {
            java.lang.Float.NaN
        } else if (isHorizontal) {
            p0.y
        } else {
            // y = mx + b
            slope * x + yIntercept
        }
    }

    open fun findX(y: Float): Float {
        return if (isVertical) {
            Float.NaN
        } else if (isHorizontal) {
            p0.x
        } else {
            (y - yIntercept) / slope
        }
    }
}
