package com.example.gaminghub.data.model

import io.kotest.matchers.shouldBe
import org.junit.Test

class ComentarioTest {

    @Test
    fun `crear una instancia de Comentario debe asignar las propiedades correctamente`() {
        // Arrange
        val id = 1L
        val contenido = "Este es un comentario"
        val autorId = 10L
        val publicacionId = 100L
        val activo = true
        val fecha = "2024-01-01"
        val likes = 5L
        val autorNombre = "Test User"

        // Act
        val comentario = Comentario(
            comentarioID = id,
            contenido = contenido,
            autorID = autorId,
            publicacionID = publicacionId,
            activo = activo,
            fechaCreacion = fecha,
            likesCount = likes,
            autorNombre = autorNombre
        )

        // Assert
        comentario.comentarioID shouldBe id
        comentario.contenido shouldBe contenido
        comentario.autorID shouldBe autorId
        comentario.publicacionID shouldBe publicacionId
        comentario.activo shouldBe activo
        comentario.fechaCreacion shouldBe fecha
        comentario.likesCount shouldBe likes
        comentario.autorNombre shouldBe autorNombre
    }
}