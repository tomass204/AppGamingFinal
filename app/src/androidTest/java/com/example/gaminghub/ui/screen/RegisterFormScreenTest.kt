package com.example.gaminghub.ui.screen

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.gaminghub.ui.theme.GamingHubTheme
import com.example.gaminghub.ui.viewmodel.state.AuthUiState
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

class RegisterFormScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Mocks para todas las lambdas que el Screen recibe
    private val onUsernameChange: (String) -> Unit = mockk(relaxed = true)
    private val onEmailChange: (String) -> Unit = mockk(relaxed = true)
    private val onPasswordChange: (String) -> Unit = mockk(relaxed = true)
    private val onConfirmPasswordChange: (String) -> Unit = mockk(relaxed = true)
    private val onRegisterClick: () -> Unit = mockk(relaxed = true)
    private val onSwitchToLogin: () -> Unit = mockk(relaxed = true)

    @Test
    fun cuandoElUsuarioEscribeEnLosCampos_seLlamanLasFuncionesCorrectas() {
        // 1. Arrange
        composeTestRule.setContent {
            GamingHubTheme {
                RegisterFormScreen(
                    authUiState = AuthUiState(),
                    onUsernameChange = onUsernameChange,
                    onEmailChange = onEmailChange,
                    onPasswordChange = onPasswordChange,
                    onConfirmPasswordChange = onConfirmPasswordChange,
                    onRoleSelected = { }, // No probamos esta interacción en este test
                    onRegisterClick = onRegisterClick,
                    onSwitchToLogin = onSwitchToLogin
                )
            }
        }

        val usernameInput = "NuevoUsuario"
        val emailInput = "nuevo@test.com"
        val passwordInput = "Password123!"

        // 2. Act
        composeTestRule.onNodeWithText("Nombre de Usuario").performTextInput(usernameInput)
        composeTestRule.onNodeWithText("Email").performTextInput(emailInput)
        composeTestRule.onNodeWithText("Contraseña").performTextInput(passwordInput)
        composeTestRule.onNodeWithText("Confirmar Contraseña").performTextInput(passwordInput)


        // 3. Assert
        verify {
            onUsernameChange(eq(usernameInput))
            onEmailChange(eq(emailInput))
            onPasswordChange(eq(passwordInput))
            onConfirmPasswordChange(eq(passwordInput))
        }
    }

    @Test
    fun cuandoHaceClicEnCrearCuenta_seLlamaAOnRegisterClick() {
        // 1. Arrange
        composeTestRule.setContent {
            GamingHubTheme {
                RegisterFormScreen(
                    authUiState = AuthUiState(),
                    onUsernameChange = { },
                    onEmailChange = { },
                    onPasswordChange = { },
                    onConfirmPasswordChange = { },
                    onRoleSelected = { },
                    onRegisterClick = onRegisterClick,
                    onSwitchToLogin = onSwitchToLogin
                )
            }
        }

        // 2. Act
        composeTestRule.onNodeWithText("Crear Cuenta").performClick()

        // 3. Assert
        verify(exactly = 1) { onRegisterClick() }
    }
}