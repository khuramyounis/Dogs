package com.khuram.dogs.presentation.ui.util

import androidx.compose.runtime.mutableStateListOf
import com.khuram.dogs.presentation.components.GenericDialogInfo
import com.khuram.dogs.presentation.components.PositiveAction


class DialogList {

    val list: MutableList<GenericDialogInfo> = mutableStateListOf()

    private fun removeHeadMessage() {
        if(list.isNotEmpty()) {
            list.removeFirst()
        }
    }

    fun appendErrorMessage(title: String, description: String){
        list.add(
            GenericDialogInfo.Builder()
                .title(title)
                .onDismiss(this::removeHeadMessage)
                .description(description)
                .positiveAction(
                    PositiveAction(
                        positiveButtonText = "Ok",
                        onPositiveAction = this::removeHeadMessage,
                    )
                )
                .build()
        )
    }
}