package dev.picon.android.miyabinano

import dev.picon.android.miyabinano.domain.util.TokenCounter
import dev.picon.android.miyabinano.domain.genai.CapabilityPreparationState
import org.junit.Assert.assertEquals
import org.junit.Test

class BaselineLogicTest {
    @Test
    fun estimateTokens_returnsZeroForBlankInput() {
        assertEquals(0, TokenCounter.estimateTokens("  "))
    }

    @Test
    fun estimateTokens_countsWordsAndPunctuation() {
        assertEquals(4, TokenCounter.estimateTokens("Hello, local AI!"))
    }

    @Test
    fun downloadingProgress_usesDownloadedAndTotalBytes() {
        val state = CapabilityPreparationState.Downloading(
            totalBytes = 200,
            downloadedBytes = 50
        )

        assertEquals(0.25f, state.progress)
        assertEquals(25, (state.progress * 100).toInt())
    }

    @Test
    fun downloadingProgress_isZeroWhenTotalBytesAreUnknown() {
        val state = CapabilityPreparationState.Downloading(
            totalBytes = 0,
            downloadedBytes = 50
        )

        assertEquals(0f, state.progress)
        assertEquals(0, (state.progress * 100).toInt())
    }
}
