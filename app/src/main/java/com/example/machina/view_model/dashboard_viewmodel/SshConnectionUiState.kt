package com.example.machina.view_model.dashboard_viewmodel

import com.example.machina.data.model.dashboard_models.SshConnectionResult

sealed class SshConnectionUiState {
    object Idle : SshConnectionUiState()
    object Loading : SshConnectionUiState()
    data class Success(val result: SshConnectionResult) : SshConnectionUiState()
    data class Error(val message: String) : SshConnectionUiState()
}

sealed class TerminalCommandUiState {
    object Idle : TerminalCommandUiState()
    object Running : TerminalCommandUiState()
    data class Error(val message: String) : TerminalCommandUiState()
}

data class TerminalLine(
    val text: String,
    val type: TerminalLineType
)

enum class TerminalLineType {
    Prompt,
    Output,
    Error
}
