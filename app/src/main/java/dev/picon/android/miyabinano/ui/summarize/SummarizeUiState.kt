package dev.picon.android.miyabinano.ui.summarize

data class SummarizeUiState(
    val inputText: String = "",
    val summary: String = "",
    val status: Status = Status.Idle,
    val downloadProgress: DownloadProgress? = null,
    val errorMessage: String? = null
) {
    enum class Status {
        Idle,
        Downloading,
        Processing,
        Success,
        Error
    }

    data class DownloadProgress(
        val bytesDownloaded: Long,
        val totalBytes: Long
    ) {
        val progressPercentage: Float
            get() = if (totalBytes > 0) {
                (bytesDownloaded.toFloat() / totalBytes.toFloat())
            } else {
                0f
            }
    }

    val characterCount: Int
        get() = inputText.length

    val wordCount: Int
        get() = if (inputText.isBlank()) {
            0
        } else {
            inputText.trim().split("\\s+".toRegex()).size
        }

    val isInputValid: Boolean
        get() {
            val hasMinCharacters = characterCount >= MIN_CHARACTERS
            val hasMaxWords = wordCount <= MAX_WORDS
            return hasMinCharacters && hasMaxWords
        }

    companion object {
        const val MIN_CHARACTERS = 400
        const val MAX_WORDS = 3000 // Approximately 4000 tokens
    }
}
