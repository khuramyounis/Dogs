package com.khuram.dogs.interactors.breed_list

import com.google.gson.GsonBuilder
import com.khuram.dogs.cache.AppDatabaseFake
import com.khuram.dogs.cache.BreedDaoFake
import com.khuram.dogs.cache.model.BreedEntity
import com.khuram.dogs.domain.model.Breed
import com.khuram.dogs.network.BreedService
import com.khuram.dogs.network.data.MockWebServerResponses
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
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


class GetRandomImageTest {

    private val appDatabase = AppDatabaseFake()
    private lateinit var mockWebServer: MockWebServer
    private lateinit var baseUrl: HttpUrl

    //system in test
    private lateinit var getRandomImage: GetRandomImage
    private val breed = Breed("hound")
    private val imageUrl = "https://images.dog.ceo/breeds/hound-blood/n02088466_7417.jpg"

    //dependencies
    private lateinit var breedService: BreedService
    private lateinit var breedDao: BreedDaoFake

    @OptIn(DelicateCoroutinesApi::class)
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
        getRandomImage = GetRandomImage(
            breedService,
            breedDao
        )

        //insert breed
        GlobalScope.launch {
            breedDao.insertBreeds(
                listOf(
                    BreedEntity(breed.name,"","","","",false)
                )
            )
        }
    }

    /**
     * 1. Is the image retrieved from the network
     * 2. Is the image inserted into the cache
     * 3. Is the image then emitted as a FLOW from the cache to the UI
     */

    @Test
    fun getRandomImageFromNetwork_emitStringFromCache(): Unit = runBlocking {

        //condition the response
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockWebServerResponses.getRandomImageResponse)
        )

        // confirm the cache is empty to start
        assert(breedDao.getRandomImage(breed.name, breed.mainBreed).isEmpty())

        // run use case
        val flowItems = getRandomImage.execute(true, breed).toList()

        // insert into cache
        flowItems[1].data?.let { breedDao.insertRandomImage(breed.name, breed.mainBreed, it) }

        // confirm the cache is no longer empty
        assert(breedDao.getRandomImage(breed.name, breed.mainBreed).isNotEmpty())

        // first emission should be loading
        assert(flowItems[0].loading)

        // second emission should be the random image string
        val randomImage = flowItems[1].data
        assert(randomImage == imageUrl)

        // confirm it is a string
        assert(randomImage is String)

        // ensure loading is false
        assert(!flowItems[1].loading)
    }

    @Test
    fun getBreedImagesFromNetwork_emitHttpError(): Unit = runBlocking {

        //condition the response
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .setBody("{}")
        )

        // run use case
        val flowItems = getRandomImage.execute(false, breed).toList()

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