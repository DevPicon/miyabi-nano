package dev.picon.android.miyabinano.data

import androidx.room.Entity
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import dev.picon.android.miyabinano.domain.model.InferenceCapability
import dev.picon.android.miyabinano.domain.model.InferenceMetrics
import dev.picon.android.miyabinano.domain.model.ExperimentContext
import dev.picon.android.miyabinano.domain.model.TimingMilestones

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
    @ColumnInfo(name = "memoryUsedMB") val processHeapDeltaMB: Long,
    @ColumnInfo(name = "peakMemoryMB") val runtimeMaxHeapMB: Long,
    val schemaVersion: Int,
    val appVersion: String,
    val deviceManufacturer: String,
    val deviceModel: String,
    val androidBuild: String,
    val apiLevel: Int,
    val baseModelName: String?,
    val featureStatusBeforeRun: String,
    val connectivity: String,
    val powerState: String,
    val thermalStatus: String,
    val runSequence: Int,
    val runClassification: String,
    val fixtureId: String?,
    val heuristicInputSize: Int,
    val outcomeCategory: String,
    val preparationWaitMs: Long?,
    val downloadDurationMs: Long?,
    val firstVisibleOutputMs: Long?,
    val inferenceCompletionMs: Long?,
    val persistenceMs: Long?,
    val userPerceivedTotalMs: Long?
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
    processHeapDeltaMB = processHeapDeltaMB,
    runtimeMaxHeapMB = runtimeMaxHeapMB,
    schemaVersion = experimentContext.schemaVersion,
    appVersion = experimentContext.appVersion,
    deviceManufacturer = experimentContext.deviceManufacturer,
    deviceModel = experimentContext.deviceModel,
    androidBuild = experimentContext.androidBuild,
    apiLevel = experimentContext.apiLevel,
    baseModelName = experimentContext.baseModelName,
    featureStatusBeforeRun = experimentContext.featureStatusBeforeRun,
    connectivity = experimentContext.connectivity,
    powerState = experimentContext.powerState,
    thermalStatus = experimentContext.thermalStatus,
    runSequence = experimentContext.runSequence,
    runClassification = experimentContext.runClassification,
    fixtureId = experimentContext.fixtureId,
    heuristicInputSize = experimentContext.heuristicInputSize,
    outcomeCategory = experimentContext.outcomeCategory,
    preparationWaitMs = timingMilestones.preparationWaitMs,
    downloadDurationMs = timingMilestones.downloadDurationMs,
    firstVisibleOutputMs = timingMilestones.firstVisibleOutputMs,
    inferenceCompletionMs = timingMilestones.inferenceCompletionMs,
    persistenceMs = timingMilestones.persistenceMs,
    userPerceivedTotalMs = timingMilestones.userPerceivedTotalMs
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
    processHeapDeltaMB = processHeapDeltaMB,
    runtimeMaxHeapMB = runtimeMaxHeapMB,
    experimentContext = ExperimentContext(
        schemaVersion = schemaVersion,
        appVersion = appVersion,
        deviceManufacturer = deviceManufacturer,
        deviceModel = deviceModel,
        androidBuild = androidBuild,
        apiLevel = apiLevel,
        baseModelName = baseModelName,
        featureStatusBeforeRun = featureStatusBeforeRun,
        connectivity = connectivity,
        powerState = powerState,
        thermalStatus = thermalStatus,
        runSequence = runSequence,
        runClassification = runClassification,
        fixtureId = fixtureId,
        heuristicInputSize = heuristicInputSize,
        outcomeCategory = outcomeCategory
    ),
    timingMilestones = TimingMilestones(
        preparationWaitMs = preparationWaitMs,
        downloadDurationMs = downloadDurationMs,
        firstVisibleOutputMs = firstVisibleOutputMs,
        inferenceCompletionMs = inferenceCompletionMs,
        persistenceMs = persistenceMs,
        userPerceivedTotalMs = userPerceivedTotalMs
    )
)
