package dev.picon.android.miyabinano.domain.genai

sealed interface CapabilityPreparationState {
    val allowedActions: Set<CapabilityPreparationAction>

    data object Checking : CapabilityPreparationState {
        override val allowedActions = emptySet<CapabilityPreparationAction>()
    }

    data object Unavailable : CapabilityPreparationState {
        override val allowedActions = setOf(CapabilityPreparationAction.CHECK_STATUS)
    }

    data object Downloadable : CapabilityPreparationState {
        override val allowedActions = setOf(
            CapabilityPreparationAction.CHECK_STATUS,
            CapabilityPreparationAction.START_PROVISIONING
        )
    }

    data class Downloading(
        val totalBytes: Long,
        val downloadedBytes: Long
    ) : CapabilityPreparationState {
        override val allowedActions = emptySet<CapabilityPreparationAction>()

        val progress: Float
            get() = if (totalBytes > 0) {
                downloadedBytes.toFloat() / totalBytes.toFloat()
            } else {
                0f
            }
    }

    data object Available : CapabilityPreparationState {
        override val allowedActions = setOf(
            CapabilityPreparationAction.CHECK_STATUS,
            CapabilityPreparationAction.RUN_INFERENCE
        )
    }

    data class Failed(
        val failure: CapabilityPreparationFailure
    ) : CapabilityPreparationState {
        override val allowedActions = setOf(CapabilityPreparationAction.RETRY)
    }
}

enum class CapabilityPreparationAction {
    CHECK_STATUS,
    START_PROVISIONING,
    RUN_INFERENCE,
    RETRY
}

object CapabilityPreparationStateMachine {
    fun fromReadiness(readiness: CapabilityReadiness): CapabilityPreparationState =
        when (readiness) {
            CapabilityReadiness.UNAVAILABLE -> CapabilityPreparationState.Unavailable
            CapabilityReadiness.DOWNLOADABLE -> CapabilityPreparationState.Downloadable
            CapabilityReadiness.DOWNLOADING -> CapabilityPreparationState.Downloading(
                totalBytes = 0,
                downloadedBytes = 0
            )
            CapabilityReadiness.AVAILABLE -> CapabilityPreparationState.Available
            CapabilityReadiness.UNKNOWN -> CapabilityPreparationState.Failed(
                failure = CapabilityPreparationFailure(
                    category = CapabilityPreparationFailure.Category.UNKNOWN,
                    userMessage = "Capability readiness is unknown.",
                    recoveryGuidance = "Wait a moment and retry.",
                    technicalDetail = "Unknown capability readiness"
                )
            )
        }

    fun onProvisioningEvent(
        currentState: CapabilityPreparationState,
        event: CapabilityProvisioningEvent
    ): CapabilityPreparationState =
        when (event) {
            is CapabilityProvisioningEvent.Started -> CapabilityPreparationState.Downloading(
                totalBytes = event.totalBytes,
                downloadedBytes = 0
            )
            is CapabilityProvisioningEvent.Progress -> {
                if (currentState is CapabilityPreparationState.Downloading) {
                    currentState.copy(downloadedBytes = event.downloadedBytes)
                } else {
                    currentState
                }
            }
            CapabilityProvisioningEvent.Completed -> CapabilityPreparationState.Available
            is CapabilityProvisioningEvent.Failed -> CapabilityPreparationState.Failed(event.failure)
        }

    fun onFailure(failure: CapabilityPreparationFailure): CapabilityPreparationState =
        CapabilityPreparationState.Failed(failure)
}
