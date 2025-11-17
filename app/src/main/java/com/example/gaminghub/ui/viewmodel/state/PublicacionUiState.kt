package com.example.gaminghub.ui.viewmodel.state

data class PublicacionUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val shouldRedirect: Boolean = false,
    val redirectRoute: String? = null,
    val errorMessage: String? = null,
    val successMessage: String? = null
)
