package com.example.gaminghub.data.model

import io.kotest.matchers.shouldBe
import org.junit.Test

class ImagenTest {

    @Test
    fun `crear una instancia de Imagen debe asignar las propiedades correctamente`() {
        // Arrange
        val id = 1L
        val entidadId = 100L
        val nombre = "imagen.jpg"
        val tipoEntidad = "publicacion"
        val fecha = "2024-05-23"
        val activo = true

        // Act
        val imagen = Imagen(
            imagenID = id,
            entidadID = entidadId,
            nombre = nombre,
            tipoEntidad = tipoEntidad,
            fechaCreacion = fecha,
            activo = activo
        )

        // Assert
        imagen.imagenID shouldBe id
        imagen.entidadID shouldBe entidadId
        imagen.nombre shouldBe nombre
        imagen.tipoEntidad shouldBe tipoEntidad
        imagen.fechaCreacion shouldBe fecha
        imagen.activo shouldBe activo
    }
}