package dev.picon.android.miyabinano.domain.model

data class TestCase(
    val id: String,
    val name: String,
    val capability: InferenceCapability,
    val inputText: String,
    val expectedOutputGuidelines: String? = null,
    val category: TestCategory = TestCategory.GENERAL
)

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
