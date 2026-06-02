package dev.picon.android.miyabinano.di

import android.content.Context
import com.google.mlkit.genai.proofreading.Proofreader
import com.google.mlkit.genai.proofreading.ProofreaderOptions
import com.google.mlkit.genai.proofreading.Proofreading
import com.google.mlkit.genai.proofreading.ProofreadingRequest
import com.google.mlkit.genai.rewriting.Rewriter
import com.google.mlkit.genai.rewriting.RewriterOptions
import com.google.mlkit.genai.rewriting.Rewriting
import com.google.mlkit.genai.rewriting.RewritingRequest
import com.google.mlkit.genai.summarization.Summarization
import com.google.mlkit.genai.summarization.Summarizer
import com.google.mlkit.genai.summarization.SummarizerOptions
import com.google.mlkit.genai.summarization.SummarizationRequest
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.picon.android.miyabinano.data.genai.MlKitCapabilityPreparationClient
import dev.picon.android.miyabinano.domain.genai.CapabilityPreparationClient
import dev.picon.android.miyabinano.domain.model.InferenceCapability
import kotlinx.coroutines.guava.await
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FormalRewriter

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CasualRewriter

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ConciseRewriter

@Module
@InstallIn(SingletonComponent::class)
object GenAiModule {

    @Provides
    @Singleton
    fun provideSummarizer(@ApplicationContext context: Context): Summarizer {
        val summarizerOptions = SummarizerOptions.builder(context)
            .setInputType(SummarizerOptions.InputType.ARTICLE)
            .setOutputType(SummarizerOptions.OutputType.ONE_BULLET)
            .setLanguage(SummarizerOptions.Language.ENGLISH)
            .build()
        return Summarization.getClient(summarizerOptions)
    }

    @Provides
    @Singleton
    fun provideProofreader(@ApplicationContext context: Context): Proofreader {
        val proofreaderOptions = ProofreaderOptions.builder(context)
            .setInputType(ProofreaderOptions.InputType.KEYBOARD)
            .setLanguage(ProofreaderOptions.Language.ENGLISH)
            .build()
        return Proofreading.getClient(proofreaderOptions)
    }

    @Provides
    @Singleton
    @FormalRewriter
    fun provideRewriterFormal(@ApplicationContext context: Context): Rewriter {
        val rewriterOptions = RewriterOptions.builder(context)
            .setOutputType(RewriterOptions.OutputType.PROFESSIONAL)
            .setLanguage(RewriterOptions.Language.ENGLISH)
            .build()
        return Rewriting.getClient(rewriterOptions)
    }

    @Provides
    @Singleton
    @CasualRewriter
    fun provideRewriterCasual(@ApplicationContext context: Context): Rewriter {
        val rewriterOptions = RewriterOptions.builder(context)
            .setOutputType(RewriterOptions.OutputType.FRIENDLY)
            .setLanguage(RewriterOptions.Language.ENGLISH)
            .build()
        return Rewriting.getClient(rewriterOptions)
    }

    @Provides
    @Singleton
    @ConciseRewriter
    fun provideRewriterConcise(@ApplicationContext context: Context): Rewriter {
        val rewriterOptions = RewriterOptions.builder(context)
            .setOutputType(RewriterOptions.OutputType.SHORTEN)
            .setLanguage(RewriterOptions.Language.ENGLISH)
            .build()
        return Rewriting.getClient(rewriterOptions)
    }

    @Provides
    @Singleton
    fun provideCapabilityPreparationClients(
        summarizer: Summarizer,
        proofreader: Proofreader,
        @FormalRewriter formalRewriter: Rewriter,
        @CasualRewriter casualRewriter: Rewriter,
        @ConciseRewriter conciseRewriter: Rewriter
    ): List<CapabilityPreparationClient> =
        listOf(
            summarizer.asCapabilityClient(),
            proofreader.asCapabilityClient(),
            formalRewriter.asCapabilityClient(InferenceCapability.REWRITE_FORMAL),
            casualRewriter.asCapabilityClient(InferenceCapability.REWRITE_CASUAL),
            conciseRewriter.asCapabilityClient(InferenceCapability.REWRITE_CONCISE)
        )

    private fun Summarizer.asCapabilityClient(): CapabilityPreparationClient =
        MlKitCapabilityPreparationClient(
            capability = InferenceCapability.SUMMARIZATION,
            checkFeatureStatus = { checkFeatureStatus().await() },
            downloadFeature = { callback -> downloadFeature(callback).await() },
            prepareEngine = { prepareInferenceEngine().await() },
            baseModelName = { getBaseModelName().await() },
            inference = { inputText ->
                runInference(SummarizationRequest.builder(inputText).build()).await().summary
            },
            closeClient = ::close
        )

    private fun Proofreader.asCapabilityClient(): CapabilityPreparationClient =
        MlKitCapabilityPreparationClient(
            capability = InferenceCapability.PROOFREADING,
            checkFeatureStatus = { checkFeatureStatus().await() },
            downloadFeature = { callback -> downloadFeature(callback).await() },
            prepareEngine = { prepareInferenceEngine().await() },
            baseModelName = { getBaseModelName().await() },
            inference = { inputText ->
                runInference(ProofreadingRequest.builder(inputText).build())
                    .await()
                    .results
                    .first()
                    .text
            },
            closeClient = ::close
        )

    private fun Rewriter.asCapabilityClient(
        capability: InferenceCapability
    ): CapabilityPreparationClient =
        MlKitCapabilityPreparationClient(
            capability = capability,
            checkFeatureStatus = { checkFeatureStatus().await() },
            downloadFeature = { callback -> downloadFeature(callback).await() },
            prepareEngine = { prepareInferenceEngine().await() },
            baseModelName = { getBaseModelName().await() },
            inference = { inputText ->
                runInference(RewritingRequest.builder(inputText).build())
                    .await()
                    .results
                    .first()
                    .text
            },
            closeClient = ::close
        )
}
