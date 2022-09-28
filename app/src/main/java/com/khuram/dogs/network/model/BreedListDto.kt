package com.khuram.dogs.network.model

import com.google.gson.annotations.SerializedName
import com.khuram.dogs.domain.model.Breed


data class BreedListDto(

    @SerializedName("message")
    var message: Map<String, List<String>>? = null,

    @SerializedName("status")
    var status: String? = null
)

fun BreedListDto.toDomain(): List<Breed> {

    val breedList = mutableListOf<Breed>()

    message?.entries?.forEach { entry ->
        breedList.add(
            Breed(entry.key, entry.value)
        )
        entry.value.forEach { name ->
            breedList.add(
                Breed(name, mainBreed = entry.key)
            )
        }
    }

    return breedList
}