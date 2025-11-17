package com.example.gaminghub.data.network.repository

import com.example.gaminghub.data.model.Imagen
import com.example.gaminghub.data.network.api.ImagenApi
import com.example.gaminghub.utils.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

class ImagenRepositoryTest {

    @Test
    fun `buscarPorEntidad retorna una lista válida de imágenes`() = runBlocking {
        // 1. Mock de la API y el Repositorio
        val api = mockk<ImagenApi>()
        val repository = ImagenRepository(api)
        val entidadId = 1L
        val tipoEntidad = "publicacion"

        // 2. Datos de prueba (¡CORREGIDO!)
        // Se usa el constructor correcto de la clase Imagen, con todos los campos requeridos.
        val sampleImagenes = listOf(
            Imagen(
                imagenID = 1,
                entidadID = entidadId,
                nombre = "imagen1.jpg",
                tipoEntidad = tipoEntidad,
                fechaCreacion = "2024-05-21T10:00:00"
            ),
            Imagen(
                imagenID = 2,
                entidadID = entidadId,
                nombre = "imagen2.png",
                tipoEntidad = tipoEntidad,
                fechaCreacion = "2024-05-21T10:01:00"
            )
        )

        // 3. Simular la respuesta de la API
        coEvery { api.buscarPorEntidad(entidadId, tipoEntidad) } returns Response.success(sampleImagenes)

        // 4. Llamar al método del repositorio
        val result = repository.buscarPorEntidad(entidadId, tipoEntidad)

        // 5. Verificaciones
        assertTrue(result is Result.Success)
        val data = (result as Result.Success).data
        assertEquals(2, data.size)
        assertEquals("imagen1.jpg", data[0].nombre)
    }

    @Test
    fun `obtenerImagenPorId retorna un array de bytes válido`() = runBlocking {
        // (Este test ya era correcto y no necesita cambios)
        // 1. Mock de la API y el Repositorio
        val api = mockk<ImagenApi>()
        val repository = ImagenRepository(api)

        // 2. Datos de prueba
        val imagenId = 1L
        val fakeImageBytes = "fake-image-data".toByteArray()
        val responseBody = fakeImageBytes.toResponseBody()

        // 3. Simular la respuesta de la API
        coEvery { api.obtenerImagenPorId(imagenId) } returns Response.success(responseBody)

        // 4. Llamar al método del repositorio
        val result = repository.obtenerImagenPorId(imagenId)

        // 5. Verificaciones
        assertTrue(result is Result.Success)
        val data = (result as Result.Success).data
        assertArrayEquals(fakeImageBytes, data)
    }
}