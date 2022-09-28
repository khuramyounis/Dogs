package com.khuram.dogs.domain.model

import com.khuram.dogs.cache.model.BreedEntity
import com.khuram.dogs.util.convertToString


data class Breed(
    val name: String,
    val subBreeds: List<String> = listOf(),
    var randomImage: String = "",
    var images: List<String> = listOf(),
    var mainBreed: String = "",
    var isFavourite: Boolean = false
)

fun Breed.toEntity(): BreedEntity {
    return BreedEntity(
        name,
        subBreeds.convertToString(),
        randomImage,
        images.convertToString(),
        mainBreed,
        isFavourite
    )
}

fun List<Breed>.toEntityList(): List<BreedEntity> {
    return map { it.toEntity() }
}
