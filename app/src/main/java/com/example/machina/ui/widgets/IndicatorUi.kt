package com.example.machina.ui.widgets
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun IndicatorUi(
    currentPage: Int,
    pageSize: Int,
    selectedColor: Color = Color(0xFF11772D),
    unselectedColor: Color = Color.LightGray,
    modifier: Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = modifier
    ) {
        repeat(pageSize) { index ->
            Box(
                modifier = Modifier
                    .height(8.dp)
                    .width(if (index == currentPage) 24.dp else 8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        if (index == currentPage) selectedColor else unselectedColor
                    )
            )
        }
    }
}


