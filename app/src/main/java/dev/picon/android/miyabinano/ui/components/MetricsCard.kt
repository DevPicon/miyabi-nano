package dev.picon.android.miyabinano.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.picon.android.miyabinano.domain.model.InferenceMetrics
import java.text.NumberFormat

@Composable
fun MetricsCard(
    metrics: InferenceMetrics,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Performance Metrics",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )

            MetricRow(
                label = "Latency",
                value = "${formatNumber(metrics.inferenceTimeMs)} ms"
            )

            MetricRow(
                label = "Tokens",
                value = "${formatNumber(metrics.inputTokenCount)} → ${formatNumber(metrics.outputTokenCount)}"
            )

            MetricRow(
                label = "Characters",
                value = "${formatNumber(metrics.inputCharCount)} → ${formatNumber(metrics.outputCharCount)}"
            )

            MetricRow(
                label = "Memory",
                value = "${formatNumber(metrics.memoryUsedMB)} MB"
            )

            if (metrics.modelLoadTimeMs != null) {
                MetricRow(
                    label = "Load Time",
                    value = "${formatNumber(metrics.modelLoadTimeMs)} ms"
                )
            }
        }
    }
}

@Composable
private fun MetricRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

private fun formatNumber(number: Number): String {
    return NumberFormat.getInstance().format(number)
}
