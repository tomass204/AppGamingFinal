package com.example.gaminghub.ui.viewmodel.state

import io.kotest.matchers.shouldBe
import org.junit.Test

class PublicacionUiStateTest {

    @Test
    fun `el estado inicial de PublicacionUiState debe tener valores por defecto`() {
        // Arrange & Act
        val initialState = PublicacionUiState()

        // Assert
        initialState.isLoading shouldBe false
        initialState.isSuccess shouldBe false
        initialState.shouldRedirect shouldBe false
        initialState.redirectRoute shouldBe null
        initialState.errorMessage shouldBe null
        initialState.successMessage shouldBe null
    }

    @Test
    fun `crear PublicacionUiState con datos debe asignar las propiedades correctamente`() {
        // Arrange
        val mensajeExito = "Publicado"
        val rutaRedireccion = "home"

        // Act
        val dataState = PublicacionUiState(
            isLoading = false,
            isSuccess = true,
            shouldRedirect = true,
            redirectRoute = rutaRedireccion,
            successMessage = mensajeExito
        )

        // Assert
        dataState.isLoading shouldBe false
        dataState.isSuccess shouldBe true
        dataState.shouldRedirect shouldBe true
        dataState.redirectRoute shouldBe rutaRedireccion
        dataState.successMessage shouldBe mensajeExito
    }
}