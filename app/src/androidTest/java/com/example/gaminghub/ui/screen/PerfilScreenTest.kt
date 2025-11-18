package com.example.gaminghub.ui.screen

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.gaminghub.data.model.UserRole
import com.example.gaminghub.data.network.repository.SolicitudModeradorRepository
import com.example.gaminghub.data.network.repository.UsuarioRepository
import com.example.gaminghub.ui.navigation.SessionManager
import com.example.gaminghub.ui.theme.GamingHubTheme
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

class PerfilScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Mocks para las dependencias del Screen
    private val sessionManager: SessionManager = mockk(relaxed = true)
    private val solicitudModeradorRepository: SolicitudModeradorRepository = mockk(relaxed = true)
    private val usuarioRepository: UsuarioRepository = mockk(relaxed = true)
    private val onLogout: () -> Unit = mockk(relaxed = true)

    @Test
    fun paraUsuarioBasico_elBotonDeSolicitudEsVisible() {
        // 1. Arrange: Simulamos que el SessionManager devuelve el rol de usuario básico
        every { sessionManager.getUserRole() } returns UserRole.USUARIO_BASICO.name
        every { sessionManager.getUserName() } returns "Test User"
        every { sessionManager.getUserEmail() } returns "test@test.com"

        // 2. Act
        composeTestRule.setContent {
            GamingHubTheme {
                PerfilScreen(
                    sessionManager = sessionManager,
                    solicitudModeradorRepository = solicitudModeradorRepository,
                    usuarioRepository = usuarioRepository,
                    onLogout = onLogout
                )
            }
        }

        // 3. Assert
        composeTestRule.onNodeWithText("Solicitar ser Moderador").assertIsDisplayed()
    }

    @Test
    fun paraModerador_elBotonDeSolicitudNoEsVisible() {
        // 1. Arrange: Simulamos que el SessionManager devuelve el rol de moderador
        every { sessionManager.getUserRole() } returns UserRole.MODERADOR.name
        every { sessionManager.getUserName() } returns "Admin User"
        every { sessionManager.getUserEmail() } returns "admin@test.com"

        // 2. Act
        composeTestRule.setContent {
            GamingHubTheme {
                PerfilScreen(
                    sessionManager = sessionManager,
                    solicitudModeradorRepository = solicitudModeradorRepository,
                    usuarioRepository = usuarioRepository,
                    onLogout = onLogout
                )
            }
        }

        // 3. Assert
        composeTestRule.onNodeWithText("Solicitar ser Moderador").assertDoesNotExist()
    }

    @Test
    fun cuandoHaceClicEnCerrarSesion_seLlamaAOnLogout() {
        // 1. Arrange
        every { sessionManager.getUserRole() } returns UserRole.USUARIO_BASICO.name

        // 2. Act
        composeTestRule.setContent {
            GamingHubTheme {
                PerfilScreen(
                    sessionManager = sessionManager,
                    solicitudModeradorRepository = solicitudModeradorRepository,
                    usuarioRepository = usuarioRepository,
                    onLogout = onLogout
                )
            }
        }
        composeTestRule.onNodeWithText("Cerrar Sesión").performClick()

        // 3. Assert
        verify(exactly = 1) { onLogout() }
    }

    // --- ¡¡NUEVO TEST DE MANIPULACIÓN AÑADIDO!! ---
    @Test
    fun cuandoHaceClicEnCambiarContraseña_seMuestraElDialogo() {
        // 1. Arrange
        every { sessionManager.getUserRole() } returns UserRole.USUARIO_BASICO.name
        every { sessionManager.getUserName() } returns "Test User"

        // 2. Act
        composeTestRule.setContent {
            GamingHubTheme {
                PerfilScreen(
                    sessionManager = sessionManager,
                    solicitudModeradorRepository = mockk(relaxed = true),
                    usuarioRepository = mockk(relaxed = true),
                    onLogout = mockk(relaxed = true)
                )
            }
        }
        // Simulamos el clic en el botón
        composeTestRule.onNodeWithText("Cambiar Contraseña").performClick()

        // 3. Assert
        // Verificamos que un texto que solo existe en el diálogo ahora es visible
        composeTestRule.onNodeWithText("Nueva Contraseña").assertIsDisplayed()
    }
}