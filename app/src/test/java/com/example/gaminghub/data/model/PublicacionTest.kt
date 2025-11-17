package com.example.gaminghub.data.model

import io.kotest.matchers.shouldBe
import org.junit.Test

class PublicacionTest {

    @Test
    fun `crear una instancia de Publicacion debe asignar las propiedades correctamente`() {
        // Arrange
        val id = 1L
        val titulo = "Título de prueba"
        val contenido = "Contenido de prueba"
        val tipo = "DEBATE"
        val autorId = 10L

        // Act
        val publicacion = Publicacion(
            publicacionID = id,
            titulo = titulo,
            contenido = contenido,
            tipo = tipo,
            autorID = autorId
        )

        // Assert
        publicacion.publicacionID shouldBe id
        publicacion.titulo shouldBe titulo
        publicacion.contenido shouldBe contenido
        publicacion.tipo shouldBe tipo
        publicacion.autorID shouldBe autorId
    }
}