package com.example.gaminghub.data.network.repository

import com.example.gaminghub.data.model.Comentario
import com.example.gaminghub.data.network.api.ComentarioApi
import com.example.gaminghub.utils.Result

class ComentarioRepository(private val comentarioApi: ComentarioApi) {


    suspend fun listarComentariosPorPublicacion(publicacionId:Long): Result<List<Comentario>> {
        return try {
            val response = comentarioApi.listarComentariosPorPublicacion(publicacionId)
            if (response.isSuccessful) {
                Result.Success(response.body()?.embedded?.comentarios ?: emptyList())
            } else {
                Result.Error("Error: ${response.code()}")
            }
        } catch (e: Exception) {
            Result.Error("Excepción: ${e.message}")
        }

    }

    suspend fun crearComentario(comentario: Comentario): Result<Comentario> {
        return try {
            val response = comentarioApi.crearComentario( comentario)
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Error: ${response.code()}")
            }
        } catch (e: Exception) {
            Result.Error("Excepción: ${e.message}")
        }

    }

    suspend fun editarComentario(id: Long, comentario: Comentario): Result<Comentario> {
        return try {
            val response = comentarioApi.editarComentario(id, comentario)
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Error: ${response.code()}")
            }
        } catch (e: Exception) {
            Result.Error("Excepción: ${e.message}")
        }

    }

    suspend fun eliminarComentario(id: Long): Result<Unit> {
        return try {
            val response = comentarioApi.eliminarComentario(id)
            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                Result.Error("Error: ${response.code()}")
            }
        } catch (e: Exception) {
            Result.Error("Excepción: ${e.message}")
        }

    }

    suspend fun obtenerComentarioPorId(id: Long) : Result<Comentario> {
        return try {
            val response = comentarioApi.obtenerComentarioPorId(id)
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Error: ${response.code()}")
            }
        } catch (e: Exception) {
            Result.Error("Excepción: ${e.message}")
        }

    }



}