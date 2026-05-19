package com.example.machina.ui.screens.dashboard.home.active_machinery.widgetst

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.machina.ui.theme.AppOrangeLight
import kotlin.math.*

@Composable
fun SemiCircularKnob(
    modifier: Modifier = Modifier,
    min: Float = 0f,
    max: Float = 100f,
    initialValue: Float = min,
    step: Float = 1f,
    valueFormatter: (Float) -> String = { value -> value.roundToInt().toString() },
    onValueChange: (Float) -> Unit
) {
    val startAngle = 180f
    val sweepAngle = 180f
    val safeMax = max.coerceAtLeast(min)
    val currentOnValueChange by rememberUpdatedState(onValueChange)

    var value by remember(min, safeMax, initialValue, step) {
        mutableStateOf(snapToStep(initialValue, min, safeMax, step))
    }
    val angle = valueToAngle(value, min, safeMax, startAngle, sweepAngle)

    fun updateValueFromTouch(position: Offset, center: Offset) {
        val touchAngle = position.toSemiCircleAngle(center)
        val nextValue = snapToStep(
            value = angleToValue(touchAngle, min, safeMax, startAngle, sweepAngle),
            min = min,
            max = safeMax,
            step = step
        )

        if (nextValue != value) {
            value = nextValue
            currentOnValueChange(nextValue)
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .width(250.dp)
            .height(250.dp)
            .pointerInput(min, safeMax, step) {
                detectDragGestures(
                    onDragStart = { start ->
                        updateValueFromTouch(
                            position = start,
                            center = Offset(size.width / 2f, size.height / 2f)
                        )
                    }
                ) { change, _ ->
                    change.consume()
                    updateValueFromTouch(
                        position = change.position,
                        center = Offset(size.width / 2f, size.height / 2f)
                    )
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 18.dp.toPx()
            val thumbRadius = 15.dp.toPx()
            val hubRadius = 7.dp.toPx()
            val radius = (size.minDimension - strokeWidth) / 2f
            val arcTopLeft = Offset(strokeWidth / 2f, strokeWidth / 2f)
            val arcSize = Size(
                width = size.width - strokeWidth,
                height = size.height - strokeWidth
            )

            drawArc(
                color = Color.LightGray,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = arcTopLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            val progressSweep = (angle - startAngle).coerceIn(0f, sweepAngle)

            drawArc(
                color = AppOrangeLight,
                startAngle = startAngle,
                sweepAngle = progressSweep,
                useCenter = false,
                topLeft = arcTopLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            val knobX = center.x + radius * cos(Math.toRadians(angle.toDouble())).toFloat()
            val knobY = center.y + radius * sin(Math.toRadians(angle.toDouble())).toFloat()
            val knobCenter = Offset(knobX, knobY)

            drawLine(
                color = AppOrangeLight,
                start = center,
                end = knobCenter,
                strokeWidth = 5.dp.toPx(),
                cap = StrokeCap.Round
            )

            drawCircle(
                color = AppOrangeLight,
                radius = hubRadius,
                center = center
            )

            drawCircle(
                color = AppOrangeLight,
                radius = thumbRadius,
                center = knobCenter
            )
        }

        Text(
            text = valueFormatter(value),
            color = AppOrangeLight,
            fontSize = 24.sp
        )
    }
}

private fun Offset.toSemiCircleAngle(center: Offset): Float {
    val x = x - center.x
    val y = y - center.y
    val normalizedAngle = (Math.toDegrees(atan2(y, x).toDouble()).toFloat() + 360f) % 360f

    return when {
        normalizedAngle == 0f -> 360f
        normalizedAngle in 180f..360f -> normalizedAngle
        normalizedAngle < 90f -> 360f
        else -> 180f
    }
}

private fun valueToAngle(
    value: Float,
    min: Float,
    max: Float,
    startAngle: Float,
    sweepAngle: Float
): Float {
    val range = max - min
    if (range == 0f) return startAngle

    val progress = ((value - min) / range).coerceIn(0f, 1f)
    return startAngle + progress * sweepAngle
}

private fun angleToValue(
    angle: Float,
    min: Float,
    max: Float,
    startAngle: Float,
    sweepAngle: Float
): Float {
    val progress = ((angle - startAngle) / sweepAngle).coerceIn(0f, 1f)
    return min + progress * (max - min)
}

private fun snapToStep(value: Float, min: Float, max: Float, step: Float): Float {
    val clampedValue = value.coerceIn(min, max)
    if (step <= 0f) return clampedValue

    val stepsFromMin = ((clampedValue - min) / step).roundToInt()
    return (min + stepsFromMin * step).coerceIn(min, max)
}
