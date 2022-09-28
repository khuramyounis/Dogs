package com.khuram.dogs.interactors.breed_list

import com.google.gson.GsonBuilder
import com.khuram.dogs.cache.AppDatabaseFake
import com.khuram.dogs.cache.BreedDaoFake
import com.khuram.dogs.domain.model.Breed
import com.khuram.dogs.network.BreedService
import com.khuram.dogs.network.data.MockWebServerResponses
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection


class GetBreedsTest {

    private val appDatabase = AppDatabaseFake()
    private lateinit var mockWebServer: MockWebServer
    private lateinit var baseUrl: HttpUrl

    //system in test
    private lateinit var getBreeds: GetBreeds

    //dependencies
    private lateinit var breedService: BreedService
    private lateinit var breedDao: BreedDaoFake

    @BeforeEach
    fun setup() {

        mockWebServer = MockWebServer()
        mockWebServer.start()
        baseUrl = mockWebServer.url("/api/breeds/")
        breedService = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(BreedService::class.java)
        breedDao = BreedDaoFake(appDatabase)

        // instantiate the system in test
        getBreeds = GetBreeds(
            breedService,
            breedDao
        )
    }

    /**
     * 1. Are the breeds retrieved from the network
     * 2. Are the breeds inserted into the cache
     * 3. Are the breeds then emitted as a FLOW from the cache to the UI
     */

    @Test
    fun getBreedsFromNetwork_emitBreedsFromCache(): Unit = runBlocking {

        //condition the response
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockWebServerResponses.getBreedsResponse)
        )

        // confirm the cache is empty to start
        assert(breedDao.getBreeds().isEmpty())

        // run use case
        val flowItems = getBreeds.execute().toList()

        // confirm the cache is no longer empty
        assert(breedDao.getBreeds().isNotEmpty())

        // first emission should be loading
        assert(flowItems[0].loading)

        // second emission should be the list of breeds
        val breeds = flowItems[1].data
        assert((breeds?.size ?: 0) > 0)

        // confirm they are actually breed objects
        assert(breeds?.get(index = 0) is Breed)

        // ensure loading is false
        assert(!flowItems[1].loading)
    }

    /**
     * Simulate a bad request
     */
    @Test
    fun getBreedsFromNetwork_emitHttpError(): Unit = runBlocking {

        // condition the response
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .setBody("{}")
        )

        // run use case
        val flowItems = getBreeds.execute().toList()

        // first emission should be loading
        assert(flowItems[0].loading)

        // second emission should be the exception
        val error = flowItems[1].error
        assert(error != null)

        // ensure loading is false
        assert(!flowItems[1].loading)
    }

    @AfterEach
    fun tearDown() {

        mockWebServer.shutdown()
    }
}