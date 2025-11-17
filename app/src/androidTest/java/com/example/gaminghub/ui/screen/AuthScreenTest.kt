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
import io.mockk.every
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class AuthScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val navController: NavController = mockk(relaxed = true)
    private val authViewModel: AuthViewModel = mockk(relaxed = true)
    private val sessionManager: SessionManager = mockk(relaxed = true)

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
}