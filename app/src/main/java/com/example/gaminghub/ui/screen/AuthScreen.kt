package com.example.gaminghub.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gaminghub.data.model.UserRole
import com.example.gaminghub.data.model.UserSession
import com.example.gaminghub.ui.navigation.Routes
import com.example.gaminghub.ui.navigation.SessionManager
import com.example.gaminghub.ui.viewmodel.AuthViewModel

@Composable
fun AuthScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    sessionManager: SessionManager
) {
    val uiState by authViewModel.uiState

    val formAccentColor = Color(0xFF03A9F4)

    LaunchedEffect(uiState.successMessage) {
        if (uiState.successMessage != null && uiState.loggedInUserId != null) {
            val userSession = UserSession(
                usuarioID = uiState.loggedInUserId,
                nombre = uiState.loggedInUsername ?: "",
                email = uiState.loggedInEmail ?: "",
                rol = uiState.loggedInUserRole?.name ?: UserRole.USUARIO_BASICO.name
            )
            sessionManager.saveUserSession(userSession)
            navController.navigate(Routes.MAIN_SCREEN) {
                popUpTo(Routes.AUTH_SCREEN) { inclusive = true }
            }
        }
    }

    LaunchedEffect(uiState.successMessage) {
        if (uiState.successMessage != null && uiState.showRegisterForm) {
            navController.navigate(Routes.REGISTRATION_SUCCESS_SCREEN) {
                popUpTo(Routes.AUTH_SCREEN) { inclusive = false }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(bottom = 40.dp)
        ) {
            val gameLetterStyle = MaterialTheme.typography.displaySmall.copy(
                color = formAccentColor,
                fontWeight = FontWeight.ExtraBold
            )
            val hubStyle = MaterialTheme.typography.headlineLarge.copy(
                color = formAccentColor.copy(alpha = 0.95f),
                fontWeight = FontWeight.Bold
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                Text("G", style = gameLetterStyle, modifier = Modifier.align(Alignment.TopCenter).offset(x = (-75).dp))
                Text("A", style = gameLetterStyle, modifier = Modifier.align(Alignment.TopCenter).offset(x = (-45).dp, y = 30.dp))
                Text("M", style = gameLetterStyle, modifier = Modifier.align(Alignment.Center).offset(x = (-5).dp, y = (-15).dp))
                Text("I", style = gameLetterStyle.copy(fontSize = MaterialTheme.typography.displaySmall.fontSize * 0.8f),
                    modifier = Modifier.align(Alignment.Center).offset(x = 30.dp, y = 15.dp))
                Text("N", style = gameLetterStyle, modifier = Modifier.align(Alignment.BottomCenter).offset(x = 55.dp, y = (-35).dp))
                Text("G", style = gameLetterStyle, modifier = Modifier.align(Alignment.BottomCenter).offset(x = 90.dp))
            }
            Text(text = "HUB", style = hubStyle)
        }

        if (uiState.isLoading) {
            CircularProgressIndicator(color = formAccentColor)
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (uiState.showRegisterForm) {
            RegisterFormScreen(
                authUiState = uiState,
                onUsernameChange = authViewModel::onUsernameChange,
                onEmailChange = authViewModel::onEmailChange,
                onPasswordChange = authViewModel::onPasswordChange,
                onConfirmPasswordChange = authViewModel::onConfirmPasswordChange,
                onRoleSelected = authViewModel::onRoleSelected,
                onRegisterClick = authViewModel::attemptRegistration,
                onSwitchToLogin = authViewModel::toggleLogin,
                modifier = Modifier.fillMaxWidth(),
                accentColor = formAccentColor
            )
        }else if (uiState.showRecoveryPasswordForm){
            RecoveryPasswordScreen(
                authUiState = uiState,
                onEmailChange = authViewModel::onEmailChange,
                onRecoveryClick = authViewModel::attemptRecovery,
                onSwitchToLogin = authViewModel::toggleLogin,
                modifier = Modifier.fillMaxWidth(),
                accentColor = formAccentColor
            )
        }else {
            LoginFormScreen(
                authUiState = uiState,
                onEmailChange = authViewModel::onEmailChange,
                onPasswordChange = authViewModel::onPasswordChange,
                onLoginClick = authViewModel::attemptLogin,
                onSwitchToRegister = authViewModel::toggleFormRegister,
                onSwitchToRecoveryPassword = authViewModel::toggleRecoveryPassword,
                modifier = Modifier.fillMaxWidth(),
                accentColor = formAccentColor
            )
        }
    }
}
