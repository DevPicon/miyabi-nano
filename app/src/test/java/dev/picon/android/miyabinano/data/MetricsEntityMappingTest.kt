package dev.picon.android.miyabinano.data

import dev.picon.android.miyabinano.domain.model.ExperimentContext
import dev.picon.android.miyabinano.domain.model.InferenceCapability
import dev.picon.android.miyabinano.domain.model.InferenceMetrics
import dev.picon.android.miyabinano.domain.model.TimingMilestones
import org.junit.Assert.assertEquals
import org.junit.Test

class MetricsEntityMappingTest {
    @Test
    fun entityRoundTrip_preservesVersionedExperimentSchema() {
        val metrics = InferenceMetrics(
            capability = InferenceCapability.SUMMARIZATION,
            inputText = "input",
            inputTokenCount = 1,
            inputCharCount = 5,
            outputText = "output",
            outputTokenCount = 1,
            outputCharCount = 6,
            modelLoadTimeMs = null,
            inferenceTimeMs = 10,
            totalTimeMs = 12,
            processHeapDeltaMB = 2,
            runtimeMaxHeapMB = 256,
            experimentContext = ExperimentContext(
                appVersion = "test",
                deviceManufacturer = "manufacturer",
                deviceModel = "model",
                baseModelName = "nano-test",
                outcomeCategory = "SUCCESS"
            ),
            timingMilestones = TimingMilestones(
                preparationWaitMs = 3,
                inferenceCompletionMs = 10,
                userPerceivedTotalMs = 12
            )
        )

        assertEquals(metrics, metrics.toEntity().toDomain())
    }
}
