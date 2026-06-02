package dev.picon.android.miyabinano.domain.genai

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CapabilityPreparationStateMachineTest {
    @Test
    fun downloadableState_allowsProvisioning() {
        val state = CapabilityPreparationStateMachine.fromReadiness(
            CapabilityReadiness.DOWNLOADABLE
        )

        assertEquals(CapabilityPreparationState.Downloadable, state)
        assertTrue(
            CapabilityPreparationAction.START_PROVISIONING in state.allowedActions
        )
    }

    @Test
    fun provisioningEvents_moveDownloadingStateToAvailable() {
        val started = CapabilityPreparationStateMachine.onProvisioningEvent(
            currentState = CapabilityPreparationState.Downloadable,
            event = CapabilityProvisioningEvent.Started(totalBytes = 200)
        )
        val progressed = CapabilityPreparationStateMachine.onProvisioningEvent(
            currentState = started,
            event = CapabilityProvisioningEvent.Progress(downloadedBytes = 50)
        )
        val completed = CapabilityPreparationStateMachine.onProvisioningEvent(
            currentState = progressed,
            event = CapabilityProvisioningEvent.Completed
        )

        assertEquals(
            CapabilityPreparationState.Downloading(
                totalBytes = 200,
                downloadedBytes = 50
            ),
            progressed
        )
        assertEquals(CapabilityPreparationState.Available, completed)
    }

    @Test
    fun failureState_allowsRetry() {
        val failure = CapabilityPreparationFailure(
            category = CapabilityPreparationFailure.Category.UNKNOWN,
            userMessage = "Capability preparation failed.",
            recoveryGuidance = "Retry.",
            technicalDetail = "AICore busy"
        )
        val state = CapabilityPreparationStateMachine.onFailure(failure)

        assertEquals(
            CapabilityPreparationState.Failed(failure),
            state
        )
        assertTrue(CapabilityPreparationAction.RETRY in state.allowedActions)
    }
}
