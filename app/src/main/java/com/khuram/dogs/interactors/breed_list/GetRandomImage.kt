package com.khuram.dogs.interactors.breed_list

import com.khuram.dogs.cache.BreedDao
import com.khuram.dogs.domain.data.DataState
import com.khuram.dogs.domain.model.Breed
import com.khuram.dogs.network.BreedService
import com.khuram.dogs.util.NO_INTERNET_AVAILABLE
import com.khuram.dogs.util.UNKNOWN_ERROR
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception


class GetRandomImage(
    private val breedService: BreedService,
    private val breedDao: BreedDao
) {
    fun execute(
        isNetworkAvailable: Boolean,
        breed: Breed
    ): Flow<DataState<String>> = flow {

        try {
            emit(DataState.loading())

            var image = breedDao.getRandomImage(breed.name, breed.mainBreed)

            // if image is empty, its not in cache, get it from the network
            if(image.isNotEmpty()) {
                emit(DataState.success(image))
            } else {
                if(isNetworkAvailable) {
                    // get random image from service
                    val networkImage = if (breed.mainBreed.isEmpty()) {
                        breedService.getRandomBreedImage(breed.name)
                    } else {
                        breedService.getRandomSubBreedImage(breed.mainBreed, breed.name)
                    }

                    // insert random image into cache
                    networkImage.message?.let { breedDao.insertRandomImage(breed.name, breed.mainBreed, it) }
                } else {
                    emit(DataState.error(NO_INTERNET_AVAILABLE))
                }

                // query the cache
                image = breedDao.getRandomImage(breed.name, breed.mainBreed)

                // emit random image from cache
                emit(DataState.success(image))
            }
        } catch (e: Exception) {
            emit(DataState.error(e.message?: UNKNOWN_ERROR))
        }
    }
}