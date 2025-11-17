package com.example.gaminghub.data.model

import io.kotest.matchers.shouldBe
import org.junit.Test

class SolicitudModeradorTest {

    @Test
    fun `crear una instancia de SolicitudModerador debe asignar las propiedades correctamente`() {
        // Arrange
        val id = 1L
        val usuarioId = 10L
        val nombre = "Test User"
        val email = "test@example.com"
        val motivo = "Quiero ayudar"
        val estado = "PENDIENTE"

        // Act
        val solicitud = SolicitudModerador(
            solicitudId = id,
            usuarioId = usuarioId,
            nombre = nombre,
            email = email,
            motivo = motivo,
            estado = estado
        )

        // Assert
        solicitud.solicitudId shouldBe id
        solicitud.usuarioId shouldBe usuarioId
        solicitud.nombre shouldBe nombre
        solicitud.email shouldBe email
        solicitud.motivo shouldBe motivo
        solicitud.estado shouldBe estado
    }
}