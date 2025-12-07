package dev.picon.android.miyabinano.ui.summarize

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.picon.android.miyabinano.domain.SummarizeResult
import dev.picon.android.miyabinano.domain.SummarizeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SummarizeViewModel @Inject constructor(
    private val summarizeUseCase: SummarizeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SummarizeUiState())
    val uiState: StateFlow<SummarizeUiState> = _uiState.asStateFlow()

    fun updateInputText(text: String) {
        _uiState.update { it.copy(inputText = text) }
    }

    fun clearAll() {
        _uiState.update {
            it.copy(
                inputText = "",
                summary = "",
                status = SummarizeUiState.Status.Idle,
                errorMessage = null
            )
        }
    }

    fun summarize(inputText: String) {
        _uiState.update {
            it.copy(
                inputText = inputText,
                summary = "",
                status = SummarizeUiState.Status.Processing,
                downloadProgress = null,
                errorMessage = null
            )
        }

        viewModelScope.launch {
            summarizeUseCase(inputText)
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            status = SummarizeUiState.Status.Error,
                            errorMessage = e.message ?: "Unknown error occurred"
                        )
                    }
                }
                .collect { result ->
                    when (result) {
                        is SummarizeResult.DownloadStarted -> {
                            _uiState.update {
                                it.copy(
                                    status = SummarizeUiState.Status.Downloading,
                                    downloadProgress = SummarizeUiState.DownloadProgress(
                                        bytesDownloaded = 0,
                                        totalBytes = result.bytesToDownload
                                    )
                                )
                            }
                        }

                        is SummarizeResult.DownloadProgress -> {
                            val newProgress = SummarizeUiState.DownloadProgress(
                                bytesDownloaded = result.bytesDownloaded,
                                totalBytes = result.totalBytes
                            )
                            // Only update if percentage changed by at least 1% to reduce recompositions
                            val currentPercentage = _uiState.value.downloadProgress?.progressPercentage ?: 0f
                            val newPercentage = newProgress.progressPercentage
                            if ((newPercentage - currentPercentage) >= 0.01f || newPercentage >= 1.0f) {
                                _uiState.update {
                                    it.copy(downloadProgress = newProgress)
                                }
                            }
                        }

                        is SummarizeResult.DownloadCompleted -> {
                            _uiState.update {
                                it.copy(
                                    status = SummarizeUiState.Status.Processing,
                                    downloadProgress = null
                                )
                            }
                        }

                        is SummarizeResult.Success -> {
                            _uiState.update {
                                it.copy(
                                    status = SummarizeUiState.Status.Success,
                                    summary = result.summary
                                )
                            }
                        }

                        is SummarizeResult.Error -> {
                            _uiState.update {
                                it.copy(
                                    status = SummarizeUiState.Status.Error,
                                    errorMessage = result.exception.message ?: "Unknown error occurred"
                                )
                            }
                        }
                    }
                }
        }
    }
}