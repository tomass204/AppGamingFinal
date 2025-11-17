package com.example.gaminghub.utils

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

fun ByteArray.toBitmap(): ImageBitmap {
    val bmp = android.graphics.BitmapFactory.decodeByteArray(this, 0, size)
    return bmp.asImageBitmap()
}