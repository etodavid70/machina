package com.example.machina.data.model.createvm_models

import com.example.machina.data.model.dashboard_models.VirtualMachineDetails

data class CreateMachinery(
    val id : Int,
    val name: String,
    val imageUrl: String,
    val mainOs: String
)
