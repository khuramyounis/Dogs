package com.khuram.dogs.interactors.breed_list


import com.khuram.dogs.cache.AppDatabaseFake
import com.khuram.dogs.cache.BreedDaoFake
import com.khuram.dogs.cache.model.BreedEntity
import com.khuram.dogs.domain.model.Breed
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class SetFavouriteTest {

    private val appDatabase = AppDatabaseFake()

    //system in test
    private lateinit var setFavourite: SetFavourite
    private val breed = Breed("hound")

    //dependencies
    private lateinit var breedDao: BreedDaoFake

    @OptIn(DelicateCoroutinesApi::class)
    @BeforeEach
    fun setup() {

        breedDao = BreedDaoFake(appDatabase)

        //insert breed
        GlobalScope.launch {
            breedDao.insertBreeds(
                listOf(
                    BreedEntity(breed.name,"","","","",false)
                )
            )
        }

        // instantiate the system in test
        setFavourite = SetFavourite(breedDao)
    }

    /**
     * 1. Is the favourite flag set for the breed
     * 2. Has the favourite flag been updated for the breed
     * 3. Is the update confirmation then emitted as a FLOW from the cache to the UI
     */

    @Test
    fun getImagesFromNetwork_emitBreedsFromCache(): Unit = runBlocking {

        // confirm breeds favourite flag is set to false
        assert(!breedDao.isFavourite(breed.name, breed.mainBreed))

        // run use case
        val flowItems = setFavourite.execute(breed, true).toList()

        // first emission should be loading
        assert(flowItems[0].loading)

        // second emission should be result of insertion
        val confirmation = flowItems[1].data

        // confirm the insertion was successful
        assert(confirmation == true)

        // ensure loading is false
        assert(!flowItems[1].loading)
    }
}