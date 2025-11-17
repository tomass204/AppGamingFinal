package com.example.gaminghub.data.network.repository


import com.example.gaminghub.api.ReaccionApi
import com.example.gaminghub.data.model.Reaccion
import com.example.gaminghub.utils.Result

class ReaccionRepository(private val reaccionApi: ReaccionApi) {

    suspend fun guardar(reaccion: Reaccion): Result<Reaccion> {
        return try {
            val response = reaccionApi.crear( reaccion)
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Error: ${response.code()}")
            }
        } catch (e: Exception) {
            Result.Error("Excepción: ${e.message}")
        }

    }

    suspend fun eliminar(reaccionId: Long): Result<Unit> {
        return try {
            val response = reaccionApi.eliminar(reaccionId)
            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                Result.Error("Error: ${response.code()}")
            }
        } catch (e: Exception) {
            Result.Error("Excepción: ${e.message}")
        }

    }

    suspend fun contarReaccionesPublicacion(publicacionId: Long): Result<Long> {
        return try {
            val response = reaccionApi.contarReaccionesPublicacion( publicacionId)
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Error: ${response.code()}")
            }
        } catch (e: Exception) {
            Result.Error("Excepción: ${e.message}")
        }

    }

    suspend fun contarReaccionesComentario(comentarioId: Long): Result<Long> {
        return try {
            val response = reaccionApi.contarReaccionesComentario( comentarioId)
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Error: ${response.code()}")
            }
        } catch (e: Exception) {
            Result.Error("Excepción: ${e.message}")
        }

    }

    suspend fun obtenerLikesPorUsuario(usuarioID: Long): Result<List<Reaccion>> {
        return try {
            val response = reaccionApi.listarLikesPorUsuario(usuarioID)
            if (response.isSuccessful) {
                Result.Success(response.body()?.embedded?.reacciones ?: emptyList())
            } else {
                Result.Error("Error: ${response.code()}")
            }
        } catch (e: Exception) {
            Result.Error("Excepción: ${e.message}")
        }

    }




}