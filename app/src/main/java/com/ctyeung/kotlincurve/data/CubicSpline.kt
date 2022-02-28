package com.ctyeung.kotlincurve

// ============================================================================
// Module:		CubicSpline.js
//
// Description:	2D Cubic Spline interpolation in javascript as defined by reference.
//
// Reference:	Numerical Recipes in C 2nd Edition, Press, Teukolsky, Vetterling, Flannery, pg.113
//
// Authors:		William H. Press
//				William T. Vetterling
//				Saul A. Teukolsky
//				Brian P. Flannery
//
//				C.T. Yeung (porting from C into javascript)
//
// Input:		pSrcX - array of anchors' x (assume positive real numbers)
//				pSrcY - array of anchors' y (assume positive real numbers)
//
// Output:		getY(x) => returns interpolated Y value.
//
// WARNING:     must invoke formulate() after all knots are added before getY(x)
//
// History:
// 20Nov11		ported it to javascript, working in HTML5 canvas.			cty
// 12Nov17      upgrade to ECMAScript6                                      cty
// ============================================================================
class CubicSpline {
    /*
     * TODO implement standard cubic spline
     */

    private val arraySrcX = ArrayList<Double>()
    private val arraySrcY = ArrayList<Double>()

    private var arrayB: DoubleArray? = null
    private var arrayC: DoubleArray? = null
    private var arrayD: DoubleArray? = null
    private var arrayH: DoubleArray? = null
    private var arraySIG: DoubleArray? = null
    private var arrayL: DoubleArray? = null
    private var arrayU: DoubleArray? = null
    private var arrayZ: DoubleArray? = null

    fun clear() {
        arraySrcX.clear()
        arraySrcY.clear()

        arrayB = null
        arrayC = null
        arrayD = null
        arrayH = null
        arraySIG = null
        arrayL = null
        arrayU = null
        arrayZ = null
    }

    private fun initIntermediate(size: Int) {
        arrayB = DoubleArray(size)
        arrayC = DoubleArray(size)
        arrayD = DoubleArray(size)
        arrayH = DoubleArray(size)
        arraySIG = DoubleArray(size)
        arrayL = DoubleArray(size)
        arrayU = DoubleArray(size)
        arrayZ = DoubleArray(size)
    }

    private fun getNumKnots(): Int {
        return arraySrcX.size
    }

    fun insert(p: MyPoint) {
        when (getNumKnots()) {
            0 -> {
                arraySrcX.add(p.x)
                arraySrcY.add(p.y)
            }
            1 -> {
                if (p.x < arraySrcX[0]) {
                    arraySrcX.add(0, p.x)
                    arraySrcY.add(0, p.y)
                } else {
                    arraySrcX.add(p.x)
                    arraySrcY.add(p.y)
                }
            }
            else -> {
                val index = bisection(p.x) + 1
                arraySrcX.add(index, p.x)
                arraySrcY.add(index, p.y)
            }
        }
    }

    /*
     * bisection search to locate x-axis values for input
     * - intended as a private method
     */
    private fun bisection(ab: Double): Int {                                               // x-axis value
        var ju = arraySrcX.size                                                // upper limit
        var jl = 0                                                                // lower limit
        var jm: Int?                                                            // midpoint

        while (ju - jl > 1) {
            jm = (ju + jl) / 2                                    // midpoint formula

            if (ab > arraySrcX[jm])
                jl = jm
            else
                ju = jm
        }
        return jl
    }

    fun formulate() {
        val numKnots = arraySrcX.size
        if (numKnots < 3)
            return

        initIntermediate(numKnots)

        // Theorem 3.11		[A].[x] = [b]					[A] -> n x n Matrix
        //													[b] -> n x n Matrix
        //													[x] -> c[] 0..n
        //	STEP 1		eq. 4 (pg. 134)
        for (aa in 0 until numKnots - 1) {
            arrayH?.set(
                aa,
                (arraySrcX[aa + 1] - arraySrcX[aa])
            ) // [A], Hj = Xj+1 - Xj
        }

        // STEP 2
        for (aa in 1 until numKnots - 1) {
            // 0 -> n-1
            arraySIG?.set(
                aa, (3.0 / arrayH!!.get(aa) * (arraySrcY.get(aa + 1) - arraySrcY.get(aa)) -
                        3.0 / arrayH!!.get(aa - 1) * (arraySrcY.get(aa) - arraySrcY.get(aa - 1)))
            )
        }

        // STEP 3
        arrayL?.set(0, 0.0)
        arrayU?.set(0, 0.0)
        arrayZ?.set(0, 0.0)
        arraySIG?.set(0, 0.0)

        // STEP 4
        for (aa in 1 until numKnots - 1) {
            arrayL?.set(
                aa,
                (2.0 * (arraySrcX.get(aa + 1) - arraySrcX.get(aa - 1)))- (arrayH!!.get(aa - 1) * arrayU!!.get(
                    aa - 1
                ))
            )

            arrayU?.set(aa, arrayH!!.get(aa) / arrayL!!.get(aa))

            arrayZ?.set(
                aa,
                (arraySIG!!.get(aa) - (arrayH!!.get(aa - 1) * arrayZ!!.get(aa - 1))) / arrayL!!.get(aa))
        }

        // STEP 5		TAIL BOUNDARY @ 0
        arrayL?.set(numKnots - 1, 1.0)
        arrayZ?.set(numKnots - 1, 0.0)
        arrayC?.set(numKnots - 1, 0.0)

        // STEP 6
        for (aa in numKnots - 2 downTo 0) {
            arrayC?.set(
                aa,
                arrayZ!!.get(aa) - arrayU!!.get(aa) * arrayC!!.get(aa + 1)
            ) // Theorem 3.11

            arrayB?.set(
                aa, ((arraySrcY[aa + 1] - arraySrcY[aa]) / arrayH!!.get(aa)
                        - (arrayH!!.get(aa) * (arrayC!!.get(aa + 1) + 2 * arrayC!!.get(aa)) / 3.0))
            ) // eq. 10

            arrayD?.set(
                aa,
                (arrayC!!.get(aa + 1) - arrayC!!.get(aa)) / (3.0 * arrayH!!.get(aa))
            ) // eq. 11
        }
    }

    /*
    * calculate the y value
    * - intended to be a private method
    * WARNING: must have invoked formulate() before 
    * RETURN: -1 if invalid, positive value if ok.
    */
    fun doCubicSpline(
        x: Double,                // [in] x value
        i: Int
    ): Double {            // [in] index of anchor to use
        val Y = arraySrcY[i] +
                arrayB?.get(i)!! * (x - arraySrcX[i]) +
                arrayC?.get(i)!! * Math.pow((x - arraySrcX[i]), 2.0) +
                arrayD?.get(i)!! * Math.pow((x - arraySrcX[i]), 3.0)
        return Y
    }

    fun interpolateY(x: Double): Double {
        val index = bisection(x)
        return if (arraySrcX[index] == x) {
            arraySrcY[index]
        } else {
            doCubicSpline(x, index)
        }
    }

    /*
     * calculated points resolution by finding x of min distance.
     * then add 1 point in between to satisfy Nyquist frequency 2X ?
     */
    fun getPoints(): ArrayList<MyPoint> {
        val numKnots = getNumKnots()
        return when (numKnots) {
            0 -> arrayListOf()
            1 -> arrayListOf<MyPoint>(MyPoint(arraySrcX[0], arraySrcY[0]))
            2 -> arrayListOf<MyPoint>(
                MyPoint(arraySrcX[0], arraySrcY[0]),
                MyPoint(arraySrcX[1], arraySrcY[1])
            )
            else -> interpolateAll()
        }
    }

    private fun interpolateAll(): ArrayList<MyPoint> {
        val listPoints = ArrayList<MyPoint>()
        val start = arraySrcX[0].toInt() + 1
        val end = arraySrcX.get(getNumKnots() - 1).toInt() - 1
        for (i in start..end) {
            val y = interpolateY(i.toDouble())
            listPoints.add(MyPoint(i.toDouble(), y))
        }
        return listPoints
    }
}