package dev.picon.android.miyabinano.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.picon.android.miyabinano.domain.model.AppDiagnostics
import dev.picon.android.miyabinano.domain.model.ExperimentContext

@Composable
fun DiagnosticsDialog(
    diagnostics: AppDiagnostics,
    readiness: String,
    baseModelName: String?,
    latestRunContext: ExperimentContext?,
    latestFailureDetail: String?,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Platform diagnostics") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 520.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DiagnosticsSection(
                    title = "App",
                    values = listOf(
                        "App version" to diagnostics.appVersion,
                        "Room database version" to diagnostics.databaseVersion.toString(),
                        "Experiment schema version" to
                            diagnostics.experimentSchemaVersion.toString()
                    )
                )
                DiagnosticsSection(
                    title = "Current platform",
                    values = listOf(
                        "Device" to
                            "${diagnostics.deviceManufacturer} ${diagnostics.deviceModel}",
                        "Android build" to diagnostics.androidBuild,
                        "API level" to diagnostics.apiLevel.toString(),
                        "Connectivity" to diagnostics.connectivity,
                        "Power" to diagnostics.powerState,
                        "Thermal status" to diagnostics.thermalStatus,
                        "Capability readiness" to readiness,
                        "Gemini Nano identity" to (baseModelName ?: "Not currently exposed")
                    )
                )
                latestRunContext?.let { context ->
                    DiagnosticsSection(
                        title = "Latest successful run",
                        values = listOf(
                            "Feature status before run" to context.featureStatusBeforeRun,
                            "Gemini Nano identity" to
                                (context.baseModelName ?: "Not exposed"),
                            "Connectivity" to context.connectivity,
                            "Power" to context.powerState,
                            "Thermal status" to context.thermalStatus,
                            "Run sequence" to context.runSequence.toString(),
                            "Fixture ID" to (context.fixtureId ?: "Arbitrary input"),
                            "Heuristic input size" to
                                context.heuristicInputSize.toString(),
                            "Outcome" to context.outcomeCategory
                        )
                    )
                }
                latestFailureDetail?.let { detail ->
                    DiagnosticsSection(
                        title = "Latest visible failure",
                        values = listOf("SDK detail" to detail)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

@Composable
private fun DiagnosticsSection(
    title: String,
    values: List<Pair<String, String>>
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )
        values.forEach { (label, value) ->
            Text(
                text = "$label: $value",
                modifier = Modifier.padding(start = 4.dp),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
