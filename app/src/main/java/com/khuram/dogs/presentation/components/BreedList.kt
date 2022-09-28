package com.khuram.dogs.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.khuram.dogs.domain.model.Breed
import com.khuram.dogs.presentation.navigation.Screen
import com.khuram.dogs.presentation.ui.breed_list.BreedListEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi


@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun BreedList(
    loading: Boolean,
    breeds: List<Breed>,
    onChangeBreedsScrollPosition: (Int) -> Unit,
    onNavigateTo: (String) -> Unit,
    onToggleFavourites: (BreedListEvent) -> Unit
) {
    if (loading && breeds.isEmpty()) {
        ShimmerLoadingBreeds(imageHeight = 270.dp)
    }
    else if(breeds.isEmpty()) {
        NothingHere()
    }
    else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colors.surface)
        ) {
            LazyColumn {
                itemsIndexed(
                    items = breeds
                ) { index: Int, breed: Breed ->
                    onChangeBreedsScrollPosition(index)
                    BreedCard(
                        breed = breed,
                        showImages = {
                            val route = Screen.BreedImageList.route + "/${breed.name}?mainBreed=${breed.mainBreed}"
                            onNavigateTo(route)
                        },
                        showSubBreeds = {
                            val route = Screen.SubBreedList.route + "/${breed.name}"
                            onNavigateTo(route)
                        },
                        onToggleFavourites = onToggleFavourites
                    )
                }
            }
        }
    }
}