package com.khuram.dogs.network.model

import com.google.gson.annotations.SerializedName


data class BreedRandomImageDto(

    @SerializedName("message")
    var message: String? = null,

    @SerializedName("status")
    var status: String? = null
)
