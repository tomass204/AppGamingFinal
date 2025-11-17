package com.example.gaminghub.ui.viewmodel

import com.example.gaminghub.data.network.repository.UsuarioRepository
import com.example.gaminghub.ui.navigation.SessionManager
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class AuthViewModelFactoryTest {

    private lateinit var repository: UsuarioRepository
    private lateinit var sessionManager: SessionManager
    private lateinit var factory: AuthViewModelFactory

    @Before
    fun setUp() {
        // 1. Arrange: Creamos mocks para las dependencias
        repository = mockk()
        sessionManager = mockk()
        factory = AuthViewModelFactory(repository, sessionManager)
    }

    @Test
    fun `create debe devolver una instancia de AuthViewModel cuando se le pide`() {
        // 2. Act: Le pedimos a la factory que cree un AuthViewModel
        val viewModel = factory.create(AuthViewModel::class.java)

        // 3. Assert: Verificamos que el resultado es del tipo correcto
        viewModel.shouldBeInstanceOf<AuthViewModel>()
    }

    @Test
    fun `create debe lanzar una excepción si se le pide un ViewModel desconocido`() {
        // 2. Act & 3. Assert: Verificamos que lanza un error si intentamos crear una clase incorrecta
        val exception = shouldThrow<IllegalArgumentException> {
            factory.create(DebateViewModel::class.java) // Usamos otro ViewModel como ejemplo
        }

        exception.message shouldBe "Unknown ViewModel class"
    }
}