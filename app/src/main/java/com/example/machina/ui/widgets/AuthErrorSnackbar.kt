package com.example.machina.ui.widgets

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.machina.view_model.auth_viewmodel.AuthUiState

@Composable
fun AuthErrorSnackbar(
    state: AuthUiState,
    snackbarHostState: SnackbarHostState,
    onMessageShown: () -> Unit
) {
    LaunchedEffect(state) {
        val errorState = state as? AuthUiState.Error ?: return@LaunchedEffect
        snackbarHostState.showSnackbar(errorState.message)
        onMessageShown()
    }
}
