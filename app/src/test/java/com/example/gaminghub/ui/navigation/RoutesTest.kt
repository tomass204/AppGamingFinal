package com.example.gaminghub.ui.navigation

import io.kotest.matchers.shouldBe
import org.junit.Test

class RoutesTest {

    @Test
    fun `createCommentsRoute debe construir la ruta correctamente`() {
        // 1. Arrange
        val contentId = 123L
        val expectedRoute = "comments_screen/123"

        // 2. Act
        val resultRoute = Routes.createCommentsRoute(contentId)

        // 3. Assert
        resultRoute shouldBe expectedRoute
    }

    @Test
    fun `createCommentsRoute con id cero debe construir la ruta correctamente`() {
        // 1. Arrange
        val contentId = 0L
        val expectedRoute = "comments_screen/0"

        // 2. Act
        val resultRoute = Routes.createCommentsRoute(contentId)

        // 3. Assert
        resultRoute shouldBe expectedRoute
    }
}