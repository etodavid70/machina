package com.example.machina.data.model.createvm_models

import com.google.gson.annotations.SerializedName

data class OperatingSystems(
    val id: Int,
    val name: String,
    @SerializedName("image_url") val imageUrl: String,
)
