package dev.picon.android.miyabinano.data.genai

import dev.picon.android.miyabinano.domain.genai.CapabilityPreparationFailure

object MlKitCapabilityFailureMapper {
    fun map(throwable: Throwable): CapabilityPreparationFailure {
        val causes = generateSequence(throwable) { it.cause }.toList()
        val technicalDetail = causes
            .mapNotNull(Throwable::message)
            .firstOrNull { "IPC_ERROR" in it || "service disconnected" in it }
            ?: throwable.message
            ?: throwable::class.java.simpleName

        return if ("IPC_ERROR" in technicalDetail || "service disconnected" in technicalDetail) {
            CapabilityPreparationFailure(
                category = CapabilityPreparationFailure.Category.SERVICE_DISCONNECTED,
                userMessage = "AICore disconnected while preparing this capability.",
                recoveryGuidance = "Keep the device online, wait a moment, then retry.",
                technicalDetail = technicalDetail
            )
        } else {
            CapabilityPreparationFailure(
                category = CapabilityPreparationFailure.Category.UNKNOWN,
                userMessage = "Capability preparation failed.",
                recoveryGuidance = "Wait a moment and retry.",
                technicalDetail = technicalDetail
            )
        }
    }
}
