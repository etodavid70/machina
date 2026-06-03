package com.example.machina.view_model.dashboard_viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.machina.data.model.createvm_models.MainOs
import com.example.machina.data.model.createvm_models.OperatingSystems
import com.example.machina.data.repository.DashboardRepository
import com.example.machina.utils.backendErrorMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CreateVmViewModel(
    private val repository: DashboardRepository
) : ViewModel() {

    private val _mainOsLoading = MutableStateFlow(false)
    val mainOsLoading: StateFlow<Boolean> = _mainOsLoading

    private val _distrosLoading = MutableStateFlow(false)
    val distrosLoading: StateFlow<Boolean> = _distrosLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _mainOs = MutableStateFlow<List<MainOs>>(emptyList())
    val mainOs: StateFlow<List<MainOs>> = _mainOs

    private val _operatingSystems = MutableStateFlow<List<OperatingSystems>>(emptyList())
    val operatingSystems: StateFlow<List<OperatingSystems>> = _operatingSystems

    private val _selectedMainOs = MutableStateFlow<String?>(null)
    val selectedMainOs: StateFlow<String?> = _selectedMainOs

    private val _selectedDistro = MutableStateFlow<OperatingSystems?>(null)
    val selectedDistro: StateFlow<OperatingSystems?> = _selectedDistro

    init {
        fetchMainOs()
    }

    fun fetchMainOs() {
        viewModelScope.launch {
            _mainOsLoading.value = true
            _errorMessage.value = null
            try {
                _mainOs.value = repository.getMainOs()
            } catch (e: Exception) {
                _errorMessage.value = e.backendErrorMessage("Failed to load main OS list")
                e.printStackTrace()
            } finally {
                _mainOsLoading.value = false
            }
        }
    }

    fun fetchOperatingSystems(os: String) {
        _selectedMainOs.value = os
        viewModelScope.launch {
            _distrosLoading.value = true
            _errorMessage.value = null
            try {
                _operatingSystems.value = repository.getOperatingSystems(os)
            } catch (e: Exception) {
                _errorMessage.value = e.backendErrorMessage("Failed to load operating systems")
                e.printStackTrace()
            } finally {
                _distrosLoading.value = false
            }
        }
    }

    fun selectDistro(distro: OperatingSystems) {
        _selectedDistro.value = distro
    }
}
