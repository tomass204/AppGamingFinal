package com.example.gaminghub.ui.viewmodel

import com.example.gaminghub.data.network.repository.PublicacionRepository
import com.example.gaminghub.ui.navigation.SessionManager
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class PublicacionViewModelFactoryTest {

    private lateinit var repository: PublicacionRepository
    private lateinit var sessionManager: SessionManager
    private lateinit var factory: PublicacionViewModelFactory

    @Before
    fun setUp() {
        // 1. Arrange
        repository = mockk()
        sessionManager = mockk()
        
        factory = PublicacionViewModelFactory(
            repository = repository,
            sessionManager = sessionManager
        )
    }

    @Test
    fun `create debe devolver una instancia de PublicacionViewModel cuando se le pide`() {
        // 2. Act
        val viewModel = factory.create(PublicacionViewModel::class.java)

        // 3. Assert
        viewModel.shouldBeInstanceOf<PublicacionViewModel>()
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