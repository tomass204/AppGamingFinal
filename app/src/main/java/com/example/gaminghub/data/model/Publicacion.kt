package com.example.gaminghub.data.model

import com.google.gson.annotations.SerializedName



data class PublicacionWrapper(
    @SerializedName("_embedded")
    val embedded: EmbeddedPublicaciones
)

data class EmbeddedPublicaciones(
    @SerializedName("publicacionList")
    val publicaciones: List<Publicacion>
)

data class Publicacion(
    @SerializedName("publicacionID")
    val publicacionID: Long? = null,

    @SerializedName("titulo")
    val titulo: String,

    @SerializedName("contenido")
    val contenido: String,

    @SerializedName("tipo")
    val tipo: String,

    @SerializedName("autorID")
    val autorID: Long,

    @SerializedName("destacada")
    val destacada: Boolean = false,

    @SerializedName("activa")
    val activa: Boolean = true,

    @SerializedName("fechaCreacion")
    val fechaCreacion: String? = null,

    @SerializedName("likesCount")
    val likesCount: Int = 0,

    @SerializedName("comentariosCount")
    val comentariosCount: Int = 0,

    @SerializedName("imagenes")
    val imagenes: List<Imagen>?=emptyList<Imagen>(),

    //extras
    val autor: String?="GamingHub",
    val imageResId: Int?=0,
    val imageUri: String? = null,
    val isLiked: Boolean = false,
    val isFavorited: Boolean = false,
    val favoritoID: Long? = 0,
    val likeID: Long? = 0



)



