package com.example.machina.view_model.dashboard_viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.machina.data.model.dashboard_models.DeviceInfo
import com.example.machina.utils.DeviceInfoUtil

class DeviceInfoViewModel : ViewModel() {

    private val _deviceInfo = MutableLiveData<DeviceInfo>()
    val deviceInfo: LiveData<DeviceInfo> = _deviceInfo

    fun loadDeviceInfo(context: Context) {
        _deviceInfo.value = DeviceInfoUtil.getDeviceInfo(context)
    }
}