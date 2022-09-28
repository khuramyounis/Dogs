package com.khuram.dogs.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.khuram.dogs.cache.model.BreedEntity


@Dao
interface BreedDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBreeds(breeds: List<BreedEntity>)

    @Query("SELECT * FROM breeds")
    suspend fun getBreeds(): List<BreedEntity>

    @Query("UPDATE breeds SET randomImage = :randomImage WHERE name =:breedName AND mainBreed = :mainBreed")
    suspend fun insertRandomImage(breedName: String, mainBreed: String, randomImage: String)

    @Query("SELECT randomImage FROM breeds WHERE name = :breedName AND mainBreed = :mainBreed")
    suspend fun getRandomImage(breedName: String, mainBreed: String): String

    @Query("UPDATE breeds SET images = :images WHERE name =:breedName AND mainBreed = :mainBreed")
    suspend fun insertImages(breedName: String, mainBreed: String, images: String)

    @Query("SELECT images FROM breeds WHERE name = :breedName AND mainBreed = :mainBreed")
    suspend fun getImages(breedName: String, mainBreed: String): String

    @Query("UPDATE breeds SET isFavourite = :isFavourite WHERE name =:breedName AND mainBreed = :mainBreed")
    suspend fun updateFavourite(breedName: String, mainBreed: String, isFavourite: Boolean)

    @Query("SELECT isFavourite FROM breeds WHERE name = :breedName AND mainBreed = :mainBreed")
    suspend fun isFavourite(breedName: String, mainBreed: String): Boolean
}