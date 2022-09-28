package com.khuram.dogs.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.khuram.dogs.R
import com.khuram.dogs.domain.model.Breed
import com.khuram.dogs.presentation.ui.breed_list.BreedListEvent
import com.khuram.dogs.presentation.ui.theme.Blue400
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
@Composable
fun BreedCard(
    breed: Breed,
    showImages: () -> Unit,
    showSubBreeds: () -> Unit,
    onToggleFavourites: (BreedListEvent) -> Unit
) {
    Card(
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        elevation = 8.dp
    ) {
        Column {
            Box(contentAlignment = Alignment.TopEnd) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(breed.randomImage)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.dog),
                    contentDescription = "dog image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(225.dp)
                        .clickable { showImages() }
                )
                FavouriteButton(
                    breed = breed,
                    isFavourite = breed.isFavourite,
                    onToggleFavourites = onToggleFavourites
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 8.dp, start = 8.dp, end = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = breed.name,
                    modifier = Modifier
                        .height(30.dp)
                        .padding(start = 5.dp)
                        .align(Alignment.CenterVertically),
                    style = MaterialTheme.typography.subtitle1,
                )
                if(breed.subBreeds.isNotEmpty()) {
                    Button(
                        modifier = Modifier
                            .height(40.dp)
                            .padding(end = 3.dp),
                        onClick = { showSubBreeds() },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Blue400)
                    ) {
                        var text = "${breed.subBreeds.size} sub breed"
                        if(breed.subBreeds.size > 1) text += "s"
                        Text(
                            text = text,
                            modifier = Modifier
                                .align(Alignment.CenterVertically),
                            style = MaterialTheme.typography.h6,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}