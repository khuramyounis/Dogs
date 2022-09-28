package com.khuram.dogs.presentation.ui.breed_images


sealed class BreedImagesEvent {

    data class GetImages(
        val breed: String,
        val mainBreed: String
    ): BreedImagesEvent()
}