package dev.picon.android.miyabinano.domain

sealed interface SummarizeResult {
    data class DownloadStarted(val bytesToDownload: Long) : SummarizeResult
    data class DownloadProgress(val bytesDownloaded: Long, val totalBytes: Long) : SummarizeResult
    data object DownloadCompleted : SummarizeResult
    data class Success(val summary: String) : SummarizeResult
    data class Error(val exception: Throwable) : SummarizeResult
}