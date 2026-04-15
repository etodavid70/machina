package com.example.machina.ui.screens.dashboard.home.active_machinery.widgetst

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke

import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.center
import androidx.compose.ui.unit.dp
import com.example.machina.ui.theme.AppOrangeLight
import kotlin.math.*

@Composable
fun SemiCircularKnob(
    modifier: Modifier = Modifier,
    min: Float = 0f,
    max: Float = 100f,
    initialValue: Float = 8f,
    onValueChange: (Float) -> Unit
) {

    val startAngle = 180f
    val sweepAngle = 180f

    val strokeWidth = 20f

    var value by remember { mutableStateOf(initialValue) }

    // ✅ STRICT CLAMPED INITIAL ANGLE
    val initialAngle = remember(initialValue) {
        (startAngle + ((initialValue - min) / (max - min)) * sweepAngle)
            .coerceIn(startAngle, startAngle + sweepAngle)
    }

    var angle by remember { mutableStateOf(initialAngle) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .width(250.dp)
            .height(250.dp)
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->

                    change.consume()

                    val center = size.center
                    val x = change.position.x - center.x
                    val y = change.position.y - center.y

                    var touchAngle = Math.toDegrees(
                        atan2(y, x).toDouble()
                    ).toFloat()

                    touchAngle = (touchAngle + 360) % 360

                    val endAngle = startAngle + sweepAngle

                    val isValid = if (endAngle <= 360) {
                        touchAngle in startAngle..endAngle
                    } else {
                        touchAngle >= startAngle || touchAngle <= (endAngle - 360)
                    }

                    if (isValid) {

                        val clampedAngle =
                            touchAngle.coerceIn(startAngle, startAngle + sweepAngle)

                        val newValue =
                            min + ((clampedAngle - startAngle) / sweepAngle) * (max - min)

                        if (abs(newValue - value) > 0.3f) {
                            angle = clampedAngle
                            value = newValue
                            onValueChange(value)
                        }
                    }
                }
            }
    ) {

        Canvas(modifier = Modifier.fillMaxSize()) {

            val radius = size.minDimension / 2 - strokeWidth

            // ✅ ALWAYS draw exactly 180°
            drawArc(
                color = Color.LightGray,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = strokeWidth)
            )

            // ✅ SAFE progress (never exceeds 180°)
            val progressSweep = (angle - startAngle).coerceIn(0f, sweepAngle)

            drawArc(
                color = AppOrangeLight,
                startAngle = startAngle,
                sweepAngle = progressSweep,
                useCenter = false,
                style = Stroke(width = strokeWidth)
            )

            val knobX =
                center.x + radius * cos(Math.toRadians(angle.toDouble())).toFloat()

            val knobY =
                center.y + radius * sin(Math.toRadians(angle.toDouble())).toFloat()

            drawCircle(
                color = AppOrangeLight,
                radius = 14f,
                center = Offset(knobX, knobY)
            )
        }

        Text(text = "${value.toInt()}")
    }
}