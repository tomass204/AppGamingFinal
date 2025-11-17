package com.example.gaminghub.ui.screen.drawer

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.gaminghub.ui.navigation.ScreenSections
import com.example.gaminghub.ui.navigation.SessionManager
import com.example.gaminghub.ui.screen.PermanentWelcomeScreen
import com.example.gaminghub.ui.viewmodel.AuthViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


@Composable
fun DrawerNavHost(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    sessionManager: SessionManager,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = ScreenSections.Home.route,
        modifier = modifier
    ) {
        composable(ScreenSections.Home.route) {
            PermanentWelcomeScreen(
                sessionManager = sessionManager
            )
        }
    }
}
