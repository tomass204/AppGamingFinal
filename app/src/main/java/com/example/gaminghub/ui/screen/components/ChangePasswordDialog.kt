package com.example.gaminghub.ui.screen.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ChangePasswordDialog(
    isLoading: Boolean = false,
    onConfirm: suspend (String, String) -> Unit,
    onDismiss: () -> Unit
) {
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var localLoading by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val loading = isLoading || localLoading

    AlertDialog(
        onDismissRequest = { if (!loading) onDismiss() },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Lock, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cambiar contraseña", style = MaterialTheme.typography.titleLarge)
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ingresa y confirma tu nueva contraseña.")

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        errorMessage = null
                    },
                    label = { Text("Nueva contraseña") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !loading
                )

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        errorMessage = null
                    },
                    label = { Text("Confirmar contraseña") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !loading
                )

                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                if (loading) {
                    Spacer(Modifier.height(16.dp))
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .size(40.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (!loading) {
                        when {
                            password.isBlank() || confirmPassword.isBlank() ->
                                errorMessage = "Los campos no pueden estar vacíos."
                            password != confirmPassword ->
                                errorMessage = "Las contraseñas no coinciden."
                            password.length < 5 ->
                                errorMessage = "Debe tener al menos 5 caracteres."
                            else -> {
                                localLoading = true
                                scope.launch {
                                    val minDelay = 1000L
                                    val startTime = System.currentTimeMillis()

                                    onConfirm(password, confirmPassword)

                                    val elapsed = System.currentTimeMillis() - startTime
                                    if (elapsed < minDelay) delay(minDelay - elapsed)

                                    localLoading = false
                                    onDismiss()
                                }
                            }
                        }
                    }
                },
                enabled = !loading
            ) {
                Text("Aceptar")
            }
        },
        dismissButton = {
            TextButton(onClick = { if (!loading) onDismiss() }) {
                Text("Cancelar")
            }
        },
        properties = DialogProperties(dismissOnClickOutside = false)
    )
}
