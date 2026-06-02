package dev.picon.android.miyabinano.domain.genai

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CapabilityInferenceAccessTest {
    @Test
    fun availableCapability_allowsInference() {
        assertEquals(
            CapabilityInferenceAccess.Allowed,
            CapabilityReadiness.AVAILABLE.toInferenceAccess()
        )
    }

    @Test
    fun downloadableCapability_requiresProvisioning() {
        val access = CapabilityReadiness.DOWNLOADABLE.toInferenceAccess()

        assertTrue(access is CapabilityInferenceAccess.Blocked)
        access as CapabilityInferenceAccess.Blocked
        assertTrue("download" in access.recoveryGuidance.lowercase())
    }

    @Test
    fun unavailableCapability_doesNotSuggestDownloading() {
        val access = CapabilityReadiness.UNAVAILABLE.toInferenceAccess()

        assertTrue(access is CapabilityInferenceAccess.Blocked)
        access as CapabilityInferenceAccess.Blocked
        assertTrue("not supported" in access.message.lowercase())
        assertTrue("download" !in access.recoveryGuidance.lowercase())
    }
}
