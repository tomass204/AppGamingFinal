package com.example.gaminghub.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.asImageBitmap
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Test

class ImageUtilsTest {

    @Before
    fun setUp() {
        mockkStatic(BitmapFactory::class)
        mockkStatic("androidx.compose.ui.graphics.ImageBitmapKt")
    }

    @After
    fun tearDown() {
        unmockkStatic(BitmapFactory::class)
        unmockkStatic("androidx.compose.ui.graphics.ImageBitmapKt")
    }

    @Test
    fun `toBitmap debe llamar a decodeByteArray y luego a asImageBitmap`() {
        // 1. Arrange
        val byteArrayDePrueba = "datos-falsos".toByteArray()
        val mockBitmap = mockk<Bitmap>()

        // Cuando se llame a decodeByteArray, le decimos que devuelva nuestro bitmap falso.
        every { BitmapFactory.decodeByteArray(any(), any(), any()) } returns mockBitmap
        // Hacemos que la llamada a asImageBitmap sobre nuestro mock devuelva un resultado falso.
        every { mockBitmap.asImageBitmap() } returns mockk()

        // 2. Act
        byteArrayDePrueba.toBitmap()

        // 3. Assert: Verificamos que las funciones se llamaron en el orden correcto.
        verifyOrder {
            BitmapFactory.decodeByteArray(byteArrayDePrueba, 0, byteArrayDePrueba.size)
            mockBitmap.asImageBitmap()
        }
    }
}