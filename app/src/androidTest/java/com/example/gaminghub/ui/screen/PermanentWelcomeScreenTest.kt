package com.example.gaminghub.ui.screen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.gaminghub.ui.navigation.SessionManager
import com.example.gaminghub.ui.theme.GamingHubTheme
import io.mockk.every
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class PermanentWelcomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Mock para la única dependencia del Screen
    private val sessionManager: SessionManager = mockk(relaxed = true)

    @Test
    fun laPantallaDeBienvenida_muestraElNombreDeUsuarioCorrecto() {
        // 1. Arrange: Simulamos que el SessionManager devuelve un nombre de usuario específico.
        val nombreDePrueba = "TestUser"
        every { sessionManager.getUserName() } returns nombreDePrueba

        // 2. Act: Renderizamos el Composable.
        composeTestRule.setContent {
            GamingHubTheme {
                PermanentWelcomeScreen(sessionManager = sessionManager)
            }
        }

        // 3. Assert: Verificamos que el texto completo de bienvenida es visible.
        composeTestRule.onNodeWithText("¡Bienvenido, $nombreDePrueba!").assertIsDisplayed()
        // También verificamos otra parte estática del texto para asegurar que todo se renderiza.
        composeTestRule.onNodeWithText("Espero que te diviertas en GamingHub.").assertIsDisplayed()
    }
}