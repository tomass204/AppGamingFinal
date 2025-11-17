package com.example.gaminghub.ui.viewmodel

import android.content.Context
import android.net.Uri
import com.example.gaminghub.data.network.repository.PublicacionRepository
import com.example.gaminghub.ui.navigation.SessionManager
import com.example.gaminghub.utils.Result
import io.kotest.matchers.shouldBe
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.File

@ExperimentalCoroutinesApi
class PublicacionViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: PublicacionRepository
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: PublicacionViewModel

    // Mocks para las dependencias de Android
    private val mockContext: Context = mockk(relaxed = true)
    private val mockUri: Uri = mockk(relaxed = true)
    private val mockFile: File = mockk(relaxed = true) {
        every { name } returns "test.jpg"
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        sessionManager = mockk(relaxed = true)

        // Usamos spyk para poder mockear el método privado uriToFile
        viewModel = spyk(PublicacionViewModel(repository, sessionManager))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll() // Limpia los mocks y spies después de cada test
    }

    @Test
    fun `crearPublicacion con éxito para noticia debe redirigir a la ruta de noticias`() = runTest {
        // 1. Arrange
        val userId = 1L
        val successMessage = "Publicación creada correctamente"
        val expectedRoute = "news" // Ruta de la sección de noticias
        val tipoNoticia = "NOTICIA"

        coEvery { sessionManager.getUserId() } returns userId
        coEvery { repository.crearPublicacion(any(), any()) } returns Result.Success(mockk())

        // Interceptamos la llamada al método privado para evitar las dependencias de Android
        every { viewModel["uriToFile"](any<Context>(), any<Uri>()) } returns mockFile

        // 2. Act
        viewModel.crearPublicacion(
            context = mockContext,
            titulo = "Mi Noticia",
            contenido = "Contenido de la noticia",
            tipo = tipoNoticia,
            imageUris = listOf(mockUri)
        )
        testDispatcher.scheduler.advanceUntilIdle() // Ejecuta las coroutines pendientes

        // 3. Assert
        val state = viewModel.uiState.value
        state.isLoading shouldBe false
        state.isSuccess shouldBe true
        state.shouldRedirect shouldBe true
        state.redirectRoute shouldBe expectedRoute
        state.successMessage shouldBe successMessage

        coVerify { repository.crearPublicacion(any(), any()) }
    }

    @Test
    fun `crearPublicacion con error en el repositorio debe mostrar un mensaje de error`() = runTest {
        // 1. Arrange
        val errorMessage = "Error en el servidor"
        coEvery { sessionManager.getUserId() } returns 1L
        coEvery { repository.crearPublicacion(any(), any()) } returns Result.Error(errorMessage)
        every { viewModel["uriToFile"](any<Context>(), any<Uri>()) } returns mockFile

        // 2. Act
        viewModel.crearPublicacion(
            context = mockContext,
            titulo = "Título",
            contenido = "Contenido",
            tipo = "NOTICIA",
            imageUris = listOf(mockUri)
        )
        testDispatcher.scheduler.advanceUntilIdle()

        // 3. Assert
        val state = viewModel.uiState.value
        state.isLoading shouldBe false
        state.isSuccess shouldBe false
        state.shouldRedirect shouldBe false
        state.errorMessage shouldBe errorMessage
    }
}