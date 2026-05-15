package com.example.machina.ui.widgets
import androidx.compose.material3.Text
import com.example.machina.ui.theme.AppGreen
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.unit.dp

@Composable
fun AppPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "Enter password",
    borderColor: Color = AppGreen,
    errorText: String? = null
) {
    var passwordVisible by remember { mutableStateOf(false) }

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
            errorText?.let {
                Text(text = it, color = Color.Red)
            }
        },
        visualTransformation = if (passwordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            val icon = if (passwordVisible) {
                Icons.Default.Visibility
            } else {
                Icons.Default.VisibilityOff
            }

            IconButton(onClick = {
                passwordVisible = !passwordVisible
            }) {
                Icon(
                    imageVector = icon,
                    contentDescription = if (passwordVisible) "Hide password" else "Show password"
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = borderColor,
            unfocusedBorderColor = borderColor,
            cursorColor = borderColor,
            errorBorderColor = Color.Red,
            errorCursorColor = Color.Red
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

