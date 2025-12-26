package dev.picon.android.miyabinano.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.picon.android.miyabinano.domain.model.InferenceCapability
import dev.picon.android.miyabinano.domain.model.InferenceMetrics

@Entity(tableName = "inference_metrics")
data class MetricsEntity(
    @PrimaryKey val id: String,
    val timestamp: Long,
    val capability: String,
    val platform: String,
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

fun InferenceMetrics.toEntity(): MetricsEntity = MetricsEntity(
    id = id,
    timestamp = timestamp,
    capability = capability.name,
    platform = platform,
    inputText = inputText,
    inputTokenCount = inputTokenCount,
    inputCharCount = inputCharCount,
    outputText = outputText,
    outputTokenCount = outputTokenCount,
    outputCharCount = outputCharCount,
    modelLoadTimeMs = modelLoadTimeMs,
    inferenceTimeMs = inferenceTimeMs,
    totalTimeMs = totalTimeMs,
    memoryUsedMB = memoryUsedMB,
    peakMemoryMB = peakMemoryMB
)

fun MetricsEntity.toDomain(): InferenceMetrics = InferenceMetrics(
    id = id,
    timestamp = timestamp,
    capability = InferenceCapability.valueOf(capability),
    platform = platform,
    inputText = inputText,
    inputTokenCount = inputTokenCount,
    inputCharCount = inputCharCount,
    outputText = outputText,
    outputTokenCount = outputTokenCount,
    outputCharCount = outputCharCount,
    modelLoadTimeMs = modelLoadTimeMs,
    inferenceTimeMs = inferenceTimeMs,
    totalTimeMs = totalTimeMs,
    memoryUsedMB = memoryUsedMB,
    peakMemoryMB = peakMemoryMB
)
