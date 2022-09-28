package com.khuram.dogs.cache.model

import androidx.room.Entity
import com.khuram.dogs.domain.model.Breed
import com.khuram.dogs.util.convertToList


@Entity(tableName = "breeds", primaryKeys = ["name", "mainBreed"])
data class BreedEntity(
    var name: String,
    var subBreeds: String,
    var randomImage: String,
    var images: String,
    var mainBreed: String,
    var isFavourite: Boolean
)

fun BreedEntity.toDomain(): Breed {
    return Breed(
        name,
        subBreeds.convertToList(),
        randomImage,
        images.convertToList(),
        mainBreed,
        isFavourite
    )
}

fun List<BreedEntity>.toDomain(): List<Breed> {
    return map { it.toDomain() }
}