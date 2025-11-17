package com.example.gaminghub.api

import com.example.gaminghub.data.model.Reaccion
import com.example.gaminghub.data.model.ReaccionWrapper
import retrofit2.Response
import retrofit2.http.*

interface ReaccionApi {

    @POST("Reacciones")
    suspend fun crear(@Body reaccion: Reaccion): Response<Reaccion>

    @DELETE("Reacciones/{id}")
    suspend fun eliminar(@Path("id") id: Long): Response<Unit>

    @GET("Reacciones/publicacion/{idPublicacion}")
    suspend fun contarReaccionesPublicacion(@Path("idPublicacion") idPublicacion: Long): Response<Long>

    @GET("Reacciones/comentario/{idComentario}")
    suspend fun contarReaccionesComentario(@Path("idComentario") idComentario: Long): Response<Long>


    @GET("Reacciones/usuario/{usuarioID}")
    suspend fun listarLikesPorUsuario(@Path("usuarioID") usuarioID: Long): Response<ReaccionWrapper>


}
