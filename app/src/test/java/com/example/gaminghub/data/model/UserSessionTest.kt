package com.example.gaminghub.data.model

import io.kotest.matchers.shouldBe
import org.junit.Test

class UserSessionTest {

    @Test
    fun `dado un rol CREADOR DE CONTENIDO, userRoleEnum debe devolver el enum correcto`() {
        // Arrange
        val session = UserSession(
            usuarioID = 1,
            nombre = "Test",
            email = "test@test.com",
            // ¡¡CORREGIDO!! Usamos la constante para evitar errores de tipeo.
            rol = UserRole.CREADOR_DE_CONTENIDO.name 
        )

        // Act
        val rolEnum = session.userRoleEnum

        // Assert
        rolEnum shouldBe UserRole.CREADOR_DE_CONTENIDO
    }

    @Test
    fun `dado un rol MODERADOR, userRoleEnum debe devolver el enum correcto`() {
        // Arrange
        val session = UserSession(
            usuarioID = 1,
            nombre = "Test",
            email = "test@test.com",
            // ¡¡CORREGIDO!!
            rol = UserRole.MODERADOR.name
        )

        // Act
        val rolEnum = session.userRoleEnum

        // Assert
        rolEnum shouldBe UserRole.MODERADOR
    }

    @Test
    fun `dado un rol PROPIETARIO, userRoleEnum debe devolver el enum correcto`() {
        // Arrange
        val session = UserSession(
            usuarioID = 1,
            nombre = "Test",
            email = "test@test.com",
            // ¡¡CORREGIDO!!
            rol = UserRole.PROPIETARIO.name
        )

        // Act
        val rolEnum = session.userRoleEnum

        // Assert
        rolEnum shouldBe UserRole.PROPIETARIO
    }

    @Test
    fun `dado un rol desconocido, userRoleEnum debe devolver USUARIO_BASICO`() {
        // Arrange
        val session = UserSession(
            usuarioID = 1,
            nombre = "Test",
            email = "test@test.com",
            rol = "UN_ROL_INEXISTENTE"
        )

        // Act
        val rolEnum = session.userRoleEnum

        // Assert
        rolEnum shouldBe UserRole.USUARIO_BASICO
    }

    @Test
    fun `dado un rol en minúsculas, userRoleEnum debe funcionar correctamente`() {
        // Arrange
        val session = UserSession(
            usuarioID = 1,
            nombre = "Test",
            email = "test@test.com",
            rol = "propietario" // Lo dejamos en minúsculas para probar .uppercase()
        )

        // Act
        val rolEnum = session.userRoleEnum

        // Assert
        rolEnum shouldBe UserRole.PROPIETARIO
    }
}