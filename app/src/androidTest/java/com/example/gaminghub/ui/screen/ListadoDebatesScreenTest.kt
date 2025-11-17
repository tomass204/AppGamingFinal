package com.example.gaminghub.ui.screen

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.NavController
import com.example.gaminghub.data.model.Publicacion
import com.example.gaminghub.ui.theme.GamingHubTheme
import com.example.gaminghub.ui.viewmodel.DebateViewModel
import com.example.gaminghub.ui.viewmodel.state.DebateUiState
import io.mockk.every
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

/**
 * Tests instrumentados para la UI de ListadoDebatesScreen.
 * Verifica cómo reacciona la UI a diferentes estados del ViewModel.
 */
class ListadoDebatesScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Mockeamos las dependencias que el Screen necesita. El ViewModel es el más importante.
    private val navController: NavController = mockk(relaxed = true)
    private val debateViewModel: DebateViewModel = mockk(relaxed = true)

    @Test
    fun cuandoLaListaEstaVacia_muestraElMensajeCorrecto() {
        // 1. Arrange: Un estado donde no hay debates y no se está cargando.
        val emptyState = DebateUiState(isLoading = false, debates = emptyList())
        every { debateViewModel.uiState } returns mutableStateOf(emptyState)

        // 2. Act: Renderizamos el Composable con el estado vacío.
        composeTestRule.setContent {
            GamingHubTheme {
                // Inyectamos el ViewModel mockeado.
                // Como no usaremos el PublicacionCard, los repositorios pueden ser mocks vacíos.
                ListadoDebatesScreen(
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

        // 3. Assert: Verificamos que el texto "No hay debates disponibles." es visible.
        composeTestRule.onNodeWithText("No hay debates disponibles.").assertIsDisplayed()
    }

    @Test
    fun cuandoHayDebates_seMuestraLaLista() {
        // 1. Arrange: Un estado con una lista que contiene un debate.
        val debateDePrueba = Publicacion(
            publicacionID = 1L,
            titulo = "Este es un debate de prueba",
            contenido = "Contenido del debate...",
            tipo = "DEBATE",
            autorID = 1L
        )
        val dataState = DebateUiState(isLoading = false, debates = listOf(debateDePrueba))
        every { debateViewModel.uiState } returns mutableStateOf(dataState)

        // 2. Act: Renderizamos el Composable con el estado de datos.
        composeTestRule.setContent {
            GamingHubTheme {
                ListadoDebatesScreen(
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

        // 3. Assert: Verificamos que el título del debate es visible en la pantalla.
        composeTestRule.onNodeWithText("Este es un debate de prueba").assertIsDisplayed()
    }
}