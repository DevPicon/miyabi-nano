package dev.picon.android.miyabinano.data.genai

import com.google.mlkit.genai.common.GenAiException
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

    @Test
    fun wrappedIpcDisconnect_preservesUnderlyingTechnicalDetail() {
        val rawMessage =
            "AICore failed with error type 1-DOWNLOAD_ERROR and error code 6-IPC_ERROR: AICore service disconnected"

        val failure = MlKitCapabilityFailureMapper.map(
            IllegalStateException("Future failed", IllegalStateException(rawMessage))
        )

        assertEquals(
            CapabilityPreparationFailure.Category.SERVICE_DISCONNECTED,
            failure.category
        )
        assertEquals(rawMessage, failure.technicalDetail)
    }

    @Test
    fun diskPressure_isMappedFromPublicErrorCode() {
        val failure = MlKitCapabilityFailureMapper.map(
            genAiException(GenAiException.ErrorCode.NOT_ENOUGH_DISK_SPACE)
        )

        assertEquals(CapabilityPreparationFailure.Category.DISK_PRESSURE, failure.category)
        assertTrue("storage" in failure.userMessage.lowercase())
    }

    @Test
    fun backgroundBlocking_isForwardCompatibleWithDocumentedErrorCode() {
        val failure = MlKitCapabilityFailureMapper.map(genAiException(30))

        assertEquals(
            CapabilityPreparationFailure.Category.BACKGROUND_USE_BLOCKED,
            failure.category
        )
        assertTrue("foreground" in failure.userMessage.lowercase())
    }

    @Test
    fun genericProcessingFailure_doesNotBlameInputWithoutEvidence() {
        val failure = MlKitCapabilityFailureMapper.map(
            genAiException(GenAiException.ErrorCode.RESPONSE_PROCESSING_ERROR)
        )

        assertEquals(
            CapabilityPreparationFailure.Category.PROCESSING_INTERRUPTED,
            failure.category
        )
        assertTrue("foreground" in failure.recoveryGuidance.lowercase())
        assertTrue(!failure.userMessage.lowercase().contains("adjust"))
    }

    private fun genAiException(errorCode: Int): GenAiException =
        GenAiException("Synthetic GenAI failure", IllegalStateException(), errorCode)
}
