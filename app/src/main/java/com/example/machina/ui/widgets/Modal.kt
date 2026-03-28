package com.example.machina.ui.widgets
import AppButton
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color


@Composable
fun AppPopupModal(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    imageRes: Int,
    title: String,
    description: String,
    buttonText: String,
    onButtonClick: () -> Unit
) {
    if (showDialog) {
        Dialog(onDismissRequest = onDismiss) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                tonalElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {


                    AppText(
                        text = title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Description
                    AppText(
                        text = description,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )

                    // Image
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Title

                    Spacer(modifier = Modifier.height(20.dp))

                    // Button
                    AppButton(
                        onClick = {
                            onButtonClick()
                            onDismiss()
                        },
                      text = buttonText
                    )
                }
            }
        }
    }
}