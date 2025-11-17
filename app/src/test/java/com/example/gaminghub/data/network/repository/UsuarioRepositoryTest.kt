package com.example.gaminghub.data.network.repository

import com.example.gaminghub.data.model.LoginResponse
import com.example.gaminghub.data.network.api.UsuarioApi
import com.example.gaminghub.utils.Result
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Test
import retrofit2.Response

class UsuarioRepositoryTest {

    private val apiFalsa: UsuarioApi = mockk()
    private val repositorio = UsuarioRepository(apiFalsa)

    @Test
    fun `login con credenciales correctas debería devolver Success con LoginResponse`() = runTest {
        val respuestaExitosa = Response.success(
            LoginResponse(1, "Test User", "test@test.com", "ADMIN", "OK", "token")
        )
        coEvery { apiFalsa.login(any(), any()) } returns respuestaExitosa

        val resultado = repositorio.login("test@test.com", "password")

        resultado.shouldBeInstanceOf<Result.Success<LoginResponse>>()
        resultado.data.usuarioId shouldBe 1
    }

    @Test
    fun `login con credenciales incorrectas debería devolver Error`() = runTest {
        val respuestaError = Response.error<LoginResponse>(401, "Unauthorized".toResponseBody())
        coEvery { apiFalsa.login(any(), any()) } returns respuestaError

        val resultado = repositorio.login("user", "badpass")

        resultado.shouldBeInstanceOf<Result.Error>()
        resultado.message shouldBe "Error: 401"
    }
}