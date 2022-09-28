package com.khuram.dogs.interactors.breed_list

import com.khuram.dogs.cache.BreedDao
import com.khuram.dogs.domain.data.DataState
import com.khuram.dogs.domain.model.Breed
import com.khuram.dogs.util.UNKNOWN_ERROR
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception


class SetFavourite(
    private val breedDao: BreedDao
) {
    fun execute(
        breed: Breed,
        isFavourite: Boolean
    ): Flow<DataState<Boolean>> = flow {

        try {
            emit(DataState.loading())

            // update favourite value in cache
            breedDao.updateFavourite(breed.name, breed.mainBreed, isFavourite)

            // query the cache
            val cacheResult = breedDao.isFavourite(breed.name, breed.mainBreed)

            // emit favourite value from cache
            emit(DataState.success(cacheResult))
        } catch (e: Exception) {
            emit(DataState.error(e.message?: UNKNOWN_ERROR))
        }
    }
}
