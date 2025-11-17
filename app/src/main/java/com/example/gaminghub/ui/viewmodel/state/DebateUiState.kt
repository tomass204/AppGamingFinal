package com.example.gaminghub.ui.viewmodel.state

import com.example.gaminghub.data.model.Publicacion

data class DebateUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val shouldRedirect: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val debates: List<Publicacion> = emptyList(),
    val favoritosMap: Map<Long, Long> = emptyMap(),
    val likesMap: Map<Long, Long> = emptyMap()
)
