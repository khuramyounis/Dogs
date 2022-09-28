package com.khuram.dogs.presentation.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout


@Composable
fun CircularIndeterminateProgressBar(
    isDisplayed: Boolean
) {
    if(isDisplayed) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val progressBar = createRef()
            val topGuideline = createGuidelineFromTop(0.5f)
            CircularProgressIndicator(
                color = MaterialTheme.colors.primary,
                modifier = Modifier.constrainAs(progressBar) {
                    top.linkTo(topGuideline)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
        }
    }
}