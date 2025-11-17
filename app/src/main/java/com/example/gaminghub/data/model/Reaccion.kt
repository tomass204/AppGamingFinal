package com.example.gaminghub.data.model

import com.google.gson.annotations.SerializedName


data class ReaccionWrapper(
    @SerializedName("_embedded")
    val embedded: EmbeddedReacciones
)

data class EmbeddedReacciones(
    @SerializedName("reaccionList")
    val reacciones: List<Reaccion>
)

data class Reaccion(
    val reaccionID: Long? = null,
    val usuarioID: Long,
    val tipoEntidad: String,
    val entidadID: Long,
    val tipoReaccion: String
)
