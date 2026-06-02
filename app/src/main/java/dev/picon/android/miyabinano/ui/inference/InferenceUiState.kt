package dev.picon.android.miyabinano.ui.inference

import dev.picon.android.miyabinano.domain.model.InferenceCapability
import dev.picon.android.miyabinano.domain.model.InferenceMetrics
import dev.picon.android.miyabinano.domain.model.TestCase
import dev.picon.android.miyabinano.domain.genai.CapabilityPreparationState

data class InferenceUiState(
    val capability: InferenceCapability,
    val preparationState: CapabilityPreparationState = CapabilityPreparationState.Checking,
    val inputText: String = "",
    val outputText: String = "",
    val isProcessing: Boolean = false,
    val error: String? = null,
    val blockingReason: String? = null,
    val recoveryGuidance: String? = null,
    val metrics: InferenceMetrics? = null,
    val selectedTestCase: TestCase? = null,
    val availableTestCases: List<TestCase> = emptyList(),
    val showTestCaseSelector: Boolean = false
)
