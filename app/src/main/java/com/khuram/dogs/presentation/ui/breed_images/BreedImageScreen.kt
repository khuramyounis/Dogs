package com.khuram.dogs.presentation.ui.breed_images

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter
import com.khuram.dogs.R
import com.khuram.dogs.presentation.ui.theme.AppTheme


@Composable
fun BreedImageScreen(
    isDarkTheme: Boolean,
    isNetworkAvailable: Boolean,
    image: String
) {
    AppTheme(
        darkTheme = isDarkTheme,
        isNetworkAvailable = isNetworkAvailable,
        displayProgressBar = false,
        dialogList = mutableListOf()
    ) {
        Image(
            painter = rememberAsyncImagePainter(image.ifEmpty { R.drawable.dog }),
            contentDescription = "fullscreen image",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxSize()
        )
    }
}