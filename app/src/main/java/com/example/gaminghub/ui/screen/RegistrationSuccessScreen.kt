package com.example.gaminghub.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gaminghub.ui.navigation.Routes
import com.example.gaminghub.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun RegistrationSuccessScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    LaunchedEffect(Unit) {
        delay(3500L)
        navController.navigate(Routes.AUTH_SCREEN) {
            popUpTo(Routes.REGISTRATION_SUCCESS_SCREEN) { inclusive = true }
        }
    }
    Box(
        Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(Color(0xFF29B6F6), Color(0xFF0277BD)))
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            authViewModel.uiState.value.registrationResultMessage ?: "¡Registro exitoso!",
            color = Color.White,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
    }
}