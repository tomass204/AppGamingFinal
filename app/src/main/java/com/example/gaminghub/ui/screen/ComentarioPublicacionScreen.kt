package com.example.gaminghub.ui.screen

import ComentarioViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gaminghub.data.network.repository.ComentarioRepository
import com.example.gaminghub.data.network.repository.ImagenRepository
import com.example.gaminghub.data.network.repository.PublicacionRepository
import com.example.gaminghub.ui.navigation.SessionManager
import com.example.gaminghub.ui.screen.components.ComentarioCard
import com.example.gaminghub.ui.screen.components.PublicacionComentariosCard
import com.example.gaminghub.ui.viewmodel.ComentarioViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComentarioPublicacionScreen(
    publicacionId: Long,
    sessionManager: SessionManager,
    comentarioRepository: ComentarioRepository,
    publicacionRepository: PublicacionRepository,
    imagenRepository: ImagenRepository,
    onNavigateBack: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val comentarioViewModel: ComentarioViewModel = viewModel(
        factory = ComentarioViewModelFactory(
            publicacionId = publicacionId,
            sessionManager = sessionManager,
            repository = comentarioRepository,
            publicacionRepository = publicacionRepository,
            imagenRepository = imagenRepository
        )
    )

    val uiState by comentarioViewModel.uiState
    var text by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        comentarioViewModel.cargarComentarios()
    }

    LaunchedEffect(uiState.errorMessage, uiState.successMessage) {
        uiState.errorMessage?.let {
            scope.launch { snackbarHostState.showSnackbar(it) }
            comentarioViewModel.clearMessages()
        }
        uiState.successMessage?.let {
            scope.launch { snackbarHostState.showSnackbar(it) }
            comentarioViewModel.clearMessages()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Escribe un comentario...") },
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = {
                        if (text.isNotBlank()) {
                            comentarioViewModel.agregarComentario(text)
                            text = ""
                        }
                    },
                    enabled = text.isNotBlank()
                ) {
                    Icon(
                        imageVector = Icons.Filled.Send,
                        contentDescription = "Enviar",
                        tint = if (text.isNotBlank())
                            MaterialTheme.colorScheme.primary
                        else Color.Gray
                    )
                }
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = padding.calculateBottomPadding())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    "Comentarios",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            uiState.publicacion?.let { pub ->
                PublicacionComentariosCard(
                    publicacion = pub,
                    imagenRepository = imagenRepository
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Comentarios (${uiState.comentarios.size})",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            PullToRefreshBox(
                isRefreshing = uiState.isLoading,
                onRefresh = { comentarioViewModel.cargarComentarios() },
                modifier = Modifier.fillMaxSize()
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (uiState.comentarios.isEmpty() && !uiState.isLoading) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillParentMaxSize()
                                    .padding(top = 32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "Sé el primero en comentar.",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    } else {
                        items(uiState.comentarios, key = { it.comentarioID ?: 0L }) { comentario ->
                            ComentarioCard(
                                comentario = comentario,
                                sessionManager = sessionManager,
                                onDelete = {
                                    comentarioViewModel.eliminarComentario(
                                        comentario.comentarioID ?: 0L
                                    )
                                },
                                onResultMessage = { mensaje ->
                                    scope.launch {
                                        snackbarHostState.showSnackbar(mensaje)
                                    }
                                }
                            )
                        }
                    }

                    if (uiState.isLoading) {
                        item {
                            Box(
                                modifier = Modifier.fillParentMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    }
}

