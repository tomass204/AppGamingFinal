package com.example.gaminghub.data.model

import io.kotest.matchers.shouldBe
import org.junit.Test

class UsuarioTest {

    @Test
    fun `dado un rol CREADOR DE CONTENIDO, userRoleEnum debe devolver el enum correcto`() {
        // Arrange
        val usuario = Usuario(
            usuarioID = 1,
            nombre = "Test",
            email = "test@test.com",
            contrasena = "pass",
            rol = "CREADOR DE CONTENIDO"
        )

        // Act
        val rolEnum = usuario.userRoleEnum

        // Assert
        rolEnum shouldBe UserRole.CREADOR_DE_CONTENIDO
    }

    @Test
    fun `dado un rol MODERADOR, userRoleEnum debe devolver el enum correcto`() {
        // Arrange
        val usuario = Usuario(
            usuarioID = 1,
            nombre = "Test",
            email = "test@test.com",
            contrasena = "pass",
            rol = "MODERADOR"
        )

        // Act
        val rolEnum = usuario.userRoleEnum

        // Assert
        rolEnum shouldBe UserRole.MODERADOR
    }

    @Test
    fun `dado un rol PROPIETARIO, userRoleEnum debe devolver el enum correcto`() {
        // Arrange
        val usuario = Usuario(
            usuarioID = 1,
            nombre = "Test",
            email = "test@test.com",
            contrasena = "pass",
            rol = "PROPIETARIO"
        )

        // Act
        val rolEnum = usuario.userRoleEnum

        // Assert
        rolEnum shouldBe UserRole.PROPIETARIO
    }

    @Test
    fun `dado un rol desconocido, userRoleEnum debe devolver USUARIO_BASICO`() {
        // Arrange
        val usuario = Usuario(
            usuarioID = 1,
            nombre = "Test",
            email = "test@test.com",
            contrasena = "pass",
            rol = "UN_ROL_QUE_NO_EXISTE"
        )

        // Act
        val rolEnum = usuario.userRoleEnum

        // Assert
        rolEnum shouldBe UserRole.USUARIO_BASICO
    }

    @Test
    fun `dado un rol en minúsculas, userRoleEnum debe funcionar correctamente`() {
        // Arrange
        val usuario = Usuario(
            usuarioID = 1,
            nombre = "Test",
            email = "test@test.com",
            contrasena = "pass",
            rol = "moderador" // En minúsculas
        )

        // Act
        val rolEnum = usuario.userRoleEnum

        // Assert
        rolEnum shouldBe UserRole.MODERADOR
    }
}