package com.example.machina.ui.screens.dashboard.home.widgets

import AppButton
import androidx.annotation.DrawableRes
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
import com.example.machina.R
import com.example.machina.ui.widgets.AppText

@Composable
fun VmDataCard(
    onButtonClick: () -> Unit,
    cardText: String,
    buttonText: String,
    backgroundColor: Color,
    @DrawableRes imageRes: Int

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


        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "Image",
            )
            Spacer(modifier = Modifier.width(5.dp))

            Column(
                horizontalAlignment = Alignment.Start
            )
            {
//                Spacer(modifier = Modifier.height(8.dp))
                AppText(text = cardText, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                AppButton(

                    text = buttonText,
                    onClick = onButtonClick
                )

            }


        }


    }
}
