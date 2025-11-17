package com.example.gaminghub.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.gaminghub.ui.navigation.SessionManager

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PermanentWelcomeScreen(
    sessionManager: SessionManager
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        visible = true
    }

    val vibrantCeleste = Color(0xFF039BE5)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(
                animationSpec = spring(
                    dampingRatio = 0.6f,
                    stiffness = 100f
                )
            ) { it / 2 } + fadeIn()
        ) {
            Text(
                text = "¡Bienvenido, ${sessionManager.getUserName()}!",
                style = MaterialTheme.typography.displaySmall,
                color = vibrantCeleste,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(
                animationSpec = spring(dampingRatio = 0.6f, stiffness = 100f),
                initialOffsetY = { it }) + fadeIn(animationSpec = tween(delayMillis = 200))
        ) {
            Text(
                text = "Espero que te diviertas en GamingHub.",
                style = MaterialTheme.typography.titleLarge,
                color = vibrantCeleste.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
        }

        Divider(
            modifier = Modifier
                .padding(vertical = 32.dp)
                .width(120.dp), color = vibrantCeleste.copy(alpha = 0.5f)
        )

        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(
                animationSpec = spring(dampingRatio = 0.6f, stiffness = 100f),
                initialOffsetY = { it }) + fadeIn(animationSpec = tween(delayMillis = 400))
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "¿Encontraste un error? ¡Comunícate con nosotros!",
                    style = MaterialTheme.typography.titleMedium,
                    color = vibrantCeleste,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(24.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.PhotoCamera,
                        contentDescription = "Instagram",
                        tint = vibrantCeleste,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "gaminhub_oficial",
                        color = vibrantCeleste,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Email,
                        contentDescription = "Email",
                        tint = vibrantCeleste,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "gaminghuboficial@gmail.com",
                        color = vibrantCeleste,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}
