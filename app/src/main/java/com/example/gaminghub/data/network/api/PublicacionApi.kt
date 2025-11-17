package com.example.gaminghub.data.network.api


import com.example.gaminghub.data.model.Publicacion
import com.example.gaminghub.data.model.PublicacionWrapper
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface PublicacionApi {

    @GET("listar")
    suspend fun listaPublicaciones(): Response<PublicacionWrapper>

    @GET("{id}")
    suspend fun verPublicacion(@Path("id") id: Long): Response<Publicacion>

    @Multipart
    @POST("crear")
    suspend fun crearPublicacion(
        @Part("data") data: RequestBody,
        @Part imagenes: List<MultipartBody.Part>
    ): Response<Publicacion>

    @PUT("{id}")
    suspend fun editarPublicacion(
        @Path("id") id: Long,
        @Body publicacion: Publicacion
    ): Response<Publicacion>

    @DELETE("{id}")
    suspend fun eliminarPublicacion(@Path("id") id: Long): Response<Unit>

}