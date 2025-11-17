package com.example.gaminghub.data.network.repository

import com.example.gaminghub.data.model.Imagen
import com.example.gaminghub.data.network.api.ImagenApi
import com.example.gaminghub.utils.Result

class ImagenRepository(private val imagenApi: ImagenApi) {

    suspend fun buscarPorEntidad(entidadID: Long, tipoEntidad: String): Result<List<Imagen>> {
        return try {
            val response = imagenApi.buscarPorEntidad(entidadID, tipoEntidad)
            if (response.isSuccessful) {
                Result.Success(response.body() ?: emptyList())
            } else {
                Result.Error("Error: ${response.code()}")
            }
        } catch (e: Exception) {
            Result.Error("Excepción: ${e.message}")
        }
    }


    suspend fun obtenerImagenPorId(id: Long): Result<ByteArray> {
        return try {
            val response = imagenApi.obtenerImagenPorId(id)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val bytes = body.bytes()
                    Result.Success(bytes)
                } else {
                    Result.Error("Imagen vacía")
                }
            } else {
                Result.Error("Error: ${response.code()}")
            }
        } catch (e: Exception) {
            Result.Error("Excepción: ${e.message}")
        }
    }



}