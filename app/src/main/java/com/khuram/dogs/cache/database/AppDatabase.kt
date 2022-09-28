package com.khuram.dogs.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.khuram.dogs.cache.BreedDao
import com.khuram.dogs.cache.model.BreedEntity


@Database(entities = [BreedEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun breedDao(): BreedDao

    companion object{
        const val DATABASE_NAME: String = "breed_db"
    }
}