package com.example.gaminghub.data.model

import io.kotest.matchers.shouldBe
import org.junit.Test

class LoginResponseTest {

    @Test
    fun `crear una instancia de LoginResponse debe asignar las propiedades correctamente`() {
        // Arrange
        val id = 1L
        val nombre = "Test User"
        val email = "test@example.com"
        val token = "sample-token"
        val message = "Success"
        val rol = "ADMIN"

        // Act
        val response = LoginResponse(
            usuarioId = id,
            nombre = nombre,
            email = email,
            token = token,
            message = message,
            rol = rol
        )

        // Assert
        response.usuarioId shouldBe id
        response.nombre shouldBe nombre
        response.email shouldBe email
        response.token shouldBe token
        response.message shouldBe message
        response.rol shouldBe rol
    }
}