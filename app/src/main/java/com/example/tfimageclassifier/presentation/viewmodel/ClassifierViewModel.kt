package com.example.tfimageclassifier.presentation.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tfimageclassifier.domain.model.ClassificationResult
import com.example.tfimageclassifier.domain.usecase.ClassifyImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Sealed class modelling all possible UI states for the classifier screen. */
sealed interface ClassifierUiState {
    object Idle : ClassifierUiState
    object Loading : ClassifierUiState
    data class Success(val results: List<ClassificationResult>) : ClassifierUiState
    data class Error(val message: String) : ClassifierUiState
}

/**
 * ViewModel for image classification.
 * Survives configuration changes and exposes a [StateFlow] of [ClassifierUiState].
 */
@HiltViewModel
class ClassifierViewModel @Inject constructor(
    private val classifyImageUseCase: ClassifyImageUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ClassifierUiState>(ClassifierUiState.Idle)
    val uiState: StateFlow<ClassifierUiState> = _uiState.asStateFlow()

    /** Kick off classification for the provided [bitmap]. */
    fun classify(bitmap: Bitmap) {
        viewModelScope.launch {
            _uiState.value = ClassifierUiState.Loading
            _uiState.value = try {
                val results = classifyImageUseCase(bitmap)
                if (results.isEmpty()) {
                    ClassifierUiState.Error("No results above confidence threshold.")
                } else {
                    ClassifierUiState.Success(results)
                }
            } catch (e: Exception) {
                ClassifierUiState.Error(e.message ?: "Unknown error during classification.")
            }
        }
    }

    /** Reset to initial state (e.g. when user picks a new image). */
    fun reset() {
        _uiState.value = ClassifierUiState.Idle
    }
}
