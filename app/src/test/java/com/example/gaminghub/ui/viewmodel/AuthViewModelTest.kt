package com.example.gaminghub.ui.viewmodel

import com.example.gaminghub.data.model.LoginResponse
import com.example.gaminghub.data.network.repository.UsuarioRepository
import com.example.gaminghub.ui.navigation.SessionManager
import com.example.gaminghub.utils.Result
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class AuthViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: UsuarioRepository
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: AuthViewModel

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        sessionManager = mockk(relaxed = true)
        viewModel = AuthViewModel(repository, sessionManager)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `attemptLogin con éxito debería actualizar el estado a shouldRedirect`() = runTest {
        val loginResponse = LoginResponse(1, "Test", "test@test.com", "ADMIN", "OK", "token")
        coEvery { repository.login(any(), any()) } returns Result.Success(loginResponse)

        viewModel.onEmailChange("test@test.com")
        viewModel.onPasswordChange("password")
        viewModel.attemptLogin()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        state.shouldRedirect shouldBe true
        state.errorMessage shouldBe null
        coVerify { sessionManager.saveUserSession(any()) }
    }

    @Test
    fun `attemptLogin con error debería mostrar un mensaje de error`() = runTest {
        val errorMessage = "Credenciales incorrectas"
        coEvery { repository.login(any(), any()) } returns Result.Error(errorMessage)

        viewModel.onEmailChange("test@test.com")
        viewModel.onPasswordChange("password")
        viewModel.attemptLogin()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        state.shouldRedirect shouldBe false
        state.errorMessage shouldBe errorMessage
    }
}