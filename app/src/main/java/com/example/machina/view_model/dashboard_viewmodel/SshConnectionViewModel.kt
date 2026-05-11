package com.example.machina.view_model.dashboard_viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.machina.data.model.dashboard_models.SshConnectionRequest
import com.example.machina.data.repository.SshShellConnection
import com.example.machina.data.repository.SshConnectionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.util.Log

class SshConnectionViewModel(
    private val repository: SshConnectionRepository
) : ViewModel() {

    private val _state = MutableStateFlow<SshConnectionUiState>(SshConnectionUiState.Idle)
    val state: StateFlow<SshConnectionUiState> = _state

    private val _terminalLines = MutableStateFlow<List<TerminalLine>>(emptyList())
    val terminalLines: StateFlow<List<TerminalLine>> = _terminalLines

    private val _terminalState =
        MutableStateFlow<TerminalCommandUiState>(TerminalCommandUiState.Idle)
    val terminalState: StateFlow<TerminalCommandUiState> = _terminalState

    private var activeConnectionRequest: SshConnectionRequest? = null

    fun connect(request: SshConnectionRequest) {
        viewModelScope.launch {
            _state.value = SshConnectionUiState.Loading

            try {
                val result = withContext(Dispatchers.IO) {
                    repository.connect(request)
                }
                Log.d("connect", "ssh validation succeeded for ${result.username}@${result.host}:${result.port}")
                activeConnectionRequest = request
                _terminalLines.value = listOf(
                    TerminalLine(
                        text = result.output,
                        type = TerminalLineType.Output
                    )
                )
                _state.value = SshConnectionUiState.Success(result)
            } catch (e: Exception) {
                Log.e("connect", "ssh validation failed", e)
                _state.value = SshConnectionUiState.Error(
                    e.message?.takeIf { it.isNotBlank() } ?: "SSH connection failed"
                )
            }
        }
    }

    fun hasActiveConnection(): Boolean = activeConnectionRequest != null

    fun getActiveConnectionRequest(): SshConnectionRequest? = activeConnectionRequest

    fun openInteractiveShell(
        columns: Int = 80,
        rows: Int = 24
    ): SshShellConnection {
        val request = activeConnectionRequest
            ?: throw IllegalStateException("Connect to a server first.")
        return repository.openShell(request, columns, rows)
    }

    fun runTerminalCommand(command: String) {
        val request = activeConnectionRequest
        val trimmedCommand = command.trim()

        if (request == null) {
            _terminalState.value = TerminalCommandUiState.Error("Connect to a server first.")
            return
        }

        if (trimmedCommand.isBlank()) return

        viewModelScope.launch {
            _terminalState.value = TerminalCommandUiState.Running
            _terminalLines.update {
                it + TerminalLine("$ $trimmedCommand", TerminalLineType.Prompt)
            }

            try {
                val result = withContext(Dispatchers.IO) {
                    repository.executeCommand(request, trimmedCommand)
                }
                val newLines = buildList {
                    if (result.output.isNotBlank()) {
                        add(TerminalLine(result.output, TerminalLineType.Output))
                    }
                    if (result.error.isNotBlank()) {
                        add(TerminalLine(result.error, TerminalLineType.Error))
                    }
                    if (result.output.isBlank() && result.error.isBlank()) {
                        add(TerminalLine("Command completed.", TerminalLineType.Output))
                    }
                }

                _terminalLines.update { it + newLines }
                _terminalState.value = TerminalCommandUiState.Idle
            } catch (e: Exception) {
                val message = e.message?.takeIf { it.isNotBlank() } ?: "Command failed"
                _terminalLines.update {
                    it + TerminalLine(message, TerminalLineType.Error)
                }
                _terminalState.value = TerminalCommandUiState.Error(message)
            }
        }
    }

    fun clearTerminal() {
        _terminalLines.value = emptyList()
        _terminalState.value = TerminalCommandUiState.Idle
    }

    fun resetState() {
        _state.value = SshConnectionUiState.Idle
    }
}
