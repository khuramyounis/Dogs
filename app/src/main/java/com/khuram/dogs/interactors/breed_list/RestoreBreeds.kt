package com.khuram.dogs.interactors.breed_list

import android.util.Log
import com.khuram.dogs.cache.BreedDao
import com.khuram.dogs.cache.model.toDomain
import com.khuram.dogs.domain.data.DataState
import com.khuram.dogs.domain.model.Breed
import com.khuram.dogs.util.TAG
import com.khuram.dogs.util.UNKNOWN_ERROR
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception


class RestoreBreeds(
    private val breedDao: BreedDao
) {
    fun execute(): Flow<DataState<List<Breed>>> = flow {

        try {
            emit(DataState.loading())

            // get breeds from cache
            val cacheResult = breedDao.getBreeds()

            // emit breeds from cache
            emit(DataState.success(cacheResult.toDomain()))
        } catch (e: Exception) {
            Log.e(TAG, "execute: ${e.message}")
            emit(DataState.error(e.message?: UNKNOWN_ERROR))
        }
    }
}