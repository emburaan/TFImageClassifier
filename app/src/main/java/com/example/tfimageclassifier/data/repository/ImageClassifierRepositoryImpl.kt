package com.example.tfimageclassifier.data.repository

import android.graphics.Bitmap
import com.example.tfimageclassifier.data.local.TFLiteImageClassifier
import com.example.tfimageclassifier.domain.model.ClassificationResult
import com.example.tfimageclassifier.domain.repository.ImageClassifierRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Concrete implementation of [ImageClassifierRepository].
 *
 * Bridges the domain layer to the data source (TFLiteImageClassifier).
 * Offloads inference to the IO dispatcher to keep the main thread free.
 */
@Singleton
class ImageClassifierRepositoryImpl @Inject constructor(
    private val classifier: TFLiteImageClassifier
) : ImageClassifierRepository {

    override suspend fun classify(bitmap: Bitmap): List<ClassificationResult> =
        withContext(Dispatchers.IO) {
            classifier.classify(bitmap)
        }
}
