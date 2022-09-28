package com.khuram.dogs.cache

import com.khuram.dogs.cache.model.BreedEntity


class BreedDaoFake(
    private val appDatabaseFake: AppDatabaseFake
): BreedDao {

    override suspend fun insertBreeds(breeds: List<BreedEntity>) {
        appDatabaseFake.breeds.addAll(breeds)
    }

    override suspend fun getBreeds(): List<BreedEntity> {
        return appDatabaseFake.breeds
    }

    override suspend fun insertRandomImage(breedName: String, mainBreed: String, randomImage: String) {
        appDatabaseFake.breeds.find { it.name == breedName }?.randomImage = randomImage
    }

    override suspend fun getRandomImage(breedName: String, mainBreed: String): String {
        return appDatabaseFake.breeds.find { it.name == breedName }?.randomImage?: ""
    }

    override suspend fun insertImages(breedName: String, mainBreed: String, images: String) {
        appDatabaseFake.breeds.find { it.name == breedName }?.images = images
    }

    override suspend fun getImages(breedName: String, mainBreed: String): String {
        return appDatabaseFake.breeds.find { it.name == breedName }?.images?: ""
    }

    override suspend fun updateFavourite(breedName: String, mainBreed: String, isFavourite: Boolean) {
        appDatabaseFake.breeds.find { it.name == breedName }?.isFavourite = isFavourite
    }

    override suspend fun isFavourite(breedName: String, mainBreed: String): Boolean {
        return appDatabaseFake.breeds.find { it.name == breedName }?.isFavourite!!
    }
}