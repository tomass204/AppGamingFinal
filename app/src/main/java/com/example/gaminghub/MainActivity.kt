package com.example.gaminghub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gaminghub.di.AppContainer
import com.example.gaminghub.ui.navigation.Routes
import com.example.gaminghub.ui.screen.AuthScreen
import com.example.gaminghub.ui.screen.EnhancedSplashScreen
import com.example.gaminghub.ui.screen.MainScreenWithDrawer
import com.example.gaminghub.ui.screen.RecoverySuccessScreen
import com.example.gaminghub.ui.screen.RegistrationSuccessScreen
import com.example.gaminghub.ui.theme.GamingHubTheme
import com.example.gaminghub.ui.viewmodel.AuthViewModel
import com.example.gaminghub.ui.viewmodel.AuthViewModelFactory
import com.jakewharton.threetenabp.AndroidThreeTen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidThreeTen.init(this)
        enableEdgeToEdge()

        setContent {

            val navController = rememberNavController()
            val appContainer = remember { AppContainer(this) }

            val authViewModel: AuthViewModel = viewModel(
                factory = AuthViewModelFactory(appContainer.usuarioRepository, appContainer.sessionManager)
            )

            GamingHubTheme {

                var showSplashScreen by remember { mutableStateOf(true) }

                if (showSplashScreen) {
                    EnhancedSplashScreen(
                        onTimeout = { showSplashScreen = false }
                    )
                } else {
                    NavHost(
                        navController = navController,
                        startDestination = Routes.AUTH_SCREEN
                    ) {
                        composable(Routes.AUTH_SCREEN) {
                            AuthScreen(
                                navController = navController,
                                authViewModel = authViewModel,
                                sessionManager = appContainer.sessionManager
                            )
                        }
                        composable(Routes.REGISTRATION_SUCCESS_SCREEN) {
                            RegistrationSuccessScreen(
                                navController = navController,
                                authViewModel = authViewModel
                            )
                        }

                        composable(Routes.RECOVERY_SUCCESS_SCREEN) {
                            RecoverySuccessScreen(
                                navController = navController,
                                authViewModel = authViewModel
                            )
                        }
                        composable(Routes.MAIN_SCREEN) {
                            MainScreenWithDrawer(
                                appNavController = navController,
                                authViewModel = authViewModel,
                                appContainer = appContainer
                            )
                        }


                    }

                    LaunchedEffect(authViewModel.uiState.value.shouldRedirect) {
                        val uiState = authViewModel.uiState.value
                        if (uiState.shouldRedirect) {
                            when {
                                uiState.registrationResultMessage != null -> {
                                    navController.navigate(Routes.REGISTRATION_SUCCESS_SCREEN) {
                                        popUpTo(Routes.AUTH_SCREEN)
                                    }
                                }
                                uiState.recoverResultMessage != null -> {
                                    navController.navigate(Routes.RECOVERY_SUCCESS_SCREEN) {
                                        popUpTo(Routes.AUTH_SCREEN)
                                    }
                                }
                                uiState.loggedInUserId != null -> {
                                    navController.navigate(Routes.MAIN_SCREEN) {
                                        popUpTo(Routes.AUTH_SCREEN) { inclusive = true }
                                    }
                                }
                            }
                            authViewModel.clearMessages()
                        }
                    }
                }
            }
        }
    }
}
