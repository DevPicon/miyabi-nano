package dev.picon.android.miyabinano.domain.model

data class InferenceRequestSnapshot(
    val requestType: String,
    val options: List<Pair<String, String>>,
    val inputText: String,
    val internalPromptVisibility: String =
        "Platform-managed internal prompt is not exposed by this ML Kit feature API."
)

object InferenceRequestSnapshotFactory {
    fun create(
        capability: InferenceCapability,
        inputText: String
    ): InferenceRequestSnapshot =
        when (capability) {
            InferenceCapability.SUMMARIZATION ->
                InferenceRequestSnapshot(
                    requestType = "SummarizationRequest",
                    options = listOf(
                        "Input type" to "ARTICLE",
                        "Output type" to "ONE_BULLET",
                        "Language" to "ENGLISH"
                    ),
                    inputText = inputText
                )
            InferenceCapability.PROOFREADING ->
                InferenceRequestSnapshot(
                    requestType = "ProofreadingRequest",
                    options = listOf(
                        "Input type" to "KEYBOARD",
                        "Language" to "ENGLISH"
                    ),
                    inputText = inputText
                )
            InferenceCapability.REWRITE_FORMAL ->
                rewritingSnapshot("PROFESSIONAL", inputText)
            InferenceCapability.REWRITE_CASUAL ->
                rewritingSnapshot("FRIENDLY", inputText)
            InferenceCapability.REWRITE_CONCISE ->
                rewritingSnapshot("SHORTEN", inputText)
        }

    private fun rewritingSnapshot(
        outputType: String,
        inputText: String
    ): InferenceRequestSnapshot =
        InferenceRequestSnapshot(
            requestType = "RewritingRequest",
            options = listOf(
                "Output type" to outputType,
                "Language" to "ENGLISH"
            ),
            inputText = inputText
        )
}
