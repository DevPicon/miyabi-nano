package dev.picon.android.miyabinano.domain

import dev.picon.android.miyabinano.domain.genai.CapabilityInferenceAccess
import dev.picon.android.miyabinano.domain.genai.CapabilityPreparationClientFactory
import dev.picon.android.miyabinano.domain.genai.toCapabilityPreparationFailure
import dev.picon.android.miyabinano.domain.genai.toInferenceAccess
import dev.picon.android.miyabinano.domain.genai.withClient
import dev.picon.android.miyabinano.domain.model.InferenceCapability
import dev.picon.android.miyabinano.domain.model.InferenceMetrics
import dev.picon.android.miyabinano.domain.model.InferenceResult
import dev.picon.android.miyabinano.domain.model.TimingMilestones
import dev.picon.android.miyabinano.domain.model.ExperimentContextInput
import dev.picon.android.miyabinano.domain.model.ExperimentContextProvider
import dev.picon.android.miyabinano.domain.util.MemoryTracker
import dev.picon.android.miyabinano.domain.util.TokenCounter
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InferenceUseCase @Inject constructor(
    private val clientFactory: CapabilityPreparationClientFactory,
    private val memoryTracker: MemoryTracker,
    private val experimentContextProvider: ExperimentContextProvider
) {
    operator fun invoke(capability: InferenceCapability, inputText: String): Flow<InferenceResult> =
        flow {
            try {
                clientFactory.withClient(capability) { client ->
                    val requestStartNanos = System.nanoTime()
                    val readiness = client.checkReadiness()
                    when (val access = readiness.toInferenceAccess()) {
                        CapabilityInferenceAccess.Allowed -> Unit
                        is CapabilityInferenceAccess.Blocked -> {
                            emit(
                                InferenceResult.Blocked(
                                    message = access.message,
                                    recoveryGuidance = access.recoveryGuidance
                                )
                            )
                            return@withClient
                        }
                    }

                    emit(InferenceResult.Loading)

                    val startMemory = memoryTracker.getCurrentMemoryUsageMB()

                    val inputTokenCount = TokenCounter.estimateTokens(inputText)
                    val inputCharCount = inputText.length
                    val baseModelName = try {
                        client.getBaseModelName()
                    } catch (e: Exception) {
                        if (e is CancellationException) throw e
                        null
                    }
                    val experimentContext = experimentContextProvider.capture(
                        ExperimentContextInput(
                            baseModelName = baseModelName,
                            featureStatusBeforeRun = readiness,
                            heuristicInputSize = inputTokenCount,
                            outcomeCategory = "SUCCESS"
                        )
                    )

                    val preparationStartNanos = System.nanoTime()
                    client.prepareInferenceEngine()
                    val preparationWaitMs = elapsedMs(preparationStartNanos)

                    val inferenceStartNanos = System.nanoTime()
                    val outputText = client.runInference(inputText)
                    val inferenceTimeMs = elapsedMs(inferenceStartNanos)
                    val completionMs = elapsedMs(requestStartNanos)

                    val endMemory = memoryTracker.getCurrentMemoryUsageMB()
                    val runtimeMaxHeap = memoryTracker.getRuntimeMaxHeapMB()

                    val outputTokenCount = TokenCounter.estimateTokens(outputText)
                    val outputCharCount = outputText.length

                    val metrics = InferenceMetrics(
                        capability = capability,
                        inputText = inputText,
                        inputTokenCount = inputTokenCount,
                        inputCharCount = inputCharCount,
                        outputText = outputText,
                        outputTokenCount = outputTokenCount,
                        outputCharCount = outputCharCount,
                        modelLoadTimeMs = preparationWaitMs,
                        inferenceTimeMs = inferenceTimeMs,
                        totalTimeMs = completionMs,
                        processHeapDeltaMB = endMemory - startMemory,
                        runtimeMaxHeapMB = runtimeMaxHeap,
                        experimentContext = experimentContext,
                        timingMilestones = TimingMilestones(
                            preparationWaitMs = preparationWaitMs,
                            firstVisibleOutputMs = completionMs,
                            inferenceCompletionMs = completionMs,
                            userPerceivedTotalMs = completionMs
                        )
                    )

                    emit(InferenceResult.Success(outputText, metrics))
                }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                emit(InferenceResult.Error(e.toCapabilityPreparationFailure()))
            }
        }

    private fun elapsedMs(startNanos: Long): Long =
        (System.nanoTime() - startNanos) / 1_000_000
}
