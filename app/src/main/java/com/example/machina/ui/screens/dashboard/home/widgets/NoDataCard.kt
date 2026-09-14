package com.example.machina.ui.screens.dashboard.home.widgets

import AppButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Card
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.machina.R
import com.example.machina.ui.theme.AppDarkGreen
import com.example.machina.ui.theme.AppGreen
import com.example.machina.ui.widgets.AppText

@Composable
fun NoDataCard(
    onButtonClick: () -> Unit,
    cardText: String,
    buttonText: String,
    backgroundColor: Color

) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.White.copy(alpha = 0.58f), RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.no_data),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                AppText(text = "Nothing here yet", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)

            }

            Spacer(modifier = Modifier.height(14.dp))
            AppText(text = cardText, fontSize = 13.sp, color = Color(0xFF3F4A46))

            Spacer(modifier = Modifier.height(18.dp))

            AppButton(
                text = buttonText,
                onClick = onButtonClick,
                selectedColor = AppDarkGreen,
                textColor = Color.White
            )

        }
    }
}
