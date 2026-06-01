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
    focusedBorderColor: Color = Color.Blue,
    errorText: String? = null
) {
    var isFocused by remember { mutableStateOf(false) }
    val currentBorderColor = when {
        errorText != null -> Color.Red
        isFocused -> focusedBorderColor
        else -> borderColor
    }

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
                .background(currentBorderColor)
        )

        errorText?.let {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = it,
                color = Color.Red,
                fontSize = 12.sp
            )
        }
    }
}




@Composable
fun AppTextFieldRounded(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    focusedBorderColor: Color = AppGreen,
    unfocusedBorderColour: Color= AppGreenLight,
    errorText: String? = null,
    helperText: String? = null
) {



    OutlinedTextField(
        value = value,
        shape = RoundedCornerShape(16.dp),
        onValueChange = onValueChange,
        placeholder = {
            Text(text = placeholder)
        },
        singleLine = true,
        isError = errorText != null,
        supportingText = {
            when {
                errorText != null -> Text(text = errorText, color = Color.Red)
                helperText != null -> Text(text = helperText, color = Color.Gray)
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = focusedBorderColor,
            unfocusedBorderColor = unfocusedBorderColour,
            errorBorderColor = Color.Red,
            errorCursorColor = Color.Red
        ),
        modifier = Modifier.fillMaxWidth()
    )
}
