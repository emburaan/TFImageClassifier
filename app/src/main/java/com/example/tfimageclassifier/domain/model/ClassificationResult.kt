package com.example.tfimageclassifier.domain.model

/**
 * Domain model representing a single classification prediction.
 *
 * @param label  Human-readable class label (e.g. "Golden Retriever")
 * @param score  Confidence score in range [0.0, 1.0]
 */
data class ClassificationResult(
    val label: String,
    val score: Float
) {
    /** Confidence as a percentage string, e.g. "87.3%" */
    val scorePercent: String get() = "%.1f%%".format(score * 100)
}
