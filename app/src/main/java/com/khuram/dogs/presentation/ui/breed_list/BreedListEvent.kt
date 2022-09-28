package com.khuram.dogs.presentation.ui.breed_list

import com.khuram.dogs.domain.model.Breed


sealed class BreedListEvent {

    object GetBreedsEvent: BreedListEvent()

    data class SetFavourite(
        val breed: Breed,
        val isFavourite: Boolean
    ): BreedListEvent()

    //restore after process death
    object RestoreStateEvent: BreedListEvent()
}