package com.example.gaminghub.ui.screen

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.gaminghub.ui.theme.GamingHubTheme
import com.example.gaminghub.ui.viewmodel.PublicacionViewModel
import com.example.gaminghub.ui.viewmodel.state.PublicacionUiState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

class CrearPublicacionScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val viewModel: PublicacionViewModel = mockk(relaxed = true)

    @Test
    fun alInicio_elBotonPublicarEstaDeshabilitado() {
        // 1. Arrange
        val initialState = PublicacionUiState()
        every { viewModel.uiState } returns mutableStateOf(initialState)

        // 2. Act
        composeTestRule.setContent {
            GamingHubTheme {
                CrearPublicacionScreen(
                    sessionManager = mockk(relaxed = true),
                    repository = mockk(relaxed = true),
                    onNavigateBack = {},
                    onNavigateToRoute = {}
                )
            }
        }

        // 3. Assert
        composeTestRule.onNodeWithText("Publicar").assertIsNotEnabled()
    }

    @Test
    fun cuandoSeRellenanLosCampos_elBotonPublicarSeHabilita() {
        // 1. Arrange
        val initialState = PublicacionUiState()
        every { viewModel.uiState } returns mutableStateOf(initialState)

        composeTestRule.setContent {
            GamingHubTheme {
                CrearPublicacionScreen(
                    sessionManager = mockk(relaxed = true),
                    repository = mockk(relaxed = true),
                    onNavigateBack = {},
                    onNavigateToRoute = {}
                )
            }
        }

        // 2. Act
        composeTestRule.onNodeWithText("Título").performTextInput("Mi Título de Prueba")
        composeTestRule.onNodeWithText("Contenido").performTextInput("Este es el contenido.")

        // 3. Assert
        composeTestRule.onNodeWithText("Publicar").assertIsEnabled()
    }

    @Test
    fun cuandoHaceClicEnPublicar_seLlamaAlViewModel() {
        // 1. Arrange
        val initialState = PublicacionUiState()
        every { viewModel.uiState } returns mutableStateOf(initialState)

        composeTestRule.setContent {
            GamingHubTheme {
                CrearPublicacionScreen(
                    sessionManager = mockk(relaxed = true),
                    repository = mockk(relaxed = true),
                    onNavigateBack = {},
                    onNavigateToRoute = {}
                )
            }
        }

        val titulo = "Título para el VM"
        val contenido = "Contenido para el VM"

        // 2. Act
        composeTestRule.onNodeWithText("Título").performTextInput(titulo)
        composeTestRule.onNodeWithText("Contenido").performTextInput(contenido)
        composeTestRule.onNodeWithText("Publicar").performClick()

        // 3. Assert
        verify(exactly = 1) {
            viewModel.crearPublicacion(
                context = any(),
                titulo = eq(titulo),
                contenido = eq(contenido),
                tipo = any(),
                imageUris = any()
            )
        }
    }
}