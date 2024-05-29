package com.ctyeung.kotlincurve.views

import android.graphics.PointF
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class MainViewModel @Inject constructor() : ViewModel() {

    val points = arrayListOf<PointF>()
    private val _event = MutableSharedFlow<MainViewEvent>()
    val event: SharedFlow<MainViewEvent> = _event

    fun select(p: PointF) {

    }

    fun drag(p: PointF) {

    }

    fun save(p: PointF) {
        viewModelScope.launch(IO) {
            points.add(p)
            _event.emit(MainViewEvent.invalidated)
        }
    }

    fun deleteAll() {
        viewModelScope.launch(IO) {
            points.clear()
            _event.emit(MainViewEvent.invalidated)
        }
    }
}

sealed class MainViewEvent() {
    object invalidated : MainViewEvent()
}