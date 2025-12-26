package dev.picon.android.miyabinano.ui.inference

import dev.picon.android.miyabinano.domain.model.InferenceCapability
import dev.picon.android.miyabinano.domain.model.InferenceMetrics
import dev.picon.android.miyabinano.domain.model.TestCase

data class InferenceUiState(
    val capability: InferenceCapability,
    val inputText: String = "",
    val outputText: String = "",
    val isProcessing: Boolean = false,
    val error: String? = null,
    val metrics: InferenceMetrics? = null,
    val selectedTestCase: TestCase? = null,
    val availableTestCases: List<TestCase> = emptyList(),
    val showTestCaseSelector: Boolean = false
)
