package dev.picon.android.miyabinano.di

import android.content.Context
import com.google.mlkit.genai.proofreading.Proofreader
import com.google.mlkit.genai.proofreading.ProofreaderOptions
import com.google.mlkit.genai.proofreading.Proofreading
import com.google.mlkit.genai.rewriting.Rewriter
import com.google.mlkit.genai.rewriting.RewriterOptions
import com.google.mlkit.genai.rewriting.Rewriting
import com.google.mlkit.genai.summarization.Summarization
import com.google.mlkit.genai.summarization.Summarizer
import com.google.mlkit.genai.summarization.SummarizerOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
}