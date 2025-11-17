package com.example.gaminghub.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gaminghub.data.model.UserRole
import com.example.gaminghub.data.model.UserSession
import com.example.gaminghub.data.model.Usuario
import com.example.gaminghub.data.network.repository.UsuarioRepository
import com.example.gaminghub.ui.navigation.SessionManager
import com.example.gaminghub.ui.viewmodel.state.AuthUiState
import com.example.gaminghub.utils.Result
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: UsuarioRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = mutableStateOf(AuthUiState())
    val uiState: State<AuthUiState> = _uiState

    fun onUsernameChange(value: String) {
        _uiState.value = _uiState.value.copy(usernameInput = value)
    }

    fun onEmailChange(value: String) {
        _uiState.value = _uiState.value.copy(emailInput = value)
    }

    fun onPasswordChange(password: String) {
        val isLengthValid = password.length >= 5
        val hasUppercase = password.any { it.isUpperCase() }
        val hasSpecialChar = password.any { !it.isLetterOrDigit() }

        _uiState.value = _uiState.value.copy(
            passwordInput = password,
            isPasswordLengthValid = isLengthValid,
            hasPasswordUppercase = hasUppercase,
            hasPasswordSpecialChar = hasSpecialChar,
            errorMessage = null
        )
    }

    fun onConfirmPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(confirmPasswordInput = value)
    }

    fun onRoleSelected(role: UserRole) {
        _uiState.value = _uiState.value.copy(selectedRole = role)
    }

    fun attemptLogin() {
        val email = _uiState.value.emailInput
        val password = _uiState.value.passwordInput

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            when (val result = repository.login(email, password)) {
                is Result.Success -> {
                    val loginResponse = result.data
                    val userSession = UserSession(
                        usuarioID = loginResponse.usuarioId,
                        nombre = loginResponse.nombre ?: "",
                        email = loginResponse.email ?: "",
                        rol = loginResponse.rol ?: "USUARIO_BASICO"
                    )
                    sessionManager.saveUserSession(userSession)

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        successMessage = "Inicio de sesión exitoso",
                        errorMessage = null,
                        loggedInUserId = userSession.usuarioID,
                        loggedInUsername = userSession.nombre,
                        loggedInEmail = userSession.email,
                        loggedInUserRole = userSession.userRoleEnum,
                        shouldRedirect = true
                    )
                }
                is Result.Error -> _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = result.message
                )
                Result.Loading -> _uiState.value = _uiState.value.copy(isLoading = true)
            }
        }
    }

    fun attemptRegistration() {
        val ui = _uiState.value
        if (ui.usernameInput.isBlank() || ui.emailInput.isBlank() || ui.passwordInput.isBlank()) {
            _uiState.value = ui.copy(errorMessage = "Todos los campos son obligatorios.")
            return
        }

        if (ui.passwordInput != ui.confirmPasswordInput) {
            _uiState.value = ui.copy(errorMessage = "Las contraseñas no coinciden.")
            return
        }

        if (!ui.isPasswordLengthValid || !ui.hasPasswordUppercase || !ui.hasPasswordSpecialChar) {
            _uiState.value = ui.copy(errorMessage = "La contraseña no cumple con todos los requisitos.")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val usuario = Usuario(
                nombre = ui.usernameInput,
                email = ui.emailInput,
                contrasena = ui.passwordInput,
                rol = ui.selectedRole.name ?: UserRole.USUARIO_BASICO.name
            )

            when (val result = repository.crear(usuario)) {
                is Result.Success -> _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    registrationResultMessage = "Registro exitoso. Bienvenido, ${ui.usernameInput}!",
                    showRegisterForm = false,
                    shouldRedirect = true
                )
                is Result.Error -> _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = result.message
                )
                Result.Loading -> _uiState.value = _uiState.value.copy(isLoading = true)
            }
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null,
            successMessage = null,
            registrationResultMessage = null,
            recoverResultMessage = null,
            shouldRedirect = false
        )
    }

    fun logout() {
        sessionManager.clearSession()
        _uiState.value = AuthUiState()
    }

    fun changePassword(
        newPassword: String,
        confirmPassword: String,
        sessionManager: SessionManager
    ) {
        viewModelScope.launch {

            if (newPassword != confirmPassword) {
                _uiState.value = _uiState.value.copy(errorMessage = "Las contraseñas no coinciden.")
                return@launch

            }


            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            val userId = sessionManager.getUserId()
            if (userId == null) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Usuario no autenticado."
                )
                return@launch
            }

            when (val result = repository.cambiarContrasena(newPassword, userId)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        successMessage = "Contraseña actualizada correctamente."
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
                Result.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
            }
        }
    }


    fun toggleRecoveryPassword() {
        _uiState.value = _uiState.value.copy(
            showLoginForm = false,
            showRegisterForm = false,
            showRecoveryPasswordForm = true,
            errorMessage = null,
            successMessage = null,
            passwordInput = "",
            isPasswordLengthValid = false,
            hasPasswordUppercase = false,
            hasPasswordSpecialChar = false
        )
    }

    fun toggleFormRegister() {
        _uiState.value = _uiState.value.copy(
            showLoginForm = false,
            showRegisterForm = true,
            showRecoveryPasswordForm = false,
            errorMessage = null,
            successMessage = null,
            passwordInput = "",
            isPasswordLengthValid = false,
            hasPasswordUppercase = false,
            hasPasswordSpecialChar = false
        )
    }

    fun toggleLogin() {
        _uiState.value = _uiState.value.copy(
            showLoginForm = true,
            showRegisterForm = false,
            showRecoveryPasswordForm = false,
            errorMessage = null,
            successMessage = null,
            passwordInput = "",
            isPasswordLengthValid = false,
            hasPasswordUppercase = false,
            hasPasswordSpecialChar = false
        )
    }

    fun attemptRecovery() {
        val email = _uiState.value.emailInput

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            when (val result = repository.recuperarContrasena(email)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = null,
                        shouldRedirect = true,
                        recoverResultMessage = "!Correo enviado correctamente.!",
                        showRecoveryPasswordForm = false
                    )
                }
                is Result.Error -> _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = result.message
                )
                Result.Loading -> _uiState.value = _uiState.value.copy(isLoading = true)
            }
        }
    }



}