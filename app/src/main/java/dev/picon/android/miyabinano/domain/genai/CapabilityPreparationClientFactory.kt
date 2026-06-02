package dev.picon.android.miyabinano.domain.genai

import dev.picon.android.miyabinano.domain.model.InferenceCapability

interface CapabilityPreparationClientFactory {
    fun create(capability: InferenceCapability): CapabilityPreparationClient
}

suspend fun <T> CapabilityPreparationClientFactory.withClient(
    capability: InferenceCapability,
    block: suspend (CapabilityPreparationClient) -> T
): T {
    val client = create(capability)
    return try {
        block(client)
    } finally {
        client.close()
    }
}
