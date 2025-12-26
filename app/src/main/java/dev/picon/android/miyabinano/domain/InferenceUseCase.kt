package dev.picon.android.miyabinano.domain

import com.google.mlkit.genai.proofreading.Proofreader
import com.google.mlkit.genai.proofreading.ProofreadingRequest
import com.google.mlkit.genai.rewriting.Rewriter
import com.google.mlkit.genai.rewriting.RewritingRequest
import com.google.mlkit.genai.summarization.Summarizer
import com.google.mlkit.genai.summarization.SummarizationRequest
import dev.picon.android.miyabinano.di.CasualRewriter
import dev.picon.android.miyabinano.di.ConciseRewriter
import dev.picon.android.miyabinano.di.FormalRewriter
import dev.picon.android.miyabinano.domain.model.InferenceCapability
import dev.picon.android.miyabinano.domain.model.InferenceMetrics
import dev.picon.android.miyabinano.domain.model.InferenceResult
import dev.picon.android.miyabinano.domain.util.MemoryTracker
import dev.picon.android.miyabinano.domain.util.TokenCounter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InferenceUseCase @Inject constructor(
    private val summarizer: Summarizer,
    private val proofreader: Proofreader,
    @FormalRewriter private val formalRewriter: Rewriter,
    @CasualRewriter private val casualRewriter: Rewriter,
    @ConciseRewriter private val conciseRewriter: Rewriter,
    private val memoryTracker: MemoryTracker
) {
    operator fun invoke(capability: InferenceCapability, inputText: String): Flow<InferenceResult> = flow {
        try {
            emit(InferenceResult.Loading)

            val totalStartTime = System.currentTimeMillis()
            val startMemory = memoryTracker.getCurrentMemoryUsageMB()

            val inputTokenCount = TokenCounter.estimateTokens(inputText)
            val inputCharCount = inputText.length

            val outputText = when (capability) {
                InferenceCapability.SUMMARIZATION -> {
                    val request = SummarizationRequest.builder(inputText).build()
                    summarizer.runInference(request).await().result
                }
                InferenceCapability.PROOFREADING -> {
                    val request = ProofreadingRequest.builder(inputText).build()
                    proofreader.runInference(request).await().result
                }
                InferenceCapability.REWRITE_FORMAL -> {
                    val request = RewritingRequest.builder(inputText).build()
                    formalRewriter.runInference(request).await().result
                }
                InferenceCapability.REWRITE_CASUAL -> {
                    val request = RewritingRequest.builder(inputText).build()
                    casualRewriter.runInference(request).await().result
                }
                InferenceCapability.REWRITE_CONCISE -> {
                    val request = RewritingRequest.builder(inputText).build()
                    conciseRewriter.runInference(request).await().result
                }
            }

            val totalEndTime = System.currentTimeMillis()
            val endMemory = memoryTracker.getCurrentMemoryUsageMB()
            val peakMemory = memoryTracker.getPeakMemoryMB()

            val outputTokenCount = TokenCounter.estimateTokens(outputText)
            val outputCharCount = outputText.length

            val totalTimeMs = totalEndTime - totalStartTime
            val inferenceTimeMs = totalTimeMs

            val metrics = InferenceMetrics(
                capability = capability,
                inputText = inputText,
                inputTokenCount = inputTokenCount,
                inputCharCount = inputCharCount,
                outputText = outputText,
                outputTokenCount = outputTokenCount,
                outputCharCount = outputCharCount,
                modelLoadTimeMs = null,
                inferenceTimeMs = inferenceTimeMs,
                totalTimeMs = totalTimeMs,
                memoryUsedMB = endMemory - startMemory,
                peakMemoryMB = peakMemory
            )

            emit(InferenceResult.Success(outputText, metrics))
        } catch (e: Exception) {
            emit(InferenceResult.Error(e.message ?: "Unknown error occurred"))
        }
    }
}
