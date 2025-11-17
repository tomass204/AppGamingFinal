package com.example.gaminghub.ui.viewmodel

import com.example.gaminghub.data.network.repository.FavoritoRepository
import com.example.gaminghub.data.network.repository.PublicacionRepository
import com.example.gaminghub.data.network.repository.ReaccionRepository
import com.example.gaminghub.ui.navigation.SessionManager
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class NoticiaViewModelFactoryTest {

    private lateinit var publicacionRepository: PublicacionRepository
    private lateinit var sessionManager: SessionManager
    private lateinit var reaccionRepository: ReaccionRepository
    private lateinit var favoritoRepository: FavoritoRepository
    private lateinit var factory: NoticiaViewModelFactory

    @Before
    fun setUp() {
        // 1. Arrange
        publicacionRepository = mockk()
        sessionManager = mockk()
        reaccionRepository = mockk()
        favoritoRepository = mockk()
        
        factory = NoticiaViewModelFactory(
            repository = publicacionRepository,
            sessionManager = sessionManager,
            reaccionRepository = reaccionRepository,
            favoritoRepository = favoritoRepository
        )
    }

    @Test
    fun `create debe devolver una instancia de NoticiaViewModel cuando se le pide`() {
        // 2. Act
        val viewModel = factory.create(NoticiaViewModel::class.java)

        // 3. Assert
        viewModel.shouldBeInstanceOf<NoticiaViewModel>()
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