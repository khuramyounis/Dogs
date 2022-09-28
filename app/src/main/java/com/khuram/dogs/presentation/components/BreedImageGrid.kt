package com.khuram.dogs.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.runtime.Composable


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageGrid(
    items: List<String>,
    onNavigateTo: (String) -> Unit,
) {
    LazyVerticalGrid(
        cells = GridCells.Fixed(2),
        content = {
            items(items.size) { index ->
                ImageGridItem(
                    imageUrl = items[index],
                    onNavigateTo = onNavigateTo
                )
            }
        }
    )
}