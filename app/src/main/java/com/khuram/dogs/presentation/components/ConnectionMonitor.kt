package com.khuram.dogs.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.khuram.dogs.presentation.ui.theme.Black1


@Composable
fun ConnectionMonitor(
    darkTheme: Boolean,
    isNetworkAvailable: Boolean
) {
    if(!isNetworkAvailable) {
        val textColor = if(darkTheme) Color.White else Black1
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                "No Network Connection",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp),
                style = MaterialTheme.typography.h6,
                color = textColor
            )
        }
    }
}