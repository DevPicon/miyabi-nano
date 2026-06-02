package dev.picon.android.miyabinano.domain.model

object ExperimentSchema {
    const val CURRENT_VERSION = 1
    const val UNKNOWN = "UNKNOWN"
}

data class ExperimentContext(
    val schemaVersion: Int = ExperimentSchema.CURRENT_VERSION,
    val appVersion: String = ExperimentSchema.UNKNOWN,
    val deviceManufacturer: String = ExperimentSchema.UNKNOWN,
    val deviceModel: String = ExperimentSchema.UNKNOWN,
    val androidBuild: String = ExperimentSchema.UNKNOWN,
    val apiLevel: Int = 0,
    val baseModelName: String? = null,
    val featureStatusBeforeRun: String = ExperimentSchema.UNKNOWN,
    val connectivity: String = ExperimentSchema.UNKNOWN,
    val powerState: String = ExperimentSchema.UNKNOWN,
    val thermalStatus: String = ExperimentSchema.UNKNOWN,
    val runSequence: Int = 0,
    val runClassification: String = ExperimentSchema.UNKNOWN,
    val fixtureId: String? = null,
    val heuristicInputSize: Int = 0,
    val outcomeCategory: String = ExperimentSchema.UNKNOWN
)

data class TimingMilestones(
    val preparationWaitMs: Long? = null,
    val downloadDurationMs: Long? = null,
    val firstVisibleOutputMs: Long? = null,
    val inferenceCompletionMs: Long? = null,
    val persistenceMs: Long? = null,
    val userPerceivedTotalMs: Long? = null
)
