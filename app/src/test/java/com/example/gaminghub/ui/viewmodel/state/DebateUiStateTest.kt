package com.example.gaminghub.ui.viewmodel.state

import com.example.gaminghub.data.model.Publicacion
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import org.junit.Test

class DebateUiStateTest {

    @Test
    fun `el estado inicial de DebateUiState debe tener valores por defecto`() {
        // Arrange & Act
        val initialState = DebateUiState()

        // Assert
        initialState.isLoading shouldBe false
        initialState.isSuccess shouldBe false
        initialState.errorMessage shouldBe null
        initialState.debates.shouldBeEmpty()
    }

    @Test
    fun `crear DebateUiState con datos debe asignar las propiedades correctamente`() {
        // Arrange
        val debatesDePrueba = listOf(
            Publicacion(publicacionID = 1L, titulo = "Debate 1", contenido = "...", tipo = "DEBATE", autorID = 1L)
        )
        val mensajeExito = "Cargado"

        // Act
        val dataState = DebateUiState(
            isSuccess = true,
            successMessage = mensajeExito,
            debates = debatesDePrueba
        )

        // Assert
        dataState.isSuccess shouldBe true
        dataState.successMessage shouldBe mensajeExito
        dataState.debates shouldBe debatesDePrueba
    }
}