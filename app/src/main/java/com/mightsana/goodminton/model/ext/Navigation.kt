package com.mightsana.goodminton.model.ext

import androidx.navigation.NavHostController

fun NavHostController.navigateAndPopUp(
    destination: String,
    current: String
) {
    navigate(destination) {
        launchSingleTop = true
        popUpTo(current) {
            inclusive = true
        }
    }
}

@Suppress("unused")
fun NavHostController.navigateSingleTop(
    destination: String
) {
    navigate(destination) {
        launchSingleTop = true
    }
}