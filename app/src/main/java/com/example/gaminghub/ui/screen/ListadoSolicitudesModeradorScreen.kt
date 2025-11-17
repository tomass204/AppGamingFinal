package com.example.gaminghub.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gaminghub.data.network.repository.SolicitudModeradorRepository
import com.example.gaminghub.ui.navigation.SessionManager
import com.example.gaminghub.ui.screen.components.SolicitudCard
import com.example.gaminghub.ui.viewmodel.SolicitudModeradorViewModel
import com.example.gaminghub.ui.viewmodel.SolicitudModeradorViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaSolicitudesScreen(
    navController: NavController,
    sessionManager: SessionManager,
    solicitudModeradorRepository: SolicitudModeradorRepository
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val solicitudModeradorViewModel: SolicitudModeradorViewModel = viewModel(
        factory = SolicitudModeradorViewModelFactory(
            solicitudModeradorRepository,
            sessionManager
        )
    )

    val uiState by solicitudModeradorViewModel.uiState

    LaunchedEffect(Unit) {
        solicitudModeradorViewModel.loadPendingRequests()
    }

    LaunchedEffect(uiState.errorMessage, uiState.successMessage) {
        uiState.errorMessage?.let {
            scope.launch { snackbarHostState.showSnackbar(it) }
            solicitudModeradorViewModel.clearMessages()
        }
        uiState.successMessage?.let {
            scope.launch { snackbarHostState.showSnackbar(it) }
            solicitudModeradorViewModel.clearMessages()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->

        PullToRefreshBox(
            isRefreshing = uiState.isLoading,
            onRefresh = { solicitudModeradorViewModel.loadPendingRequests() },
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = innerPadding.calculateBottomPadding()
                )
        ) {
            when {
                uiState.isLoading && uiState.solicitudes.isEmpty() -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                uiState.solicitudes.isEmpty() -> {
                    Text(
                        text = "No hay solicitudes pendientes.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(
                            top = 8.dp,
                            bottom = innerPadding.calculateBottomPadding()
                        )
                    ) {
                        items(uiState.solicitudes, key = { it.solicitudId ?: 0L }) { solicitud ->
                            SolicitudCard(
                                solicitud = solicitud,
                                onAceptar = {
                                    solicitudModeradorViewModel.processApproval(solicitud, sessionManager)
                                },
                                onRechazar = {
                                    solicitudModeradorViewModel.rejectRequest(solicitud, sessionManager)
                                },
                                onResultMessage = { mensaje ->
                                    scope.launch {
                                        snackbarHostState.showSnackbar(mensaje)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
