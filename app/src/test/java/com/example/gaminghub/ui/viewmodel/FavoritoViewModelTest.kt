package com.example.gaminghub.ui.viewmodel

import com.example.gaminghub.data.model.Favorito
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
class FavoritoViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var publicacionRepository: PublicacionRepository
    private lateinit var reaccionRepository: ReaccionRepository
    private lateinit var favoritoRepository: FavoritoRepository
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: FavoritoViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        publicacionRepository = mockk(relaxed = true)
        reaccionRepository = mockk(relaxed = true)
        favoritoRepository = mockk(relaxed = true)
        sessionManager = mockk(relaxed = true)

        viewModel = FavoritoViewModel(
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
    fun `loadFavoritos con éxito debe mostrar solo las publicaciones marcadas como favoritas`() = runTest {
        // 1. Arrange
        val userId = 1L
        val publicacionFavoritaId = 10L
        val publicacionNoFavoritaId = 11L

        val todasLasPublicaciones = listOf(
            Publicacion(publicacionID = publicacionFavoritaId, titulo = "Publi Favorita", contenido = "", tipo = "DEBATE", autorID = 2),
            Publicacion(publicacionID = publicacionNoFavoritaId, titulo = "Publi Normal", contenido = "", tipo = "NOTICIA", autorID = 2)
        )
        val listaDeFavoritos = listOf(Favorito(favoritoID = 100L, usuarioID = userId, publicacionID = publicacionFavoritaId))

        coEvery { sessionManager.getUserId() } returns userId
        coEvery { publicacionRepository.listaPublicaciones() } returns Result.Success(todasLasPublicaciones)
        coEvery { favoritoRepository.obtenerPorUsuario(userId) } returns Result.Success(listaDeFavoritos)
        coEvery { reaccionRepository.obtenerLikesPorUsuario(userId) } returns Result.Success(emptyList())

        // 2. Act
        viewModel.loadFavoritos()
        testDispatcher.scheduler.advanceUntilIdle()

        // 3. Assert
        val state = viewModel.uiState.value
        state.isLoading shouldBe false
        state.isSuccess shouldBe true
        state.favoritos shouldHaveSize 1 // Solo debe haber una publicación
        state.favoritos.first().publicacionID shouldBe publicacionFavoritaId
    }

    @Test
    fun `loadFavoritos sin publicaciones favoritas debe devolver una lista vacía`() = runTest {
        // 1. Arrange
        val userId = 1L
        val publicaciones = listOf(Publicacion(publicacionID = 10L, titulo = "Publi 1", contenido = "", tipo = "DEBATE", autorID = 2))

        coEvery { sessionManager.getUserId() } returns userId
        coEvery { publicacionRepository.listaPublicaciones() } returns Result.Success(publicaciones)
        coEvery { favoritoRepository.obtenerPorUsuario(userId) } returns Result.Success(emptyList()) // El usuario no tiene favoritos

        // 2. Act
        viewModel.loadFavoritos()
        testDispatcher.scheduler.advanceUntilIdle()

        // 3. Assert
        val state = viewModel.uiState.value
        state.isLoading shouldBe false
        state.isSuccess shouldBe true
        state.favoritos.shouldBeEmpty()
    }

    @Test
    fun `toggleFavorito para quitar un favorito debe eliminarlo de la lista de la UI`() = runTest {
        // 1. Arrange
        val userId = 1L
        val publicacionFavoritaId = 10L
        val favoritoId = 100L

        val publicacionFavorita = Publicacion(
            publicacionID = publicacionFavoritaId,
            titulo = "Publi Favorita",
            contenido = "",
            tipo = "DEBATE",
            autorID = 2,
            isFavorited = true, // Marcada como favorita
            favoritoID = favoritoId
        )
        val todasLasPublicaciones = listOf(publicacionFavorita)
        val listaDeFavoritos = listOf(Favorito(favoritoID = favoritoId, usuarioID = userId, publicacionID = publicacionFavoritaId))

        coEvery { sessionManager.getUserId() } returns userId
        coEvery { publicacionRepository.listaPublicaciones() } returns Result.Success(todasLasPublicaciones)
        coEvery { favoritoRepository.obtenerPorUsuario(userId) } returns Result.Success(listaDeFavoritos)

        // Carga inicial
        viewModel.loadFavoritos()
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.uiState.value.favoritos shouldHaveSize 1

        // Simular éxito al eliminar
        coEvery { favoritoRepository.eliminar(favoritoId) } returns Result.Success(Unit)

        // 2. Act
        viewModel.toggleFavorito(publicacionFavorita)
        testDispatcher.scheduler.advanceUntilIdle()

        // 3. Assert
        val finalState = viewModel.uiState.value
        finalState.isLoading shouldBe false
        finalState.errorMessage shouldBe null
        finalState.favoritos.shouldBeEmpty() // La lista de favoritos en la UI debe quedar vacía
    }
}