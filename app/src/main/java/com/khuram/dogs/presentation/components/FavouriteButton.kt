package com.khuram.dogs.presentation.components

import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import com.khuram.dogs.domain.model.Breed
import com.khuram.dogs.presentation.ui.breed_list.BreedListEvent
import com.khuram.dogs.presentation.ui.theme.Blue600


@Composable
fun FavouriteButton(
    breed: Breed,
    isFavourite: Boolean,
    onToggleFavourites: (BreedListEvent) -> Unit
) {
    var isFav by remember { mutableStateOf(isFavourite) }
    IconToggleButton(
        checked = isFav,
        onCheckedChange = {
            isFav = !isFav
            onToggleFavourites(BreedListEvent.SetFavourite(breed, isFav))
        }
    ) {
        Icon(
            modifier = Modifier.scale(1.5f),
            tint = Blue600,
            imageVector = if (isFav) {
                Icons.Filled.Favorite
            } else {
                Icons.Default.FavoriteBorder
            },
            contentDescription = null
        )
    }
}
