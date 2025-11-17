package com.example.gaminghub.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gaminghub.data.model.Favorito
import com.example.gaminghub.data.model.Publicacion
import com.example.gaminghub.data.model.PublicacionCategory
import com.example.gaminghub.data.model.Reaccion
import com.example.gaminghub.data.network.repository.FavoritoRepository
import com.example.gaminghub.data.network.repository.PublicacionRepository
import com.example.gaminghub.data.network.repository.ReaccionRepository
import com.example.gaminghub.ui.navigation.SessionManager
import com.example.gaminghub.ui.viewmodel.state.NoticiaUiState
import com.example.gaminghub.utils.Result
import kotlinx.coroutines.launch


class NoticiaViewModel(
    private val repository: PublicacionRepository,
    private val sessionManager: SessionManager,
    private val reaccionRepository: ReaccionRepository,
    private val favoritoRepository: FavoritoRepository
) : ViewModel() {

    private val _uiState = mutableStateOf(NoticiaUiState())
    val uiState: State<NoticiaUiState> = _uiState


    fun loadNoticias() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                isSuccess = false,
                errorMessage = null,
                successMessage = null
            )

            val userId = sessionManager.getUserId() ?: 0L

            val publicacionesResult = repository.listaPublicaciones()
            if (publicacionesResult is Result.Error) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = publicacionesResult.message
                )
                return@launch
            }

            val favoritosResult = favoritoRepository.obtenerPorUsuario(userId)
            val likesResult = reaccionRepository.obtenerLikesPorUsuario(userId)

            val favoritosMap = when (favoritosResult) {
                is Result.Success -> favoritosResult.data
                    .mapNotNull { fav ->
                        fav.favoritoID?.let { favID ->
                            fav.publicacionID to favID
                        }
                    }.toMap()
                else -> emptyMap()
            }

            val likesMap = when (likesResult) {
                is Result.Success -> likesResult.data
                    .mapNotNull { like ->
                        like.reaccionID?.let { reaccionID ->
                            like.entidadID to reaccionID
                        }
                    }.toMap()
                else -> emptyMap()
            }

            val publicaciones = (publicacionesResult as? Result.Success)?.data ?: emptyList()
            val noticias = publicaciones.filter {
                it.tipo.equals(PublicacionCategory.NOTICIA.name, ignoreCase = true)
            }.map { noticia ->
                val likeId = likesMap[noticia.publicacionID]
                val favoritoId = favoritosMap[noticia.publicacionID]

                noticia.copy(
                    isLiked = likesMap.containsKey(noticia.publicacionID),
                    isFavorited = favoritosMap.containsKey(noticia.publicacionID),
                    likeID = likeId,
                    favoritoID = favoritoId
                )
            }

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                isSuccess = true,
                noticias = noticias,
                likesMap = likesMap,
                favoritosMap = favoritosMap
            )
        }
    }

    fun toggleLike(publicacion: Publicacion) {
        viewModelScope.launch {
            val userId = sessionManager.getUserId() ?: 0L
            if (userId == 0L) {
                _uiState.value = _uiState.value.copy(errorMessage = "Usuario no autenticado")
                return@launch
            }

            val actuales = _uiState.value.noticias.toMutableList()
            val index = actuales.indexOfFirst { it.publicacionID == publicacion.publicacionID }
            if (index == -1) return@launch

            val item = actuales[index]
            val nuevoEstadoLike = !item.isLiked

            val provisionalItem = if (nuevoEstadoLike) {
                item.copy(
                    isLiked = true,
                    likesCount = item.likesCount + 1
                )
            } else {
                item.copy(
                    isLiked = false,
                    likesCount = (item.likesCount - 1).coerceAtLeast(0)
                )
            }

            actuales[index] = provisionalItem
            _uiState.value = _uiState.value.copy(noticias = actuales)

            try {
                if (nuevoEstadoLike) {
                    val reaccion = Reaccion(
                        usuarioID = userId,
                        entidadID = item.publicacionID ?: 0L,
                        tipoEntidad = "publicacion",
                        tipoReaccion = "like"
                    )

                    when (val result = reaccionRepository.guardar(reaccion)) {
                        is Result.Success -> {
                            val reaccionGuardada = result.data
                            val newLikesMap = _uiState.value.likesMap.toMutableMap()
                            newLikesMap[item.publicacionID ?: 0L] = reaccionGuardada.reaccionID ?: 0L

                            actuales[index] = provisionalItem.copy(
                                likeID = reaccionGuardada.reaccionID,
                                isLiked = true
                            )

                            _uiState.value = _uiState.value.copy(
                                noticias = actuales,
                                likesMap = newLikesMap
                            )
                        }

                        is Result.Error -> {
                            actuales[index] = item
                            _uiState.value = _uiState.value.copy(
                                noticias = actuales,
                                errorMessage = result.message
                            )
                        }

                        else -> Unit
                    }
                } else {
                    val likeID = item.likeID ?: 0L
                    if (likeID != 0L) {
                        when (val result = reaccionRepository.eliminar(likeID)) {
                            is Result.Success -> {
                                val newLikesMap = _uiState.value.likesMap.toMutableMap()
                                newLikesMap.remove(item.publicacionID)

                                actuales[index] = provisionalItem.copy(
                                    likeID = null,
                                    isLiked = false
                                )

                                _uiState.value = _uiState.value.copy(
                                    noticias = actuales,
                                    likesMap = newLikesMap
                                )
                            }

                            is Result.Error -> {
                                actuales[index] = item
                                _uiState.value = _uiState.value.copy(
                                    noticias = actuales,
                                    errorMessage = result.message
                                )
                            }

                            else -> Unit
                        }
                    } else {
                        actuales[index] = item
                        _uiState.value = _uiState.value.copy(noticias = actuales)
                    }
                }
            } catch (e: Exception) {
                actuales[index] = item
                _uiState.value = _uiState.value.copy(
                    noticias = actuales,
                    errorMessage = "Error toggleLike: ${e.message}"
                )
            }
        }
    }

    fun toggleFavorito(publicacion: Publicacion) {
        viewModelScope.launch {
            val userId = sessionManager.getUserId() ?: 0L
            if (userId == 0L) {
                _uiState.value = _uiState.value.copy(errorMessage = "Usuario no autenticado")
                return@launch
            }

            val actuales = _uiState.value.noticias.toMutableList()
            val index = actuales.indexOfFirst { it.publicacionID == publicacion.publicacionID }
            if (index == -1) return@launch

            val item = actuales[index]
            val nuevoEstadoFavorito = !item.isFavorited

            val provisionalItem = item.copy(isFavorited = nuevoEstadoFavorito)

            actuales[index] = provisionalItem
            _uiState.value = _uiState.value.copy(noticias = actuales)

            try {
                if (nuevoEstadoFavorito) {
                    val favorito = Favorito(
                        usuarioID = userId,
                        publicacionID = item.publicacionID ?: 0L
                    )

                    when (val result = favoritoRepository.guardar(favorito)) {
                        is Result.Success -> {
                            val favoritoGuardado = result.data
                            val newFavoritoMap = _uiState.value.favoritosMap.toMutableMap()
                            newFavoritoMap[item.publicacionID ?: 0L] = favoritoGuardado.favoritoID ?: 0L

                            actuales[index] = provisionalItem.copy(
                                favoritoID = favoritoGuardado.favoritoID,
                                isFavorited = true
                            )

                            _uiState.value = _uiState.value.copy(
                                noticias = actuales,
                                favoritosMap = newFavoritoMap
                            )
                        }

                        is Result.Error -> {
                            actuales[index] = item
                            _uiState.value = _uiState.value.copy(
                                noticias = actuales,
                                errorMessage = result.message
                            )
                        }

                        else -> Unit
                    }
                } else {
                    val favoritoId = item.favoritoID ?: 0L
                    if (favoritoId != 0L) {
                        when (val result = favoritoRepository.eliminar(favoritoId)) {
                            is Result.Success -> {
                                val newFavoritoMap = _uiState.value.favoritosMap.toMutableMap()
                                newFavoritoMap.remove(item.publicacionID)

                                actuales[index] = provisionalItem.copy(
                                    favoritoID = null,
                                    isFavorited = false
                                )

                                _uiState.value = _uiState.value.copy(
                                    noticias = actuales,
                                    favoritosMap = newFavoritoMap
                                )
                            }

                            is Result.Error -> {
                                actuales[index] = item
                                _uiState.value = _uiState.value.copy(
                                    noticias = actuales,
                                    errorMessage = result.message
                                )
                            }

                            else -> Unit
                        }
                    } else {
                        actuales[index] = item
                        _uiState.value = _uiState.value.copy(noticias = actuales)
                    }
                }
            } catch (e: Exception) {
                actuales[index] = item
                _uiState.value = _uiState.value.copy(
                    noticias = actuales,
                    errorMessage = "Error toggleFavorito: ${e.message}"
                )
            }
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null,
            successMessage = null
        )
    }






}
