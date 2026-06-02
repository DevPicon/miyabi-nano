package dev.picon.android.miyabinano.domain.genai

sealed interface BaseModelIdentityState {
    data object Checking : BaseModelIdentityState

    data object NotReady : BaseModelIdentityState

    data class Available(val name: String) : BaseModelIdentityState

    data object Unavailable : BaseModelIdentityState

    data class Failed(val failure: CapabilityPreparationFailure) : BaseModelIdentityState
}

object BaseModelIdentityStateMachine {
    fun fromBootstrapReadiness(readiness: CapabilityReadiness): BaseModelIdentityState =
        when (readiness) {
            CapabilityReadiness.AVAILABLE -> BaseModelIdentityState.Checking
            CapabilityReadiness.UNAVAILABLE -> BaseModelIdentityState.Unavailable
            CapabilityReadiness.DOWNLOADABLE,
            CapabilityReadiness.DOWNLOADING,
            CapabilityReadiness.UNKNOWN -> BaseModelIdentityState.NotReady
        }
}
