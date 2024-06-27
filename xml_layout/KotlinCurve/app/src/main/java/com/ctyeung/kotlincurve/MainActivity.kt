package com.ctyeung.kotlincurve

import android.graphics.PointF
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ctyeung.kotlincurve.data.BezierQuad
import com.ctyeung.kotlincurve.data.CubicSpline
import com.ctyeung.kotlincurve.data.LinearSpline
import com.ctyeung.kotlincurve.data.Median
import com.ctyeung.kotlincurve.databinding.ActivityMainBinding
import com.ctyeung.kotlincurve.views.MyPaperView
import com.ctyeung.kotlincurve.views.PaperEvent

class MainActivity : AppCompatActivity(), PaperEvent
{
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mPaper: MyPaperView

    private val linearSpline = LinearSpline()
    private val cubicSpline = CubicSpline()
    private val bezierQuad = BezierQuad()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        mPaper = this.findViewById(R.id.paper)
        mPaper.setListener(this)
    }

    override fun onResume() {
        super.onResume()
        mBinding.btnDeleteLine.setOnClickListener {
            linearSpline.clear()
            cubicSpline.clear()
            mPaper.clear()
        }

        mBinding.chkMedian.setOnClickListener {
            render()
        }


        mBinding.chkSpline.setOnClickListener {
            render()
        }
    }

    override fun onActionUp(point:PointF) {
        linearSpline.insert(point)
        cubicSpline.insert(point)
        bezierQuad.insert(point)
        render()
    }

    private fun render() {
        val isMedianChecked = mBinding.chkMedian.isChecked
        val isSplineChecked = mBinding.chkSpline.isChecked
        val listPoints = if (isSplineChecked) {
            cubicSpline.formulate()
            cubicSpline.getPoints()
        }
        else {
            linearSpline.knots
        }

        if(isMedianChecked) {
            val filtered = Median.filter(listPoints)
            mPaper.setKnots(filtered)
        }
        else {
            mPaper.setKnots(listPoints)
        }
    }
}
