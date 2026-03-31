package com.example.machina.ui.screens.dashboard.home.widgets

import AppButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
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
            .wrapContentHeight(),// makes it visually prominent
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Image(
                    painter = painterResource(id = R.drawable.no_data),
                    contentDescription = "Image",
                )
                Spacer(modifier = Modifier.width(5.dp))
                AppText(text = "No Data", fontWeight = FontWeight.Bold)

            }

            Spacer(modifier = Modifier.height(8.dp))
            AppText(text = cardText, fontSize = 12.sp)

            Spacer(modifier = Modifier.height(16.dp))

            AppButton(
                text = buttonText,
                onClick = onButtonClick
            )

        }
    }
}