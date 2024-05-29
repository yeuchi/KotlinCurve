package com.ctyeung.kotlincurve.views

import android.graphics.PointF

interface PaperEvent {
    fun onActionUp(p:PointF)
}