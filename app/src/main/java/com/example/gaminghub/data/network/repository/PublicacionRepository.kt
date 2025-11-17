package com.example.gaminghub.data.network.repository


import com.example.gaminghub.data.model.Publicacion
import com.example.gaminghub.data.network.api.PublicacionApi
import com.example.gaminghub.utils.Result
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import com.google.gson.Gson
import okhttp3.RequestBody.Companion.toRequestBody


class PublicacionRepository(private val publicacionApi: PublicacionApi) {

    suspend fun listaPublicaciones(): Result<List<Publicacion>> {
        return try {
            val response = publicacionApi.listaPublicaciones()
            if (response.isSuccessful) {
                Result.Success(response.body()?.embedded?.publicaciones ?: emptyList())
            } else {
                Result.Error("Error: ${response.code()}")
            }
        } catch (e: Exception) {
            Result.Error("Excepción: ${e.message}")
        }

    }

    suspend fun crearPublicacion( publicacion: Publicacion,
                                  imagenes: List<MultipartBody.Part>): Result<Publicacion> {
        return try {
            val gson = Gson()
            val json = gson.toJson(publicacion)
            val dataPart = json.toRequestBody("application/json".toMediaType())
            val response = publicacionApi.crearPublicacion( dataPart, imagenes)
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Error: ${response.code()}")
            }
        } catch (e: Exception) {
            Result.Error("Excepción: ${e.message}")
        }

    }

    suspend fun eliminarPublicacion(id: Long): Result<Unit> {
        return try {
            val response = publicacionApi.eliminarPublicacion(id)
            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                Result.Error("Error: ${response.code()}")
            }
        } catch (e: Exception) {
            Result.Error("Excepción: ${e.message}")
        }

    }

    suspend fun editarPublicacion(id: Long, publicacion: Publicacion): Result<Publicacion> {
        return try {
            val response = publicacionApi.editarPublicacion(id, publicacion)
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Error: ${response.code()}")
            }
        } catch (e: Exception) {
            Result.Error("Excepción: ${e.message}")
        }

    }

    suspend fun verPublicacion(id: Long) : Result<Publicacion> {
        return try {
            val response = publicacionApi.verPublicacion(id)
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