package com.khuram.dogs.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.khuram.dogs.R
import com.khuram.dogs.presentation.navigation.Screen
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@Composable
fun ImageGridItem(
    imageUrl: String,
    onNavigateTo: (String) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp)
            .padding(6.dp),
        backgroundColor = Color.DarkGray,
        elevation = 12.dp,
        shape = RoundedCornerShape(6.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = rememberAsyncImagePainter(imageUrl.ifEmpty { R.drawable.dog }),
                contentDescription = "profile image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(225.dp)
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = rememberRipple(bounded = true, color = Color.Black),
                        onClick = {
                            val encodedUrl = URLEncoder.encode(imageUrl, StandardCharsets.UTF_8.toString())
                            val route = Screen.BreedImage.route + "/${encodedUrl}"
                            onNavigateTo(route)
                        }
                    )
            )
        }
    }
}