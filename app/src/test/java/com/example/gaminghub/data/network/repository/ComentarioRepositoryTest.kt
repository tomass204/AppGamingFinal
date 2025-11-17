package com.example.gaminghub.data.network.repository

import com.example.gaminghub.data.model.Comentario
import com.example.gaminghub.data.model.ComentarioWrapper
import com.example.gaminghub.data.model.EmbeddedComentarios
import com.example.gaminghub.data.network.api.ComentarioApi
import com.example.gaminghub.utils.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

class ComentarioRepositoryTest {

    @Test
    fun `listarComentariosPorPublicacion retorna una lista válida`() = runBlocking {
        // 1. Mock de la API y el Repositorio
        val api = mockk<ComentarioApi>()
        val repository = ComentarioRepository(api)
        val publicacionId = 1L

        // 2. Datos de prueba (usando la estructura correcta de Comentario)
        val sampleComentarios = listOf(
            Comentario(
                comentarioID = 1,
                contenido = "Buen post!",
                autorID = 1,
                publicacionID = publicacionId
            ),
            Comentario(
                comentarioID = 2,
                contenido = "Interesante.",
                autorID = 2,
                publicacionID = publicacionId
            )
        )
        // Se crea la estructura de respuesta completa que espera la API real
        val embedded = EmbeddedComentarios(comentarios = sampleComentarios)
        val responseWrapper = ComentarioWrapper(embedded = embedded)

        // 3. Simular la respuesta de la API (con el Wrapper correcto)
        coEvery { api.listarComentariosPorPublicacion(publicacionId) } returns Response.success(responseWrapper)

        // 4. Llamar al método del repositorio
        val result = repository.listarComentariosPorPublicacion(publicacionId)

        // 5. Verificaciones (usando las propiedades correctas)
        assertTrue(result is Result.Success)
        val data = (result as Result.Success).data
        assertEquals(2, data.size)
        assertEquals("Buen post!", data[0].contenido)
    }

    @Test
    fun `crearComentario retorna un comentario válido`() = runBlocking {
        // 1. Mock de la API y el Repositorio
        val api = mockk<ComentarioApi>()
        val repository = ComentarioRepository(api)

        // 2. Datos de prueba (usando la estructura correcta de Comentario)
        val nuevoComentario = Comentario(
            contenido = "Nuevo comentario",
            autorID = 3,
            publicacionID = 1
        )
        val comentarioCreado = nuevoComentario.copy(comentarioID = 3)

        // 3. Simular la respuesta de la API
        coEvery { api.crearComentario(nuevoComentario) } returns Response.success(comentarioCreado)

        // 4. Llamar al método del repositorio
        val result = repository.crearComentario(nuevoComentario)

        // 5. Verificaciones (usando las propiedades correctas)
        assertTrue(result is Result.Success)
        val data = (result as Result.Success).data
        assertEquals(3L, data.comentarioID) // Se compara con Long
        assertEquals("Nuevo comentario", data.contenido)
    }
}