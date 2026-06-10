package com.example.machina.ui.screens.dashboard.home.widgets

import androidx.compose.runtime.Composable
import AppButton
import AppWhiteButton
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.machina.ui.theme.DeleteColor1
import com.example.machina.ui.theme.DeleteColor2
import com.example.machina.ui.theme.deleteModalMessage
import com.example.machina.ui.theme.deleteModalTitle
import com.example.machina.ui.widgets.AppText

@Composable
fun DeleteConfirmationDialog(
    isEnabled: Boolean,
    isLoading: Boolean,
    showDialog: Boolean,
    title: String = deleteModalTitle,
    message: String = deleteModalMessage,
    onDelete: () -> Unit,
    onCancel: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onCancel,
            title = {
                AppText(text = title, fontSize = 20.sp)
            },
            text = {
                AppText(text = message)
            },
            confirmButton = {
                AppWhiteButton(
                    borderColor = DeleteColor2,
                    onClick = {
                        onDelete()
                    },
                    text = "Delete",
                    textColor = DeleteColor2,
                    isEnabled = isEnabled,
                    isLoading = isLoading
                )
            },
            dismissButton = {
                AppWhiteButton(
                    onClick = {
                        onCancel()
                    },
                    text = "Cancel"
                )
            }
        )
    }
}