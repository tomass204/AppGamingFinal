package com.example.gaminghub.data.model


data class Imagen(
    val imagenID: Long,
    val entidadID: Long,
    val nombre: String,
    val tipoEntidad: String,
    val fechaCreacion: String,
    val activo: Boolean = true
)
