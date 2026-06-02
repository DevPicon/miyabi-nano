package dev.picon.android.miyabinano.data.genai

import android.content.Context
import com.google.mlkit.genai.proofreading.ProofreaderOptions
import com.google.mlkit.genai.proofreading.Proofreading
import com.google.mlkit.genai.proofreading.ProofreadingRequest
import com.google.mlkit.genai.rewriting.RewriterOptions
import com.google.mlkit.genai.rewriting.Rewriting
import com.google.mlkit.genai.rewriting.RewritingRequest
import com.google.mlkit.genai.summarization.Summarization
import com.google.mlkit.genai.summarization.SummarizationRequest
import com.google.mlkit.genai.summarization.SummarizerOptions
import dev.picon.android.miyabinano.domain.genai.CapabilityPreparationClient
import dev.picon.android.miyabinano.domain.genai.CapabilityPreparationClientFactory
import dev.picon.android.miyabinano.domain.model.InferenceCapability
import kotlinx.coroutines.guava.await

class MlKitCapabilityPreparationClientFactory(
    private val context: Context
) : CapabilityPreparationClientFactory {
    override fun create(capability: InferenceCapability): CapabilityPreparationClient =
        when (capability) {
            InferenceCapability.SUMMARIZATION -> createSummarizer()
            InferenceCapability.PROOFREADING -> createProofreader()
            InferenceCapability.REWRITE_FORMAL -> createRewriter(
                capability,
                RewriterOptions.OutputType.PROFESSIONAL
            )
            InferenceCapability.REWRITE_CASUAL -> createRewriter(
                capability,
                RewriterOptions.OutputType.FRIENDLY
            )
            InferenceCapability.REWRITE_CONCISE -> createRewriter(
                capability,
                RewriterOptions.OutputType.SHORTEN
            )
        }

    private fun createSummarizer(): CapabilityPreparationClient {
        val client = Summarization.getClient(
            SummarizerOptions.builder(context)
                .setInputType(SummarizerOptions.InputType.ARTICLE)
                .setOutputType(SummarizerOptions.OutputType.ONE_BULLET)
                .setLanguage(SummarizerOptions.Language.ENGLISH)
                .build()
        )
        return MlKitCapabilityPreparationClient(
            capability = InferenceCapability.SUMMARIZATION,
            checkFeatureStatus = { client.checkFeatureStatus().await() },
            downloadFeature = { callback -> client.downloadFeature(callback).await() },
            prepareEngine = { client.prepareInferenceEngine().await() },
            baseModelName = { client.getBaseModelName().await() },
            inference = { input ->
                client.runInference(SummarizationRequest.builder(input).build()).await().summary
            },
            closeClient = client::close
        )
    }

    private fun createProofreader(): CapabilityPreparationClient {
        val client = Proofreading.getClient(
            ProofreaderOptions.builder(context)
                .setInputType(ProofreaderOptions.InputType.KEYBOARD)
                .setLanguage(ProofreaderOptions.Language.ENGLISH)
                .build()
        )
        return MlKitCapabilityPreparationClient(
            capability = InferenceCapability.PROOFREADING,
            checkFeatureStatus = { client.checkFeatureStatus().await() },
            downloadFeature = { callback -> client.downloadFeature(callback).await() },
            prepareEngine = { client.prepareInferenceEngine().await() },
            baseModelName = { client.getBaseModelName().await() },
            inference = { input ->
                client.runInference(ProofreadingRequest.builder(input).build())
                    .await()
                    .results
                    .first()
                    .text
            },
            closeClient = client::close
        )
    }

    private fun createRewriter(
        capability: InferenceCapability,
        outputType: Int
    ): CapabilityPreparationClient {
        val client = Rewriting.getClient(
            RewriterOptions.builder(context)
                .setOutputType(outputType)
                .setLanguage(RewriterOptions.Language.ENGLISH)
                .build()
        )
        return MlKitCapabilityPreparationClient(
            capability = capability,
            checkFeatureStatus = { client.checkFeatureStatus().await() },
            downloadFeature = { callback -> client.downloadFeature(callback).await() },
            prepareEngine = { client.prepareInferenceEngine().await() },
            baseModelName = { client.getBaseModelName().await() },
            inference = { input ->
                client.runInference(RewritingRequest.builder(input).build())
                    .await()
                    .results
                    .first()
                    .text
            },
            closeClient = client::close
        )
    }
}
