package com.example.gaminghub.api

import com.example.gaminghub.data.model.Favorito
import com.example.gaminghub.data.model.FavoritoWrapper
import retrofit2.Response
import retrofit2.http.*

interface FavoritoApi {

    @POST("guardar")
    suspend fun guardar(@Body favorito: Favorito): Response<Favorito>
    @DELETE("{id}")
    suspend fun eliminar(@Path("id") id: Long): Response<Unit>

    @GET("{usuarioID}")
    suspend fun obtenerPorUsuario(@Path("usuarioID") usuarioID: Long): Response<FavoritoWrapper>
}
