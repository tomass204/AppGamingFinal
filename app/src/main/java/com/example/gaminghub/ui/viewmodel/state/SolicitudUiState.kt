package com.example.gaminghub.ui.viewmodel.state

import com.example.gaminghub.data.model.SolicitudModerador

data class SolicitudUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val shouldRedirect: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val solicitudes: List<SolicitudModerador> = emptyList()
)

