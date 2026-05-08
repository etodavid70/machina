package com.example.machina.view_model.dashboard_viewmodel
import androidx.lifecycle.ViewModel
import com.example.machina.data.repository.CloudInstanceRepository
import androidx.lifecycle.viewModelScope
import com.example.machina.data.model.dashboard_models.CloudInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class CloudInstanceViewModel(
    private val repository: CloudInstanceRepository
) : ViewModel() {

    private val _instances = MutableStateFlow<List<CloudInstance>>(emptyList())
    val instances: StateFlow<List<CloudInstance>> = _instances

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun fetchInstances() {
        viewModelScope.launch {
            _loading.value = true
            try {
                _instances.value = repository.getCloudInstances()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _loading.value = false
            }
        }
    }
}
