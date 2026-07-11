package com.example.tfimageclassifier.domain.usecase

import android.graphics.Bitmap
import com.example.tfimageclassifier.domain.model.ClassificationResult
import com.example.tfimageclassifier.domain.repository.ImageClassifierRepository
import javax.inject.Inject

/**
 * Single-responsibility use case: classify one image.
 *
 * Encapsulates business logic (e.g. minimum confidence threshold, top-N filtering)
 * so the ViewModel and Repository stay thin.
 */
class ClassifyImageUseCase @Inject constructor(
    private val repository: ImageClassifierRepository
) {
    companion object {
        private const val MIN_CONFIDENCE = 0.05f   // ignore very low-confidence labels
        private const val TOP_N = 5                 // return at most 5 results
    }

    /**
     * Classify [bitmap] and return filtered, top-N results.
     * Throws on error — caller (ViewModel) maps to UI state.
     */
    suspend operator fun invoke(bitmap: Bitmap): List<ClassificationResult> {
        return repository.classify(bitmap)
            .filter { it.score >= MIN_CONFIDENCE }
            .sortedByDescending { it.score }
            .take(TOP_N)
    }
}
