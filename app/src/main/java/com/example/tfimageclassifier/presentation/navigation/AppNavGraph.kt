package com.example.tfimageclassifier.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tfimageclassifier.presentation.ui.home.HomeScreen
import com.example.tfimageclassifier.presentation.ui.result.ResultScreen
import com.example.tfimageclassifier.presentation.viewmodel.ClassifierUiState
import com.example.tfimageclassifier.presentation.viewmodel.ClassifierViewModel

/**
 * Central navigation graph.
 * The ViewModel is scoped to the NavBackStackEntry of the Home screen so that
 * both Home and Result can share the same instance (Result reads the last state).
 */
@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) { backStackEntry ->
            val viewModel: ClassifierViewModel = hiltViewModel(backStackEntry)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            HomeScreen(
                uiState = uiState,
                onImageSelected = { bitmap -> viewModel.classify(bitmap) },
                onNavigateToResult = { navController.navigate(Screen.Result.route) },
                onReset = { viewModel.reset() }
            )
        }

        composable(Screen.Result.route) {
            // Retrieve the Home back-stack entry so we share the same ViewModel instance
            val homeEntry = navController.getBackStackEntry(Screen.Home.route)
            val viewModel: ClassifierViewModel = hiltViewModel(homeEntry)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            ResultScreen(
                uiState = uiState as? ClassifierUiState.Success,
                onNavigateBack = {
                    viewModel.reset()
                    navController.popBackStack(Screen.Home.route, inclusive = false)
                }
            )
        }
    }
}
