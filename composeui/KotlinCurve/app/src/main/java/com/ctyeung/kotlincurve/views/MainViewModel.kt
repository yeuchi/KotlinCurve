package com.ctyeung.kotlincurve.views

import android.graphics.PointF
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ctyeung.kotlincurve.data.CubicSpline
import com.ctyeung.kotlincurve.data.LinearSpline
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class MainViewModel @Inject constructor() : ViewModel() {

    private val _event = MutableSharedFlow<MainViewEvent>()
    val event: SharedFlow<MainViewEvent> = _event

    val cubicSpline = CubicSpline()
    val linearSpline = LinearSpline()

    val cubicSplinePoints:List<PointF>
        get() {
            return cubicSpline.getPoints()
        }

    val linearSplinePoints:List<PointF>
        get() {
            return linearSpline.knots
        }

    fun select(p: PointF) {

    }

    fun drag(p: PointF) {

    }

    fun save(p: PointF) {
        viewModelScope.launch(IO) {
            cubicSpline.apply {
                insert(p)
                formulate()
            }

            linearSpline.insert(p)
            _event.emit(MainViewEvent.invalidated)
        }
    }

    fun deleteAll() {
        viewModelScope.launch(IO) {
            cubicSpline.clear()
            linearSpline.clear()
            _event.emit(MainViewEvent.invalidated)
        }
    }
}

sealed class MainViewEvent() {
    object invalidated : MainViewEvent()
}