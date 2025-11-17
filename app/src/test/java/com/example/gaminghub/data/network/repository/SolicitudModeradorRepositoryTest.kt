package com.example.gaminghub.data.network.repository

import com.example.gaminghub.data.network.api.SolicitudModeradorApi
import com.example.gaminghub.data.model.SolicitudModerador
import com.example.gaminghub.data.model.SolicitudModeradorWrapper
import com.example.gaminghub.data.model.EmbeddedSolicitudModerador
import com.example.gaminghub.utils.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

class SolicitudModeradorRepositoryTest {

    @Test
    fun `listarSolicitudes retorna una lista válida`() = runBlocking {
        // 1. Mock de la API y el Repositorio
        val api = mockk<SolicitudModeradorApi>()
        val repository = SolicitudModeradorRepository(api)

        // 2. Datos de prueba (usando la estructura correcta)
        val sampleSolicitudes = listOf(
            SolicitudModerador(
                solicitudId = 1L,
                usuarioId = 10L,
                nombre = "Usuario A",
                email = "a@example.com",
                motivo = "Quiero ayudar",
                estado = "PENDIENTE"
            ),
            SolicitudModerador(
                solicitudId = 2L,
                usuarioId = 11L,
                nombre = "Usuario B",
                email = "b@example.com",
                motivo = "Tengo experiencia",
                estado = "PENDIENTE"
            )
        )
        val embedded = EmbeddedSolicitudModerador(solicitudes = sampleSolicitudes)
        val responseWrapper = SolicitudModeradorWrapper(embedded = embedded)

        // 3. Simular la respuesta de la API
        coEvery { api.listarSolicitudes() } returns Response.success(responseWrapper)

        // 4. Llamar al método del repositorio
        val result = repository.listarSolicitudes()

        // 5. Verificaciones
        assertTrue(result is Result.Success)
        val data = (result as Result.Success).data
        assertEquals(2, data.size)
        assertEquals("Usuario A", data[0].nombre)
    }

    @Test
    fun `crearSolicitud retorna una solicitud válida`() = runBlocking {
        // 1. Mock de la API y el Repositorio
        val api = mockk<SolicitudModeradorApi>()
        val repository = SolicitudModeradorRepository(api)

        // 2. Datos de prueba (usando la estructura correcta)
        val nuevaSolicitud = SolicitudModerador(
            usuarioId = 12L,
            nombre = "Usuario C",
            email = "c@example.com",
            motivo = "Soy muy activo"
        )
        val solicitudCreada = nuevaSolicitud.copy(solicitudId = 3L)

        // 3. Simular la respuesta de la API
        coEvery { api.crearSolicitud(nuevaSolicitud) } returns Response.success(solicitudCreada)

        // 4. Llamar al método del repositorio
        val result = repository.crearSolicitud(nuevaSolicitud)

        // 5. Verificaciones
        assertTrue(result is Result.Success)
        val data = (result as Result.Success).data
        assertEquals(3L, data.solicitudId)
        assertEquals("Usuario C", data.nombre)
    }

    @Test
    fun `aprobarSolicitud retorna la solicitud aprobada`() = runBlocking {
        // 1. Mock de la API y el Repositorio
        val api = mockk<SolicitudModeradorApi>()
        val repository = SolicitudModeradorRepository(api)
        val solicitudId = 4L

        // 2. Datos de prueba
        val solicitudAprobada = SolicitudModerador(
            solicitudId = solicitudId,
            usuarioId = 13L,
            nombre = "Usuario D",
            email = "d@example.com",
            motivo = "Revisado OK",
            estado = "APROBADO"
        )

        // 3. Simular la respuesta de la API
        coEvery { api.aprobarSolicitud(solicitudId) } returns Response.success(solicitudAprobada)

        // 4. Llamar al método del repositorio
        val result = repository.aprobarSolicitud(solicitudId)

        // 5. Verificaciones
        assertTrue(result is Result.Success)
        val data = (result as Result.Success).data
        assertEquals(solicitudId, data.solicitudId)
        assertEquals("APROBADO", data.estado)
    }
}