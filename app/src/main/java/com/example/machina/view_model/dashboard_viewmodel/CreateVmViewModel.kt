package com.example.machina.view_model.dashboard_viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.machina.data.model.createvm_models.CreateMachinery
import com.example.machina.data.model.createvm_models.MainOs


class CreateVmViewModel: ViewModel() {
    var mainOsVmList by mutableStateOf<List<MainOs>>(emptyList())
        private set

    var vmList by mutableStateOf<List<CreateMachinery>>(emptyList())
        private set


    init{
        loadOsData()
        loadVMData()
    }

    fun loadOsData() {
        mainOsVmList = listOf(
             MainOs(
                    id = 1,
                    osImageUrl = "https://imageurl",
                    osName = "Linux"
                ),
          MainOs(
                    id = 2,
                    osImageUrl = "https://imageurl",
                    osName = "Windows"
                ),
        )

    }


    fun loadVMData() {
        vmList = listOf(
            CreateMachinery(
                id=1,
                name = "karly linux",
                mainOs = "linux",
                imageUrl = "https://me.com"
            ),

            CreateMachinery(
                id=2,
                name = "RedHat",
                mainOs = "linux",
                imageUrl = "https://me.com"
            ),

            CreateMachinery(
                id=3,
                name = "Ubuntu",
                mainOs = "linux",
                imageUrl = "https://me.com"
            ),


        )

    }

}