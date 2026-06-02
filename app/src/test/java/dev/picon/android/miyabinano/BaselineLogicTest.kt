package dev.picon.android.miyabinano

import dev.picon.android.miyabinano.domain.util.TokenCounter
import dev.picon.android.miyabinano.ui.main.ModelDownloadState
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
        val state = ModelDownloadState.Downloading(
            totalBytes = 200,
            downloadedBytes = 50
        )

        assertEquals(0.25f, state.progress)
        assertEquals(25, state.progressPercentage)
    }

    @Test
    fun downloadingProgress_isZeroWhenTotalBytesAreUnknown() {
        val state = ModelDownloadState.Downloading(
            totalBytes = 0,
            downloadedBytes = 50
        )

        assertEquals(0f, state.progress)
        assertEquals(0, state.progressPercentage)
    }
}
