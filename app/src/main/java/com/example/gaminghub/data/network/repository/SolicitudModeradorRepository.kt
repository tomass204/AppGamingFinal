package com.example.gaminghub.data.network.repository

import com.example.gaminghub.data.model.SolicitudModerador
import com.example.gaminghub.data.network.api.SolicitudModeradorApi
import com.example.gaminghub.utils.Result


class SolicitudModeradorRepository(private val solicitudModeradorApi: SolicitudModeradorApi) {

    suspend fun listarSolicitudes(): Result<List<SolicitudModerador>> {
        return try {
            val response = solicitudModeradorApi.listarSolicitudes()
            if (response.isSuccessful) {
                Result.Success(response.body()?.embedded?.solicitudes ?: emptyList())
            } else {
                Result.Error("Error: ${response.code()}")
            }
        } catch (e: Exception) {
            Result.Error("Excepción: ${e.message}")
        }
    }


    suspend fun crearSolicitud(solicitudModerador: SolicitudModerador): Result<SolicitudModerador> {
        return try {
            val response = solicitudModeradorApi.crearSolicitud(solicitudModerador)
            if (response.isSuccessful) {
                Result.Success(response.body()!!)

            } else {
                Result.Error("Error: ${response.code()}")
            }
        } catch (e: Exception) {
            Result.Error("Excepción: ${e.message}")
        }
    }

    suspend fun aprobarSolicitud(solicitudId: Long): Result<SolicitudModerador> {
        return try {
            val response = solicitudModeradorApi.aprobarSolicitud(solicitudId)
            if (response.isSuccessful) {
                Result.Success(response.body()!!)

            } else {
                Result.Error("Error: ${response.code()}")
            }
        } catch (e: Exception) {
            Result.Error("Excepción: ${e.message}")
        }
    }


    suspend fun rechazarSolicitud(solicitudId: Long): Result<SolicitudModerador> {
        return try {
            val response = solicitudModeradorApi.rechazarSolicitud(solicitudId)
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