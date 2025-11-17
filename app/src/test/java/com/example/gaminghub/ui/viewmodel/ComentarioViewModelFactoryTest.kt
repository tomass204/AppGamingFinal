package com.example.gaminghub.ui.viewmodel

import ComentarioViewModel // <-- ¡¡LA LÍNEA CLAVE QUE FALTA!!
import com.example.gaminghub.data.network.repository.ComentarioRepository
import com.example.gaminghub.data.network.repository.ImagenRepository
import com.example.gaminghub.data.network.repository.PublicacionRepository
import com.example.gaminghub.ui.navigation.SessionManager
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class ComentarioViewModelFactoryTest {

    private lateinit var comentarioRepository: ComentarioRepository
    private lateinit var publicacionRepository: PublicacionRepository
    private lateinit var imagenRepository: ImagenRepository
    private lateinit var sessionManager: SessionManager
    private lateinit var factory: ComentarioViewModelFactory

    private val publicacionIdDePrueba = 123L

    @Before
    fun setUp() {
        // 1. Arrange: Creamos mocks para todas las dependencias
        comentarioRepository = mockk()
        publicacionRepository = mockk()
        imagenRepository = mockk()
        sessionManager = mockk()
        
        factory = ComentarioViewModelFactory(
            publicacionId = publicacionIdDePrueba,
            sessionManager = sessionManager,
            repository = comentarioRepository,
            publicacionRepository = publicacionRepository,
            imagenRepository = imagenRepository
        )
    }

    @Test
    fun `create debe devolver una instancia de ComentarioViewModel cuando se le pide`() {
        // 2. Act: Le pedimos a la factory que cree un ComentarioViewModel
        val viewModel = factory.create(ComentarioViewModel::class.java)

        // 3. Assert: Verificamos que el resultado es del tipo correcto
        viewModel.shouldBeInstanceOf<ComentarioViewModel>()
    }

    @Test
    fun `create debe lanzar una excepción si se le pide un ViewModel desconocido`() {
        // 2. Act & 3. Assert: Verificamos que lanza un error si intentamos crear una clase incorrecta
        val exception = shouldThrow<IllegalArgumentException> {
            factory.create(AuthViewModel::class.java) // Usamos otro ViewModel como ejemplo
        }

        exception.message shouldBe "Unknown ViewModel class"
    }
}