
package com.example.shaddai_app.ui.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shaddai_app.ui.theme.ShaddaiColors

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.loginSuccess) {
        onLoginSuccess()
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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ShaddaiColors.Background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            LogoSection()

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Bienvenido",
                style = MaterialTheme.typography.headlineLarge,
                color = ShaddaiColors.TextPrimary
            )

            Spacer(modifier = Modifier.height(40.dp))

            LoginTextField(
                value = state.email,
                onValueChange = { onEvent(LoginEvent.OnEmailChange(it)) },
                placeholder = "Usuario",
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )

            Spacer(modifier = Modifier.height(16.dp))

            PasswordTextField(
                value = state.password,
                onValueChange = { onEvent(LoginEvent.OnPasswordChange(it)) },
                placeholder = "Contraseña",
                isPasswordVisible = state.isPasswordVisible,
                onTogglePasswordVisibility = { onEvent(LoginEvent.TogglePasswordVisibility) },
                onImeAction = { onEvent(LoginEvent.LoginClicked) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Olvido su contraseña",
                style = MaterialTheme.typography.bodyMedium,
                color = ShaddaiColors.TextSecondary,
                modifier = Modifier.align(Alignment.End)
            )

            Spacer(modifier = Modifier.height(32.dp))

            LoginButton(
                onClick = { onEvent(LoginEvent.LoginClicked) },
                enabled = !state.isLoading
            )

            Spacer(modifier = Modifier.height(24.dp))

            SocialLoginButtons(
                onGoogleClick = { onEvent(LoginEvent.SocialLogin(SocialLoginProvider.GOOGLE)) },
                onFacebookClick = { onEvent(LoginEvent.SocialLogin(SocialLoginProvider.FACEBOOK)) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            RegisterLink(onClick = onNavigateToRegister)

            AnimatedVisibility(visible = state.errorMessage != null) {
                Text(
                    text = state.errorMessage ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun LogoSection() {
    Box(
        modifier = Modifier
            .size(140.dp, 100.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(ShaddaiColors.White),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = "⚡",
            fontSize = 64.sp,
            color = ShaddaiColors.AccentBlue
        )
    }
}

@Composable
private fun LoginTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                color = ShaddaiColors.TextSecondary
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = ShaddaiColors.White,
            unfocusedContainerColor = ShaddaiColors.White,
            focusedBorderColor = ShaddaiColors.AccentBlue,
            unfocusedBorderColor = Color.Transparent,
            focusedTextColor = ShaddaiColors.TextPrimary,
            unfocusedTextColor = ShaddaiColors.TextPrimary,
        ),
        shape = RoundedCornerShape(8.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        )
    )
}

@Composable
private fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPasswordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    onImeAction: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val iconScale by animateFloatAsState(
        targetValue = if (isPressed) 0.85f else 1f,
        animationSpec = tween(durationMillis = 100)
    )

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                color = ShaddaiColors.TextSecondary
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = ShaddaiColors.White,
            unfocusedContainerColor = ShaddaiColors.White,
            focusedBorderColor = ShaddaiColors.AccentBlue,
            unfocusedBorderColor = Color.Transparent,
            focusedTextColor = ShaddaiColors.TextPrimary,
            unfocusedTextColor = ShaddaiColors.TextPrimary,
        ),
        shape = RoundedCornerShape(8.dp),
        singleLine = true,
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { onImeAction() }
        ),
        trailingIcon = {
            IconButton(
                onClick = {
                    isPressed = true
                    onTogglePasswordVisibility()
                },
                modifier = Modifier.scale(iconScale)
            ) {
                Icon(
                    imageVector = if (isPasswordVisible) VisibilityOnIcon else VisibilityOffIcon,
                    contentDescription = if (isPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                    tint = ShaddaiColors.TextSecondary
                )
            }

            LaunchedEffect(isPressed) {
                if (isPressed) {
                    kotlinx.coroutines.delay(100)
                    isPressed = false
                }
            }
        }
    )
}

val VisibilityOnIcon = Icons.Filled.Visibility
val VisibilityOffIcon = Icons.Filled.VisibilityOff

@Composable
private fun LoginButton(
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = tween(durationMillis = 100)
    )

    Button(
        onClick = {
            isPressed = true
            onClick()
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .scale(scale),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = ShaddaiColors.AccentBlue,
            contentColor = ShaddaiColors.White,
            disabledContainerColor = ShaddaiColors.TextSecondary,
            disabledContentColor = ShaddaiColors.White
        ),
        shape = RoundedCornerShape(28.dp)
    ) {
        Text(
            text = "Inicia Sesión",
            style = MaterialTheme.typography.labelLarge
        )
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(100)
            isPressed = false
        }
    }
}

@Composable
private fun SocialLoginButtons(
    onGoogleClick: () -> Unit,
    onFacebookClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SocialLoginButton(
            icon = "f",
            backgroundColor = Color(0xFF1877F2),
            onClick = onFacebookClick,
            contentDescription = "Login con Facebook"
        )

        Spacer(modifier = Modifier.width(24.dp))

        SocialLoginButton(
            icon = "G",
            backgroundColor = Color(0xFFDB4437),
            onClick = onGoogleClick,
            contentDescription = "Login con Google"
        )
    }
}

@Composable
private fun SocialLoginButton(
    icon: String,
    backgroundColor: Color,
    onClick: () -> Unit,
    contentDescription: String
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = tween(durationMillis = 100)
    )

    Box(
        modifier = Modifier
            .size(56.dp)
            .scale(scale)
            .clip(RoundedCornerShape(28.dp))
            .background(backgroundColor)
            .clickable(
                onClick = {
                    isPressed = true
                    onClick()
                },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = icon,
            color = ShaddaiColors.White,
            fontSize = 24.sp,
            style = MaterialTheme.typography.labelLarge
        )
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(100)
            isPressed = false
        }
    }
}

@Composable
private fun RegisterLink(onClick: () -> Unit) {
    Text(
        text = "Regístrate aquí",
        style = MaterialTheme.typography.bodyMedium,
        color = ShaddaiColors.TextSecondary,
        modifier = Modifier.clickable(onClick = onClick),
        textAlign = TextAlign.Center
    )
}

// Dummy imports for Icons that are not available in commonMain
object Icons {
    object Filled {
        val Visibility: ImageVector
            get() = ImageVector.Builder(
                name = "Visibility",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f
            ).apply {
                path(fill = SolidColor(Color.Black)) {
                    moveTo(12f, 4.5f)
                    curveTo(7f, 4.5f, 2.73f, 7.61f, 1f, 12f)
                    curveTo(2.73f, 16.39f, 7f, 19.5f, 12f, 19.5f)
                    curveTo(17f, 19.5f, 21.27f, 16.39f, 23f, 12f)
                    curveTo(21.27f, 7.61f, 17f, 4.5f, 12f, 4.5f)
                    close()
                    moveTo(12f, 17f)
                    curveTo(9.24f, 17f, 7f, 14.76f, 7f, 12f)
                    curveTo(7f, 9.24f, 9.24f, 7f, 12f, 7f)
                    curveTo(14.76f, 7f, 17f, 9.24f, 17f, 12f)
                    curveTo(17f, 14.76f, 14.76f, 17f, 12f, 17f)
                    close()
                    moveTo(12f, 9f)
                    curveTo(10.34f, 9f, 9f, 10.34f, 9f, 12f)
                    curveTo(9f, 13.66f, 10.34f, 15f, 12f, 15f)
                    curveTo(13.66f, 15f, 15f, 13.66f, 15f, 12f)
                    curveTo(15f, 10.34f, 13.66f, 9f, 12f, 9f)
                    close()
                }
            }.build()

        val VisibilityOff: ImageVector
            get() = ImageVector.Builder(
                name = "VisibilityOff",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f
            ).apply {
                path(fill = SolidColor(Color.Black)) {
                    moveTo(12f, 7f)
                    curveTo(14.76f, 7f, 17f, 9.24f, 17f, 12f)
                    curveTo(17f, 12.65f, 16.87f, 13.26f, 16.64f, 13.83f)
                    lineTo(19.56f, 16.75f)
                    curveTo(21.07f, 15.49f, 22.26f, 13.86f, 23f, 12f)
                    curveTo(21.27f, 7.61f, 17f, 4.5f, 12f, 4.5f)
                    curveTo(10.6f, 4.5f, 9.26f, 4.75f, 8.01f, 5.2f)
                    lineTo(10.17f, 7.36f)
                    curveTo(10.74f, 7.13f, 11.35f, 7f, 12f, 7f)
                    close()
                    moveTo(2f, 4.27f)
                    lineTo(4.28f, 6.55f)
                    lineTo(4.73f, 7f)
                    curveTo(3.08f, 8.3f, 1.78f, 10.02f, 1f, 12f)
                    curveTo(2.73f, 16.39f, 7f, 19.5f, 12f, 19.5f)
                    curveTo(13.55f, 19.5f, 15.03f, 19.2f, 16.38f, 18.66f)
                    lineTo(16.8f, 19.08f)
                    lineTo(19.73f, 22f)
                    lineTo(21f, 20.73f)
                    lineTo(3.27f, 3f)
                    close()
                    moveTo(7.53f, 9.8f)
                    lineTo(9.08f, 11.35f)
                    curveTo(9.03f, 11.56f, 9f, 11.78f, 9f, 12f)
                    curveTo(9f, 13.66f, 10.34f, 15f, 12f, 15f)
                    curveTo(12.22f, 15f, 12.44f, 14.97f, 12.65f, 14.92f)
                    lineTo(14.2f, 16.47f)
                    curveTo(13.53f, 16.8f, 12.79f, 17f, 12f, 17f)
                    curveTo(9.24f, 17f, 7f, 14.76f, 7f, 12f)
                    curveTo(7f, 11.21f, 7.2f, 10.47f, 7.53f, 9.8f)
                    close()
                }
            }.build()
    }
}
