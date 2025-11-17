package com.example.gaminghub.data.model

import com.google.gson.annotations.SerializedName

data class ComentarioWrapper(
    @SerializedName("_embedded")
    val embedded: EmbeddedComentarios
)

data class EmbeddedComentarios(
    @SerializedName("comentarioList")
    val comentarios: List<Comentario>
)


data class Comentario(
    @SerializedName("comentarioID")
    val comentarioID: Long? = null,
    @SerializedName("contenido")
    val contenido: String,
    @SerializedName("autorID")
    val autorID: Long,
    @SerializedName("publicacionID")
    val publicacionID: Long,
    @SerializedName("activo")
    val activo: Boolean = true,
    @SerializedName("fechaCreacion")
    val fechaCreacion: String? = null,
    @SerializedName("likesCount")
    val likesCount: Long?= null,
    @SerializedName("autorNombre")
    val autorNombre: String?= null
)

