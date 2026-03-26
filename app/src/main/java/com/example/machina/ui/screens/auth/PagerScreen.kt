package com.example.machina.ui.screens.auth
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.machina.data.model.onboarding_models.PagerModel
import com.example.machina.ui.widgets.IndicatorUi


@Composable
fun PagerScreenUI(
    onboardingModel: PagerModel,
    currentPage: Int = 0,
    totalPages: Int = 3,
    onContinue: () -> Unit,
    onSkip: () -> Unit

) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        //  Background Image
        Image(
            painter = painterResource(id = onboardingModel.backgroundImage),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Makes it behave like a background
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
//                .background(Color(0x5511772D)) // translucent dark green
        )

        // ✅ TextButton at the top-right corner
        TextButton(
            onClick =  onSkip ,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Text(
                text = "Skip",
                color = Color(0xFF11772D),
                style = TextStyle(fontSize = 16.sp)
            )
        }


        // ✅ Foreground Content (texts, etc.)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(100.dp))

            Text(
                text = onboardingModel.mainHeader,
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White // Make sure text is visible on background
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = onboardingModel.subHeader,
                style = TextStyle(
                    fontSize = 15.sp,
                    color = Color(0xFFEFEFEF)
                )
            )

            Spacer(modifier = Modifier.height(50.dp))



            IndicatorUi(
                currentPage = currentPage,
                pageSize = totalPages,
                modifier = Modifier.align(Alignment.End)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(

                onClick = onContinue

                ,
                modifier = Modifier
                    .fillMaxWidth()  // Make button take up the full width of the screen
                    .height(45.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF11772D)// Set the background color to #E9C475
                ),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(if(currentPage==3) "Start" else "Continue", color = Color.White)

            }
        }
    }
}

