package dev.picon.android.miyabinano.domain.genai

data class CapabilityPreparationFailure(
    val category: Category,
    val userMessage: String,
    val recoveryGuidance: String,
    val technicalDetail: String
) {
    enum class Category {
        SERVICE_DISCONNECTED,
        UNKNOWN
    }
}

class CapabilityPreparationException(
    val failure: CapabilityPreparationFailure,
    cause: Throwable
) : Exception(failure.technicalDetail, cause)

fun Throwable.toCapabilityPreparationFailure(): CapabilityPreparationFailure =
    generateSequence(this) { it.cause }
        .filterIsInstance<CapabilityPreparationException>()
        .firstOrNull()
        ?.failure
        ?: CapabilityPreparationFailure(
            category = CapabilityPreparationFailure.Category.UNKNOWN,
            userMessage = "Capability preparation failed.",
            recoveryGuidance = "Wait a moment and retry.",
            technicalDetail = message ?: this::class.java.simpleName
        )
