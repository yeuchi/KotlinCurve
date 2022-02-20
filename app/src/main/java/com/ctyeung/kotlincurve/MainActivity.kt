package com.ctyeung.kotlincurve

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.ctyeung.kotlincurve.databinding.ActivityMainBinding
import java.util.ArrayList

class MainActivity : AppCompatActivity(), PaperEvent {

    lateinit var mBinding: ActivityMainBinding
    lateinit var mPaper:MyPaperView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

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

    val linearSpline = LinearSpline()

    override fun onActionUp(point:MyPoint) {

        linearSpline.insert(point)
        mPaper.setKnots(linearSpline.knots)

        // control what is drawn here
    }
}