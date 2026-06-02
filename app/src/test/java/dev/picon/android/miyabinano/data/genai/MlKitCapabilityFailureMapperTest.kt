package dev.picon.android.miyabinano.data.genai

import dev.picon.android.miyabinano.domain.genai.CapabilityPreparationFailure
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MlKitCapabilityFailureMapperTest {
    @Test
    fun ipcDisconnect_isRecoverableWithoutClaimingUnsupportedDevice() {
        val rawMessage =
            "AICore failed with error type 1-DOWNLOAD_ERROR and error code 6-IPC_ERROR: AICore service disconnected"

        val failure = MlKitCapabilityFailureMapper.map(IllegalStateException(rawMessage))

        assertEquals(
            CapabilityPreparationFailure.Category.SERVICE_DISCONNECTED,
            failure.category
        )
        assertEquals(rawMessage, failure.technicalDetail)
        assertTrue("retry" in failure.recoveryGuidance.lowercase())
        assertTrue("unsupported" !in failure.userMessage.lowercase())
    }
}
