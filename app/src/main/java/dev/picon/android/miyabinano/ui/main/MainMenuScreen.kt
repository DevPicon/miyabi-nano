package dev.picon.android.miyabinano.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.ShortText
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.picon.android.miyabinano.domain.genai.CapabilityPreparationState
import dev.picon.android.miyabinano.domain.genai.BaseModelIdentityState
import dev.picon.android.miyabinano.domain.model.InferenceCapability
import dev.picon.android.miyabinano.ui.components.DiagnosticsDialog
import dev.picon.android.miyabinano.ui.components.diagnosticsLabel
import dev.picon.android.miyabinano.ui.components.exposedNameOrNull
import dev.picon.android.miyabinano.ui.components.failureDetailOrNull

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenuScreen(
    onCapabilitySelected: (InferenceCapability) -> Unit,
    viewModel: ModelDownloadViewModel = hiltViewModel()
) {
    val bootstrapState by viewModel.bootstrapState.collectAsState()
    val baseModelIdentity by viewModel.baseModelIdentity.collectAsState()
    val diagnostics by viewModel.diagnostics.collectAsState()
    var showDiagnostics by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Miyabi Nano",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.refreshDiagnostics()
                            showDiagnostics = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Platform diagnostics"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "On-device AI with Gemini Nano",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            ModelDownloadSection(
                bootstrapState = bootstrapState,
                baseModelIdentity = baseModelIdentity,
                onDownloadClick = viewModel::startProvisioning,
                onRetryClick = viewModel::refresh,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Experiments",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            CapabilityCard(
                capability = InferenceCapability.SUMMARIZATION,
                icon = Icons.Default.Article,
                onClick = { onCapabilitySelected(InferenceCapability.SUMMARIZATION) }
            )

            CapabilityCard(
                capability = InferenceCapability.PROOFREADING,
                icon = Icons.Default.RateReview,
                onClick = { onCapabilitySelected(InferenceCapability.PROOFREADING) }
            )

            CapabilityCard(
                capability = InferenceCapability.REWRITE_FORMAL,
                icon = Icons.Default.TextFields,
                onClick = { onCapabilitySelected(InferenceCapability.REWRITE_FORMAL) }
            )

            CapabilityCard(
                capability = InferenceCapability.REWRITE_CASUAL,
                icon = Icons.Default.Edit,
                onClick = { onCapabilitySelected(InferenceCapability.REWRITE_CASUAL) }
            )

            CapabilityCard(
                capability = InferenceCapability.REWRITE_CONCISE,
                icon = Icons.Default.ShortText,
                onClick = { onCapabilitySelected(InferenceCapability.REWRITE_CONCISE) }
            )
        }
    }

    if (showDiagnostics) {
        DiagnosticsDialog(
            diagnostics = diagnostics,
            readiness = bootstrapState.diagnosticsLabel(),
            baseModelName = baseModelIdentity.exposedNameOrNull(),
            latestRunContext = null,
            latestFailureDetail = bootstrapState.failureDetailOrNull()
                ?: baseModelIdentity.failureDetailOrNull(),
            onDismiss = { showDiagnostics = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CapabilityCard(
    capability: InferenceCapability,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(24.dp)
            )

            Text(
                text = capability.displayName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Text(
                text = capability.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
private fun ModelDownloadSection(
    bootstrapState: CapabilityPreparationState,
    baseModelIdentity: BaseModelIdentityState,
    onDownloadClick: () -> Unit,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Gemini Nano Model",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = when (baseModelIdentity) {
                    BaseModelIdentityState.Checking -> "Gemini Nano identity: checking..."
                    BaseModelIdentityState.NotReady -> "Gemini Nano identity: available after initial setup"
                    is BaseModelIdentityState.Available ->
                        "Gemini Nano identity: ${baseModelIdentity.name}"
                    BaseModelIdentityState.Unavailable ->
                        "Gemini Nano identity: unavailable for this configuration"
                    is BaseModelIdentityState.Failed ->
                        "Gemini Nano identity: could not be retrieved"
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            when (bootstrapState) {
                is CapabilityPreparationState.Checking -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp
                        )
                        Text(
                            text = "Checking model status...",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                is CapabilityPreparationState.Unavailable -> {
                    Text(
                        text = "Model not supported on this device",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                is CapabilityPreparationState.Downloadable -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Download model for offline AI",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Button(onClick = onDownloadClick) {
                            Text("Initial setup")
                        }
                    }
                }

                is CapabilityPreparationState.Downloading -> {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Downloading model...",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "${(bootstrapState.progress * 100).toInt()}%",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        LinearProgressIndicator(
                            progress = { bootstrapState.progress },
                            modifier = Modifier.fillMaxWidth(),
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "${formatBytes(bootstrapState.downloadedBytes)} / ${formatBytes(bootstrapState.totalBytes)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                is CapabilityPreparationState.Available -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(
                                    MaterialTheme.colorScheme.primary,
                                    shape = RoundedCornerShape(4.dp)
                                )
                        )
                        Text(
                            text = "Capability ready",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                is CapabilityPreparationState.Failed -> {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = bootstrapState.failure.userMessage,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )

                        Text(
                            text = bootstrapState.failure.recoveryGuidance,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Technical detail: ${bootstrapState.failure.technicalDetail}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        TextButton(onClick = onRetryClick) {
                            Text("Retry")
                        }
                    }
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
