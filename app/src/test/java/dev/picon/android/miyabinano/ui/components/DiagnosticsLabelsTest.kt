package dev.picon.android.miyabinano.ui.components

import dev.picon.android.miyabinano.domain.genai.BaseModelIdentityState
import dev.picon.android.miyabinano.domain.genai.CapabilityPreparationFailure
import dev.picon.android.miyabinano.domain.genai.CapabilityPreparationState
import org.junit.Assert.assertEquals
import org.junit.Test

class DiagnosticsLabelsTest {
    @Test
    fun preparationFailure_exposesCategoryAndTechnicalDetail() {
        val state = CapabilityPreparationState.Failed(failure())

        assertEquals("Failed: BUSY", state.diagnosticsLabel())
        assertEquals("raw SDK detail", state.failureDetailOrNull())
    }

    @Test
    fun availableBaseModel_exposesIdentity() {
        val identity = BaseModelIdentityState.Available("gemini-nano-test")

        assertEquals("gemini-nano-test", identity.exposedNameOrNull())
    }

    private fun failure() =
        CapabilityPreparationFailure(
            category = CapabilityPreparationFailure.Category.BUSY,
            userMessage = "AICore is busy.",
            recoveryGuidance = "Retry.",
            technicalDetail = "raw SDK detail"
        )
}
