package com.example.gaminghub.data.network.api

import com.example.gaminghub.data.model.EntityModelSolicitudModerador
import com.example.gaminghub.data.model.SolicitudModerador
import com.example.gaminghub.data.model.SolicitudModeradorWrapper
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface  SolicitudModeradorApi {

    @GET("listar")
    suspend fun listarSolicitudes(): Response<SolicitudModeradorWrapper>

    @POST("crear")
    suspend fun crearSolicitud(@Body solicitudModerador: SolicitudModerador): Response<SolicitudModerador>

    @PUT("aprobar/{solicitudId}")
    suspend fun aprobarSolicitud(@Path("solicitudId") solicitudId: Long?): Response<SolicitudModerador>

    @PUT("rechazar/{solicitudId}")
    suspend fun rechazarSolicitud(@Path("solicitudId") solicitudId: Long?): Response<SolicitudModerador>

}