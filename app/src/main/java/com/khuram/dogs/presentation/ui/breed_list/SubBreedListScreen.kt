package com.khuram.dogs.presentation.ui.breed_list

import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import com.khuram.dogs.presentation.components.AppBar
import com.khuram.dogs.presentation.components.BreedList
import com.khuram.dogs.presentation.ui.theme.AppTheme


@Composable
fun SubBreedListScreen(
    isDarkTheme: Boolean,
    isNetworkAvailable: Boolean,
    onToggleTheme: () -> Unit,
    onNavigateTo: (String) -> Unit,
    viewModel: BreedListViewModel,
    breedName: String
) {
    val breeds = viewModel.breeds.value.toMutableList().apply {
        removeAll { breed -> breed.mainBreed != breedName }
    }
    val loading = viewModel.loading.value
    val scaffoldState = rememberScaffoldState()
    val dialogList = viewModel.dialogList

    AppTheme(
        darkTheme = isDarkTheme,
        isNetworkAvailable = isNetworkAvailable,
        displayProgressBar = loading,
        dialogList = dialogList.list
    ) {
        Scaffold(
            topBar = {
                AppBar(
                    "$breedName sub breeds",
                    onToggleTheme = { onToggleTheme() },
                    false
                )
            },
            scaffoldState = scaffoldState,
            snackbarHost = {
                scaffoldState.snackbarHostState
            }
        ) {
            BreedList(
                loading = loading,
                breeds = breeds,
                onChangeBreedsScrollPosition = viewModel::onChangeBreedsScrollPosition,
                onNavigateTo = onNavigateTo,
                onToggleFavourites = viewModel::onTriggerEvent
            )
        }
    }
}