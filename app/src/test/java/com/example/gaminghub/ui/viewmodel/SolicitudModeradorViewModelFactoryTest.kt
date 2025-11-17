package com.example.gaminghub.ui.viewmodel

import com.example.gaminghub.data.network.repository.SolicitudModeradorRepository
import com.example.gaminghub.ui.navigation.SessionManager
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class SolicitudModeradorViewModelFactoryTest {

    private lateinit var repository: SolicitudModeradorRepository
    private lateinit var sessionManager: SessionManager
    private lateinit var factory: SolicitudModeradorViewModelFactory

    @Before
    fun setUp() {
        // 1. Arrange
        repository = mockk()
        sessionManager = mockk()

        factory = SolicitudModeradorViewModelFactory(
            repository = repository,
            sessionManager = sessionManager
        )
    }

    @Test
    fun `create debe devolver una instancia de SolicitudModeradorViewModel cuando se le pide`() {
        // 2. Act
        val viewModel = factory.create(SolicitudModeradorViewModel::class.java)

        // 3. Assert
        viewModel.shouldBeInstanceOf<SolicitudModeradorViewModel>()
    }

    @Test
    fun `create debe lanzar una excepción si se le pide un ViewModel desconocido`() {
        // 2. Act & 3. Assert
        val exception = shouldThrow<IllegalArgumentException> {
            factory.create(AuthViewModel::class.java)
        }

        exception.message shouldBe "Unknown ViewModel class"
    }
}