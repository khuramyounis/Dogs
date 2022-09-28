package com.khuram.dogs.presentation.ui.breed_images

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khuram.dogs.interactors.breed_images.GetImages
import com.khuram.dogs.presentation.ui.util.DialogList
import com.khuram.dogs.util.AN_ERROR_OCCURRED
import com.khuram.dogs.util.ConnectionManager
import com.khuram.dogs.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BreedImagesViewModel
@Inject
constructor(
    private val getImages: GetImages,
    private val connectionManager: ConnectionManager
): ViewModel() {

    val images: MutableState<List<String>?> = mutableStateOf(null)
    val loading = mutableStateOf(false)
    val onLoad: MutableState<Boolean> = mutableStateOf(false)
    val dialogList = DialogList()

    fun onTriggerEvent(event: BreedImagesEvent){
        viewModelScope.launch {
            try {
                when(event){
                    is BreedImagesEvent.GetImages -> {
                        if(images.value == null){
                            getImages(event.breed, event.mainBreed)
                        }
                    }
                }
            } catch (e: Exception){
                Log.e(TAG, "launchJob: Exception: ${e}, ${e.cause}")
                e.printStackTrace()
            }
        }
    }

    private fun getImages(breedName: String, mainBreed: String) {
        getImages.execute(
            isNetworkAvailable = connectionManager.isNetworkAvailable.value,
            breedName = breedName,
            mainBreed = mainBreed
        ).onEach { dataState ->
            loading.value = dataState.loading

            dataState.data?.let { data ->
                images.value = data
            }

            dataState.error?.let { error ->
                Log.e(TAG, "getImages: $error")
                dialogList.appendErrorMessage(AN_ERROR_OCCURRED, error)
            }
        }.launchIn(viewModelScope)
    }
}