package dev.picon.android.miyabinano.ui.main

/**
 * Represents the different states of the Gemini Nano model download process
 */
sealed interface ModelDownloadState {
    /**
     * Initial state - checking model status
     */
    data object Checking : ModelDownloadState

    /**
     * Model is not supported on this device
     */
    data object Unavailable : ModelDownloadState

    /**
     * Model is available for download
     */
    data object Downloadable : ModelDownloadState

    /**
     * Model download is in progress
     * @param totalBytes Total size of the download in bytes
     * @param downloadedBytes Bytes downloaded so far
     */
    data class Downloading(
        val totalBytes: Long,
        val downloadedBytes: Long
    ) : ModelDownloadState {
        val progress: Float
            get() = if (totalBytes > 0) {
                (downloadedBytes.toFloat() / totalBytes.toFloat())
            } else {
                0f
            }

        val progressPercentage: Int
            get() = (progress * 100).toInt()
    }

    /**
     * Model has been successfully downloaded and is ready to use
     */
    data object Downloaded : ModelDownloadState

    /**
     * Model download failed
     * @param errorMessage Description of the error
     */
    data class Failed(val errorMessage: String) : ModelDownloadState
}
