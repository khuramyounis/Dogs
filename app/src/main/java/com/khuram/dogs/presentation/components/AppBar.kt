package com.khuram.dogs.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.khuram.dogs.presentation.ui.theme.Blue600


@Composable
fun AppBar(
    text: String,
    onToggleTheme: () -> Unit,
    showIcons: Boolean,
    onNavigateToFavourites: () -> Unit = {}
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colors.surface,
        elevation = 8.dp
    ) {
        Column {
            Box(
                modifier = Modifier.height(55.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Start,
                ) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 15.dp),
                        text = text,
                        style = MaterialTheme.typography.subtitle1,
                    )
                }
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.End
                ) {
                    if(showIcons) {
                        IconButton(
                            onClick = onNavigateToFavourites,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        ) {
                            Icon(
                                Icons.Filled.Favorite,
                                null,
                                tint = Blue600
                            )
                        }
                        IconButton(
                            onClick = onToggleTheme,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        ) {
                            Icon(
                                Icons.Filled.MoreVert,
                                null,
                                tint = Blue600
                            )
                        }
                    }
                }
            }
        }
    }
}