package com.example.machina.data.model.dashboard_models

data class ActiveMachinery (
   val id: Int,
    val name: String,
    val status: String,
    val imageUrl: String,
    val vmDetails: VirtualMachineDetails

)



