package dev.picon.android.miyabinano.domain.genai

import org.junit.Assert.assertEquals
import org.junit.Test

class BaseModelIdentityStateMachineTest {
    @Test
    fun availableBootstrapCapability_requestsIdentityLookup() {
        assertEquals(
            BaseModelIdentityState.Checking,
            BaseModelIdentityStateMachine.fromBootstrapReadiness(CapabilityReadiness.AVAILABLE)
        )
    }

    @Test
    fun downloadableBootstrapCapability_isNotReadyForIdentityLookup() {
        assertEquals(
            BaseModelIdentityState.NotReady,
            BaseModelIdentityStateMachine.fromBootstrapReadiness(CapabilityReadiness.DOWNLOADABLE)
        )
    }

    @Test
    fun unavailableBootstrapCapability_isDistinctFromUnknownIdentity() {
        assertEquals(
            BaseModelIdentityState.Unavailable,
            BaseModelIdentityStateMachine.fromBootstrapReadiness(CapabilityReadiness.UNAVAILABLE)
        )
    }
}
