package com.example.gaminghub.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SolicitudModeradorDialog(
    isLoadingRemote: Boolean = false,
    onSubmit: suspend (String) -> Unit,
    onDismiss: () -> Unit
) {
    var textInput by remember { mutableStateOf("") }
    var localLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    AlertDialog(
        onDismissRequest = { if (!isLoadingRemote && !localLoading) onDismiss() },
        title = { Text("Solicitud de moderador", fontWeight = FontWeight.Bold) },
        text = {
            if (isLoadingRemote || localLoading) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(48.dp))
                    Spacer(Modifier.height(12.dp))
                    Text("Enviando solicitud...")
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Por favor, explique brevemente por qué desea convertirse en moderador.")
                    OutlinedTextField(
                        value = textInput,
                        onValueChange = { textInput = it },
                        label = { Text("Ingrese el motivo") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoadingRemote && !localLoading,
                        minLines = 3,
                        maxLines = 5
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (textInput.isNotBlank() && !isLoadingRemote && !localLoading) {
                        localLoading = true
                        scope.launch {
                            delay(600)
                            onSubmit(textInput)
                            localLoading = false
                            onDismiss()
                        }
                    }
                },
                enabled = !isLoadingRemote && !localLoading && textInput.isNotBlank()
            ) {
                Text("Enviar")
            }
        },
        dismissButton = {
            TextButton(onClick = { if (!isLoadingRemote && !localLoading) onDismiss() }) {
                Text("Cancelar")
            }
        },
        properties = DialogProperties(dismissOnClickOutside = false)
    )
}
