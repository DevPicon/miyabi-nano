package dev.picon.android.miyabinano.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.picon.android.miyabinano.domain.genai.BaseModelIdentityState
import dev.picon.android.miyabinano.domain.genai.BaseModelIdentityStateMachine
import dev.picon.android.miyabinano.domain.genai.CapabilityPreparationClientFactory
import dev.picon.android.miyabinano.domain.genai.CapabilityPreparationState
import dev.picon.android.miyabinano.domain.genai.CapabilityPreparationStateMachine
import dev.picon.android.miyabinano.domain.genai.CapabilityReadiness
import dev.picon.android.miyabinano.domain.genai.toCapabilityPreparationFailure
import dev.picon.android.miyabinano.domain.model.InferenceCapability
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ModelDownloadViewModel @Inject constructor(
    clientFactory: CapabilityPreparationClientFactory
) : ViewModel() {
    private val bootstrapClient = clientFactory.create(InferenceCapability.SUMMARIZATION)

    private val _bootstrapState =
        MutableStateFlow<CapabilityPreparationState>(CapabilityPreparationState.Checking)
    val bootstrapState: StateFlow<CapabilityPreparationState> = _bootstrapState.asStateFlow()

    private val _baseModelIdentity =
        MutableStateFlow<BaseModelIdentityState>(BaseModelIdentityState.Checking)
    val baseModelIdentity: StateFlow<BaseModelIdentityState> =
        _baseModelIdentity.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _bootstrapState.value = CapabilityPreparationState.Checking
            try {
                val readiness = bootstrapClient.checkReadiness()
                _bootstrapState.value = CapabilityPreparationStateMachine.fromReadiness(readiness)
                updateBaseModelIdentity(readiness)
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                val failure = e.toCapabilityPreparationFailure()
                _bootstrapState.value = CapabilityPreparationStateMachine.onFailure(failure)
                _baseModelIdentity.value = BaseModelIdentityState.Failed(failure)
            }
        }
    }

    fun startProvisioning() {
        viewModelScope.launch {
            try {
                bootstrapClient.provision { event ->
                    _bootstrapState.value = CapabilityPreparationStateMachine.onProvisioningEvent(
                        currentState = _bootstrapState.value,
                        event = event
                    )
                }
                refresh()
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                _bootstrapState.value = CapabilityPreparationStateMachine.onFailure(
                    e.toCapabilityPreparationFailure()
                )
            }
        }
    }

    private suspend fun updateBaseModelIdentity(readiness: CapabilityReadiness) {
        _baseModelIdentity.value = BaseModelIdentityStateMachine.fromBootstrapReadiness(readiness)
        if (readiness != CapabilityReadiness.AVAILABLE) return

        try {
            _baseModelIdentity.value =
                BaseModelIdentityState.Available(bootstrapClient.getBaseModelName())
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            _baseModelIdentity.value = BaseModelIdentityState.Failed(
                e.toCapabilityPreparationFailure()
            )
        }
    }

    override fun onCleared() {
        bootstrapClient.close()
    }
}
