package com.example.gaminghub.ui.viewmodel

import ComentarioViewModel
import com.example.gaminghub.data.model.Comentario
import com.example.gaminghub.data.model.Publicacion
import com.example.gaminghub.data.network.repository.ComentarioRepository
import com.example.gaminghub.data.network.repository.ImagenRepository
import com.example.gaminghub.data.network.repository.PublicacionRepository
import com.example.gaminghub.ui.navigation.SessionManager
import com.example.gaminghub.utils.Result
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ComentarioViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var comentarioRepository: ComentarioRepository
    private lateinit var publicacionRepository: PublicacionRepository
    private lateinit var imagenRepository: ImagenRepository
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: ComentarioViewModel

    private val publicacionIdDePrueba = 1L

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        comentarioRepository = mockk(relaxed = true)
        publicacionRepository = mockk(relaxed = true)
        imagenRepository = mockk(relaxed = true)
        sessionManager = mockk(relaxed = true)

        viewModel = ComentarioViewModel(
            publicacionId = publicacionIdDePrueba,
            sessionManager = sessionManager,
            repository = comentarioRepository,
            publicacionRepository = publicacionRepository,
            imagenRepository = imagenRepository
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `cargarComentarios con éxito debe actualizar el estado con la publicación y los comentarios`() = runTest {
        // 1. Arrange
        val publicacionDePrueba = Publicacion(publicacionID = publicacionIdDePrueba, titulo = "Mi Publicación", contenido = "...", tipo = "DEBATE", autorID = 1)
        val listaComentarios = listOf(
            Comentario(comentarioID = 1, contenido = "Primer comentario", autorID = 2, publicacionID = publicacionIdDePrueba),
            Comentario(comentarioID = 2, contenido = "Segundo comentario", autorID = 3, publicacionID = publicacionIdDePrueba)
        )
        coEvery { publicacionRepository.verPublicacion(publicacionIdDePrueba) } returns Result.Success(publicacionDePrueba)
        coEvery { comentarioRepository.listarComentariosPorPublicacion(publicacionIdDePrueba) } returns Result.Success(listaComentarios)

        // 2. Act
        viewModel.cargarComentarios()
        testDispatcher.scheduler.advanceUntilIdle()

        // 3. Assert
        val state = viewModel.uiState.value
        state.isLoading shouldBe false
        state.errorMessage shouldBe null
        state.publicacion shouldBe publicacionDePrueba
        state.comentarios.shouldNotBeEmpty()
        state.comentarios.size shouldBe 2
    }

    @Test
    fun `cargarComentarios con error en el repositorio de publicaciones debe mostrar un error`() = runTest {
        // 1. Arrange
        val errorMessage = "No se pudo cargar la publicación"
        coEvery { publicacionRepository.verPublicacion(publicacionIdDePrueba) } returns Result.Error(errorMessage)

        // 2. Act
        viewModel.cargarComentarios()
        testDispatcher.scheduler.advanceUntilIdle()

        // 3. Assert
        val state = viewModel.uiState.value
        state.isLoading shouldBe false
        state.errorMessage shouldBe errorMessage
    }

    @Test
    fun `agregarComentario con éxito debe mostrar un mensaje de éxito y recargar los comentarios`() = runTest {
        // 1. Arrange
        val userId = 10L
        val textoComentario = "Este es un nuevo comentario"
        val comentarioCreado = Comentario(comentarioID = 3, contenido = textoComentario, autorID = userId, publicacionID = publicacionIdDePrueba)

        coEvery { sessionManager.getUserId() } returns userId
        coEvery { comentarioRepository.crearComentario(any()) } returns Result.Success(comentarioCreado)
        // Simulamos la recarga que ocurre después de agregar exitosamente
        coEvery { publicacionRepository.verPublicacion(any()) } returns Result.Success(mockk())
        coEvery { comentarioRepository.listarComentariosPorPublicacion(any()) } returns Result.Success(emptyList())

        // 2. Act
        viewModel.agregarComentario(textoComentario)
        testDispatcher.scheduler.advanceUntilIdle()

        // 3. Assert
        val state = viewModel.uiState.value
        state.isLoading shouldBe false
        state.successMessage shouldBe "Comentario publicado correctamente"
    }

    @Test
    fun `agregarComentario con texto vacío debe mostrar un mensaje de error`() = runTest {
        // 1. Arrange
        val textoVacio = "   " // Texto con solo espacios

        // 2. Act
        viewModel.agregarComentario(textoVacio)
        testDispatcher.scheduler.advanceUntilIdle()

        // 3. Assert
        val state = viewModel.uiState.value
        state.errorMessage shouldBe "El comentario no puede estar vacío"
    }
}