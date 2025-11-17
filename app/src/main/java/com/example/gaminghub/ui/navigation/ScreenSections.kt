package com.example.gaminghub.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

enum class ScreenSections(val route: String, val title: String, val icon: ImageVector) {
    Home("home", "Inicio", Icons.Filled.Home),
    News("news", "Noticias", Icons.AutoMirrored.Filled.Article),
    Debates("debates", "Debates", Icons.Filled.ChatBubble),
    Favoritos("favoritos", "Favoritos", Icons.Filled.Favorite),
    Perfil("perfil", "Perfil", Icons.Filled.Person),
    Solicitudes(Routes.REQUESTS_SCREEN, "Solicitudes de moderador", Icons.Filled.HowToReg),
    Logout("logout", "Cerrar sesión", Icons.Filled.ExitToApp)
}