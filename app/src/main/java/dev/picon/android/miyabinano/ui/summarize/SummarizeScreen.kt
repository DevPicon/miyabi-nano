package dev.picon.android.miyabinano.ui.summarize

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummarizeScreen(
    uiState: SummarizeUiState,
    uiActions: SummarizeUiActions
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Memory,
                            contentDescription = null,
                            tint = Color.Blue,
                            modifier = Modifier.size(40.dp)
                        )
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Gemini Nano Ready",
                                fontWeight = FontWeight.SemiBold,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                "100% Offline • On-Device AI",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            )

        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {


            OutlinedTextField(
                value = uiState.inputText,
                onValueChange = { uiActions.onSummarizeValueChanged(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
                enabled = uiState.status == SummarizeUiState.Status.Idle || uiState.status == SummarizeUiState.Status.Success || uiState.status == SummarizeUiState.Status.Error
            )

            // Character and word counter with clear button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${uiState.characterCount} characters · ${uiState.wordCount} words " +
                            "(min: ${SummarizeUiState.MIN_CHARACTERS} chars, max: ${SummarizeUiState.MAX_WORDS} words)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f)
                )

                if (uiState.inputText.isNotEmpty()) {
                    TextButton(
                        onClick = { uiActions.onClearClicked() }
                    ) {
                        Text("Clear")
                    }
                }
            }

            Button(
                onClick = { uiActions.onSummarizeButtonClicked(uiState.inputText) },
                modifier = Modifier.padding(top = 16.dp),
                enabled = uiState.isInputValid && (uiState.status == SummarizeUiState.Status.Idle || uiState.status == SummarizeUiState.Status.Success || uiState.status == SummarizeUiState.Status.Error)
            ) {
                Text("Summarize")
            }

            // Download progress
            when (uiState.status) {
                SummarizeUiState.Status.Downloading -> {
                    uiState.downloadProgress?.let { progress ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 24.dp)
                        ) {
                            Text(
                                text = "Downloading model...",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            LinearProgressIndicator(
                                progress = { progress.progressPercentage },
                                modifier = Modifier.fillMaxWidth(),
                            )
                            Text(
                                text = "${(progress.progressPercentage * 100).toInt()}% (${
                                    formatBytes(
                                        progress.bytesDownloaded
                                    )
                                } / ${formatBytes(progress.totalBytes)})",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }

                SummarizeUiState.Status.Processing -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                        Text(
                            text = "Processing...",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                SummarizeUiState.Status.Success -> {
                    if (uiState.summary.isNotBlank()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 24.dp)
                        ) {
                            Text(
                                text = "Summary:",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                text = uiState.summary,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                SummarizeUiState.Status.Error -> {
                    uiState.errorMessage?.let { errorMessage ->
                        Text(
                            text = "Error: $errorMessage",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(top = 24.dp)
                        )
                    }
                }

                SummarizeUiState.Status.Idle -> {
                    // No additional UI when idle
                }
            }
        }
    }
}

private fun formatBytes(bytes: Long): String {
    return when {
        bytes < 1024 -> "$bytes B"
        bytes < 1024 * 1024 -> "${bytes / 1024} KB"
        else -> "${bytes / (1024 * 1024)} MB"
    }
}

@Preview
@Composable
fun SummarizeScreenPreview() {
    MaterialTheme {
        SummarizeScreen(
            uiState = SummarizeUiState(status = SummarizeUiState.Status.Success),
            uiActions = SummarizeUiActions.noOp
        )
    }
}