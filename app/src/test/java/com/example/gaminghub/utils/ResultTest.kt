package com.example.gaminghub.utils

import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.Test

class ResultTest {

    @Test
    fun `Result Success debe contener los datos correctos`() {
        // 1. Arrange: Creamos un dato de prueba
        val dataDePrueba = "Estos son los datos"

        // 2. Act: Creamos una instancia de Success
        val resultado = Result.Success(dataDePrueba)

        // 3. Assert: Verificamos el tipo y el contenido
        resultado.shouldBeInstanceOf<Result.Success<String>>()
        resultado.data shouldBe dataDePrueba
    }

    @Test
    fun `Result Error debe contener el mensaje de error correcto`() {
        // 1. Arrange
        val mensajeDeError = "Error en la red"

        // 2. Act
        val resultado = Result.Error(mensajeDeError)

        // 3. Assert
        resultado.shouldBeInstanceOf<Result.Error>()
        resultado.message shouldBe mensajeDeError
    }

    @Test
    fun `Result Loading debe ser un objeto singleton`() {
        // 1. Arrange y 2. Act
        val resultado = Result.Loading

        // 3. Assert
        resultado.shouldBeInstanceOf<Result.Loading>()
        // Podemos verificar que dos accesos al singleton son el mismo objeto
        val otroLoading = Result.Loading
        resultado shouldBe otroLoading
    }
}