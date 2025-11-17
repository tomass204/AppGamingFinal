package com.example.gaminghub.ui.screen

import ComentarioViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.gaminghub.data.model.Publicacion
import com.example.gaminghub.ui.theme.GamingHubTheme
import com.example.gaminghub.ui.viewmodel.state.ComentarioUiState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

class ComentarioPublicacionScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Mockeamos la dependencia principal: el ViewModel
    private val viewModel: ComentarioViewModel = mockk(relaxed = true)

    @Test
    fun alInicio_elBotonEnviarEstaDeshabilitado() {
        // 1. Arrange: Estado inicial, da igual si hay comentarios o no.
        val initialState = ComentarioUiState(
            publicacion = Publicacion(1L, "Título", "Contenido", "DEBATE", 1L)
        )
        every { viewModel.uiState } returns mutableStateOf(initialState)

        // 2. Act
        composeTestRule.setContent {
            GamingHubTheme {
                ComentarioPublicacionScreen(
                    publicacionId = 1L,
                    sessionManager = mockk(relaxed = true),
                    comentarioRepository = mockk(relaxed = true),
                    publicacionRepository = mockk(relaxed = true),
                    imagenRepository = mockk(relaxed = true),
                    onNavigateBack = {}
                )
            }
        }

        // 3. Assert: Buscamos el botón por su descripción de contenido
        composeTestRule.onNodeWithContentDescription("Enviar").assertIsNotEnabled()
    }

    @Test
    fun cuandoSeEscribeTexto_elBotonEnviarSeHabilita() {
        // 1. Arrange
        val initialState = ComentarioUiState(
            publicacion = Publicacion(1L, "Título", "Contenido", "DEBATE", 1L)
        )
        every { viewModel.uiState } returns mutableStateOf(initialState)

        composeTestRule.setContent {
            GamingHubTheme {
                ComentarioPublicacionScreen(
                    publicacionId = 1L,
                    sessionManager = mockk(relaxed = true),
                    comentarioRepository = mockk(relaxed = true),
                    publicacionRepository = mockk(relaxed = true),
                    imagenRepository = mockk(relaxed = true),
                    onNavigateBack = {}
                )
            }
        }

        // 2. Act: Simulamos que el usuario escribe en el campo de texto
        composeTestRule.onNodeWithText("Escribe un comentario...").performTextInput("Hola!")

        // 3. Assert
        composeTestRule.onNodeWithContentDescription("Enviar").assertIsEnabled()
    }

    @Test
    fun cuandoHaceClicEnEnviar_seLlamaAlViewModelYSeLimpiaElCampo() {
        // 1. Arrange
        val initialState = ComentarioUiState(
            publicacion = Publicacion(1L, "Título", "Contenido", "DEBATE", 1L)
        )
        every { viewModel.uiState } returns mutableStateOf(initialState)

        composeTestRule.setContent {
            GamingHubTheme {
                ComentarioPublicacionScreen(
                    publicacionId = 1L,
                    sessionManager = mockk(relaxed = true),
                    comentarioRepository = mockk(relaxed = true),
                    publicacionRepository = mockk(relaxed = true),
                    imagenRepository = mockk(relaxed = true),
                    onNavigateBack = {}
                )
            }
        }

        val comentarioTexto = "Este es mi comentario"

        // 2. Act
        composeTestRule.onNodeWithText("Escribe un comentario...").performTextInput(comentarioTexto)
        composeTestRule.onNodeWithContentDescription("Enviar").performClick()

        // 3. Assert
        // Verificamos que se llamó a la función del ViewModel con el texto correcto
        verify(exactly = 1) { viewModel.agregarComentario(eq(comentarioTexto)) }

        // Verificamos que el campo de texto se ha limpiado después de enviar
        composeTestRule.onNodeWithText(comentarioTexto).assertDoesNotExist()
        composeTestRule.onNodeWithText("Escribe un comentario...").assertIsDisplayed()
    }
}