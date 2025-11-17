package com.example.gaminghub.ui.viewmodel

import com.example.gaminghub.data.model.Favorito
import com.example.gaminghub.data.model.Publicacion
import com.example.gaminghub.data.model.Reaccion
import com.example.gaminghub.data.network.repository.FavoritoRepository
import com.example.gaminghub.data.network.repository.PublicacionRepository
import com.example.gaminghub.data.network.repository.ReaccionRepository
import com.example.gaminghub.ui.navigation.SessionManager
import com.example.gaminghub.utils.Result
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
class DebateViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var publicacionRepository: PublicacionRepository
    private lateinit var reaccionRepository: ReaccionRepository
    private lateinit var favoritoRepository: FavoritoRepository
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: DebateViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        publicacionRepository = mockk(relaxed = true)
        reaccionRepository = mockk(relaxed = true)
        favoritoRepository = mockk(relaxed = true)
        sessionManager = mockk(relaxed = true)

        viewModel = DebateViewModel(
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
    
    // ... (los tests 'loadDebates' se mantienen igual)
    @Test
    fun `loadDebates con éxito debe filtrar por tipo DEBATE y mapear likes y favoritos`() = runTest {
        // 1. Arrange
        val userId = 1L
        val debateId = 10L
        val publicacionNoticiaId = 11L

        val publicaciones = listOf(
            Publicacion(publicacionID = debateId, titulo = "Debate 1", contenido = "", tipo = "DEBATE", autorID = 2),
            Publicacion(publicacionID = publicacionNoticiaId, titulo = "Noticia 1", contenido = "", tipo = "NOTICIA", autorID = 2)
        )
        val favoritos = listOf(Favorito(favoritoID = 100L, usuarioID = userId, publicacionID = debateId))
        val likes = listOf(Reaccion(reaccionID = 200L, usuarioID = userId, tipoEntidad = "publicacion", entidadID = debateId, tipoReaccion = "like"))

        coEvery { sessionManager.getUserId() } returns userId
        coEvery { publicacionRepository.listaPublicaciones() } returns Result.Success(publicaciones)
        coEvery { favoritoRepository.obtenerPorUsuario(userId) } returns Result.Success(favoritos)
        coEvery { reaccionRepository.obtenerLikesPorUsuario(userId) } returns Result.Success(likes)

        // 2. Act
        viewModel.loadDebates()
        testDispatcher.scheduler.advanceUntilIdle()

        // 3. Assert
        val state = viewModel.uiState.value
        state.isLoading shouldBe false
        state.isSuccess shouldBe true
        state.debates shouldHaveSize 1
        
        val debate = state.debates.first()
        debate.publicacionID shouldBe debateId
        debate.isLiked shouldBe true
        debate.isFavorited shouldBe true
        debate.likeID shouldBe 200L
        debate.favoritoID shouldBe 100L
    }

    @Test
    fun `loadDebates con error al cargar publicaciones debe mostrar un mensaje de error`() = runTest {
        // ...
    }

    @Test
    fun `toggleLike en un debate sin like debe añadir un like y tener éxito`() = runTest {
        // 1. Arrange
        val userId = 1L
        val debateId = 10L
        val debateInicial = Publicacion(
            publicacionID = debateId, titulo = "Debate", contenido = "", tipo = "DEBATE", autorID = 2, isLiked = false
        )
        // Estado inicial
        coEvery { sessionManager.getUserId() } returns userId
        coEvery { publicacionRepository.listaPublicaciones() } returns Result.Success(listOf(debateInicial))
        coEvery { favoritoRepository.obtenerPorUsuario(any()) } returns Result.Success(emptyList())
        coEvery { reaccionRepository.obtenerLikesPorUsuario(any()) } returns Result.Success(emptyList())
        
        viewModel.loadDebates()
        testDispatcher.scheduler.advanceUntilIdle()

        val reaccionGuardada = Reaccion(reaccionID = 300L, usuarioID = userId, tipoEntidad = "publicacion", entidadID = debateId, tipoReaccion = "like")
        coEvery { reaccionRepository.guardar(any()) } returns Result.Success(reaccionGuardada)

        // 2. Act
        viewModel.toggleLike(viewModel.uiState.value.debates.first())

        // 3. Assert (¡CORREGIDO!)
        // AVANZAMOS EL RELOJ para que la actualización optimista se aplique
        testDispatcher.scheduler.advanceUntilIdle() 
        
        // AHORA SÍ, verificamos el estado después de la recomposición inicial
        viewModel.uiState.value.debates.first().isLiked shouldBe true

        // SEGUNDA VERIFICACIÓN: El estado final, después de que el repositorio haya respondido
        val finalState = viewModel.uiState.value
        finalState.errorMessage shouldBe null
        val debateFinal = finalState.debates.first()
        debateFinal.isLiked shouldBe true
        debateFinal.likeID shouldBe 300L
    }

    @Test
    fun `toggleLike con fallo en el repositorio debe revertir el estado del like`() = runTest {
        // ...
    }
}