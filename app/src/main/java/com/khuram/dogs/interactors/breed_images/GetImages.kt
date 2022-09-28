package com.khuram.dogs.interactors.breed_images

import com.khuram.dogs.cache.BreedDao
import com.khuram.dogs.domain.data.DataState
import com.khuram.dogs.network.BreedService
import com.khuram.dogs.util.NO_INTERNET_AVAILABLE
import com.khuram.dogs.util.UNKNOWN_ERROR
import com.khuram.dogs.util.convertToList
import com.khuram.dogs.util.convertToString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception


class GetImages(
    private val breedService: BreedService,
    private val breedDao: BreedDao
) {
    fun execute(
        isNetworkAvailable: Boolean,
        breedName: String,
        mainBreed: String
    ): Flow<DataState<List<String>>> = flow {

        try {
            emit(DataState.loading())

            var images = breedDao.getImages(breedName, mainBreed).convertToList()

            // if breed list is empty, its not in cache, get it from the network
            if(images.isNotEmpty()) {
                emit(DataState.success(images))
            } else {
                if(isNetworkAvailable) {
                    // get images from service
                    val networkImages = if(mainBreed.isEmpty()) {
                        breedService.getBreedImages(breedName)
                    } else {
                        breedService.getSubBreedImages(mainBreed, breedName)
                    }

                    // insert into cache
                    networkImages.message?.let { breedDao.insertImages(breedName, mainBreed, it.convertToString()) }
                } else {
                    emit(DataState.error(NO_INTERNET_AVAILABLE))
                }

                // query the cache
                images = breedDao.getImages(breedName, mainBreed).convertToList()

                // emit list of images from cache
                emit(DataState.success(images))
            }
        } catch (e: Exception) {
            emit(DataState.error(e.message?: UNKNOWN_ERROR))
        }
    }
}