package dev.picon.android.miyabinano.domain.util

object TokenCounter {
    fun estimateTokens(text: String): Int {
        if (text.isBlank()) return 0

        val words = text.trim().split(Regex("\\s+")).size
        val punctuation = text.count { it in ".,!?;:\"'()[]{}…—–-" }

        return (words * 1.3 + punctuation * 0.3).toInt()
    }
}
