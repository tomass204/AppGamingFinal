package com.example.gaminghub.ui.viewmodel.state

import com.example.gaminghub.data.model.Publicacion
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import org.junit.Test

class NoticiaUiStateTest {

    @Test
    fun `el estado inicial de NoticiaUiState debe tener valores por defecto`() {
        // Arrange & Act
        val initialState = NoticiaUiState()

        // Assert
        initialState.isLoading shouldBe false
        initialState.isSuccess shouldBe false
        initialState.errorMessage shouldBe null
        initialState.noticias.shouldBeEmpty()
    }

    @Test
    fun `crear NoticiaUiState con datos debe asignar las propiedades correctamente`() {
        // Arrange
        val noticiasDePrueba = listOf(
            Publicacion(publicacionID = 1L, titulo = "Noticia 1", contenido = "...", tipo = "NOTICIA", autorID = 1L)
        )
        val mensajeExito = "Noticias cargadas"

        // Act
        val dataState = NoticiaUiState(
            isSuccess = true,
            successMessage = mensajeExito,
            noticias = noticiasDePrueba
        )

        // Assert
        dataState.isSuccess shouldBe true
        dataState.successMessage shouldBe mensajeExito
        dataState.noticias shouldBe noticiasDePrueba
    }
}