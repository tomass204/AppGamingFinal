package com.example.gaminghub.data.network.repository

import com.example.gaminghub.api.FavoritoApi // <-- ¡CORREGIDO!
import com.example.gaminghub.data.model.Favorito
import com.example.gaminghub.data.model.FavoritoWrapper
import com.example.gaminghub.data.model.EmbeddedFavoritos
import com.example.gaminghub.utils.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

class FavoritoRepositoryTest {

    @Test
    fun `obtenerPorUsuario retorna una lista válida`() = runBlocking {
        // 1. Mock de la API y el Repositorio
        val api = mockk<FavoritoApi>()
        val repository = FavoritoRepository(api)
        val usuarioId = 1L

        // 2. Datos de prueba (con la estructura correcta de Favorito)
        val sampleFavoritos = listOf(
            Favorito(favoritoID = 1, usuarioID = usuarioId, publicacionID = 1),
            Favorito(favoritoID = 2, usuarioID = usuarioId, publicacionID = 2)
        )
        // Se crea la estructura de respuesta completa que espera la API real
        val embedded = EmbeddedFavoritos(favoritos = sampleFavoritos)
        val responseWrapper = FavoritoWrapper(embedded = embedded)

        // 3. Simular la respuesta de la API (con el Wrapper correcto)
        coEvery { api.obtenerPorUsuario(usuarioId) } returns Response.success(responseWrapper)

        // 4. Llamar al método del repositorio
        val result = repository.obtenerPorUsuario(usuarioId)

        // 5. Verificaciones (con las propiedades correctas)
        assertTrue(result is Result.Success)
        val data = (result as Result.Success).data
        assertEquals(2, data.size)
        assertEquals(1L, data[0].publicacionID)
    }

    @Test
    fun `guardar retorna un favorito válido`() = runBlocking {
        // 1. Mock de la API y el Repositorio
        val api = mockk<FavoritoApi>()
        val repository = FavoritoRepository(api)

        // 2. Datos de prueba (con la estructura correcta de Favorito)
        val nuevoFavorito = Favorito(usuarioID = 1, publicacionID = 3)
        val favoritoGuardado = nuevoFavorito.copy(favoritoID = 3)

        // 3. Simular la respuesta de la API
        coEvery { api.guardar(nuevoFavorito) } returns Response.success(favoritoGuardado)

        // 4. Llamar al método del repositorio
        val result = repository.guardar(nuevoFavorito)

        // 5. Verificaciones (con las propiedades correctas)
        assertTrue(result is Result.Success)
        val data = (result as Result.Success).data
        assertEquals(3L, data.favoritoID)
        assertEquals(1L, data.usuarioID)
        assertEquals(3L, data.publicacionID)
    }
}