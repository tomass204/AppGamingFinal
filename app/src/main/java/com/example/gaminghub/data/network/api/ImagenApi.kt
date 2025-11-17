package com.example.gaminghub.data.network.api

import com.example.gaminghub.data.model.Favorito
import com.example.gaminghub.data.model.Imagen
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Streaming

interface ImagenApi {


    @Multipart
    @POST("subir")
    suspend fun subirImagenes(
        @Query("entidadID") entidadId: Long,
        @Query("tipoEntidad") tipoEntidad: Long,
        @Part archivos: List<MultipartBody.Part>
    ): Response<List<Imagen>>

    @GET("buscar-por-entidad")
    suspend fun buscarPorEntidad(
        @Query("entidadID") entidadId: Long,
        @Query("tipoEntidad") tipoEntidad: String
    ): Response<List<Imagen>>

    @GET("{id}")
    @Streaming
    suspend fun obtenerImagenPorId(
        @Path("id") id: Long
    ): Response<ResponseBody>





}