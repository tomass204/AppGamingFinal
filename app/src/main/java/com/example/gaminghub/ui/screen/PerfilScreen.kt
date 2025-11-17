package com.example.gaminghub.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gaminghub.data.model.UserRole
import com.example.gaminghub.data.network.repository.SolicitudModeradorRepository
import com.example.gaminghub.data.network.repository.UsuarioRepository
import com.example.gaminghub.ui.components.SolicitudModeradorDialog
import com.example.gaminghub.ui.navigation.SessionManager
import com.example.gaminghub.ui.screen.components.ChangePasswordDialog
import com.example.gaminghub.ui.screen.components.PerfilHeader
import com.example.gaminghub.ui.viewmodel.AuthViewModel
import com.example.gaminghub.ui.viewmodel.AuthViewModelFactory
import com.example.gaminghub.ui.viewmodel.SolicitudModeradorViewModel
import com.example.gaminghub.ui.viewmodel.SolicitudModeradorViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun PerfilScreen(
    sessionManager: SessionManager,
    solicitudModeradorRepository: SolicitudModeradorRepository,
    usuarioRepository: UsuarioRepository,
    onLogout: () -> Unit
) {
    val solicitudModeradorViewModel: SolicitudModeradorViewModel = viewModel(
        factory = SolicitudModeradorViewModelFactory(solicitudModeradorRepository, sessionManager)
    )

    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(usuarioRepository, sessionManager)
    )

    val solicitudState by solicitudModeradorViewModel.uiState
    val authState by authViewModel.uiState

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var showSolicitudDialog by remember { mutableStateOf(false) }
    var showPasswordDialog by remember { mutableStateOf(false) }

    val buttonWidth = Modifier.fillMaxWidth(0.85f)

    LaunchedEffect(
        solicitudState.successMessage,
        solicitudState.errorMessage,
        authState.successMessage,
        authState.errorMessage
    ) {
        solicitudState.successMessage?.let {
            scope.launch { snackbarHostState.showSnackbar(it) }
            solicitudModeradorViewModel.clearMessages()
        }
        solicitudState.errorMessage?.let {
            scope.launch { snackbarHostState.showSnackbar(it) }
            solicitudModeradorViewModel.clearMessages()
        }

        authState.successMessage?.let {
            scope.launch { snackbarHostState.showSnackbar(it) }
            authViewModel.clearMessages()
            showPasswordDialog = false
        }
        authState.errorMessage?.let {
            scope.launch { snackbarHostState.showSnackbar(it) }
            authViewModel.clearMessages()
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PerfilHeader(sessionManager)
            Spacer(Modifier.height(32.dp))

            if (sessionManager.getUserRole().equals(UserRole.USUARIO_BASICO.name)) {
                Button(
                    onClick = { showSolicitudDialog = true },
                    modifier = buttonWidth
                ) {
                    Text("Solicitar ser Moderador")
                }
                Spacer(Modifier.height(16.dp))
            }

            Button(
                onClick = { showPasswordDialog = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFA000)
                ),
                modifier = buttonWidth
            ) {
                Text("Cambiar Contraseña")
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = onLogout,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                modifier = buttonWidth
            ) {
                Text("Cerrar Sesión")
            }
        }
    }

    if (showSolicitudDialog) {
        SolicitudModeradorDialog(
            isLoadingRemote = authState.isLoading,
            onSubmit = { motivo ->
                solicitudModeradorViewModel.crearSolicitud(sessionManager,motivo)
            },
            onDismiss = { showSolicitudDialog = false }
        )
    }

    if (showPasswordDialog) {
        ChangePasswordDialog(
            isLoading = authState.isLoading,
            onConfirm = { newPassword, confirmPassword ->
                authViewModel.changePassword(newPassword, confirmPassword, sessionManager)
            },
            onDismiss = { showPasswordDialog = false }
        )
    }

}
