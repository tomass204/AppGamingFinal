package com.example.gaminghub.ui.viewmodel.state

import com.example.gaminghub.data.model.SolicitudModerador
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import org.junit.Test

class SolicitudUiStateTest {

    @Test
    fun `el estado inicial de SolicitudUiState debe tener valores por defecto`() {
        // Arrange & Act
        val initialState = SolicitudUiState()

        // Assert
        initialState.isLoading shouldBe false
        initialState.isSuccess shouldBe false
        initialState.errorMessage shouldBe null
        initialState.solicitudes.shouldBeEmpty()
    }

    @Test
    fun `crear SolicitudUiState con datos debe asignar las propiedades correctamente`() {
        // Arrange
        val solicitudesDePrueba = listOf(
            SolicitudModerador(solicitudId = 1L, nombre = "User A", email = "a@a.com", motivo = "Motivo")
        )
        val mensajeExito = "Cargadas"

        // Act
        val dataState = SolicitudUiState(
            isSuccess = true,
            successMessage = mensajeExito,
            solicitudes = solicitudesDePrueba
        )

        // Assert
        dataState.isSuccess shouldBe true
        dataState.successMessage shouldBe mensajeExito
        dataState.solicitudes shouldBe solicitudesDePrueba
    }
}