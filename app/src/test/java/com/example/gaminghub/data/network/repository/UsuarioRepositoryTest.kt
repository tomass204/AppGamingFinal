package com.example.gaminghub.data.network.repository

import com.example.gaminghub.data.model.LoginResponse
import com.example.gaminghub.data.model.Usuario
import com.example.gaminghub.data.network.api.UsuarioApi
import com.example.gaminghub.utils.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

class UsuarioRepositoryTest {

    @Test
    fun `login retorna una respuesta válida`() = runBlocking {
        // 1. Mock de la API y el Repositorio
        val api = mockk<UsuarioApi>()
        val repository = UsuarioRepository(api)

        // 2. Datos de prueba (¡CORREGIDO!)
        // Se usa el constructor correcto de LoginResponse con parámetros nombrados
        val loginResponse = LoginResponse(
            usuarioId = 1L,
            nombre = "Test User",
            email = "test@example.com",
            token = "token-de-prueba",
            message = "Login exitoso",
            rol = "USUARIO_BASICO"
        )

        // 3. Simular la respuesta de la API
        coEvery { api.login("test@example.com", "password") } returns Response.success(loginResponse)

        // 4. Llamar al método del repositorio
        val result = repository.login("test@example.com", "password")

        // 5. Verificaciones (con las propiedades correctas)
        assertTrue(result is Result.Success)
        val data = (result as Result.Success).data
        assertEquals(1L, data.usuarioId)
        assertEquals("token-de-prueba", data.token)
    }

    @Test
    fun `crear retorna un usuario válido`() = runBlocking {
        // 1. Mock de la API y el Repositorio
        val api = mockk<UsuarioApi>()
        val repository = UsuarioRepository(api)

        // 2. Datos de prueba (¡CORREGIDO!)
        // Se usa el constructor correcto de Usuario con parámetros nombrados
        val nuevoUsuario = Usuario(
            nombre = "Test User",
            email = "test@example.com",
            contrasena = "password",
            rol = "USUARIO_BASICO"
        )
        // Se usa .copy con el nombre de propiedad correcto (usuarioID)
        val usuarioCreado = nuevoUsuario.copy(usuarioID = 1L)

        // 3. Simular la respuesta de la API
        coEvery { api.crear(nuevoUsuario) } returns Response.success(usuarioCreado)

        // 4. Llamar al método del repositorio
        val result = repository.crear(nuevoUsuario)

        // 5. Verificaciones (con las propiedades correctas)
        assertTrue(result is Result.Success)
        val data = (result as Result.Success).data
        assertEquals(1L, data.usuarioID)
        assertEquals("Test User", data.nombre)
    }
}