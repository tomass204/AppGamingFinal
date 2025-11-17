package com.example.gaminghub.ui.screen.drawer

import com.example.gaminghub.data.model.UserRole
import com.example.gaminghub.ui.navigation.ScreenSections


fun getDrawerItems(userRole: String?): List<ScreenSections> {
    val baseDrawerItems = listOf(
        ScreenSections.Home,
        ScreenSections.News,
        ScreenSections.Debates,
        ScreenSections.Favoritos,
        ScreenSections.Perfil,
        ScreenSections.Logout
    )
    return if (userRole == UserRole.PROPIETARIO.name) baseDrawerItems + ScreenSections.Solicitudes
    else baseDrawerItems
}
