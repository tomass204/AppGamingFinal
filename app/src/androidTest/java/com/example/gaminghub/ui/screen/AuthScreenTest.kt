package com.example.gaminghub.ui.screen

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.NavController
import com.example.gaminghub.ui.navigation.SessionManager
import com.example.gaminghub.ui.theme.GamingHubTheme
import com.example.gaminghub.ui.viewmodel.AuthViewModel
import com.example.gaminghub.ui.viewmodel.state.AuthUiState
// --- ¡¡CORREGIDO!! Imports de MockK que faltaban ---
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
// ---------------------------------------------------
import org.junit.Rule
import org.junit.Test

class AuthScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Mocks para las dependencias del Screen
    private val navController: NavController = mockk(relaxed = true)
    private val authViewModel: AuthViewModel = mockk(relaxed = true)
    private val sessionManager: SessionManager = mockk(relaxed = true)

    // --- Tests de Visibilidad (Los que ya tenías) ---

    @Test
    fun formularioLogin_cuandoSeMuestra_inputsSonVisibles() {
        val loginState = AuthUiState(showLoginForm = true)
        every { authViewModel.uiState } returns mutableStateOf(loginState)

        composeTestRule.setContent {
            GamingHubTheme {
                AuthScreen(
                    navController = navController,
                    authViewModel = authViewModel,
                    sessionManager = sessionManager
                )
            }
        }

        composeTestRule.onNodeWithText("Email").assertIsDisplayed()
        composeTestRule.onNodeWithText("Contraseña").assertIsDisplayed()
    }

    @Test
    fun mensajeDeError_cuandoHayUnError_seMuestraEnPantalla() {
        val errorMessage = "Usuario o contraseña incorrectos"
        val errorState = AuthUiState(
            showLoginForm = true,
            errorMessage = errorMessage
        )
        every { authViewModel.uiState } returns mutableStateOf(errorState)

        composeTestRule.setContent {
            GamingHubTheme {
                AuthScreen(
                    navController = navController,
                    authViewModel = authViewModel,
                    sessionManager = sessionManager
                )
            }
        }

        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }

    // --- Tests de Manipulación de Interfaz Gráfica (CORREGIDOS) ---

    @Test
    fun cuandoElUsuarioEscribe_elViewModelRecibeLosDatos() {
        // 1. Arrange
        val loginState = AuthUiState(showLoginForm = true)
        every { authViewModel.uiState } returns mutableStateOf(loginState)

        composeTestRule.setContent {
            GamingHubTheme {
                AuthScreen(
                    navController = navController,
                    authViewModel = authViewModel,
                    sessionManager = sessionManager
                )
            }
        }

        val emailInput = "test@email.com"
        val passwordInput = "password123"

        // 2. Act
        composeTestRule.onNodeWithText("Email").performTextInput(emailInput)
        composeTestRule.onNodeWithText("Contraseña").performTextInput(passwordInput)

        // 3. Assert
        verify {
            authViewModel.onEmailChange(emailInput)
            authViewModel.onPasswordChange(passwordInput)
        }
    }

    @Test
    fun cuandoHaceClicEnLogin_seLlamaAttemptLogin() {
        // 1. Arrange
        val loginState = AuthUiState(showLoginForm = true)
        every { authViewModel.uiState } returns mutableStateOf(loginState)

        composeTestRule.setContent {
            GamingHubTheme {
                AuthScreen(
                    navController = navController,
                    authViewModel = authViewModel,
                    sessionManager = sessionManager
                )
            }
        }

        // 2. Act
        composeTestRule.onNodeWithText("Iniciar Sesión").performClick()

        // 3. Assert
        verify(exactly = 1) { authViewModel.attemptLogin() }
    }
}