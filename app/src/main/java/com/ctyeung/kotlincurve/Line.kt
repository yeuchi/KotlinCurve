package com.ctyeung.kotlincurve

open class Line {

    var p0: MyPoint
    var p1: MyPoint

    /*
     * default constructor
     */
    constructor(p0: MyPoint, p1: MyPoint) {
        this.p0 = p0
        this.p1 = p1
    }

    /*
     * construct from 1 point and slope
     */
    constructor(p0: MyPoint, slope: Double) {
        this.p0 = p0

        // horionzontal line
        if (0.0 == slope) {
            this.p1 = MyPoint(p0.x + 5, p0.y)
        } else {
            // default as yIntercept
            val y = p0.y - slope * p0.x
            this.p1 = MyPoint(0.0, y)
        }
    }

    /*
     * Find slope of this line
     * return: Double.NAN if vertical
     */
    open val slope: Double
        get() = if (isVertical) {
            java.lang.Double.NaN
        } else (p1.y - p0.y) / (p1.x - p0.x)

    /*
     * Find Y-Intercept or b
     * - x point where y=0
     *
     * return: x if line is not vertical or horizontal
     */
    open val yIntercept: Double
        get() {
            if (isHorizontal) {
                return java.lang.Double.NaN
            } else {
                val m = slope
                return if (m == java.lang.Double.NaN) java.lang.Double.NaN else p1.y - p1.x * m

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
    open fun findNormalLineFrom(p: MyPoint): Line {
        // if vertical line
        return if (isVertical) {
            Line(p, MyPoint(p0.x, p.y))
        } else if (isHorizontal) {
            Line(p, MyPoint(p.x, p0.y))
        } else {
            // slope is inverse of this line
            Line(p, -1 / slope)
        }// tangent line
        // if horizontal line
    }

    /*
     * Find intersection point from a given line
     */
    open fun findIntersectionFrom(line: Line): MyPoint? {
        // parallel lines
        if (this.isVertical && line.isVertical || this.isHorizontal && line.isHorizontal)
            return null

        if (isVertical) {
            val x = this.p0.x
            val y = line.findY(x)
            return MyPoint(x, y)
        } else if (isHorizontal) {
            val y = this.p0.y
            val x = line.findX(y)
            return MyPoint(x, y)
        } else {
            val m = this.slope
            val b = this.yIntercept
            val x = (line.yIntercept - b) / (m - line.slope)
            val y = findY(x)
            return MyPoint(x, y)

            /*
             * alternative method
             * https://math.stackexchange.com/questions/27388/intersection-of-2-lines-in-2d-via-general-form-linear-equations
             */
        }
    }

    open fun findY(x: Double): Double {
        return if (isVertical) {
            java.lang.Double.NaN
        } else if (isHorizontal) {
            p0.y
        } else {
            // y = mx + b
            slope * x + yIntercept
        }
    }

    open fun findX(y: Double): Double {
        return if (isVertical) {
            java.lang.Double.NaN
        } else if (isHorizontal) {
            p0.x
        } else {
            (y - yIntercept) / slope
        }
    }
}