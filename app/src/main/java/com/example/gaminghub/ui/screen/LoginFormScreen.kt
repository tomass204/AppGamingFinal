package com.example.gaminghub.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.gaminghub.ui.viewmodel.state.AuthUiState

@Composable
fun LoginFormScreen(
    authUiState: AuthUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onSwitchToRegister: () -> Unit,
    onSwitchToRecoveryPassword: () -> Unit,
    modifier: Modifier = Modifier,
    accentColor: Color = MaterialTheme.colorScheme.primary
) {
    val textFieldColors = TextFieldDefaults.colors(
        focusedIndicatorColor = accentColor,
        focusedLabelColor = accentColor,
        cursorColor = accentColor,
        errorCursorColor = MaterialTheme.colorScheme.error,
        errorIndicatorColor = MaterialTheme.colorScheme.error,
        errorLabelColor = MaterialTheme.colorScheme.error
    )

    Column(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Iniciar Sesión",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        LoginTextField(
            value = authUiState.emailInput,
            label = "Email o Nombre de Usuario",
            onValueChange = onEmailChange,
            keyboardType = KeyboardType.Email,
            colors = textFieldColors,
            isError = authUiState.errorMessage != null,
            modifier = Modifier.testTag("EmailInput")
        )

        LoginTextField(
            value = authUiState.passwordInput,
            label = "Contraseña",
            onValueChange = onPasswordChange,
            isPassword = true,
            keyboardType = KeyboardType.Password,
            colors = textFieldColors,
            isError = authUiState.errorMessage != null,
            modifier = Modifier.testTag("PasswordInput")
        )

        Spacer(modifier = Modifier.height(8.dp))

        authUiState.errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
        }

        Button(
            onClick = onLoginClick,
            enabled = !authUiState.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("LoginButton"),
            colors = ButtonDefaults.buttonColors(containerColor = accentColor)
        ) {
            Text("Iniciar Sesión")
        }

        TextButton(
            onClick = onSwitchToRegister,
            enabled = !authUiState.isLoading
        ) {
            Text("¿No tienes cuenta? Regístrate", color = accentColor)
        }

        TextButton(
            onClick = onSwitchToRecoveryPassword,
            enabled = !authUiState.isLoading
        ) {
            Text("¿Olvidaste tú contraseña?", color = accentColor)
        }
    }
}

@Composable
private fun LoginTextField(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    colors: TextFieldColors,
    isError: Boolean
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = colors,
        isError = isError
    )
}