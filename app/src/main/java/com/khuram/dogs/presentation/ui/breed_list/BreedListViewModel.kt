package com.khuram.dogs.presentation.ui.breed_list

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khuram.dogs.domain.model.Breed
import com.khuram.dogs.interactors.breed_list.GetBreeds
import com.khuram.dogs.interactors.breed_list.GetRandomImage
import com.khuram.dogs.interactors.breed_list.RestoreBreeds
import com.khuram.dogs.interactors.breed_list.SetFavourite
import com.khuram.dogs.presentation.ui.util.DialogList
import com.khuram.dogs.util.AN_ERROR_OCCURRED
import com.khuram.dogs.util.ConnectionManager
import com.khuram.dogs.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject


const val STATE_KEY_LIST_POSITION = "breed.state.query.list_position"

@HiltViewModel
class BreedListViewModel
@Inject
constructor(
    private val getBreeds: GetBreeds,
    private val getRandomImage: GetRandomImage,
    private val setFavourite: SetFavourite,
    private val restoreBreeds: RestoreBreeds,
    private val connectionManager: ConnectionManager,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    val breeds: MutableState<List<Breed>> = mutableStateOf(ArrayList())
    val loading = mutableStateOf(false)
    private var breedListScrollPosition = 0
    val dialogList = DialogList()

    init {
        savedStateHandle.get<Int>(STATE_KEY_LIST_POSITION)?.let { p ->
            setListScrollPosition(p)
        }

        if(breedListScrollPosition != 0){
            onTriggerEvent(BreedListEvent.RestoreStateEvent)
        }
        else{
            onTriggerEvent(BreedListEvent.GetBreedsEvent)
        }
    }

    fun onTriggerEvent(event: BreedListEvent) {
        viewModelScope.launch {
            try {
                when(event){
                    is BreedListEvent.GetBreedsEvent -> {
                        getBreeds()
                    }
                    is BreedListEvent.RestoreStateEvent -> {
                        restoreState()
                    }
                    is BreedListEvent.SetFavourite -> {
                        setFavourite(event.breed, event.isFavourite)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "launchJob: Exception: ${e}, ${e.cause}")
                e.printStackTrace()
            }
            finally {
                Log.d(TAG, "launchJob: finally called.")
            }
        }
    }

    private fun setFavourite(breed: Breed, isFavourite: Boolean) {
        setFavourite.execute(breed, isFavourite).onEach { dataState ->
            loading.value = dataState.loading

            dataState.data?.let { isFavourite ->
                breeds.value.find { breedInList ->
                    breedInList == breed
                }?.isFavourite = isFavourite
            }

            dataState.error?.let { error ->
                dialogList.appendErrorMessage(AN_ERROR_OCCURRED, error)
            }
        }.launchIn(viewModelScope)
    }

    private fun restoreState() {
        restoreBreeds.execute().onEach { dataState ->
            loading.value = dataState.loading

            dataState.data?.let { list ->
                breeds.value = list
            }

            dataState.error?.let { error ->
                dialogList.appendErrorMessage(AN_ERROR_OCCURRED, error)
            }
        }.launchIn(viewModelScope)
    }

    private fun getBreeds() {
        getBreeds.execute().onEach { dataState ->
            loading.value = dataState.loading

            dataState.data?.let { list ->
                breeds.value = list
                list.forEach { getRandomImage(it) }
            }

            dataState.error?.let { error ->
                Log.e(TAG, "getBreeds: $error")
                dialogList.appendErrorMessage(AN_ERROR_OCCURRED, error)
            }
        }.launchIn(viewModelScope)
    }

    private fun getRandomImage(breed: Breed) {
        getRandomImage.execute(
            isNetworkAvailable = connectionManager.isNetworkAvailable.value,
            breed = breed
        ).onEach { dataState ->
            loading.value = dataState.loading

            dataState.data?.let {
                breeds.value.find { breedInList ->
                    breedInList == breed
                }?.randomImage = it
            }

            dataState.error?.let { error ->
                Log.e(TAG, "getRandomImage: $error")
                dialogList.appendErrorMessage(AN_ERROR_OCCURRED, error)
            }
        }.launchIn(viewModelScope)
    }

    fun onChangeBreedsScrollPosition(position: Int){
        setListScrollPosition(position)
    }

    private fun setListScrollPosition(position: Int){
        breedListScrollPosition = position
        savedStateHandle[STATE_KEY_LIST_POSITION] = position
    }
}
