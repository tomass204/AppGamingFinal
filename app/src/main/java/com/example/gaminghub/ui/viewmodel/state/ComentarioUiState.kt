package com.example.gaminghub.ui.viewmodel.state

import com.example.gaminghub.data.model.Comentario
import com.example.gaminghub.data.model.Publicacion

data class ComentarioUiState (

    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val comentarios: List<Comentario> = emptyList(),
    val publicacion: Publicacion? =null

)