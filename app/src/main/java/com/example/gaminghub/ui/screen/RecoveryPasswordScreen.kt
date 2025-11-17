package com.example.gaminghub.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.gaminghub.ui.viewmodel.state.AuthUiState

@Composable
fun RecoveryPasswordScreen(
    authUiState: AuthUiState,
    onEmailChange: (String) -> Unit,
    onRecoveryClick: () -> Unit,
    onSwitchToLogin: () -> Unit,
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
            text = "Recuperar contraseña",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        LoginTextField(
            value = authUiState.emailInput,
            label = "Email",
            onValueChange = onEmailChange,
            keyboardType = KeyboardType.Email,
            colors = textFieldColors,
            isError = authUiState.errorMessage != null
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
            onClick = onRecoveryClick,
            enabled = !authUiState.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = accentColor)
        ) {
            Text("Recuperar contraseña")
        }

        TextButton(
            onClick = onSwitchToLogin,
            enabled = !authUiState.isLoading
        ) {
            Text("¿Ya tienes cuenta? Inicia Sesión", color = accentColor)
        }


    }
}

@Composable
private fun LoginTextField(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = colors,
        isError = isError
    )
}
