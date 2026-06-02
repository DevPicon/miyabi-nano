package dev.picon.android.miyabinano.domain.model

import dev.picon.android.miyabinano.domain.genai.CapabilityReadiness

data class ExperimentContextInput(
    val baseModelName: String?,
    val featureStatusBeforeRun: CapabilityReadiness,
    val fixtureId: String?,
    val heuristicInputSize: Int,
    val outcomeCategory: String
)

interface ExperimentContextProvider {
    fun capture(input: ExperimentContextInput): ExperimentContext
}
