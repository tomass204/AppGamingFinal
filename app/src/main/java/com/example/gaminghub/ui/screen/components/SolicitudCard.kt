package com.example.gaminghub.ui.screen.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gaminghub.data.model.SolicitudModerador
import com.example.gaminghub.ui.components.ConfirmDialog
import com.example.gaminghub.utils.Result

@Composable
fun SolicitudCard(
    solicitud: SolicitudModerador,
    onAceptar: () -> Unit,
    onRechazar: () -> Unit,
    onResultMessage: (String) -> Unit
) {
    var showConfirmDialog by remember { mutableStateOf<Pair<String, () -> Unit>?>(null) }

    showConfirmDialog?.let { (message, action) ->
        ConfirmDialog(
            title = "Confirmación",
            message = message,
            onConfirm = {
                action()
                Result.Success(Unit)
            },
            onDismiss = { showConfirmDialog = null }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Usuario: ${solicitud.nombre}", style = MaterialTheme.typography.titleMedium)
            Text("Motivo: ${solicitud.motivo}", style = MaterialTheme.typography.bodyMedium)
            Text("Fecha: ${solicitud.fechaCreacion}", style = MaterialTheme.typography.bodySmall)

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = {
                        showConfirmDialog = "¿Deseas rechazar esta solicitud?" to onRechazar
                    }
                ) {
                    Text("Rechazar", color = MaterialTheme.colorScheme.error)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        showConfirmDialog = "¿Deseas aceptar esta solicitud?" to onAceptar
                    }
                ) {
                    Text("Aceptar")
                }
            }
        }
    }
}
