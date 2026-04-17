package com.example.machina.ui.widgets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.machina.ui.theme.AppGreenLight
import androidx.compose.ui.graphics.StrokeCap
import com.example.machina.ui.theme.AppOrangeLight

@Composable
fun AppProgressBar(progress: Float) {
    LinearProgressIndicator(
        progress = {
            progress // value between 0f and 1f
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp),
        color = AppGreenLight,
        trackColor = Color.White,
        strokeCap = StrokeCap.Round ,
    )
}