package com.example.gaminghub.data.network.repository

import com.example.gaminghub.data.model.LoginResponse
import com.example.gaminghub.data.model.Usuario
import com.example.gaminghub.data.network.api.UsuarioApi
import com.example.gaminghub.utils.Result
import retrofit2.http.Body
import retrofit2.http.Path


class UsuarioRepository(private val usuarioApi: UsuarioApi) {

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val response = usuarioApi.login(email, password)
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Error: ${response.code()}")
            }
        } catch (e: Exception) {
            Result.Error("Excepción: ${e.message}")
        }
    }


    suspend fun crear(usuario: Usuario): Result<Usuario> {
        return try {
            val response = usuarioApi.crear( usuario)
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Error: ${response.code()}")
            }
        } catch (e: Exception) {
            Result.Error("Excepción: ${e.message}")
        }
    }


    suspend fun cambiarContrasena(nuevaContrasena: String, usuarioID: Long): Result<Usuario> {
        return try {
            val response = usuarioApi.cambiarContrasena(usuarioID, nuevaContrasena)
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Error: ${response.code()}")
            }
        } catch (e: Exception) {
            Result.Error("Excepción: ${e.message}")
        }

    }

    suspend fun recuperarContrasena(email: String): Result<Usuario> {
        return try {
            val response = usuarioApi.recuperarContrasena(email)
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