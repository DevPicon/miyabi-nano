package dev.picon.android.miyabinano.domain.model

data class TestCase(
    val id: String,
    val name: String,
    val capability: InferenceCapability,
    val inputText: String,
    val expectedOutputGuidelines: String? = null,
    val category: TestCategory = TestCategory.GENERAL
) {
    val sizeBand: TestSizeBand
        get() = TestSizeBand.fromCharacters(inputText.length)

    val qualitativeCheck: String
        get() = expectedOutputGuidelines ?: when (capability) {
            InferenceCapability.SUMMARIZATION -> "Preserve the central topic without inventing facts."
            InferenceCapability.PROOFREADING -> "Correct obvious errors without changing the intended meaning."
            InferenceCapability.REWRITE_FORMAL -> "Increase professional tone while preserving meaning."
            InferenceCapability.REWRITE_CASUAL -> "Use a friendlier tone while preserving meaning."
            InferenceCapability.REWRITE_CONCISE -> "Reduce verbosity while preserving essential meaning."
        }
}

enum class TestSizeBand {
    SHORT,
    MEDIUM,
    LONG;

    companion object {
        fun fromCharacters(characters: Int): TestSizeBand =
            when {
                characters < 150 -> SHORT
                characters < 500 -> MEDIUM
                else -> LONG
            }
    }
}

enum class TestCategory {
    SHORT_TEXT,
    MEDIUM_TEXT,
    LONG_TEXT,
    TECHNICAL,
    CASUAL,
    FORMAL,
    ERROR_RICH,
    GENERAL
}
