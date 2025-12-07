package dev.picon.android.miyabinano.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.genai.common.DownloadCallback
import com.google.mlkit.genai.common.FeatureStatus
import com.google.mlkit.genai.common.GenAiException
import com.google.mlkit.genai.summarization.Summarizer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing Gemini Nano model download on the main menu screen.
 * This is separate from SummarizeViewModel to avoid introducing bugs into existing functionality.
 */
@HiltViewModel
class ModelDownloadViewModel @Inject constructor(
    private val summarizer: Summarizer
) : ViewModel() {

    private val _state = MutableStateFlow<ModelDownloadState>(ModelDownloadState.Checking)
    val state: StateFlow<ModelDownloadState> = _state.asStateFlow()

    init {
        checkModelStatus()
    }

    /**
     * Checks the current status of the Gemini Nano model
     */
    fun checkModelStatus() {
        viewModelScope.launch {
            try {
                _state.value = ModelDownloadState.Checking

                val featureStatus = summarizer.checkFeatureStatus().await()

                _state.value = when (featureStatus) {
                    FeatureStatus.UNAVAILABLE -> ModelDownloadState.Unavailable
                    FeatureStatus.DOWNLOADABLE -> ModelDownloadState.Downloadable
                    FeatureStatus.DOWNLOADING -> ModelDownloadState.Downloading(0, 0)
                    FeatureStatus.AVAILABLE -> ModelDownloadState.Downloaded
                    else -> ModelDownloadState.Failed("Unknown status: $featureStatus")
                }
            } catch (e: Exception) {
                _state.value = ModelDownloadState.Failed(
                    e.message ?: "Failed to check model status"
                )
            }
        }
    }

    /**
     * Triggers the download of the Gemini Nano model
     */
    fun startDownload() {
        viewModelScope.launch {
            try {
                val callback = object : DownloadCallback {
                    override fun onDownloadStarted(bytesToDownload: Long) {
                        _state.value = ModelDownloadState.Downloading(
                            totalBytes = bytesToDownload,
                            downloadedBytes = 0
                        )
                    }

                    override fun onDownloadProgress(totalBytesDownloaded: Long) {
                        val currentState = _state.value
                        if (currentState is ModelDownloadState.Downloading) {
                            _state.value = ModelDownloadState.Downloading(
                                totalBytes = currentState.totalBytes,
                                downloadedBytes = totalBytesDownloaded
                            )
                        }
                    }

                    override fun onDownloadCompleted() {
                        _state.value = ModelDownloadState.Downloaded
                    }

                    override fun onDownloadFailed(e: GenAiException) {
                        _state.value = ModelDownloadState.Failed(
                            e.message ?: "Download failed"
                        )
                    }
                }

                summarizer.downloadFeature(callback)
            } catch (e: Exception) {
                _state.value = ModelDownloadState.Failed(
                    e.message ?: "Failed to start download"
                )
            }
        }
    }

    /**
     * Retries the download after a failure
     */
    fun retryDownload() {
        checkModelStatus()
    }
}
