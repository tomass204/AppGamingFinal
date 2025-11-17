package com.example.gaminghub.data.model

import io.kotest.matchers.shouldBe
import org.junit.Test

class FavoritoTest {

    @Test
    fun `crear una instancia de Favorito debe asignar las propiedades correctamente`() {
        // 1. Arrange: Definimos los datos de prueba
        val favoritoIdPrueba = 1L
        val usuarioIdPrueba = 10L
        val publicacionIdPrueba = 100L
        val activoPrueba = true

        // 2. Act: Creamos el objeto
        val favorito = Favorito(
            favoritoID = favoritoIdPrueba,
            usuarioID = usuarioIdPrueba,
            publicacionID = publicacionIdPrueba,
            activo = activoPrueba
        )

        // 3. Assert: Verificamos que cada propiedad tiene el valor esperado
        favorito.favoritoID shouldBe favoritoIdPrueba
        favorito.usuarioID shouldBe usuarioIdPrueba
        favorito.publicacionID shouldBe publicacionIdPrueba
        favorito.activo shouldBe activoPrueba
    }
}