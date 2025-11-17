package com.example.gaminghub.ui.screen.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.gaminghub.data.model.Comentario
import com.example.gaminghub.data.model.UserRole
import com.example.gaminghub.ui.components.ConfirmDialog
import com.example.gaminghub.ui.navigation.SessionManager
import com.example.gaminghub.utils.Result

@Composable
fun ComentarioCard(
    comentario: Comentario,
    sessionManager: SessionManager,
    onDelete: () -> Unit,
    onResultMessage: (String) -> Unit
) {
    var showConfirmDialog by remember { mutableStateOf<Pair<String, () -> Unit>?>(null) }

    showConfirmDialog?.let { (message, action) ->
        ConfirmDialog(
            title = "Eliminar comentario",
            message = message,
            confirmText = "Eliminar",
            cancelText = "Cancelar",
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
            .padding(horizontal = 8.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = comentario.autorNombre ?: "Usuario",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    comentario.contenido,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            val currentUserRole = sessionManager.getUserRole()
            if (currentUserRole == UserRole.MODERADOR.name || currentUserRole == UserRole.PROPIETARIO.name) {
                IconButton(
                    onClick = {
                        showConfirmDialog = "¿Deseas eliminar este comentario?" to onDelete
                    }
                ) {
                    Icon(
                        Icons.Filled.DeleteOutline,
                        "Eliminar Comentario",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
