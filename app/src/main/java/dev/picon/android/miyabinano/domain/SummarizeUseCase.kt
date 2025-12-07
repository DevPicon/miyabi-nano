package dev.picon.android.miyabinano.domain

import com.google.mlkit.genai.common.DownloadCallback
import com.google.mlkit.genai.common.FeatureStatus
import com.google.mlkit.genai.common.GenAiException
import com.google.mlkit.genai.summarization.SummarizationRequest
import com.google.mlkit.genai.summarization.Summarizer
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch
import javax.inject.Inject

class SummarizeUseCase @Inject constructor(
    private val summarizer: Summarizer
) {
    operator fun invoke(text: String): Flow<SummarizeResult> = callbackFlow {
        val featureStatus = summarizer.checkFeatureStatus().await()

        val summarizationRequest = SummarizationRequest.builder(text).build()

        var totalBytesToDownload = 0L

        val callback = object : DownloadCallback {
            override fun onDownloadStarted(bytesToDownload: Long) {
                totalBytesToDownload = bytesToDownload
                trySend(SummarizeResult.DownloadStarted(bytesToDownload))
            }

            override fun onDownloadFailed(e: GenAiException) {
                trySend(SummarizeResult.Error(e))
                close(e)
            }

            override fun onDownloadProgress(totalBytesDownloaded: Long) {
                trySend(SummarizeResult.DownloadProgress(totalBytesDownloaded, totalBytesToDownload))
            }

            override fun onDownloadCompleted() {
                trySend(SummarizeResult.DownloadCompleted)
                launch {
                    try {
                        // Use non-streaming approach to get complete summary at once
                        val result = summarizer.runInference(summarizationRequest).await()
                        trySend(SummarizeResult.Success(result.summary))
                    } catch (e: Exception) {
                        trySend(SummarizeResult.Error(e))
                        close(e)
                    }
                }
            }
        }

        when (featureStatus) {
            FeatureStatus.DOWNLOADABLE -> {
                summarizer.downloadFeature(callback)
            }

            FeatureStatus.DOWNLOADING, FeatureStatus.AVAILABLE -> {
                launch {
                    try {
                        // Use non-streaming approach to get complete summary at once
                        val result = summarizer.runInference(summarizationRequest).await()
                        trySend(SummarizeResult.Success(result.summary))
                    } catch (e: Exception) {
                        trySend(SummarizeResult.Error(e))
                        close(e)
                    }
                }
            }

            else -> {
                val error = IllegalStateException("Summarizer feature is unavailable")
                trySend(SummarizeResult.Error(error))
                close(error)
            }
        }

        awaitClose { summarizer.close() }
    }
}