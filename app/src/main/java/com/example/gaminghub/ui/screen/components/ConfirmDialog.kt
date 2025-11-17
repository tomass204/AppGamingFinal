package com.example.gaminghub.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.launch


@Composable
fun ConfirmDialog(
    title: String,
    message: String,
    icon: ImageVector? = Icons.Default.Warning,
    confirmText: String = "Aceptar",
    cancelText: String = "Cancelar",
    isLoading: Boolean = false,
    onConfirm: suspend () -> Unit,
    onDismiss: () -> Unit
) {
    var localLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    AlertDialog(
        onDismissRequest = { if (!localLoading && !isLoading) onDismiss() },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (icon != null) {
                    Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(title)
            }
        },
        text = { Text(message) },
        confirmButton = {
            Button(
                onClick = {
                    if (!localLoading && !isLoading) {
                        localLoading = true
                        scope.launch {
                            onConfirm()
                            localLoading = false
                            onDismiss()
                        }
                    }
                },
                enabled = !localLoading && !isLoading
            ) {
                if (localLoading || isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(confirmText)
                }
            }
        },
        dismissButton = {
            TextButton(onClick = { if (!localLoading && !isLoading) onDismiss() }) {
                Text(cancelText)
            }
        },
        properties = DialogProperties(dismissOnClickOutside = false)
    )
}
