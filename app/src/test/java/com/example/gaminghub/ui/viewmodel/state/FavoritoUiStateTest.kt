package com.example.gaminghub.ui.viewmodel.state

import com.example.gaminghub.data.model.Publicacion
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import org.junit.Test

class FavoritoUiStateTest {

    @Test
    fun `el estado inicial de FavoritoUiState debe tener valores por defecto`() {
        // Arrange & Act
        val initialState = FavoritoUiState()

        // Assert
        initialState.isLoading shouldBe false
        initialState.isSuccess shouldBe false
        initialState.errorMessage shouldBe null
        initialState.favoritos.shouldBeEmpty()
    }

    @Test
    fun `crear FavoritoUiState con datos debe asignar las propiedades correctamente`() {
        // Arrange
        val favoritosDePrueba = listOf(
            Publicacion(publicacionID = 1L, titulo = "Favorito 1", contenido = "...", tipo = "DEBATE", autorID = 1L)
        )
        val mensajeExito = "Cargado"

        // Act
        val dataState = FavoritoUiState(
            isSuccess = true,
            successMessage = mensajeExito,
            favoritos = favoritosDePrueba
        )

        // Assert
        dataState.isSuccess shouldBe true
        dataState.successMessage shouldBe mensajeExito
        dataState.favoritos shouldBe favoritosDePrueba
    }
}