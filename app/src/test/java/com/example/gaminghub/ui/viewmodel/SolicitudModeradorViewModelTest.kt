package com.example.gaminghub.ui.viewmodel

import com.example.gaminghub.data.model.SolicitudModerador
import com.example.gaminghub.data.network.repository.SolicitudModeradorRepository
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
class SolicitudModeradorViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: SolicitudModeradorRepository
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: SolicitudModeradorViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
        sessionManager = mockk(relaxed = true)
        viewModel = SolicitudModeradorViewModel(repository, sessionManager)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadPendingRequests con éxito debe filtrar y mostrar solo las solicitudes PENDIENTES`() = runTest {
        // 1. Arrange
        val solicitudes = listOf(
            SolicitudModerador(solicitudId = 1, nombre = "User A", email = "a@a.com", motivo = "", estado = "PENDIENTE"),
            SolicitudModerador(solicitudId = 2, nombre = "User B", email = "b@b.com", motivo = "", estado = "APROBADO"),
            SolicitudModerador(solicitudId = 3, nombre = "User C", email = "c@c.com", motivo = "", estado = "RECHAZADO")
        )
        coEvery { repository.listarSolicitudes() } returns Result.Success(solicitudes)

        // 2. Act
        viewModel.loadPendingRequests()
        testDispatcher.scheduler.advanceUntilIdle()

        // 3. Assert
        val state = viewModel.uiState.value
        state.isLoading shouldBe false
        state.isSuccess shouldBe true
        state.solicitudes shouldHaveSize 1
        state.solicitudes.first().estado shouldBe "PENDIENTE"
        state.solicitudes.first().solicitudId shouldBe 1
    }

    @Test
    fun `processApproval con éxito debe eliminar la solicitud de la lista de la UI`() = runTest {
        // 1. Arrange
        val solicitudPendiente = SolicitudModerador(solicitudId = 1, nombre = "User A", email = "a@a.com", motivo = "", estado = "PENDIENTE")
        val solicitudAprobada = solicitudPendiente.copy(estado = "APROBADO")

        // Carga inicial
        coEvery { repository.listarSolicitudes() } returns Result.Success(listOf(solicitudPendiente))
        viewModel.loadPendingRequests()
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.uiState.value.solicitudes shouldHaveSize 1

        // Simular éxito al aprobar
        coEvery { repository.aprobarSolicitud(solicitudPendiente.solicitudId!!) } returns Result.Success(solicitudAprobada)

        // 2. Act
        viewModel.processApproval(solicitudPendiente, sessionManager)
        testDispatcher.scheduler.advanceUntilIdle()

        // 3. Assert
        val state = viewModel.uiState.value
        state.isLoading shouldBe false
        state.solicitudes.shouldBeEmpty() // La solicitud debe desaparecer de la lista
        state.successMessage shouldBe "Solicitud aprobada correctamente."
    }

    @Test
    fun `crearSolicitud con éxito debe mostrar un mensaje de éxito`() = runTest {
        // 1. Arrange
        val userId = 1L
        val userName = "Test User"
        val userEmail = "test@example.com"
        val motivo = "Quiero ser moderador"
        val solicitudCreada = SolicitudModerador(solicitudId = 10, usuarioId = userId, nombre = userName, email = userEmail, motivo = motivo)

        coEvery { sessionManager.getUserId() } returns userId
        coEvery { sessionManager.getUserName() } returns userName
        coEvery { sessionManager.getUserEmail() } returns userEmail
        coEvery { repository.crearSolicitud(any()) } returns Result.Success(solicitudCreada)

        // 2. Act
        viewModel.crearSolicitud(sessionManager, motivo)
        testDispatcher.scheduler.advanceUntilIdle()

        // 3. Assert
        val state = viewModel.uiState.value
        state.isLoading shouldBe false
        state.successMessage shouldBe "Solicitud creada correctamente."
    }

    @Test
    fun `loadPendingRequests con error en el repositorio debe mostrar un mensaje de error`() = runTest {
        // 1. Arrange
        val errorMessage = "Error en la base de datos"
        coEvery { repository.listarSolicitudes() } returns Result.Error(errorMessage)

        // 2. Act
        viewModel.loadPendingRequests()
        testDispatcher.scheduler.advanceUntilIdle()

        // 3. Assert
        val state = viewModel.uiState.value
        state.isLoading shouldBe false
        state.errorMessage shouldBe errorMessage
        state.solicitudes.shouldBeEmpty()
    }
}