package com.khuram.dogs.network.model

import com.google.gson.annotations.SerializedName


data class BreedImagesDto(

    @SerializedName("message")
    var message: List<String>? = null,

    @SerializedName("status")
    var status: String? = null
)
