package dev.picon.android.miyabinano.domain.genai

sealed interface CapabilityInferenceAccess {
    data object Allowed : CapabilityInferenceAccess

    data class Blocked(
        val message: String,
        val recoveryGuidance: String
    ) : CapabilityInferenceAccess
}

fun CapabilityReadiness.toInferenceAccess(): CapabilityInferenceAccess =
    when (this) {
        CapabilityReadiness.AVAILABLE -> CapabilityInferenceAccess.Allowed
        CapabilityReadiness.UNAVAILABLE -> CapabilityInferenceAccess.Blocked(
            message = "This capability is not supported by the current device configuration.",
            recoveryGuidance = "Choose another experiment or check status again later."
        )
        CapabilityReadiness.DOWNLOADABLE -> CapabilityInferenceAccess.Blocked(
            message = "This capability needs additional on-device assets.",
            recoveryGuidance = "Return to the home screen and download the capability before running inference."
        )
        CapabilityReadiness.DOWNLOADING -> CapabilityInferenceAccess.Blocked(
            message = "This capability is still downloading.",
            recoveryGuidance = "Wait for provisioning to finish, then try again."
        )
        CapabilityReadiness.UNKNOWN -> CapabilityInferenceAccess.Blocked(
            message = "The capability readiness could not be determined.",
            recoveryGuidance = "Wait a moment, refresh status, and try again."
        )
    }
