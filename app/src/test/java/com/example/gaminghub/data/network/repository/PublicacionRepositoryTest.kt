package com.example.gaminghub.data.network.repository

import com.example.gaminghub.data.model.Publicacion
import com.example.gaminghub.data.model.PublicacionWrapper
import com.example.gaminghub.data.model.EmbeddedPublicaciones
import com.example.gaminghub.data.network.api.PublicacionApi
import com.example.gaminghub.utils.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

class PublicacionRepositoryTest {

    @Test
    fun `listaPublicaciones retorna una lista válida`() = runBlocking {
        // 1. Mock de la API y el Repositorio
        val api = mockk<PublicacionApi>()
        val repository = PublicacionRepository(api)

        // 2. Datos de prueba (¡CORREGIDO!)
        // Se usa el constructor correcto de la clase Publicacion con sus parámetros nombrados
        val samplePublicaciones = listOf(
            Publicacion(
                publicacionID = 1,
                titulo = "Título de prueba",
                contenido = "Descripción de prueba",
                tipo = "noticia",
                autorID = 1
            ),
            Publicacion(
                publicacionID = 2,
                titulo = "Otra publicación",
                contenido = "Más detalles",
                tipo = "review",
                autorID = 1
            )
        )
        // Se crea la estructura de respuesta completa que espera la API real
        val embedded = EmbeddedPublicaciones(publicaciones = samplePublicaciones)
        val responseWrapper = PublicacionWrapper(embedded = embedded)

        // 3. Simular la respuesta de la API (con el Wrapper correcto)
        coEvery { api.listaPublicaciones() } returns Response.success(responseWrapper)

        // 4. Llamar al método del repositorio
        val result = repository.listaPublicaciones()

        // 5. Verificaciones (con las propiedades correctas)
        assertTrue(result is Result.Success)
        val data = (result as Result.Success).data
        assertEquals(2, data.size)
        assertEquals("Título de prueba", data[0].titulo)
    }
}