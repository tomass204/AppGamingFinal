package com.example.gaminghub.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gaminghub.data.model.UserRole
import com.example.gaminghub.ui.viewmodel.state.AuthUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterFormScreen(
    authUiState: AuthUiState,
    onUsernameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onRoleSelected: (UserRole) -> Unit,
    onRegisterClick: () -> Unit,
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

    var rolesDropdownExpanded by remember { mutableStateOf(false) }
    // --- LÍNEA CORREGIDA AQUÍ ---
    val roles = remember { UserRole.values().filter { it != UserRole.PROPIETARIO && it != UserRole.MODERADOR } }

    Column(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Crear Cuenta",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        RegisterTextField(
            value = authUiState.usernameInput,
            label = "Nombre de Usuario",
            onValueChange = onUsernameChange,
            colors = textFieldColors,
            isError = authUiState.errorMessage != null
        )

        RegisterTextField(
            value = authUiState.emailInput,
            label = "Email",
            onValueChange = onEmailChange,
            keyboardType = KeyboardType.Email,
            colors = textFieldColors,
            isError = authUiState.errorMessage != null
        )

        RegisterTextField(
            value = authUiState.passwordInput,
            label = "Contraseña",
            onValueChange = onPasswordChange,
            isPassword = true,
            colors = textFieldColors,
            isError = authUiState.errorMessage != null
        )

        PasswordRequirementIndicator(uiState = authUiState)

        RegisterTextField(
            value = authUiState.confirmPasswordInput,
            label = "Confirmar Contraseña",
            onValueChange = onConfirmPasswordChange,
            isPassword = true,
            colors = textFieldColors,
            isError = authUiState.errorMessage != null
        )

        Spacer(modifier = Modifier.height(8.dp))

        RoleDropdown(
            expanded = rolesDropdownExpanded,
            onExpandedChange = { rolesDropdownExpanded = !rolesDropdownExpanded },
            selectedRole = authUiState.selectedRole,
            roles = roles,
            onRoleSelected = onRoleSelected,
            colors = textFieldColors
        )

        Spacer(modifier = Modifier.height(16.dp))

        authUiState.errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Button(
            onClick = onRegisterClick,
            enabled = !authUiState.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = accentColor)
        ) {
            Text(
                text = if (authUiState.selectedRole == UserRole.MODERADOR)
                    "Enviar Solicitud"
                else
                    "Crear Cuenta"
            )
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
private fun RegisterTextField(
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RoleDropdown(
    expanded: Boolean,
    onExpandedChange: () -> Unit,
    selectedRole: UserRole,
    roles: List<UserRole>,
    onRoleSelected: (UserRole) -> Unit,
    colors: TextFieldColors
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { onExpandedChange() }
    ) {
        OutlinedTextField(
            value = selectedRole.roleName,
            onValueChange = {},
            readOnly = true,
            label = { Text("Quiero ser...") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = colors,
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange() }
        ) {
            roles.forEach { role ->
                DropdownMenuItem(
                    text = { Text(role.roleName) },
                    onClick = {
                        onRoleSelected(role)
                        onExpandedChange()
                    }
                )
            }
        }
    }
}
@Composable
private fun PasswordRequirementIndicator(uiState: AuthUiState) {
    // Decide qué mensaje mostrar
    val requirementText = when {
        // Solo muestra algo si el usuario ha empezado a escribir la contraseña
        uiState.passwordInput.isNotEmpty() -> when {
            !uiState.isPasswordLengthValid -> "Debe tener al menos 5 caracteres."
            !uiState.hasPasswordUppercase -> "Debe incluir una letra mayúscula."
            !uiState.hasPasswordSpecialChar -> "Debe incluir un signo especial (ej. @, #, !)."
            else -> null // Si todo está bien, no muestra nada
        }
        else -> null // Si está vacío, no muestra nada
    }
    // Solo muestra el texto si hay algo que mostrar
    if (requirementText != null) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Requisito de contraseña",
                tint = Color.Gray,
                modifier = Modifier.size(14.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = requirementText,
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
    }
}
