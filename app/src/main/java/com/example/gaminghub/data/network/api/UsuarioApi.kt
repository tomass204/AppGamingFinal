package com.example.gaminghub.data.network.api

import com.example.gaminghub.data.model.LoginResponse
import com.example.gaminghub.data.model.Usuario
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface UsuarioApi {

    @POST("Usuario/iniciar-session")
    suspend fun login(
        @Query("email") email: String,
        @Query("contrasena") contrasena: String
    ): Response<LoginResponse>

    @POST("Usuario")
    suspend fun crear(@Body usuario: Usuario): Response<Usuario>


    @PUT("Usuario/cambiar-contrasena/{usuarioID}")
    suspend fun cambiarContrasena(
        @Path("usuarioID") usuarioID: Long,
        @Query("nuevaContrasena") nuevaContrasena: String
    ): Response<Usuario>

    @PUT("Usuario/recuperar-contrasena")
    suspend fun recuperarContrasena(
        @Query("email") email: String
    ): Response<Usuario>

}