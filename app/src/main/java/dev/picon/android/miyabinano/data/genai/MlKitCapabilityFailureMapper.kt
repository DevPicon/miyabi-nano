package dev.picon.android.miyabinano.data.genai

import com.google.mlkit.genai.common.GenAiException
import dev.picon.android.miyabinano.domain.genai.CapabilityPreparationFailure

object MlKitCapabilityFailureMapper {
    fun map(throwable: Throwable): CapabilityPreparationFailure {
        val causes = generateSequence(throwable) { it.cause }.toList()
        val technicalDetail = causes
            .mapNotNull(Throwable::message)
            .firstOrNull { "IPC_ERROR" in it || "service disconnected" in it }
            ?: throwable.message
            ?: throwable::class.java.simpleName

        if ("IPC_ERROR" in technicalDetail || "service disconnected" in technicalDetail) {
            return failure(
                category = CapabilityPreparationFailure.Category.SERVICE_DISCONNECTED,
                userMessage = "AICore disconnected while preparing this capability.",
                recoveryGuidance = "Keep the device online, wait a moment, then retry.",
                technicalDetail = technicalDetail
            )
        }

        val errorCode = causes.filterIsInstance<GenAiException>().firstOrNull()?.errorCode
        return when (errorCode) {
            GenAiException.ErrorCode.AICORE_INCOMPATIBLE -> failure(
                CapabilityPreparationFailure.Category.AICORE_INCOMPATIBLE,
                "AICore is missing or incompatible.",
                "Update the system AI components before trying again.",
                technicalDetail
            )
            GenAiException.ErrorCode.NEEDS_SYSTEM_UPDATE -> failure(
                CapabilityPreparationFailure.Category.NEEDS_SYSTEM_UPDATE,
                "The Android system version is too old for this capability.",
                "Install available system updates before trying again.",
                technicalDetail
            )
            GenAiException.ErrorCode.NOT_AVAILABLE -> failure(
                CapabilityPreparationFailure.Category.FEATURE_UNAVAILABLE,
                "This capability is not available on the current device configuration.",
                "Choose another experiment or check status again after system updates.",
                technicalDetail
            )
            GenAiException.ErrorCode.NOT_ENOUGH_DISK_SPACE -> failure(
                CapabilityPreparationFailure.Category.DISK_PRESSURE,
                "There is not enough storage to prepare this capability.",
                "Free device storage, then retry.",
                technicalDetail
            )
            GenAiException.ErrorCode.BUSY -> failure(
                CapabilityPreparationFailure.Category.BUSY,
                "AICore is busy.",
                "Wait briefly, then retry. Repeated retries should use backoff.",
                technicalDetail
            )
            GenAiException.ErrorCode.CANCELLED -> failure(
                CapabilityPreparationFailure.Category.CANCELLED,
                "The AICore request was cancelled.",
                "Retry if the operation is still needed.",
                technicalDetail
            )
            BACKGROUND_USE_BLOCKED -> failure(
                CapabilityPreparationFailure.Category.BACKGROUND_USE_BLOCKED,
                "On-device AI is blocked while the app is not in the foreground.",
                "Return to the app, then retry.",
                technicalDetail
            )
            BATTERY_QUOTA_EXCEEDED -> failure(
                CapabilityPreparationFailure.Category.BATTERY_QUOTA_EXCEEDED,
                "The app reached AICore's long-duration battery quota.",
                "Try again later after the quota recovers.",
                technicalDetail
            )
            GenAiException.ErrorCode.REQUEST_TOO_LARGE -> failure(
                CapabilityPreparationFailure.Category.INPUT_TOO_LARGE,
                "The input is too large for this capability.",
                "Use a shorter input, then retry.",
                technicalDetail
            )
            GenAiException.ErrorCode.REQUEST_TOO_SMALL -> failure(
                CapabilityPreparationFailure.Category.INPUT_TOO_SMALL,
                "The input is too small for this capability.",
                "Use a longer input, then retry.",
                technicalDetail
            )
            GenAiException.ErrorCode.REQUEST_PROCESSING_ERROR,
            GenAiException.ErrorCode.RESPONSE_PROCESSING_ERROR,
            GenAiException.ErrorCode.RESPONSE_GENERATION_ERROR -> failure(
                CapabilityPreparationFailure.Category.PROCESSING_FAILED,
                "AICore could not generate a response for this request.",
                "Retry once. If the failure repeats with the same input, try different input. Keep the app in the foreground while inference runs.",
                technicalDetail
            )
            else -> failure(
                CapabilityPreparationFailure.Category.UNKNOWN,
                "Capability preparation failed.",
                "Wait a moment and retry.",
                technicalDetail
            )
        }
    }

    private fun failure(
        category: CapabilityPreparationFailure.Category,
        userMessage: String,
        recoveryGuidance: String,
        technicalDetail: String
    ): CapabilityPreparationFailure =
            CapabilityPreparationFailure(
                category = category,
                userMessage = userMessage,
                recoveryGuidance = recoveryGuidance,
                technicalDetail = technicalDetail
            )

    // Documented by the current ML Kit reference but absent from 1.0.0-beta1 constants.
    private const val BATTERY_QUOTA_EXCEEDED = 27
    private const val BACKGROUND_USE_BLOCKED = 30
}
