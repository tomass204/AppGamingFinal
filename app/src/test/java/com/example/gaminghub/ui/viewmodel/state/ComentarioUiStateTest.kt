package com.example.gaminghub.ui.viewmodel.state

import com.example.gaminghub.data.model.Comentario
import com.example.gaminghub.data.model.Publicacion
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import org.junit.Test

class ComentarioUiStateTest {

    @Test
    fun `el estado inicial de ComentarioUiState debe tener valores por defecto`() {
        // Arrange & Act
        val initialState = ComentarioUiState()

        // Assert
        initialState.isLoading shouldBe false
        initialState.isSuccess shouldBe false
        initialState.errorMessage shouldBe null
        initialState.comentarios.shouldBeEmpty()
        initialState.publicacion shouldBe null
    }

    @Test
    fun `crear ComentarioUiState con datos debe asignar las propiedades correctamente`() {
        // Arrange
        val publicacionDePrueba = Publicacion(publicacionID = 1L, titulo = "Publi", contenido = "...", tipo = "DEBATE", autorID = 1L)
        val comentariosDePrueba = listOf(
            Comentario(comentarioID = 10L, contenido = "Comentario 1", autorID = 2L, publicacionID = 1L)
        )

        // Act
        val dataState = ComentarioUiState(
            isSuccess = true,
            publicacion = publicacionDePrueba,
            comentarios = comentariosDePrueba
        )

        // Assert
        dataState.isSuccess shouldBe true
        dataState.publicacion shouldBe publicacionDePrueba
        dataState.comentarios shouldBe comentariosDePrueba
    }
}