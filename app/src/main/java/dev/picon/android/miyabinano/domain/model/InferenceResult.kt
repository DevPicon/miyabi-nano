package dev.picon.android.miyabinano.domain.model

sealed interface InferenceResult {
    data object Idle : InferenceResult
    data object Loading : InferenceResult
    data class Success(
        val outputText: String,
        val metrics: InferenceMetrics
    ) : InferenceResult
    data class Error(val message: String) : InferenceResult
}
