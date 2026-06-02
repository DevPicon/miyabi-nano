package dev.picon.android.miyabinano.domain.model

object SummarizationInputPolicy {
    const val MINIMUM_ARTICLE_CHARACTERS_EXCLUSIVE = 400
    const val RECOMMENDED_ARTICLE_WORDS = 300

    fun evaluate(input: String): Evaluation {
        val characterCount = input.length
        val wordCount = input.trim()
            .split(Regex("\\s+"))
            .count { it.isNotBlank() }
        return Evaluation(
            characterCount = characterCount,
            wordCount = wordCount,
            meetsMinimum = characterCount > MINIMUM_ARTICLE_CHARACTERS_EXCLUSIVE,
            meetsRecommendation = wordCount >= RECOMMENDED_ARTICLE_WORDS
        )
    }

    data class Evaluation(
        val characterCount: Int,
        val wordCount: Int,
        val meetsMinimum: Boolean,
        val meetsRecommendation: Boolean
    ) {
        val guidance: String?
            get() = when {
                !meetsMinimum ->
                    "Article summarization requires more than $MINIMUM_ARTICLE_CHARACTERS_EXCLUSIVE characters."
                !meetsRecommendation ->
                    "Valid article input. ML Kit reports best results with at least $RECOMMENDED_ARTICLE_WORDS words."
                else -> null
            }
    }
}
