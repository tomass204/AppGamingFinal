package com.example.gaminghub.data.model

import com.google.gson.annotations.SerializedName

data class SolicitudModeradorWrapper(
    @SerializedName("_embedded")
    val embedded: EmbeddedSolicitudModerador
)


data class EmbeddedSolicitudModerador(
    @SerializedName("solicitudList")
    val solicitudes: List<SolicitudModerador>
)


data class EntityModelSolicitudModerador(
    val content: SolicitudModerador
)


data class SolicitudModerador(
    @SerializedName("solicitudId")
    val solicitudId: Long? = null,

    @SerializedName("usuarioId")
    val usuarioId: Long? = null,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("motivo")
    val motivo: String,

    @SerializedName("estado")
    val estado: String? = "PENDIENTE",

    @SerializedName("fechaCreacion")
    val fechaCreacion: String? = null,

    @SerializedName("fechaGestion")
    val fechaGestion: String? = null
)

