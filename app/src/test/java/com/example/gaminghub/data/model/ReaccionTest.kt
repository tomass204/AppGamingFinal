package com.example.gaminghub.data.model

import io.kotest.matchers.shouldBe
import org.junit.Test

class ReaccionTest {

    @Test
    fun `crear una instancia de Reaccion debe asignar las propiedades correctamente`() {
        // 1. Arrange
        val reaccionIdPrueba = 1L
        val usuarioIdPrueba = 10L
        val tipoEntidadPrueba = "Publicacion"
        val entidadIdPrueba = 100L
        val tipoReaccionPrueba = "LIKE"

        // 2. Act
        val reaccion = Reaccion(
            reaccionID = reaccionIdPrueba,
            usuarioID = usuarioIdPrueba,
            tipoEntidad = tipoEntidadPrueba,
            entidadID = entidadIdPrueba,
            tipoReaccion = tipoReaccionPrueba
        )

        // 3. Assert
        reaccion.reaccionID shouldBe reaccionIdPrueba
        reaccion.usuarioID shouldBe usuarioIdPrueba
        reaccion.tipoEntidad shouldBe tipoEntidadPrueba
        reaccion.entidadID shouldBe entidadIdPrueba
        reaccion.tipoReaccion shouldBe tipoReaccionPrueba
    }
}