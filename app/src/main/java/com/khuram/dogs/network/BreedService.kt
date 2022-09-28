package com.khuram.dogs.network

import com.khuram.dogs.network.model.BreedImagesDto
import com.khuram.dogs.network.model.BreedListDto
import com.khuram.dogs.network.model.BreedRandomImageDto
import retrofit2.http.GET
import retrofit2.http.Path


interface BreedService {

    @GET("/api/breeds/list/all")
    suspend fun getBreeds(): BreedListDto

    @GET("/api/breed/{breedName}/images/random")
    suspend fun getRandomBreedImage(
        @Path("breedName") breed : String
    ): BreedRandomImageDto

    @GET("/api/breed/{breedName}/{subBreed}/images/random")
    suspend fun getRandomSubBreedImage(
        @Path("breedName") breed : String,
        @Path("subBreed") subBreed: String
    ): BreedRandomImageDto

    @GET("api/breed/{breed}/images")
    suspend fun getBreedImages(
        @Path("breed") breed : String
    ): BreedImagesDto

    @GET("api/breed/{breed}/{subBreed}/images")
    suspend fun getSubBreedImages(
        @Path("breed") breed : String,
        @Path("subBreed") subBreed: String
    ): BreedImagesDto
}