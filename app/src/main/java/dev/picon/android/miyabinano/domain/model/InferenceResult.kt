package dev.picon.android.miyabinano.domain.model

import dev.picon.android.miyabinano.domain.genai.CapabilityPreparationFailure

sealed interface InferenceResult {
    data object Idle : InferenceResult
    data object Loading : InferenceResult
    data class Success(
        val outputText: String,
        val metrics: InferenceMetrics
    ) : InferenceResult
    data class Blocked(
        val message: String,
        val recoveryGuidance: String
    ) : InferenceResult
    data class Error(val failure: CapabilityPreparationFailure) : InferenceResult
}
