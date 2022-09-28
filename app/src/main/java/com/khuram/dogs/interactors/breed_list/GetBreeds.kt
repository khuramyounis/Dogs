package com.khuram.dogs.interactors.breed_list

import com.khuram.dogs.cache.BreedDao
import com.khuram.dogs.cache.model.toDomain
import com.khuram.dogs.domain.data.DataState
import com.khuram.dogs.domain.model.Breed
import com.khuram.dogs.domain.model.toEntityList
import com.khuram.dogs.network.BreedService
import com.khuram.dogs.network.model.toDomain
import com.khuram.dogs.util.NO_INTERNET_AVAILABLE
import com.khuram.dogs.util.UNKNOWN_ERROR
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class GetBreeds(
    private val breedService: BreedService,
    private val breedDao: BreedDao
) {
    fun execute(): Flow<DataState<List<Breed>>> = flow {

        try {
            emit(DataState.loading())

            // delay to show shimmer effect
            delay(2000)

            try {
                // get breeds from service
                val breeds = breedService.getBreeds().toDomain()

                // insert into cache
                breedDao.insertBreeds(breeds.toEntityList())
            } catch (e: Exception) {
                // there was a network issue
                emit(DataState.error(e.message?: NO_INTERNET_AVAILABLE))
            }

            // query the cache
            val cacheResult = breedDao.getBreeds()

            // emit breeds from cache
            emit(DataState.success(cacheResult.toDomain()))
        } catch (e: Exception) {
            emit(DataState.error(e.message?: UNKNOWN_ERROR))
        }
    }
}
