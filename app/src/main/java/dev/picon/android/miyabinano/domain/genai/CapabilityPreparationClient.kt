package dev.picon.android.miyabinano.domain.genai

import dev.picon.android.miyabinano.domain.model.InferenceCapability

/**
 * Application-owned boundary for one configured on-device GenAI capability.
 *
 * ML Kit callback and future types stay behind adapters so readiness and
 * provisioning behavior can be tested without an AICore-backed device.
 */
interface CapabilityPreparationClient {
    val capability: InferenceCapability

    suspend fun checkReadiness(): CapabilityReadiness

    suspend fun provision(onEvent: (CapabilityProvisioningEvent) -> Unit)

    suspend fun prepareInferenceEngine()

    suspend fun getBaseModelName(): String

    suspend fun runInference(inputText: String): String

    fun close()
}

enum class CapabilityReadiness {
    UNAVAILABLE,
    DOWNLOADABLE,
    DOWNLOADING,
    AVAILABLE,
    UNKNOWN
}

sealed interface CapabilityProvisioningEvent {
    data class Started(val totalBytes: Long) : CapabilityProvisioningEvent

    data class Progress(val downloadedBytes: Long) : CapabilityProvisioningEvent

    data object Completed : CapabilityProvisioningEvent

    data class Failed(val failure: CapabilityPreparationFailure) :
        CapabilityProvisioningEvent
}
