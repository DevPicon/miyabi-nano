package dev.picon.android.miyabinano.ui.inference

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.picon.android.miyabinano.data.MetricsRepository
import dev.picon.android.miyabinano.domain.InferenceUseCase
import dev.picon.android.miyabinano.domain.genai.CapabilityPreparationClient
import dev.picon.android.miyabinano.domain.genai.CapabilityPreparationClientFactory
import dev.picon.android.miyabinano.domain.genai.CapabilityPreparationState
import dev.picon.android.miyabinano.domain.genai.CapabilityPreparationStateMachine
import dev.picon.android.miyabinano.domain.genai.toCapabilityPreparationFailure
import dev.picon.android.miyabinano.domain.model.InferenceCapability
import dev.picon.android.miyabinano.domain.model.InferenceResult
import dev.picon.android.miyabinano.domain.model.TestCase
import dev.picon.android.miyabinano.domain.repository.TestDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InferenceViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val inferenceUseCase: InferenceUseCase,
    private val metricsRepository: MetricsRepository,
    clientFactory: CapabilityPreparationClientFactory
) : ViewModel() {

    private val capability: InferenceCapability = InferenceCapability.valueOf(
        savedStateHandle.get<String>("capability") ?: InferenceCapability.SUMMARIZATION.name
    )
    private val preparationClient = clientFactory.create(capability)

    private val _uiState = MutableStateFlow(
        InferenceUiState(
            capability = capability,
            availableTestCases = TestDataRepository.getTestCases(capability)
        )
    )
    val uiState: StateFlow<InferenceUiState> = _uiState.asStateFlow()

    init {
        refreshPreparation()
    }

    fun refreshPreparation() {
        viewModelScope.launch {
            updatePreparationState(CapabilityPreparationState.Checking)
            try {
                updatePreparationState(
                    CapabilityPreparationStateMachine.fromReadiness(
                        preparationClient.checkReadiness()
                    )
                )
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                updatePreparationState(
                    CapabilityPreparationStateMachine.onFailure(
                        e.toCapabilityPreparationFailure()
                    )
                )
            }
        }
    }

    fun startProvisioning() {
        viewModelScope.launch {
            try {
                preparationClient.provision { event ->
                    updatePreparationState(
                        CapabilityPreparationStateMachine.onProvisioningEvent(
                            currentState = _uiState.value.preparationState,
                            event = event
                        )
                    )
                }
                refreshPreparation()
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                updatePreparationState(
                    CapabilityPreparationStateMachine.onFailure(
                        e.toCapabilityPreparationFailure()
                    )
                )
            }
        }
    }

    fun onInputTextChanged(text: String) {
        if (_uiState.value.preparationState !is CapabilityPreparationState.Available) return
        _uiState.update {
            it.copy(
                inputText = text,
                selectedTestCase = it.selectedTestCase?.takeIf { testCase ->
                    testCase.inputText == text
                }
            )
        }
    }

    fun onTestCaseSelected(testCase: TestCase) {
        if (_uiState.value.preparationState !is CapabilityPreparationState.Available) return
        _uiState.update {
            it.copy(
                selectedTestCase = testCase,
                inputText = testCase.inputText,
                showTestCaseSelector = false
            )
        }
    }

    fun onShowTestCaseSelector() {
        if (_uiState.value.preparationState !is CapabilityPreparationState.Available) return
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
                errorTechnicalDetail = null,
                blockingReason = null,
                recoveryGuidance = null
            )
        }
    }

    fun onRunInference() {
        if (_uiState.value.preparationState !is CapabilityPreparationState.Available) return
        val inputText = _uiState.value.inputText.trim()
        if (inputText.isEmpty()) {
            _uiState.update {
                it.copy(
                    error = "Please enter some text",
                    errorTechnicalDetail = null
                )
            }
            return
        }

        viewModelScope.launch {
            inferenceUseCase(capability, inputText, _uiState.value.selectedTestCase?.id)
                .collect { result ->
                when (result) {
                    is InferenceResult.Idle -> {
                        _uiState.update {
                            it.copy(
                                isProcessing = false,
                                error = null,
                                errorTechnicalDetail = null
                            )
                        }
                    }
                    is InferenceResult.Loading -> {
                        _uiState.update {
                            it.copy(
                                isProcessing = true,
                                error = null,
                                errorTechnicalDetail = null,
                                blockingReason = null,
                                recoveryGuidance = null,
                                outputText = "",
                                metrics = null
                            )
                        }
                    }
                    is InferenceResult.Success -> {
                        _uiState.update {
                            it.copy(
                                isProcessing = false,
                                outputText = result.outputText,
                                metrics = result.metrics,
                                error = null,
                                errorTechnicalDetail = null
                            )
                        }

                        val persistedMetrics = metricsRepository.saveMetrics(result.metrics)
                        _uiState.update { it.copy(metrics = persistedMetrics) }
                    }
                    is InferenceResult.Blocked -> {
                        _uiState.update {
                            it.copy(
                                isProcessing = false,
                                blockingReason = result.message,
                                recoveryGuidance = result.recoveryGuidance,
                                error = null,
                                errorTechnicalDetail = null,
                                outputText = "",
                                metrics = null
                            )
                        }
                    }
                    is InferenceResult.Error -> {
                        _uiState.update {
                            it.copy(
                                isProcessing = false,
                                error = "${result.failure.userMessage} ${result.failure.recoveryGuidance}",
                                errorTechnicalDetail = result.failure.technicalDetail,
                                blockingReason = null,
                                recoveryGuidance = null
                            )
                        }
                    }
                }
            }
        }
    }

    private fun updatePreparationState(state: CapabilityPreparationState) {
        _uiState.update { it.copy(preparationState = state) }
    }

    override fun onCleared() {
        preparationClient.close()
    }
}
