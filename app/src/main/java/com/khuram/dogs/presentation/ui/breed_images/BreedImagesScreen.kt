package com.khuram.dogs.presentation.ui.breed_images

import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import com.khuram.dogs.presentation.components.AppBar
import com.khuram.dogs.presentation.components.ImageGrid
import com.khuram.dogs.presentation.ui.theme.AppTheme


@Composable
fun BreedImagesScreen(
    isDarkTheme: Boolean,
    isNetworkAvailable: Boolean,
    onToggleTheme: () -> Unit,
    viewModel: BreedImagesViewModel,
    onNavigateTo: (String) -> Unit,
    breedName: String,
    mainBreed: String
) {
    val onLoad = viewModel.onLoad.value
    if(!onLoad) {
        viewModel.onLoad.value = true
        viewModel.onTriggerEvent(BreedImagesEvent.GetImages(breedName, mainBreed))
    }
    val images = viewModel.images.value
    val loading = viewModel.loading.value
    val scaffoldState = rememberScaffoldState()
    val dialogList = viewModel.dialogList

    AppTheme(
        darkTheme = isDarkTheme,
        isNetworkAvailable = isNetworkAvailable,
        displayProgressBar = loading,
        dialogList = dialogList.list
    ) {
        var appBarText = "Images for $breedName "
        if(mainBreed.isNotEmpty()) appBarText += mainBreed

        Scaffold(
            topBar = {
                AppBar(
                    appBarText,
                    onToggleTheme = { onToggleTheme() },
                    false
                )
            },
            scaffoldState = scaffoldState,
            snackbarHost = {
                scaffoldState.snackbarHostState
            }
        ) {
            images?.let {
                ImageGrid(
                    items = it,
                    onNavigateTo = onNavigateTo
                )
            }
        }
    }
}