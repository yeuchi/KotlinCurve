package com.ctyeung.kotlincurve

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.ctyeung.kotlincurve.data.Median
import com.ctyeung.kotlincurve.databinding.ActivityMainBinding
import java.util.ArrayList

class MainActivity : AppCompatActivity(), PaperEvent {

    lateinit var mBinding: ActivityMainBinding
    lateinit var mPaper:MyPaperView
    val linearSpline = LinearSpline()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.listener = this

        mPaper = this.findViewById(R.id.paper)
        mPaper.setListener(this)
    }

    /*
     * clear the screen
     * - clear paperview points and path
     */
    fun onClickButtonClear()
    {
        linearSpline.clear()
        mPaper.clear()
    }

    fun onCheckMedian() {
        render()
    }


    override fun onActionUp(point:MyPoint) {
        linearSpline.insert(point)
        render()
    }

    private fun render() {
        val isMedianChecked = mBinding.chkMedian.isChecked
        if(isMedianChecked) {
            val filtered = Median.filter(linearSpline.knots)
            mPaper.setKnots(filtered)
        }
        else {
            mPaper.setKnots(linearSpline.knots)
        }
    }
}