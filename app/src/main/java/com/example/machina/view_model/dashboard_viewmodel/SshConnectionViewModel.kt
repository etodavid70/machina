package com.example.machina.view_model.dashboard_viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.machina.data.model.dashboard_models.SshConnectionRequest
import com.example.machina.data.repository.SshConnectionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.util.Log

class SshConnectionViewModel(
    private val repository: SshConnectionRepository
) : ViewModel() {

    private val _state = MutableStateFlow<SshConnectionUiState>(SshConnectionUiState.Idle)
    val state: StateFlow<SshConnectionUiState> = _state

    fun connect(request: SshConnectionRequest) {
        viewModelScope.launch {
            _state.value = SshConnectionUiState.Loading

            try {
                val result = withContext(Dispatchers.IO) {
                    repository.connect(request)
                }
                Log.d("connect", "ssh validation succeeded for ${result.username}@${result.host}:${result.port}")
                _state.value = SshConnectionUiState.Success(result)
            } catch (e: Exception) {
                Log.e("connect", "ssh validation failed", e)
                _state.value = SshConnectionUiState.Error(
                    e.message?.takeIf { it.isNotBlank() } ?: "SSH connection failed"
                )
            }
        }
    }

    fun resetState() {
        _state.value = SshConnectionUiState.Idle
    }
}
