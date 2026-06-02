package dev.picon.android.miyabinano.domain.genai

import dev.picon.android.miyabinano.domain.model.InferenceCapability
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test

class CapabilityPreparationClientFactoryTest {
    @Test
    fun withClient_closesClientAfterSuccess() = runBlocking {
        val client = FakeClient()

        FakeFactory(client).withClient(InferenceCapability.SUMMARIZATION) { }

        assertTrue(client.closed)
    }

    @Test
    fun withClient_closesClientAfterFailure() = runBlocking {
        val client = FakeClient()

        runCatching {
            FakeFactory(client).withClient(InferenceCapability.SUMMARIZATION) {
                error("Synthetic failure")
            }
        }

        assertTrue(client.closed)
    }

    @Test
    fun withClient_closesClientAfterCancellation() = runBlocking {
        val client = FakeClient()

        runCatching {
            FakeFactory(client).withClient(InferenceCapability.SUMMARIZATION) {
                throw CancellationException("Synthetic cancellation")
            }
        }

        assertTrue(client.closed)
    }

    private class FakeFactory(
        private val client: CapabilityPreparationClient
    ) : CapabilityPreparationClientFactory {
        override fun create(capability: InferenceCapability): CapabilityPreparationClient = client
    }

    private class FakeClient : CapabilityPreparationClient {
        var closed = false

        override val capability = InferenceCapability.SUMMARIZATION

        override suspend fun checkReadiness() = CapabilityReadiness.AVAILABLE

        override suspend fun provision(onEvent: (CapabilityProvisioningEvent) -> Unit) = Unit

        override suspend fun prepareInferenceEngine() = Unit

        override suspend fun getBaseModelName() = "synthetic"

        override suspend fun runInference(inputText: String) = inputText

        override fun close() {
            closed = true
        }
    }
}
