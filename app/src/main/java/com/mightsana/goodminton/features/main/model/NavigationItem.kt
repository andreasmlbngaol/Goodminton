package com.mightsana.goodminton.features.main.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationItem(
    val iconSelected: ImageVector,
    val iconUnselected: ImageVector,
    val label: String,
    val route: String,
    val badgeCount: Int? = null,
    val content: @Composable () -> Unit = {}
)