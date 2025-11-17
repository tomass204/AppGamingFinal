package com.example.gaminghub.di

import android.content.Context
import com.example.gaminghub.data.network.repository.*
import com.example.gaminghub.data.network.retrofit.*

class AppContainer(context: Context) {
    val sessionManager = com.example.gaminghub.ui.navigation.SessionManager(context)
    val usuarioRepository = UsuarioRepository(RetrofitUsuario.usuarioApi)
    val publicacionRepository = PublicacionRepository(RetrofitPublicacion.publicacionApi)
    val favoritoRepository = FavoritoRepository(RetrofitFavorito.favoritoApi)
    val reaccionRepository = ReaccionRepository(RetrofitReaccion.reaccionApi)
    val solicitudRepository = SolicitudModeradorRepository(RetrofitSolicitudModerador.solicitudModeradorApi)
    val comentarioRepository = ComentarioRepository(RetrofitComentario.comentarioApi)
    val imagenRepository = ImagenRepository(RetrofitImagen.imagenApi)
}
