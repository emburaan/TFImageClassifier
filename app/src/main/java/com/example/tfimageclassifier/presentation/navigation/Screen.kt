package com.example.tfimageclassifier.presentation.navigation

/** Typesafe route definitions for Compose Navigation. */
sealed class Screen(val route: String) {
    object Home   : Screen("home")
    object Result : Screen("result")
}
