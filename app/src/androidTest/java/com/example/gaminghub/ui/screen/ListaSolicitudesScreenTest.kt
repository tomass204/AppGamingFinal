package com.example.gaminghub.ui.screen

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavController
import com.example.gaminghub.data.model.SolicitudModerador
import com.example.gaminghub.ui.theme.GamingHubTheme
import com.example.gaminghub.ui.viewmodel.SolicitudModeradorViewModel
import com.example.gaminghub.ui.viewmodel.state.SolicitudUiState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

class ListaSolicitudesScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val navController: NavController = mockk(relaxed = true)
    private val viewModel: SolicitudModeradorViewModel = mockk(relaxed = true)

    @Test
    fun cuandoLaListaDeSolicitudesEstaVacia_muestraElMensajeCorrecto() {
        // 1. Arrange
        val emptyState = SolicitudUiState(isLoading = false, solicitudes = emptyList())
        every { viewModel.uiState } returns mutableStateOf(emptyState)

        // 2. Act
        composeTestRule.setContent {
            GamingHubTheme {
                ListaSolicitudesScreen(
                    navController = navController,
                    sessionManager = mockk(relaxed = true),
                    solicitudModeradorRepository = mockk(relaxed = true)
                )
            }
        }

        // 3. Assert
        composeTestRule.onNodeWithText("No hay solicitudes pendientes.").assertIsDisplayed()
    }

    @Test
    fun cuandoHaySolicitudes_seMuestraLaLista() {
        // 1. Arrange
        val solicitudDePrueba = SolicitudModerador(
            solicitudId = 1L,
            nombre = "Usuario de Prueba",
            email = "test@test.com",
            motivo = "Quiero ser moderador"
        )
        val dataState = SolicitudUiState(isLoading = false, solicitudes = listOf(solicitudDePrueba))
        every { viewModel.uiState } returns mutableStateOf(dataState)

        // 2. Act
        composeTestRule.setContent {
            GamingHubTheme {
                ListaSolicitudesScreen(
                    navController = navController,
                    sessionManager = mockk(relaxed = true),
                    solicitudModeradorRepository = mockk(relaxed = true)
                )
            }
        }

        // 3. Assert
        composeTestRule.onNodeWithText("Usuario de Prueba").assertIsDisplayed()
        composeTestRule.onNodeWithText("Quiero ser moderador").assertIsDisplayed()
    }

    @Test
    fun cuandoHaceClicEnAceptar_seLlamaAProcessApproval() {
        // 1. Arrange
        val solicitudDePrueba = SolicitudModerador(
            solicitudId = 1L,
            nombre = "Usuario de Prueba",
            email = "test@test.com",
            motivo = "Motivo de prueba"
        )
        val dataState = SolicitudUiState(isLoading = false, solicitudes = listOf(solicitudDePrueba))
        every { viewModel.uiState } returns mutableStateOf(dataState)

        composeTestRule.setContent {
            GamingHubTheme {
                ListaSolicitudesScreen(
                    navController = navController,
                    sessionManager = mockk(relaxed = true),
                    solicitudModeradorRepository = mockk(relaxed = true)
                )
            }
        }

        // 2. Act
        composeTestRule.onNodeWithText("Aceptar").performClick()

        // 3. Assert
        verify(exactly = 1) { viewModel.processApproval(eq(solicitudDePrueba), any()) }
    }
}