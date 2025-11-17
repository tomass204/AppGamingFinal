package com.example.gaminghub.data.network.repository

import com.example.gaminghub.api.ReaccionApi
import com.example.gaminghub.data.model.Reaccion
import com.example.gaminghub.data.model.ReaccionWrapper
import com.example.gaminghub.data.model.EmbeddedReacciones
import com.example.gaminghub.utils.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

class ReaccionRepositoryTest {

    @Test
    fun `guardar retorna una reaccion válida`() = runBlocking {
        // 1. Mock de la API y el Repositorio
        val api = mockk<ReaccionApi>()
        val repository = ReaccionRepository(api)

        // 2. Datos de prueba (usando la estructura correcta)
        val nuevaReaccion = Reaccion(
            usuarioID = 1L,
            tipoEntidad = "Publicacion",
            entidadID = 10L,
            tipoReaccion = "LIKE"
        )
        val reaccionGuardada = nuevaReaccion.copy(reaccionID = 1L)

        // 3. Simular la respuesta de la API (usa api.crear)
        coEvery { api.crear(nuevaReaccion) } returns Response.success(reaccionGuardada)

        // 4. Llamar al método del repositorio
        val result = repository.guardar(nuevaReaccion)

        // 5. Verificaciones
        assertTrue(result is Result.Success)
        val data = (result as Result.Success).data
        assertEquals(1L, data.reaccionID)
        assertEquals("LIKE", data.tipoReaccion)
    }

    @Test
    fun `obtenerLikesPorUsuario retorna una lista válida`() = runBlocking {
        // 1. Mock de la API y el Repositorio
        val api = mockk<ReaccionApi>()
        val repository = ReaccionRepository(api)
        val usuarioId = 1L

        // 2. Datos de prueba (usando la estructura correcta)
        val sampleReacciones = listOf(
            Reaccion(reaccionID = 1L, usuarioID = usuarioId, tipoEntidad = "Publicacion", entidadID = 10L, tipoReaccion = "LIKE"),
            Reaccion(reaccionID = 2L, usuarioID = usuarioId, tipoEntidad = "Comentario", entidadID = 25L, tipoReaccion = "LIKE")
        )
        val embedded = EmbeddedReacciones(reacciones = sampleReacciones)
        val responseWrapper = ReaccionWrapper(embedded = embedded)

        // 3. Simular la respuesta de la API (usa api.listarLikesPorUsuario)
        coEvery { api.listarLikesPorUsuario(usuarioId) } returns Response.success(responseWrapper)

        // 4. Llamar al método del repositorio
        val result = repository.obtenerLikesPorUsuario(usuarioId)

        // 5. Verificaciones
        assertTrue(result is Result.Success)
        val data = (result as Result.Success).data
        assertEquals(2, data.size)
        assertEquals(10L, data[0].entidadID)
    }
}