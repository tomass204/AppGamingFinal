package com.example.gaminghub.data.model

import com.google.gson.annotations.SerializedName


data class FavoritoWrapper(
    @SerializedName("_embedded")
    val embedded: EmbeddedFavoritos
)


data class EmbeddedFavoritos(
    @SerializedName("favoritoList")
    val favoritos: List<Favorito>
)

data class EntityModelFavorito(
    val content: Favorito
)

data class Favorito(
    val favoritoID: Long? = null,
    val usuarioID: Long,
    val publicacionID: Long,
    val activo: Boolean = true
)
