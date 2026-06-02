package dev.picon.android.miyabinano.domain.model

data class InferenceRequestSnapshot(
    val requestType: String,
    val options: List<Pair<String, String>>,
    val inputText: String,
    val internalPromptVisibility: String =
        "Platform-managed internal prompt is not exposed by this ML Kit feature API."
)
