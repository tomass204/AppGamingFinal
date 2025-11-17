package com.example.gaminghub.ui.screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.gaminghub.ui.navigation.SessionManager
import com.example.gaminghub.data.model.UserRole

@Composable
fun PerfilHeader(
    sessionManager: SessionManager,
) {
    val roleName = sessionManager.getUserRole() ?: UserRole.USUARIO_BASICO.name
    val role = UserRole.values().find { it.name == roleName } ?: UserRole.USUARIO_BASICO

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = role.profileImage),
            contentDescription = role.roleName,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = sessionManager.getUserName() ?: "GamingHub",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = sessionManager.getUserEmail() ?: "sc@correo.com",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray
        )
    }
}
