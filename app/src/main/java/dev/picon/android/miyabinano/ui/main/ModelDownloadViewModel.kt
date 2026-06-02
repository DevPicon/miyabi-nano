package dev.picon.android.miyabinano.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.picon.android.miyabinano.domain.genai.CapabilityPreparationClient
import dev.picon.android.miyabinano.domain.genai.CapabilityPreparationState
import dev.picon.android.miyabinano.domain.genai.CapabilityPreparationStateMachine
import dev.picon.android.miyabinano.domain.model.InferenceCapability
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ModelDownloadViewModel @Inject constructor(
    clients: List<@JvmSuppressWildcards CapabilityPreparationClient>
) : ViewModel() {
    private val clientsByCapability = clients.associateBy(CapabilityPreparationClient::capability)

    private val _states = MutableStateFlow<Map<InferenceCapability, CapabilityPreparationState>>(
        InferenceCapability.entries.associateWith {
            CapabilityPreparationState.Checking
        }
    )
    val states: StateFlow<Map<InferenceCapability, CapabilityPreparationState>> =
        _states.asStateFlow()

    init {
        refreshAll()
    }

    fun refreshAll() {
        clientsByCapability.keys.forEach(::checkStatus)
    }

    fun checkStatus(capability: InferenceCapability) {
        val client = clientsByCapability[capability] ?: return

        viewModelScope.launch {
            updateState(capability, CapabilityPreparationState.Checking)
            try {
                updateState(
                    capability,
                    CapabilityPreparationStateMachine.fromReadiness(
                        client.checkReadiness()
                    )
                )
            } catch (e: Exception) {
                updateState(
                    capability,
                    CapabilityPreparationStateMachine.onFailure(
                        e.message ?: "Failed to check capability status"
                    )
                )
            }
        }
    }

    fun startProvisioning(capability: InferenceCapability) {
        val client = clientsByCapability[capability] ?: return

        viewModelScope.launch {
            try {
                client.provision { event ->
                    _states.update { currentStates ->
                        val currentState =
                            currentStates[capability] ?: CapabilityPreparationState.Checking
                        currentStates + (
                            capability to CapabilityPreparationStateMachine.onProvisioningEvent(
                                currentState = currentState,
                                event = event
                            )
                        )
                    }
                }
                checkStatus(capability)
            } catch (e: Exception) {
                updateState(
                    capability,
                    CapabilityPreparationStateMachine.onFailure(
                        e.message ?: "Failed to provision capability"
                    )
                )
            }
        }
    }

    fun retry(capability: InferenceCapability) {
        checkStatus(capability)
    }

    private fun updateState(
        capability: InferenceCapability,
        state: CapabilityPreparationState
    ) {
        _states.update { it + (capability to state) }
    }
}
