package dev.picon.android.miyabinano.domain.model

enum class InferenceCapability {
    SUMMARIZATION,
    PROOFREADING,
    REWRITE_FORMAL,
    REWRITE_CASUAL,
    REWRITE_CONCISE;

    val displayName: String
        get() = when (this) {
            SUMMARIZATION -> "Summarization"
            PROOFREADING -> "Proofreading"
            REWRITE_FORMAL -> "Rewrite (Formal)"
            REWRITE_CASUAL -> "Rewrite (Casual)"
            REWRITE_CONCISE -> "Rewrite (Concise)"
        }

    val description: String
        get() = when (this) {
            SUMMARIZATION -> "Summarize long texts into concise key points"
            PROOFREADING -> "Fix grammar, spelling, and punctuation errors"
            REWRITE_FORMAL -> "Rewrite text in a professional, formal tone"
            REWRITE_CASUAL -> "Rewrite text in a friendly, conversational tone"
            REWRITE_CONCISE -> "Rewrite text to be more direct and concise"
        }
}
