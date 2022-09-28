package com.khuram.dogs.di

import com.google.gson.GsonBuilder
import com.khuram.dogs.network.BreedService
import com.khuram.dogs.util.SERVICE_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideBreedService(): BreedService {
        return Retrofit.Builder()
            .baseUrl(SERVICE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(BreedService::class.java)
    }
}