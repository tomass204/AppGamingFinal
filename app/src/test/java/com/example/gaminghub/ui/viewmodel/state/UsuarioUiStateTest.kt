package com.example.gaminghub.ui.viewmodel.state

import io.kotest.matchers.shouldBe
import org.junit.Test

class UsuarioUiStateTest {

    @Test
    fun `el estado inicial de UsuarioUiState debe tener valores por defecto`() {
        // Arrange & Act
        val initialState = UsuarioUiState()

        // Assert
        initialState.isLoading shouldBe false
        initialState.isSuccess shouldBe false
        initialState.errorMessage shouldBe null
        initialState.solicitudes shouldBe null
        initialState.isPasswordLengthValid shouldBe false
        initialState.hasPasswordUppercase shouldBe false
        initialState.hasPasswordSpecialChar shouldBe false
    }

    @Test
    fun `crear UsuarioUiState con datos debe asignar las propiedades correctamente`() {
        // Arrange
        val mensajeExito = "OK"

        // Act
        val dataState = UsuarioUiState(
            isSuccess = true,
            successMessage = mensajeExito,
            isPasswordLengthValid = true
        )

        // Assert
        dataState.isSuccess shouldBe true
        dataState.successMessage shouldBe mensajeExito
        dataState.isPasswordLengthValid shouldBe true
    }
}