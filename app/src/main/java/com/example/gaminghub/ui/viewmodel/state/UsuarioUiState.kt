package com.example.gaminghub.ui.viewmodel.state

import com.example.gaminghub.data.model.Usuario

data class UsuarioUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val shouldRedirect: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val solicitudes: Usuario? = null,
    val isPasswordLengthValid: Boolean = false,
    val hasPasswordUppercase: Boolean = false,
    val hasPasswordSpecialChar: Boolean = false
)