package com.example.gaminghub.data.network.repository


import com.example.gaminghub.api.FavoritoApi
import com.example.gaminghub.data.model.Favorito
import com.example.gaminghub.utils.Result

class FavoritoRepository(private val favoritoApi: FavoritoApi) {


    suspend fun obtenerPorUsuario(usuarioID: Long): Result<List<Favorito>> {
        return try {
            val response = favoritoApi.obtenerPorUsuario(usuarioID)
            if (response.isSuccessful) {
                Result.Success(response.body()?.embedded?.favoritos ?: emptyList())
            } else {
                Result.Error("Error: ${response.code()}")
            }
        } catch (e: Exception) {
            Result.Error("Excepción: ${e.message}")
        }

    }

    suspend fun guardar(favorito: Favorito): Result<Favorito> {
        return try {
            val response = favoritoApi.guardar( favorito)
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Error: ${response.code()}")
            }
        } catch (e: Exception) {
            Result.Error("Excepción: ${e.message}")
        }

    }

    suspend fun eliminar(favoritoId: Long): Result<Unit> {
        return try {
            val response = favoritoApi.eliminar(favoritoId)
            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                Result.Error("Error: ${response.code()}")
            }
        } catch (e: Exception) {
            Result.Error("Excepción: ${e.message}")
        }

    }




}