package com.example.gaminghub.ui.screen

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.NavController
import com.example.gaminghub.data.model.Publicacion
import com.example.gaminghub.ui.theme.GamingHubTheme
import com.example.gaminghub.ui.viewmodel.FavoritoViewModel
import com.example.gaminghub.ui.viewmodel.state.FavoritoUiState
import io.mockk.every
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class ListadoFavoritosScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val navController: NavController = mockk(relaxed = true)
    private val favoritoViewModel: FavoritoViewModel = mockk(relaxed = true)

    @Test
    fun cuandoLaListaDeFavoritosEstaVacia_muestraElMensajeCorrecto() {
        // 1. Arrange: Estado vacío
        val emptyState = FavoritoUiState(isLoading = false, favoritos = emptyList())
        every { favoritoViewModel.uiState } returns mutableStateOf(emptyState)

        // 2. Act
        composeTestRule.setContent {
            GamingHubTheme {
                ListadoFavoritosScreen(
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
        composeTestRule.onNodeWithText("No hay favoritos disponibles.").assertIsDisplayed()
    }

    @Test
    fun cuandoHayFavoritos_seMuestraLaLista() {
        // 1. Arrange: Estado con datos
        val favoritoDePrueba = Publicacion(
            publicacionID = 1L,
            titulo = "Mi Publicación Favorita",
            contenido = "...",
            tipo = "DEBATE",
            autorID = 1L
        )
        val dataState = FavoritoUiState(isLoading = false, favoritos = listOf(favoritoDePrueba))
        every { favoritoViewModel.uiState } returns mutableStateOf(dataState)

        // 2. Act
        composeTestRule.setContent {
            GamingHubTheme {
                ListadoFavoritosScreen(
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
        composeTestRule.onNodeWithText("Mi Publicación Favorita").assertIsDisplayed()
    }
}