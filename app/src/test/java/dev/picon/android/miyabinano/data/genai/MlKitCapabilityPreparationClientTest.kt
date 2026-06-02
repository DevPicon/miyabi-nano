package dev.picon.android.miyabinano.data.genai

import com.google.mlkit.genai.common.DownloadCallback
import com.google.mlkit.genai.common.GenAiException
import dev.picon.android.miyabinano.domain.genai.CapabilityPreparationFailure
import dev.picon.android.miyabinano.domain.genai.CapabilityProvisioningEvent
import dev.picon.android.miyabinano.domain.model.InferenceCapability
import dev.picon.android.miyabinano.domain.model.InferenceRequestSnapshot
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class MlKitCapabilityPreparationClientTest {
    @Test
    fun requestSnapshot_exposesConfiguredPublicBoundary() {
        val input = "Exact article submitted to ML Kit"
        val client = createClient {
            error("Unused")
        }

        val snapshot = client.requestSnapshot(input)

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
    }

    @Test
    fun provision_forwardsLifecycleCallbacks() = runBlocking {
        val events = mutableListOf<CapabilityProvisioningEvent>()
        val client = createClient { callback ->
            callback.onDownloadStarted(200)
            callback.onDownloadProgress(50)
            callback.onDownloadCompleted()
        }

        client.provision(events::add)

        assertEquals(
            listOf(
                CapabilityProvisioningEvent.Started(200),
                CapabilityProvisioningEvent.Progress(50),
                CapabilityProvisioningEvent.Completed
            ),
            events
        )
    }

    @Test
    fun provision_forwardsMappedFailureCallback() = runBlocking {
        val events = mutableListOf<CapabilityProvisioningEvent>()
        val client = createClient { callback ->
            callback.onDownloadFailed(
                GenAiException(
                    "Synthetic disk failure",
                    IllegalStateException(),
                    GenAiException.ErrorCode.NOT_ENOUGH_DISK_SPACE
                )
            )
        }

        client.provision(events::add)

        val failure = (events.single() as CapabilityProvisioningEvent.Failed).failure
        assertEquals(CapabilityPreparationFailure.Category.DISK_PRESSURE, failure.category)
    }

    private fun createClient(
        download: suspend (DownloadCallback) -> Unit
    ) = MlKitCapabilityPreparationClient(
        capability = InferenceCapability.SUMMARIZATION,
        checkFeatureStatus = { error("Unused") },
        downloadFeature = download,
        prepareEngine = { error("Unused") },
        baseModelName = { error("Unused") },
        inference = { error("Unused") },
        snapshot = { input ->
            InferenceRequestSnapshot(
                requestType = "SummarizationRequest",
                options = listOf(
                    "Input type" to "ARTICLE",
                    "Output type" to "ONE_BULLET",
                    "Language" to "ENGLISH"
                ),
                inputText = input
            )
        },
        closeClient = {}
    )
}
