package com.example.tfimageclassifier.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.tfimageclassifier.presentation.navigation.AppNavGraph
import com.example.tfimageclassifier.presentation.theme.TFClassifierTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Single entry-point Activity.
 * All UI is rendered by Compose; navigation is handled by AppNavGraph.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TFClassifierTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    AppNavGraph(navController = navController)
                }
            }
        }
    }
}
