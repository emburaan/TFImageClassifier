package com.example.tfimageclassifier.domain.repository

import android.graphics.Bitmap
import com.example.tfimageclassifier.domain.model.ClassificationResult

/**
 * Contract for image classification.
 * The domain layer depends ONLY on this interface — never on the data layer directly.
 */
interface ImageClassifierRepository {

    /**
     * Runs inference on the provided [bitmap] and returns a list of top predictions
     * sorted by descending confidence score.
     */
    suspend fun classify(bitmap: Bitmap): List<ClassificationResult>
}
