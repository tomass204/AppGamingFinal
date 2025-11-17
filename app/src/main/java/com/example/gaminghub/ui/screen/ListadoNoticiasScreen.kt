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
import com.example.gaminghub.data.network.repository.FavoritoRepository
import com.example.gaminghub.data.network.repository.ImagenRepository
import com.example.gaminghub.data.network.repository.PublicacionRepository
import com.example.gaminghub.data.network.repository.ReaccionRepository
import com.example.gaminghub.ui.navigation.SessionManager
import com.example.gaminghub.ui.screen.components.PublicacionCard
import com.example.gaminghub.ui.viewmodel.NoticiaViewModel
import com.example.gaminghub.ui.viewmodel.NoticiaViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListadoNoticiasScreen(
    navController: NavController,
    sessionManager: SessionManager,
    repository: PublicacionRepository,
    reaccionRepository: ReaccionRepository,
    favoritoRepository: FavoritoRepository,
    imagenRepository: ImagenRepository,
    onCommentClick: (Long) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val noticiaViewModel: NoticiaViewModel = viewModel(
        factory = NoticiaViewModelFactory(
            repository,
            sessionManager,
            reaccionRepository,
            favoritoRepository
        )
    )

    val uiState by noticiaViewModel.uiState

    LaunchedEffect(Unit) {
        noticiaViewModel.loadNoticias()
    }

    LaunchedEffect(uiState.errorMessage, uiState.successMessage) {
        uiState.errorMessage?.let {
            scope.launch { snackbarHostState.showSnackbar(it) }
            noticiaViewModel.clearMessages()
        }
        uiState.successMessage?.let {
            scope.launch { snackbarHostState.showSnackbar(it) }
            noticiaViewModel.clearMessages()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->

        PullToRefreshBox(
            isRefreshing = uiState.isLoading,
            onRefresh = { noticiaViewModel.loadNoticias() },
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = innerPadding.calculateBottomPadding()
                )
        ) {
            when {
                uiState.isLoading && uiState.noticias.isEmpty() -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                uiState.noticias.isEmpty() -> {
                    Text(
                        text = "No hay noticias disponibles.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(
                            top = 8.dp,
                            bottom = innerPadding.calculateBottomPadding()
                        )
                    ) {
                        items(uiState.noticias, key = { it.publicacionID ?: 0L }) { noticia ->
                            PublicacionCard(
                                publicacion = noticia,
                                imagenRepository = imagenRepository,
                                onLikeClick = { noticiaViewModel.toggleLike(noticia) },
                                onCommentClick = {
                                    onCommentClick(noticia.publicacionID ?: 0L)
                                },
                                onFavoriteClick = {
                                    noticiaViewModel.toggleFavorito(noticia)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

