package com.example.gaminghub.ui.screen

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.NavController
import com.example.gaminghub.ui.navigation.SessionManager
import com.example.gaminghub.ui.theme.GamingHubTheme
import com.example.gaminghub.ui.viewmodel.AuthViewModel
import com.example.gaminghub.ui.viewmodel.state.AuthUiState
import io.mockk.every // <<<--- EL IMPORT QUE FALTABA
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

/**
 * Tests instrumentados para la UI de AuthScreen.
 * Versión final, corregida y completa.
 */
class AuthScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val navController: NavController = mockk(relaxed = true)
    private val authViewModel: AuthViewModel = mockk(relaxed = true)
    private val sessionManager: SessionManager = mockk(relaxed = true)

    @Test
    fun formularioLogin_cuandoSeMuestra_inputsSonVisibles() {
        // Given: Un estado donde el formulario de login debe mostrarse
        val loginState = AuthUiState(showLoginForm = true)

        // --- CORRECCIÓN DEFINITIVA ---
        // Le decimos al mock que CUANDO se acceda a su propiedad `uiState`, DEVUELVA nuestro estado de prueba.
        every { authViewModel.uiState } returns mutableStateOf(loginState)

        // When: Renderizamos el Composable
        composeTestRule.setContent {
            GamingHubTheme {
                AuthScreen(
                    navController = navController,
                    authViewModel = authViewModel,
                    sessionManager = sessionManager
                )
            }
        }

        // Then: Verificamos que los textos están en pantalla
        composeTestRule.onNodeWithText("Email").assertIsDisplayed()
        composeTestRule.onNodeWithText("Contraseña").assertIsDisplayed()
    }

    @Test
    fun mensajeDeError_cuandoHayUnError_seMuestraEnPantalla() {
        // Given: Un estado que contiene un mensaje de error
        val errorMessage = "Usuario o contraseña incorrectos"
        val errorState = AuthUiState(
            showLoginForm = true,
            errorMessage = errorMessage
        )
        // --- CORRECCIÓN DEFINITIVA ---
        every { authViewModel.uiState } returns mutableStateOf(errorState)

        // When: Renderizamos el Composable
        composeTestRule.setContent {
            GamingHubTheme {
                AuthScreen(
                    navController = navController,
                    authViewModel = authViewModel,
                    sessionManager = sessionManager
                )
            }
        }

        // Then: Verificamos que el texto del error es visible
        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }
}