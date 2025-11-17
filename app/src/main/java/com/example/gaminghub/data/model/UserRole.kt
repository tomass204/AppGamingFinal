package com.example.gaminghub.data.model

import com.example.gaminghub.R

enum class UserRole(val roleName: String, val profileImage: Int) {
    USUARIO_BASICO("Usuario Básico", R.drawable.usuariobasico),
    CREADOR_DE_CONTENIDO("Creador de Contenido", R.drawable.influencer),
    MODERADOR("Moderador", R.drawable.moderador),
    PROPIETARIO("Propietario", R.drawable.propietario)
}
