package com.ctyeung.kotlincurve

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ctyeung.kotlincurve.databinding.ActivityMainBinding
import com.ctyeung.kotlincurve.views.InterpolationEnum
import com.ctyeung.kotlincurve.views.MyPaperView

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mPaper: MyPaperView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mPaper = this.findViewById(R.id.paper)
    }

    override fun onResume() {
        super.onResume()
        mBinding.apply {
            btnDeleteLine.setOnClickListener {
                mPaper.clear()
            }

            rdoBezierCubic.setOnClickListener {
                mPaper.apply {
                    interpolation = InterpolationEnum.BezierCubic
                    invalidate()
                }
            }

            rdoBezierQuad.setOnClickListener {
                mPaper.apply {
                    interpolation = InterpolationEnum.BezierQuad
                    invalidate()
                }
            }

            rdoLinear.setOnClickListener {
                mPaper.apply {
                    interpolation = InterpolationEnum.Linear
                    invalidate()
                }
            }

            rdoMedian.setOnClickListener {
                mPaper.apply {
                    interpolation = InterpolationEnum.MedianFilter
                    invalidate()
                }
            }

            rdoCubicSpline.setOnClickListener {
                mPaper.apply {
                    interpolation = InterpolationEnum.CubicSpline
                    invalidate()
                }
            }
        }
    }
}
