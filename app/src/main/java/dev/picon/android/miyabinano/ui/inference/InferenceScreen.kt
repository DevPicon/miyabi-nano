package dev.picon.android.miyabinano.ui.inference

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.picon.android.miyabinano.ui.components.MetricsCard
import dev.picon.android.miyabinano.ui.components.TestDataSelectorDialog
import dev.picon.android.miyabinano.domain.genai.CapabilityPreparationState
import dev.picon.android.miyabinano.ui.components.DiagnosticsDialog
import dev.picon.android.miyabinano.ui.components.diagnosticsLabel
import dev.picon.android.miyabinano.ui.components.failureDetailOrNull

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InferenceScreen(
    onNavigateBack: () -> Unit,
    viewModel: InferenceViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val diagnostics by viewModel.diagnostics.collectAsState()
    val isReady = uiState.preparationState is CapabilityPreparationState.Available
    var showDiagnostics by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = uiState.capability.displayName,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = uiState.capability.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            CapabilityPreparationCard(
                state = uiState.preparationState,
                onDownload = viewModel::startProvisioning,
                onRetry = viewModel::refreshPreparation
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { viewModel.onShowTestCaseSelector() },
                    enabled = isReady,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Load Test Data")
                }

                OutlinedButton(
                    onClick = { viewModel.onClearInput() },
                    enabled = uiState.inputText.isNotEmpty()
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = null
                    )
                    Text("Clear")
                }
            }

            OutlinedTextField(
                value = uiState.inputText,
                onValueChange = { viewModel.onInputTextChanged(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                label = { Text("Input Text") },
                placeholder = { Text("Enter text to ${uiState.capability.name.lowercase()}...") },
                enabled = isReady && !uiState.isProcessing,
                supportingText = {
                    Text("${uiState.inputText.length} characters")
                }
            )

            Button(
                onClick = { viewModel.onRunInference() },
                modifier = Modifier.fillMaxWidth(),
                enabled = isReady && !uiState.isProcessing && uiState.inputText.isNotEmpty()
            ) {
                if (uiState.isProcessing) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(end = 8.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
                Text(if (uiState.isProcessing) "Processing..." else "Run Inference")
            }

            if (uiState.error != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = uiState.error ?: "",
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        if (uiState.errorTechnicalDetail != null) {
                            Text(
                                text = "Technical detail: ${uiState.errorTechnicalDetail}",
                                modifier = Modifier.padding(top = 8.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }
            }

            if (uiState.blockingReason != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = uiState.blockingReason ?: "",
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Text(
                            text = uiState.recoveryGuidance ?: "",
                            modifier = Modifier.padding(top = 8.dp),
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }

            if (uiState.outputText.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Output",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(
                        text = uiState.outputText,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            if (uiState.metrics != null) {
                Spacer(modifier = Modifier.height(8.dp))
                MetricsCard(metrics = uiState.metrics!!)
            }
        }

        if (uiState.showTestCaseSelector) {
            TestDataSelectorDialog(
                testCases = uiState.availableTestCases,
                onTestCaseSelected = { viewModel.onTestCaseSelected(it) },
                onDismiss = { viewModel.onHideTestCaseSelector() }
            )
        }

        if (showDiagnostics) {
            DiagnosticsDialog(
                diagnostics = diagnostics,
                readiness = uiState.preparationState.diagnosticsLabel(),
                baseModelName = uiState.metrics?.experimentContext?.baseModelName,
                latestRunContext = uiState.metrics?.experimentContext,
                latestFailureDetail = uiState.errorTechnicalDetail
                    ?: uiState.preparationState.failureDetailOrNull(),
                onDismiss = { showDiagnostics = false }
            )
        }
    }
}

@Composable
private fun CapabilityPreparationCard(
    state: CapabilityPreparationState,
    onDownload: () -> Unit,
    onRetry: () -> Unit
) {
    if (state is CapabilityPreparationState.Available) return

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            when (state) {
                CapabilityPreparationState.Checking ->
                    Text("Checking capability readiness...")
                CapabilityPreparationState.Downloadable -> {
                    Text("This experiment needs an additional on-device capability asset.")
                    Button(onClick = onDownload) {
                        Text("Set up capability")
                    }
                }
                is CapabilityPreparationState.Downloading -> {
                    Text("Setting up capability...")
                    LinearProgressIndicator(
                        progress = { state.progress },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                CapabilityPreparationState.Unavailable ->
                    Text("This experiment is not supported by the current device configuration.")
                is CapabilityPreparationState.Failed -> {
                    Text(state.failure.userMessage)
                    Text(state.failure.recoveryGuidance)
                    TextButton(onClick = onRetry) {
                        Text("Retry")
                    }
                }
                CapabilityPreparationState.Available -> Unit
            }
        }
    }
}
