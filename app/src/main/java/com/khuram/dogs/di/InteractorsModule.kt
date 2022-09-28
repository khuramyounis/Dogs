package com.khuram.dogs.di

import com.khuram.dogs.cache.BreedDao
import com.khuram.dogs.interactors.breed_images.GetImages
import com.khuram.dogs.interactors.breed_list.GetBreeds
import com.khuram.dogs.interactors.breed_list.GetRandomImage
import com.khuram.dogs.interactors.breed_list.RestoreBreeds
import com.khuram.dogs.interactors.breed_list.SetFavourite
import com.khuram.dogs.network.BreedService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
class InteractorsModule {

    @ViewModelScoped
    @Provides
    fun provideGetBreeds(breedService: BreedService, breedDao: BreedDao): GetBreeds {
        return GetBreeds(breedService = breedService, breedDao = breedDao)
    }

    @ViewModelScoped
    @Provides
    fun provideGetImages(breedService: BreedService, breedDao: BreedDao): GetImages {
        return GetImages(breedService = breedService, breedDao = breedDao)
    }

    @ViewModelScoped
    @Provides
    fun provideGetRandomImage(breedService: BreedService, breedDao: BreedDao): GetRandomImage {
        return GetRandomImage(breedService = breedService, breedDao = breedDao)
    }

    @ViewModelScoped
    @Provides
    fun provideRestoreBreeds(breedDao: BreedDao): RestoreBreeds {
        return RestoreBreeds(breedDao)
    }

    @ViewModelScoped
    @Provides
    fun setFavourite(breedDao: BreedDao): SetFavourite {
        return SetFavourite(breedDao)
    }
}