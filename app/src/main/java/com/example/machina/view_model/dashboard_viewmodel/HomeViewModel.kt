package com.example.machina.view_model.dashboard_viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.machina.data.model.dashboard_models.ActiveMachinery
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.machina.data.model.dashboard_models.CloudInstances
import com.example.machina.data.model.dashboard_models.VirtualMachineDetails


class HomeViewModel : ViewModel() {

    var vmList by mutableStateOf<List<ActiveMachinery>>(emptyList())
        private set

    var cloudList by mutableStateOf<List<CloudInstances>>(emptyList())
        private set


    init {
        loadVMData() // automatically loads data
    }


    fun refresh() {
        loadVMData()
    }
    fun loadVMData() {
        // simulate API response
     vmList = listOf(
            ActiveMachinery(
                id = 1,
                name = "Ubuntu VM",
                status = "Running",
                imageUrl = "https://imageurl",
                vmDetails = VirtualMachineDetails(
                    cpu = "4 cores",
                    ram = "8GB",
                    storage = "100GB",
                    os = "Ubuntu 22.04",
                    ipAddress = "192.168.1.10",
                    createdAt = "2026-04-07"
                )
            ),
            ActiveMachinery(
                id = 2,
                name = "Windows VM",
                status = "Stopped",
                imageUrl = "https://imageurl2",
                vmDetails = VirtualMachineDetails(
                    cpu = "2 cores",
                    ram = "4GB",
                    storage = "50GB",
                    os = "Windows 10",
                    ipAddress = "192.168.1.11",
                    createdAt = "2026-04-06"
                )
            )
        )


      cloudList = listOf(
            CloudInstances(
                id = 1,
                name = "Ubuntu VM",
                status = "Running",
                imageUrl = "https://www.clipartmax.com/middle/m2i8A0m2d3H7G6d3_lets-talk-about-those-os-x-yosemite-app-icons-ubuntu-icon-png/",
                serviceProvider = "Microsoft",
                vmDetails = VirtualMachineDetails(
                    cpu = "4 cores",
                    ram = "8GB",
                    storage = "100GB",
                    os = "Ubuntu 22.04",
                    ipAddress = "192.168.1.10",
                    createdAt = "2026-04-07"
                )
            ),
            CloudInstances(
                id = 2,
                name = "Windows VM",
                status = "Stopped",
                imageUrl = "https://www.google.com/imgres?q=microsoft%20icon&imgurl=https%3A%2F%2Fimg.favpng.com%2F20%2F1%2F2%2Fmicrosoft-logo-icon-png-favpng-685u6bvjmVTSVQvgf1yFZk2yh.jpg&imgrefurl=https%3A%2F%2Ffavpng.com%2Fpng_view%2Fmicrosoft-icon-microsoft-logo-icon-png%2FREbq5A5N&docid=Jfo1mm-l0m6sqM&tbnid=m-PKJjYScicU8M&vet=12ahUKEwiCxdC7uuCTAxVcXUEAHSMLMDsQnPAOegQIIhAB..i&w=820&h=820&hcb=2&ved=2ahUKEwiCxdC7uuCTAxVcXUEAHSMLMDsQnPAOegQIIhAB",
                serviceProvider = "Aws",
                vmDetails = VirtualMachineDetails(
                    cpu = "Intel Core i7",
                    ram = "4GB",
                    storage = "50GB",
                    os = "Windows 10",
                    ipAddress = "192.168.1.11",
                    createdAt = "2026-04-06"
                )
            ),

          CloudInstances(
              id = 2,
              name = "Karly Linux",
              status = "Stopped",
              imageUrl = "https://www.google.com/imgres?q=microsoft%20icon&imgurl=https%3A%2F%2Fimg.favpng.com%2F20%2F1%2F2%2Fmicrosoft-logo-icon-png-favpng-685u6bvjmVTSVQvgf1yFZk2yh.jpg&imgrefurl=https%3A%2F%2Ffavpng.com%2Fpng_view%2Fmicrosoft-icon-microsoft-logo-icon-png%2FREbq5A5N&docid=Jfo1mm-l0m6sqM&tbnid=m-PKJjYScicU8M&vet=12ahUKEwiCxdC7uuCTAxVcXUEAHSMLMDsQnPAOegQIIhAB..i&w=820&h=820&hcb=2&ved=2ahUKEwiCxdC7uuCTAxVcXUEAHSMLMDsQnPAOegQIIhAB",
              serviceProvider = "GCP",
              vmDetails = VirtualMachineDetails(
                  cpu = "3 Cores",
                  ram = "4GB",
                  storage = "50GB",
                  os = "Windows 10",
                  ipAddress = "192.168.1.11",
                  createdAt = "2026-04-06"
              )
          ),
          CloudInstances(
              id = 2,
              name = "RedHat",
              status = "Stopped",
              imageUrl = "https://www.google.com/imgres?q=microsoft%20icon&imgurl=https%3A%2F%2Fimg.favpng.com%2F20%2F1%2F2%2Fmicrosoft-logo-icon-png-favpng-685u6bvjmVTSVQvgf1yFZk2yh.jpg&imgrefurl=https%3A%2F%2Ffavpng.com%2Fpng_view%2Fmicrosoft-icon-microsoft-logo-icon-png%2FREbq5A5N&docid=Jfo1mm-l0m6sqM&tbnid=m-PKJjYScicU8M&vet=12ahUKEwiCxdC7uuCTAxVcXUEAHSMLMDsQnPAOegQIIhAB..i&w=820&h=820&hcb=2&ved=2ahUKEwiCxdC7uuCTAxVcXUEAHSMLMDsQnPAOegQIIhAB",
              serviceProvider = "Aws",
              vmDetails = VirtualMachineDetails(
                  cpu = "4 Cores",
                  ram = "4GB",
                  storage = "50GB",
                  os = "Windows 10",
                  ipAddress = "192.168.1.11",
                  createdAt = "2026-04-06"
              )
          )
        )
//
//

    }
}