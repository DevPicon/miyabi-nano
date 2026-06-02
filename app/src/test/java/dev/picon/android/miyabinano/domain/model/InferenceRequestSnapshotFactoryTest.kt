package dev.picon.android.miyabinano.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class InferenceRequestSnapshotFactoryTest {
    @Test
    fun summarizationSnapshot_exposesExactPublicRequestBoundary() {
        val input = "Exact article submitted to ML Kit"

        val snapshot = InferenceRequestSnapshotFactory.create(
            capability = InferenceCapability.SUMMARIZATION,
            inputText = input
        )

        assertEquals("SummarizationRequest", snapshot.requestType)
        assertEquals(input, snapshot.inputText)
        assertEquals(
            listOf(
                "Input type" to "ARTICLE",
                "Output type" to "ONE_BULLET",
                "Language" to "ENGLISH"
            ),
            snapshot.options
        )
        assertTrue("not exposed" in snapshot.internalPromptVisibility)
    }
}
