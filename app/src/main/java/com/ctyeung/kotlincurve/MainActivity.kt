package com.ctyeung.kotlincurve

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import com.ctyeung.kotlincurve.data.Median
import com.ctyeung.kotlincurve.databinding.ActivityMainBinding
import java.util.ArrayList

class MainActivity : AppCompatActivity(), PaperEvent {

    lateinit var mBinding: ActivityMainBinding
    lateinit var mPaper:MyPaperView
    val linearSpline = LinearSpline()
    val cubicSpline = CubicSpline()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.listener = this

        mPaper = this.findViewById(R.id.paper)
        mPaper.setListener(this)

        window.statusBarColor = Color.TRANSPARENT
        // Making status bar overlaps with the activity
        WindowCompat.setDecorFitsSystemWindows(window, false)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }

    /*
     * clear the screen
     * - clear paperview points and path
     */
    fun onClickButtonClear()
    {
        linearSpline.clear()
        cubicSpline.clear()
        mPaper.clear()
    }

    fun onCheckMedian() {
        render()
    }

    fun onCheckCubicSpline() {
        render()
    }

    override fun onActionUp(point:MyPoint) {
        linearSpline.insert(point)
        cubicSpline.insert(point)
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