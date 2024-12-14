package com.mightsana.goodminton.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ErrorSupportingText(
    visible: Boolean = true,
    message: String,
) {
    AnimatedVisibility(visible) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error
        )
    }
}