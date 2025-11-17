package com.example.gaminghub.ui.navigation

import android.content.Context
import android.content.SharedPreferences
import com.example.gaminghub.data.model.UserRole
import com.example.gaminghub.data.model.UserSession
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Test

class SessionManagerTest {

    private lateinit var mockContext: Context
    private lateinit var mockPrefs: SharedPreferences
    private lateinit var mockEditor: SharedPreferences.Editor
    private lateinit var sessionManager: SessionManager

    @Before
    fun setUp() {
        mockContext = mockk(relaxed = true)
        mockPrefs = mockk(relaxed = true)
        mockEditor = mockk(relaxed = true)

        every { mockContext.getSharedPreferences(any(), any()) } returns mockPrefs
        every { mockPrefs.edit() } returns mockEditor

        // --- ¡¡LA LÍNEA CLAVE QUE FALTABA!! ---
        // Le decimos al mock que cuando se llame a .clear(), se devuelva a sí mismo para permitir el encadenamiento.
        every { mockEditor.clear() } returns mockEditor
        // -----------------------------------------
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `saveUserSession debe guardar los datos del usuario en SharedPreferences`() {
        val userSession = UserSession(
            usuarioID = 123L,
            nombre = "Test User",
            email = "test@example.com",
            rol = UserRole.CREADOR_DE_CONTENIDO.name
        )

        sessionManager.saveUserSession(userSession)

        verify {
            mockEditor.putLong("usuarioID", 123L)
            mockEditor.putString("nombre", "Test User")
            mockEditor.putString("email", "test@example.com")
            mockEditor.putString("rol", UserRole.CREADOR_DE_CONTENIDO.name)
            mockEditor.apply()
        }
    }
    
    @Test
    fun `getUserSession cuando hay datos guardados debe devolver el objeto UserSession`() {
        every { mockPrefs.getLong("usuarioID", -1) } returns 123L
        every { mockPrefs.getString("nombre", any()) } returns "Test User"
        every { mockPrefs.getString("email", any()) } returns "test@example.com"
        every { mockPrefs.getString("rol", any()) } returns UserRole.MODERADOR.name

        val userSession = sessionManager.getUserSession()

        userSession shouldNotBe null
        userSession?.usuarioID shouldBe 123L
        userSession?.nombre shouldBe "Test User"
        userSession?.rol shouldBe UserRole.MODERADOR.name
    }

    @Test
    fun `getUserSession cuando no hay datos guardados debe devolver null`() {
        every { mockPrefs.getLong("usuarioID", -1) } returns -1L
        val userSession = sessionManager.getUserSession()
        userSession shouldBe null
    }


    @Test
    fun `clearSession debe limpiar los datos de SharedPreferences`() {
        // Act
        sessionManager.clearSession()

        // Assert
        // Ahora sí, esta verificación funcionará.
        verify(exactly = 1) {
            mockEditor.clear()
            mockEditor.apply()
        }
    }
}