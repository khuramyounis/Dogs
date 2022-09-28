package com.khuram.dogs.presentation.navigation


sealed class Screen(
    val route: String
) {
    object BreedList: Screen("breedList")

    object SubBreedList: Screen("subBreedList")

    object BreedImageList: Screen("breedImageList")

    object BreedImage: Screen("breedImage")

    object FavouriteBreeds: Screen("favouriteBreeds")
}