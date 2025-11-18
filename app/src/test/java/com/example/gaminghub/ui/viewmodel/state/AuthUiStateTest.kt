package com.example.gaminghub.ui.viewmodel.state

import io.kotest.matchers.shouldBe
import org.junit.Test // <-- ¡¡LA LÍNEA MÁS IMPORTANTE!!

class AuthUiStateTest {

    @Test
    fun `el estado inicial debe tener las validaciones en falso`() {
        val state = AuthUiState()
        state.isPasswordLengthValid shouldBe false
        state.hasPasswordUppercase shouldBe false
        state.hasPasswordSpecialChar shouldBe false
    }

    @Test
    fun `se puede crear un estado con validaciones verdaderas`() {
        val state = AuthUiState(
            isPasswordLengthValid = true,
            hasPasswordUppercase = true,
            hasPasswordSpecialChar = true
        )
        state.isPasswordLengthValid shouldBe true
        state.hasPasswordUppercase shouldBe true
        state.hasPasswordSpecialChar shouldBe true
    }
}