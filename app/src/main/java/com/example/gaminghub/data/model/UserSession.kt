package com.example.gaminghub.data.model

data class UserSession(
    val usuarioID: Long?,
    val nombre: String,
    val email: String,
    val rol: String
) {
    val userRoleEnum: UserRole
        get() = when (rol.uppercase()) {
            "CREADOR_DE_CONTENIDO" -> UserRole.CREADOR_DE_CONTENIDO
            "MODERADOR" -> UserRole.MODERADOR
            "PROPIETARIO" -> UserRole.PROPIETARIO
            else -> UserRole.USUARIO_BASICO
        }
}

