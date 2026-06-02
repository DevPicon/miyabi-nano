package dev.picon.android.miyabinano.ui.inference

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.picon.android.miyabinano.data.MetricsRepository
import dev.picon.android.miyabinano.domain.InferenceUseCase
import dev.picon.android.miyabinano.domain.model.InferenceCapability
import dev.picon.android.miyabinano.domain.model.InferenceResult
import dev.picon.android.miyabinano.domain.model.TestCase
import dev.picon.android.miyabinano.domain.repository.TestDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InferenceViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val inferenceUseCase: InferenceUseCase,
    private val metricsRepository: MetricsRepository
) : ViewModel() {

    private val capability: InferenceCapability = InferenceCapability.valueOf(
        savedStateHandle.get<String>("capability") ?: InferenceCapability.SUMMARIZATION.name
    )

    private val _uiState = MutableStateFlow(
        InferenceUiState(
            capability = capability,
            availableTestCases = TestDataRepository.getTestCases(capability)
        )
    )
    val uiState: StateFlow<InferenceUiState> = _uiState.asStateFlow()

    fun onInputTextChanged(text: String) {
        _uiState.update { it.copy(inputText = text) }
    }

    fun onTestCaseSelected(testCase: TestCase) {
        _uiState.update {
            it.copy(
                selectedTestCase = testCase,
                inputText = testCase.inputText,
                showTestCaseSelector = false
            )
        }
    }

    fun onShowTestCaseSelector() {
        _uiState.update { it.copy(showTestCaseSelector = true) }
    }

    fun onHideTestCaseSelector() {
        _uiState.update { it.copy(showTestCaseSelector = false) }
    }

    fun onClearInput() {
        _uiState.update {
            it.copy(
                inputText = "",
                selectedTestCase = null,
                outputText = "",
                metrics = null,
                error = null,
                blockingReason = null,
                recoveryGuidance = null
            )
        }
    }

    fun onRunInference() {
        val inputText = _uiState.value.inputText.trim()
        if (inputText.isEmpty()) {
            _uiState.update { it.copy(error = "Please enter some text") }
            return
        }

        viewModelScope.launch {
            inferenceUseCase(capability, inputText).collect { result ->
                when (result) {
                    is InferenceResult.Idle -> {
                        _uiState.update {
                            it.copy(
                                isProcessing = false,
                                error = null
                            )
                        }
                    }
                    is InferenceResult.Loading -> {
                        _uiState.update {
                            it.copy(
                                isProcessing = true,
                                error = null,
                                blockingReason = null,
                                recoveryGuidance = null,
                                outputText = "",
                                metrics = null
                            )
                        }
                    }
                    is InferenceResult.Success -> {
                        // Save metrics to database
                        metricsRepository.saveMetrics(result.metrics)

                        _uiState.update {
                            it.copy(
                                isProcessing = false,
                                outputText = result.outputText,
                                metrics = result.metrics,
                                error = null
                            )
                        }
                    }
                    is InferenceResult.Blocked -> {
                        _uiState.update {
                            it.copy(
                                isProcessing = false,
                                blockingReason = result.message,
                                recoveryGuidance = result.recoveryGuidance,
                                error = null
                            )
                        }
                    }
                    is InferenceResult.Error -> {
                        _uiState.update {
                            it.copy(
                                isProcessing = false,
                                error = result.message
                            )
                        }
                    }
                }
            }
        }
    }
}
