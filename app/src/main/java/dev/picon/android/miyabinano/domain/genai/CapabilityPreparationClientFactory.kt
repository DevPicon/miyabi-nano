package dev.picon.android.miyabinano.domain.genai

import dev.picon.android.miyabinano.domain.model.InferenceCapability

interface CapabilityPreparationClientFactory {
    fun create(capability: InferenceCapability): CapabilityPreparationClient
}
