package com.example.gaminghub.data.model

import com.google.gson.annotations.SerializedName

data class Usuario(

    @SerializedName("usuarioId")
    val usuarioID: Long?=null,
    val nombre: String,
    val email: String,
    val contrasena: String,
    val rol: String,
    val activo: Boolean?=true
) {
    val userRoleEnum: UserRole
        get() = when (rol.uppercase()) {
            "CREADOR DE CONTENIDO" -> UserRole.CREADOR_DE_CONTENIDO
            "MODERADOR" -> UserRole.MODERADOR
            "PROPIETARIO" -> UserRole.PROPIETARIO
            else -> UserRole.USUARIO_BASICO
        }
}
