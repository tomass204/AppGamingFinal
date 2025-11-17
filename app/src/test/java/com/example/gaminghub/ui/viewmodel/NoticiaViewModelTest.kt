package com.example.gaminghub.ui.viewmodel

import com.example.gaminghub.data.model.Publicacion
import com.example.gaminghub.data.network.repository.FavoritoRepository
import com.example.gaminghub.data.network.repository.PublicacionRepository
import com.example.gaminghub.data.network.repository.ReaccionRepository
import com.example.gaminghub.ui.navigation.SessionManager
import com.example.gaminghub.utils.Result
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class NoticiaViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var publicacionRepository: PublicacionRepository
    private lateinit var reaccionRepository: ReaccionRepository
    private lateinit var favoritoRepository: FavoritoRepository
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: NoticiaViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        publicacionRepository = mockk(relaxed = true)
        reaccionRepository = mockk(relaxed = true)
        favoritoRepository = mockk(relaxed = true)
        sessionManager = mockk(relaxed = true)

        viewModel = NoticiaViewModel(
            repository = publicacionRepository,
            sessionManager = sessionManager,
            reaccionRepository = reaccionRepository,
            favoritoRepository = favoritoRepository
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadNoticias con éxito debe filtrar por tipo NOTICIA`() = runTest {
        // 1. Arrange
        val userId = 1L
        val noticiaId = 20L
        val debateId = 21L

        val publicaciones = listOf(
            Publicacion(publicacionID = noticiaId, titulo = "Noticia 1", contenido = "", tipo = "NOTICIA", autorID = 2),
            Publicacion(publicacionID = debateId, titulo = "Debate 1", contenido = "", tipo = "DEBATE", autorID = 2)
        )

        coEvery { sessionManager.getUserId() } returns userId
        coEvery { publicacionRepository.listaPublicaciones() } returns Result.Success(publicaciones)
        coEvery { favoritoRepository.obtenerPorUsuario(userId) } returns Result.Success(emptyList())
        coEvery { reaccionRepository.obtenerLikesPorUsuario(userId) } returns Result.Success(emptyList())

        // 2. Act
        viewModel.loadNoticias()
        testDispatcher.scheduler.advanceUntilIdle()

        // 3. Assert
        val state = viewModel.uiState.value
        state.isLoading shouldBe false
        state.isSuccess shouldBe true
        state.noticias shouldHaveSize 1
        state.noticias.first().publicacionID shouldBe noticiaId
        state.noticias.first().tipo shouldBe "NOTICIA"
    }

    @Test
    fun `loadNoticias con error al cargar publicaciones debe mostrar un mensaje de error`() = runTest {
        // 1. Arrange
        val errorMessage = "Error de conexión"
        coEvery { publicacionRepository.listaPublicaciones() } returns Result.Error(errorMessage)

        // 2. Act
        viewModel.loadNoticias()
        testDispatcher.scheduler.advanceUntilIdle()

        // 3. Assert
        val state = viewModel.uiState.value
        state.isLoading shouldBe false
        state.isSuccess shouldBe false
        state.errorMessage shouldBe errorMessage
        state.noticias.shouldBeEmpty()
    }
}