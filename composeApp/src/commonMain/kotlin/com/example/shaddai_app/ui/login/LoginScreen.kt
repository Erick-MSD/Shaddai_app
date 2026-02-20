package com.example.shaddai_app.ui.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shaddai_app.data.model.User

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: (User) -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // Si ya tenemos un usuario autenticado, notificamos al root.
    uiState.loggedInUser?.let { user ->
        LaunchedEffect(user.id) {
            onLoginSuccess(user)
        }
    }

    LoginContent(
        state = uiState,
        onEvent = viewModel::onEvent,
        onNavigateToRegister = onNavigateToRegister
    )
}

@Composable
private fun LoginContent(
    state: LoginState,
    onEvent: (LoginEvent) -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F9FF))
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Logo Section
            LogoSection()

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Bienvenido",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.Black,
                fontWeight = FontWeight.Normal
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Campos de entrada
            LoginTextField(
                value = state.email,
                onValueChange = { onEvent(LoginEvent.OnEmailChange(it)) },
                placeholder = "Usuario o Correo",
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
                isError = state.emailError != null,
                supportingText = state.emailError
            )

            Spacer(modifier = Modifier.height(20.dp))

            Column(horizontalAlignment = Alignment.Start) {
                PasswordTextField(
                    value = state.password,
                    onValueChange = { onEvent(LoginEvent.OnPasswordChange(it)) },
                    placeholder = "Contraseña",
                    isPasswordVisible = state.isPasswordVisible,
                    onTogglePasswordVisibility = { onEvent(LoginEvent.TogglePasswordVisibility) },
                    onImeAction = { onEvent(LoginEvent.LoginClicked) },
                    isError = state.passwordError != null,
                    supportingText = state.passwordError
                )
                Text(
                    text = "Olvide mi contraseña",
                    fontSize = 10.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp, start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Botón Inicia Sesión
            Button(
                onClick = { onEvent(LoginEvent.LoginClicked) },
                enabled = !state.isLoading,
                modifier = Modifier
                    .width(200.dp)
                    .height(45.dp)
                    .border(1.dp, Color.Black, RoundedCornerShape(22.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1E88E5),
                    contentColor = Color.Black,
                    disabledContainerColor = Color(0xFF1E88E5).copy(alpha = 0.6f),
                    disabledContentColor = Color.Black
                ),
                shape = RoundedCornerShape(22.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = Color.Black
                    )
                } else {
                    Text(
                        text = "Inicia Sesión",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            // Social Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SocialCircleButton(
                    backgroundColor = Color(0xFF1877F2),
                    iconContent = {
                        Text("f", color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Bold)
                    },
                    onClick = { /* Facebook Login */ }
                )

                Spacer(modifier = Modifier.width(20.dp))

                SocialCircleButton(
                    backgroundColor = Color.White,
                    border = BorderStroke(1.dp, Color.LightGray),
                    iconContent = {
                        // Usando un texto "G" coloreado para simular el logo de Google
                        Text("G", fontWeight = FontWeight.ExtraBold, fontSize = 28.sp, color = Color(0xFFDB4437))
                    },
                    onClick = { /* Google Login */ }
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Regístrate aquí",
                fontSize = 24.sp,
                color = Color.LightGray,
                modifier = Modifier.clickable { onNavigateToRegister() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            AnimatedVisibility(visible = state.authErrorMessage != null) {
                Text(
                    text = state.authErrorMessage ?: "",
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun LogoSection() {
    Box(
        modifier = Modifier
            .size(180.dp, 120.dp)
            .border(2.dp, Color(0xFF2196F3))
            .background(Color.White)
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        // Marco interior delgado
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(1.dp, Color.LightGray)
        )

        // Rayo central (Simulado con Icono o Vector)
        Text(
            text = "⚡",
            fontSize = 80.sp,
            color = Color(0xFF1E88E5)
        )
    }
}

@Composable
private fun LoginTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    isError: Boolean = false,
    supportingText: String? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = placeholder, color = Color.Gray, fontSize = 18.sp) },
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp),
        isError = isError,
        supportingText = if (supportingText != null) {
            { Text(text = supportingText, color = MaterialTheme.colorScheme.error) }
        } else null,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = if (isError) MaterialTheme.colorScheme.error else Color.Black,
            unfocusedBorderColor = if (isError) MaterialTheme.colorScheme.error else Color.Black,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black
        ),
        shape = RoundedCornerShape(8.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction)
    )
}

@Composable
private fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPasswordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    onImeAction: () -> Unit,
    isError: Boolean = false,
    supportingText: String? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(text = placeholder, color = Color.Gray, fontSize = 18.sp) },
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp),
        isError = isError,
        supportingText = if (supportingText != null) {
            { Text(text = supportingText, color = MaterialTheme.colorScheme.error) }
        } else null,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = if (isError) MaterialTheme.colorScheme.error else Color.Black,
            unfocusedBorderColor = if (isError) MaterialTheme.colorScheme.error else Color.Black,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black
        ),
        shape = RoundedCornerShape(8.dp),
        singleLine = true,
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { onImeAction() })
    )
}

@Composable
private fun SocialCircleButton(
    backgroundColor: Color,
    border: BorderStroke? = null,
    iconContent: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .size(56.dp)
            .clickable { onClick() },
        shape = CircleShape,
        color = backgroundColor,
        border = border,
        shadowElevation = 2.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            iconContent()
        }
    }
}
