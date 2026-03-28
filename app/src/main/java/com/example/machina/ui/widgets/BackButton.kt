package com.example.machina.ui.widgets

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.ui.Modifier
import com.example.machina.ui.theme.AppGreen

@Composable
fun BackButton(
    navController: NavController? = null,
    onClick: (() -> Unit)? = null,
    modifier: Modifier
) {
    IconButton(
        onClick = {
            when {
                onClick != null -> onClick()
                navController != null -> navController.popBackStack()
            }
        }
    ) {
        Icon(
            imageVector = Icons.Filled.KeyboardArrowLeft,
            contentDescription = "Back",
            tint = AppGreen
        )
    }
}