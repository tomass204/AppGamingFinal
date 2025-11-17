package com.example.gaminghub.data.network.api

import com.example.gaminghub.data.model.Comentario
import com.example.gaminghub.data.model.ComentarioWrapper
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ComentarioApi {


    @GET("Comentarios/publicacion/{idPublicacion}")
    suspend fun listarComentariosPorPublicacion(
        @Path("idPublicacion") idPublicacion: Long
    ): Response<ComentarioWrapper>

    @POST("Comentarios")
    suspend fun crearComentario(
        @Body comentario: Comentario
    ): Response<Comentario>

    @PUT("Comentarios/{id}")
    suspend fun editarComentario(
        @Path("id") idComentario: Long,
        @Body comentario: Comentario
    ): Response<Comentario>

    @DELETE("Comentarios/{id}")
    suspend fun eliminarComentario(
        @Path("id") idComentario: Long
    ): Response<Unit>

    @GET("Comentarios/{id}")
    suspend fun obtenerComentarioPorId(
        @Path("id") idComentario: Long
    ): Response<Comentario>

}