package com.example.gaminghub.ui.screen

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.NavController
import com.example.gaminghub.data.model.Publicacion
import com.example.gaminghub.ui.theme.GamingHubTheme
import com.example.gaminghub.ui.viewmodel.NoticiaViewModel
import com.example.gaminghub.ui.viewmodel.state.NoticiaUiState
import io.mockk.every
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class ListadoNoticiasScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val navController: NavController = mockk(relaxed = true)
    private val noticiaViewModel: NoticiaViewModel = mockk(relaxed = true)

    @Test
    fun cuandoLaListaDeNoticiasEstaVacia_muestraElMensajeCorrecto() {
        // 1. Arrange: Estado vacío
        val emptyState = NoticiaUiState(isLoading = false, noticias = emptyList())
        every { noticiaViewModel.uiState } returns mutableStateOf(emptyState)

        // 2. Act: Renderizamos el screen
        composeTestRule.setContent {
            GamingHubTheme {
                ListadoNoticiasScreen(
                    navController = navController,
                    sessionManager = mockk(relaxed = true),
                    repository = mockk(relaxed = true),
                    reaccionRepository = mockk(relaxed = true),
                    favoritoRepository = mockk(relaxed = true),
                    imagenRepository = mockk(relaxed = true),
                    onCommentClick = {}
                )
            }
        }

        // 3. Assert
        composeTestRule.onNodeWithText("No hay noticias disponibles.").assertIsDisplayed()
    }

    @Test
    fun cuandoHayNoticias_seMuestraLaLista() {
        // 1. Arrange: Estado con datos
        val noticiaDePrueba = Publicacion(
            publicacionID = 1L,
            titulo = "Gran Lanzamiento Este Viernes",
            contenido = "...",
            tipo = "NOTICIA",
            autorID = 1L
        )
        val dataState = NoticiaUiState(isLoading = false, noticias = listOf(noticiaDePrueba))
        every { noticiaViewModel.uiState } returns mutableStateOf(dataState)

        // 2. Act
        composeTestRule.setContent {
            GamingHubTheme {
                ListadoNoticiasScreen(
                    navController = navController,
                    sessionManager = mockk(relaxed = true),
                    repository = mockk(relaxed = true),
                    reaccionRepository = mockk(relaxed = true),
                    favoritoRepository = mockk(relaxed = true),
                    imagenRepository = mockk(relaxed = true),
                    onCommentClick = {}
                )
            }
        }

        // 3. Assert
        composeTestRule.onNodeWithText("Gran Lanzamiento Este Viernes").assertIsDisplayed()
    }
}