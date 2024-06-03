package com.ctyeung.kotlincurve.views

import android.graphics.PointF
import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ComposeCanvas(viewModel: MainViewModel) {
    viewModel.apply {
        Box(modifier = Modifier
            .fillMaxSize()
            .pointerInteropFilter {
                when (it.action) {
                    MotionEvent.ACTION_DOWN -> select(PointF(it.rawX, it.rawY))
                    MotionEvent.ACTION_MOVE -> drag(PointF(it.rawX, it.rawY))
                    MotionEvent.ACTION_UP -> save(PointF(it.rawX, it.rawY))
                    else -> false
                }
                true
            })
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            /**
             * Draw regression line
             */
            val points = cubicSplinePoints

            /**
             * Draw selected points
             */

            points.forEach {
                drawCircle(Color.Blue, radius = 20F, center = Offset(it.x, it.y))
            }

            /**
             * Draw tangent lines
             */
            if(points.size > 1) {
                Path().let { path ->
                    path.moveTo(points[0].x, points[0].y)
                    for(i in 1 until points.size){
                        path.lineTo(points[i].x, points[i].y)
                        this.drawPath(
                            path = path,
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.DarkGray,
                                    Color.DarkGray
                                )
                            ),
                            style = Stroke(width = 5f, cap = StrokeCap.Round)
                        )
                    }
                }
            }
        }
    }
}

