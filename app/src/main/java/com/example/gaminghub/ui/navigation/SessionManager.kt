package com.example.gaminghub.ui.navigation

import android.content.Context
import android.content.SharedPreferences
import com.example.gaminghub.data.model.UserRole
import com.example.gaminghub.data.model.UserSession

class SessionManager(private val context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("gaminghub_prefs", Context.MODE_PRIVATE)

    private var userSession: UserSession? = null

    fun saveUserSession(user: UserSession) {
        userSession = user
        prefs.edit().apply {
            putLong("usuarioID", user.usuarioID ?: -1)
            putString("nombre", user.nombre)
            putString("email", user.email)
            putString("rol", user.rol)
            apply()
        }
    }

    fun getUserSession(): UserSession? {
        if (userSession == null) {
            val id = prefs.getLong("usuarioID", -1)
            if (id != -1L) {
                userSession = UserSession(
                    usuarioID = id,
                    nombre = prefs.getString("nombre", "") ?: "",
                    email = prefs.getString("email", "") ?: "",
                    rol = prefs.getString("rol", UserRole.USUARIO_BASICO.name) ?: UserRole.USUARIO_BASICO.name
                )
            }
        }
        return userSession
    }

    fun clearSession() {
        userSession = null
        prefs.edit().clear().apply()
    }

    fun getUserId(): Long? = getUserSession()?.usuarioID
    fun getUserName(): String? = getUserSession()?.nombre
    fun getUserEmail(): String? = getUserSession()?.email
    fun getUserRole(): String? = getUserSession()?.rol ?: UserRole.USUARIO_BASICO.name
}
