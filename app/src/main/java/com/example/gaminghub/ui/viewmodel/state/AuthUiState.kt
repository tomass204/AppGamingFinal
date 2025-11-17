package com.example.gaminghub.ui.viewmodel.state

import com.example.gaminghub.data.model.UserRole

data class AuthUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val shouldRedirect: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,

    val emailInput: String = "",
    val passwordInput: String = "",
    val usernameInput: String = "",
    val confirmPasswordInput: String = "",
    val selectedRole: UserRole = UserRole.USUARIO_BASICO,
    val showRegisterForm: Boolean = false,
    val showLoginForm: Boolean = false,
    val showRecoveryPasswordForm: Boolean = false,
    val registrationResultMessage: String? = null,
    val loggedInUserRole: UserRole? = null,
    val loggedInUsername: String? = null,
    val loggedInEmail: String? = null,
    val loggedInUserId: Long? = null,
    val banMessage: String? = null,
    val navigateTo: String? = null,

    val recoverResultMessage: String? = null,

    val isPasswordLengthValid: Boolean = false,
    val hasPasswordUppercase: Boolean = false,
    val hasPasswordSpecialChar: Boolean = false
)
