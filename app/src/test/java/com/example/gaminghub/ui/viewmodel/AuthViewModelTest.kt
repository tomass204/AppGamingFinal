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
import org.junit.After      // <-- CORREGIDO a JUnit 4
import org.junit.Before     // <-- CORREGIDO a JUnit 4
import org.junit.Test       // <-- CORREGIDO a JUnit 4

@ExperimentalCoroutinesApi
class AuthViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: UsuarioRepository
    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: AuthViewModel

    @Before // <-- CORREGIDO a JUnit 4
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        sessionManager = mockk(relaxed = true)
        viewModel = AuthViewModel(repository, sessionManager)
    }

    @After // <-- CORREGIDO a JUnit 4
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `attemptLogin con éxito debería actualizar el estado a shouldRedirect`() = runTest {
        // Arrange
        val loginResponse = LoginResponse(
            usuarioId = 1L,
            nombre = "Test",
            email = "test@test.com",
            rol = "ADMIN",
            message = "OK",
            token = "token"
        )
        coEvery { repository.login(any(), any()) } returns Result.Success(loginResponse)

        // Act
        viewModel.onEmailChange("test@test.com")
        viewModel.onPasswordChange("password")
        viewModel.attemptLogin()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        state.shouldRedirect shouldBe true
        state.errorMessage shouldBe null
        coVerify { sessionManager.saveUserSession(any()) }
    }

    @Test
    fun `attemptLogin con error debería mostrar un mensaje de error`() = runTest {
        // Arrange
        val errorMessage = "Credenciales incorrectas"
        coEvery { repository.login(any(), any()) } returns Result.Error(errorMessage)

        // Act
        viewModel.onEmailChange("test@test.com")
        viewModel.onPasswordChange("password")
        viewModel.attemptLogin()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        state.shouldRedirect shouldBe false
        state.errorMessage shouldBe errorMessage
    }
}