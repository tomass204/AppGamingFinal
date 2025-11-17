package com.example.gaminghub.ui.screen

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.example.gaminghub.ui.navigation.Routes
import com.example.gaminghub.ui.theme.GamingHubTheme
import com.example.gaminghub.ui.viewmodel.AuthViewModel
import com.example.gaminghub.ui.viewmodel.state.AuthUiState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

class RegistrationSuccessScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val navController: NavController = mockk(relaxed = true)
    private val authViewModel: AuthViewModel = mockk(relaxed = true)

    @Test
    fun conMensajePorDefecto_seMuestraCorrectamente() {
        val initialState = AuthUiState(registrationResultMessage = null)
        every { authViewModel.uiState } returns mutableStateOf(initialState)

        composeTestRule.setContent {
            GamingHubTheme {
                RegistrationSuccessScreen(
                    navController = navController,
                    authViewModel = authViewModel
                )
            }
        }

        composeTestRule.onNodeWithText("¡Registro exitoso!").assertIsDisplayed()
    }

    @Test
    fun conMensajeDelViewModel_seMuestraCorrectamente() {
        val successMessage = "¡Tu cuenta ha sido creada!"
        val successState = AuthUiState(registrationResultMessage = successMessage)
        every { authViewModel.uiState } returns mutableStateOf(successState)

        composeTestRule.setContent {
            GamingHubTheme {
                RegistrationSuccessScreen(
                    navController = navController,
                    authViewModel = authViewModel
                )
            }
        }

        composeTestRule.onNodeWithText(successMessage).assertIsDisplayed()
    }

    @Test
    fun despuesDeUnRetraso_seNavegaALaPantallaDeAuth() {
        val initialState = AuthUiState()
        every { authViewModel.uiState } returns mutableStateOf(initialState)

        composeTestRule.setContent {
            GamingHubTheme {
                RegistrationSuccessScreen(
                    navController = navController,
                    authViewModel = authViewModel
                )
            }
        }

        // Avanzamos el reloj virtual más allá del delay de 3.5s
        composeTestRule.mainClock.advanceTimeBy(4000L)

        // ¡¡CORREGIDO!! Usamos argumentos con nombre para una verificación robusta.
        verify(exactly = 1) {
            navController.navigate(
                route = eq(Routes.AUTH_SCREEN),
                builder = any()
            )
        }
    }
}