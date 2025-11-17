package com.example.gaminghub.data.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("usuarioID")
    val usuarioId: Long?,

    @SerializedName("nombre")
    val nombre: String?,

    @SerializedName("email")
    val email: String?,

    @SerializedName("token")
    val token: String?,

    @SerializedName("message")
    val message: String?,

    @SerializedName("rol")
    val rol: String?
)