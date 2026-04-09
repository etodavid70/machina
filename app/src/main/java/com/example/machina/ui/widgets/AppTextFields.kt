package com.example.machina.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.machina.ui.theme.AppGreen
import com.example.machina.ui.theme.AppGreenLight

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    modifier: Modifier = Modifier,
    textColor: Color = Color.Black,
    borderColor: Color = Color.Gray,
    focusedBorderColor: Color = Color.Blue
) {
    var isFocused by remember { mutableStateOf(false) }

    Column(modifier = modifier) {

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(color = textColor, fontSize = 16.sp),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { isFocused = it.isFocused }
        ) { innerTextField ->

            Box {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = Color.Gray
                    )
                }
                innerTextField()
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Bottom border line
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(if (isFocused) focusedBorderColor else borderColor)
        )
    }
}




@Composable
fun AppTextFieldRounded(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    focusedBorderColor: Color = AppGreen,
    unfocusedBorderColour: Color= AppGreenLight
) {



    OutlinedTextField(
        value = value,
        shape = RoundedCornerShape(16.dp),
        onValueChange = onValueChange,
        placeholder = {
            Text(text = placeholder)
        },
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = focusedBorderColor,
            unfocusedBorderColor = unfocusedBorderColour
        ),
        modifier = Modifier.fillMaxWidth()
    )
}