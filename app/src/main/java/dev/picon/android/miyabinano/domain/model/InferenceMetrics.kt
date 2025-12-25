package dev.picon.android.miyabinano.domain.model

import java.util.UUID

data class InferenceMetrics(
    val id: String = UUID.randomUUID().toString(),
    val timestamp: Long = System.currentTimeMillis(),
    val capability: InferenceCapability,
    val platform: String = "Android/Gemini Nano",

    val inputText: String,
    val inputTokenCount: Int,
    val inputCharCount: Int,

    val outputText: String,
    val outputTokenCount: Int,
    val outputCharCount: Int,

    val modelLoadTimeMs: Long?,
    val inferenceTimeMs: Long,
    val totalTimeMs: Long,

    val memoryUsedMB: Long,
    val peakMemoryMB: Long
)
