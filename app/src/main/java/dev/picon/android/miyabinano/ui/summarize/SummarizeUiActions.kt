package dev.picon.android.miyabinano.ui.summarize

data class SummarizeUiActions(
    val onSummarizeButtonClicked: (String) -> Unit,
    val onSummarizeValueChanged: (String) -> Unit,
    val onClearClicked: () -> Unit
) {
    companion object {
        val noOp = SummarizeUiActions(
            onSummarizeButtonClicked = {},
            onSummarizeValueChanged = {},
            onClearClicked = {}
        )
    }
}
