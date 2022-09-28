package com.khuram.dogs.interactors.breed_images


import com.google.gson.GsonBuilder
import com.khuram.dogs.cache.AppDatabaseFake
import com.khuram.dogs.cache.BreedDaoFake
import com.khuram.dogs.cache.model.BreedEntity
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


class GetBreedImagesTest {

    private val appDatabase = AppDatabaseFake()
    private lateinit var mockWebServer: MockWebServer
    private lateinit var baseUrl: HttpUrl

    //system in test
    private lateinit var getImages: GetImages
    private val breedName = "hound"

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

        //insert breed
        GlobalScope.launch {
            breedDao.insertBreeds(
                listOf(
                    BreedEntity(breedName,"","","","",false)
                )
            )
        }

        // instantiate the system in test
        getImages = GetImages(
            breedService,
            breedDao
        )
    }

    /**
     * 1. Are the images retrieved from the network
     * 2. Are the images inserted into the cache
     * 3. Are the images then emitted as a FLOW from the cache to the UI
     */

    @Test
    fun getImagesFromNetwork_emitBreedsFromCache(): Unit = runBlocking {

        //condition the response
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockWebServerResponses.getImagesResponse)
        )

        // confirm the image cache is empty to start
        assert(breedDao.getImages(breedName, "").isEmpty())

        // run use case
        val flowItems = getImages.execute(true, breedName, "").toList()

        // confirm the cache is no longer empty
        assert(breedDao.getImages(breedName, "").isNotEmpty())

        // first emission should be loading
        assert(flowItems[0].loading)

        // second emission should be the list of images
        val images = flowItems[1].data
        assert((images?.size ?: 0) > 0)

        // confirm they are actually strings
        assert(images?.get(index = 0) is String)

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
        val flowItems = getImages.execute(true, breedName, "").toList()

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