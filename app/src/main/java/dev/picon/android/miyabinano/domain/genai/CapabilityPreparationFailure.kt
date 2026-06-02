package dev.picon.android.miyabinano.domain.genai

data class CapabilityPreparationFailure(
    val category: Category,
    val userMessage: String,
    val recoveryGuidance: String,
    val technicalDetail: String
) {
    enum class Category {
        SERVICE_DISCONNECTED,
        AICORE_INCOMPATIBLE,
        NEEDS_SYSTEM_UPDATE,
        FEATURE_UNAVAILABLE,
        DISK_PRESSURE,
        BUSY,
        CANCELLED,
        BACKGROUND_USE_BLOCKED,
        BATTERY_QUOTA_EXCEEDED,
        INPUT_TOO_LARGE,
        INPUT_TOO_SMALL,
        PROCESSING_INTERRUPTED,
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
