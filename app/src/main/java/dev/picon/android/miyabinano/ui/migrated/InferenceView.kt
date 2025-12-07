package dev.picon.android.miyabinano.ui.migrated

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * -------------------------------------------------------
 *  MODELOS (Dummy para que compile — reemplázalos)
 * -------------------------------------------------------
 */
enum class InferenceTask(val title: String) {
    Summarize("Resumir"),
    RewriteFormal("Reescritura formal")
}

class MainViewModel {

    // Estados simplificados
    sealed interface State {
        object Idle : State
        object Processing : State
    }

    private val _state = MutableStateFlow<State>(State.Idle)
    val state: StateFlow<State> = _state

    private val _generatedResponse = MutableStateFlow("")
    val generatedResponse: StateFlow<String> = _generatedResponse

    fun runTaskInference(task: InferenceTask, text: String) {
        _state.value = State.Processing

        // Simulación de trabajo
        _generatedResponse.value =
            "Resultado de '${task.title}'\n\nTexto procesado:\n$text"

        _state.value = State.Idle
    }
}

/**
 * -------------------------------------------------------
 *  SEGMENTED CONTROL (Equivalente al Picker de SwiftUI)
 * -------------------------------------------------------
 */
@Composable
fun TaskPicker(
    selected: InferenceTask,
    onSelected: (InferenceTask) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFE0E0E0))
            .padding(4.dp)
    ) {
        InferenceTask.values().forEach { task ->
            val isSelected = task == selected
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isSelected) Color(0xFF4CAF50) else Color.Transparent)
                    .clickable { onSelected(task) }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    task.title,
                    color = if (isSelected) Color.White else Color.Black,
                    fontSize = 14.sp
                )
            }
        }
    }
}

/**
 * -------------------------------------------------------
 *  INFERENCE VIEW (Traducción 1:1 de tu SwiftUI)
 * -------------------------------------------------------
 */
@Composable
fun InferenceView(viewModel: MainViewModel) {
    val state by viewModel.state.collectAsState()
    val generatedResponse by viewModel.generatedResponse.collectAsState()

    var inputText by remember { mutableStateOf("") }
    var selectedTask by remember { mutableStateOf(InferenceTask.Summarize) }

    val isProcessing = state is MainViewModel.State.Processing
    val focusManager = LocalFocusManager.current
    val clipboardManager = LocalClipboardManager.current

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { focusManager.clearFocus() }
        ) {

            Spacer(Modifier.height(16.dp))

            // HEADER
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Memory,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(40.dp)
                )
                Text(
                    "Gemma 2B Ready",
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    "100% Offline • On-Device AI",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(Modifier.height(24.dp))

            // TASK PICKER
            TaskPicker(
                selected = selectedTask,
                onSelected = { selectedTask = it },
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(24.dp))

            // INPUT
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    "Texto de entrada:",
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.bodyMedium
                )

                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    textStyle = LocalTextStyle.current.copy(fontSize = 16.sp)
                )
            }

            Spacer(Modifier.height(24.dp))

            // BUTTON
            Button(
                onClick = { viewModel.runTaskInference(selectedTask, inputText) },
                enabled = inputText.isNotEmpty() && !isProcessing,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                if (isProcessing) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(20.dp)
                            .padding(end = 8.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )
                    Text("Procesando…")
                } else {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Ejecutar")
                }
            }

            Spacer(Modifier.height(24.dp))

            // RESPONSE
            if (generatedResponse.isNotEmpty()) {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "Respuesta:",
                            fontWeight = FontWeight.Medium,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(Modifier.weight(1f))

                        TextButton(onClick = {
                            clipboardManager.setText(AnnotatedString(generatedResponse))
                        }) {
                            Icon(Icons.Default.ContentCopy, contentDescription = null)
                            Spacer(Modifier.width(4.dp))
                            Text("Copiar")
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 200.dp)
                            .background(Color(0xFFF2F2F2), RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            generatedResponse,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            Spacer(Modifier.height(40.dp))
        }
    }
}

/**
 * -------------------------------------------------------
 *  PREVIEW
 * -------------------------------------------------------
 */
@Preview(showBackground = true)
@Composable
fun InferenceViewPreview() {
    MaterialTheme {
        InferenceView(MainViewModel())
    }
}