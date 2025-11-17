package com.example.gaminghub.ui.screen

import androidx.compose.ui.test.assertIsDisplayed
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

class RecoveryPasswordScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Mocks para las lambdas que recibe el Screen
    private val onEmailChange: (String) -> Unit = mockk(relaxed = true)
    private val onRecoveryClick: () -> Unit = mockk(relaxed = true)
    private val onSwitchToLogin: () -> Unit = mockk(relaxed = true)

    @Test
    fun cuandoElUsuarioEscribeEnEmail_seLlamaAOnEmailChange() {
        // 1. Arrange
        composeTestRule.setContent {
            GamingHubTheme {
                RecoveryPasswordScreen(
                    authUiState = AuthUiState(),
                    onEmailChange = onEmailChange,
                    onRecoveryClick = onRecoveryClick,
                    onSwitchToLogin = onSwitchToLogin
                )
            }
        }

        val emailInput = "test@recovery.com"

        // 2. Act
        composeTestRule.onNodeWithText("Email").performTextInput(emailInput)

        // 3. Assert
        verify(exactly = 1) { onEmailChange(eq(emailInput)) }
    }

    @Test
    fun cuandoHaceClicEnRecuperar_seLlamaAOnRecoveryClick() {
        // 1. Arrange
        composeTestRule.setContent {
            GamingHubTheme {
                RecoveryPasswordScreen(
                    authUiState = AuthUiState(),
                    onEmailChange = onEmailChange,
                    onRecoveryClick = onRecoveryClick,
                    onSwitchToLogin = onSwitchToLogin
                )
            }
        }

        // 2. Act
        composeTestRule.onNodeWithText("Recuperar contraseña").performClick()

        // 3. Assert
        verify(exactly = 1) { onRecoveryClick() }
    }
}