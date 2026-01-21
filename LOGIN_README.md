# Pantalla de Login - Shaddai App

## 📱 Descripción
Implementación de la pantalla de Login siguiendo las especificaciones de diseño usando Kotlin Multiplatform y Compose Multiplatform.

## 🎨 Especificaciones de Diseño

### Paleta de Colores
- **Fondo**: `#D7F4F5` (Azul claro/Turquesa)
- **Texto Secundario/Placeholder**: `#A9A9A9` (Gris)
- **Texto Principal**: `#000000` (Negro)
- **Color de Acento/Botón**: `#0E88E6` (Azul)
- **Blanco**: `#FFFFFF`

### Tipografía
- **Fuente**: Manrope (Google Fonts)
- Ver instrucciones de instalación abajo

## 📁 Estructura del Proyecto

```
composeApp/src/commonMain/kotlin/com/example/shaddai_app/
├── ui/
│   ├── theme/
│   │   ├── Color.kt          # Definición de colores
│   │   ├── Type.kt           # Tipografía personalizada
│   │   └── Theme.kt          # Tema de la aplicación
│   └── login/
│       ├── LoginState.kt     # Estado de la pantalla
│       ├── LoginViewModel.kt # Lógica de negocio
│       └── LoginScreen.kt    # Componentes UI
└── App.kt                    # Punto de entrada
```

## 🚀 Características Implementadas

### ✅ UI Completa
- [x] Logo placeholder con diseño personalizado
- [x] Texto "Bienvenido" con tipografía apropiada
- [x] Campo de texto para usuario/email
- [x] Campo de contraseña con toggle de visibilidad
- [x] Iconos animados de mostrar/ocultar contraseña
- [x] Botón de "Iniciar Sesión" con animación
- [x] Botones de login social (Facebook y Google)
- [x] Link de "Regístrate aquí"
- [x] Link de "Olvido su contraseña"

### ✅ Arquitectura
- [x] Patrón MVVM con ViewModel
- [x] State management con StateFlow
- [x] Separación de concerns (UI, State, Logic)
- [x] Preparado para integración con base de datos

### ✅ Animaciones
- [x] Animación de click en botones (scale effect)
- [x] Animación de toggle en ícono de visibilidad
- [x] Transiciones suaves (tween animations)

### ✅ UX
- [x] Campos de texto con placeholder
- [x] Validación básica de campos vacíos
- [x] Feedback visual en interacciones
- [x] Keyboard actions (Next, Done)
- [x] Sistema de colores coherente

## 🔧 Configuración de la Fuente Manrope

### Paso 1: Descargar la fuente
1. Ve a [Google Fonts - Manrope](https://fonts.google.com/specimen/Manrope)
2. Haz clic en "Download family"
3. Extrae el archivo ZIP

### Paso 2: Agregar los archivos de fuente
1. Crea la carpeta: `composeApp/src/commonMain/composeResources/font/`
2. Copia los siguientes archivos .ttf al directorio:
   - `Manrope-Regular.ttf`
   - `Manrope-Medium.ttf`
   - `Manrope-SemiBold.ttf`
   - `Manrope-Bold.ttf`

### Paso 3: Actualizar Type.kt
Descomenta el código en `ui/theme/Type.kt` y actualiza las referencias:

```kotlin
@Composable
fun getManropeFontFamily(): FontFamily {
    return FontFamily(
        Font(Res.font.manrope_regular, FontWeight.Normal),
        Font(Res.font.manrope_medium, FontWeight.Medium),
        Font(Res.font.manrope_semibold, FontWeight.SemiBold),
        Font(Res.font.manrope_bold, FontWeight.Bold)
    )
}

// Luego actualiza ShaddaiTypography para usar esta fuente
val ShaddaiTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = getManropeFontFamily(),
        // ...resto de propiedades
    ),
    // ...resto de estilos
)
```

## 🔌 Integración Futura

### Base de Datos
El `LoginViewModel` está preparado para conectarse con un repositorio. Agrega:

```kotlin
class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    // ...código existente
    
    fun onLoginClick() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val result = authRepository.login(
                    email = _uiState.value.email,
                    password = _uiState.value.password
                )
                // Manejar resultado exitoso
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        errorMessage = e.message,
                        isLoading = false
                    )
                }
            }
        }
    }
}
```

### Navegación
Para implementar navegación entre pantallas, considera usar:
- Voyager (recomendado para KMP)
- Decompose
- Jetpack Compose Navigation (solo Android)

Ejemplo con Voyager:
```kotlin
// En LoginScreen.kt
navigator.push(HomeScreen())
```

### Logo Real
Reemplaza el placeholder del logo:

1. Agrega tu imagen en `composeApp/src/commonMain/composeResources/drawable/`
2. Actualiza `LogoSection()` en `LoginScreen.kt`:

```kotlin
@Composable
private fun LogoSection() {
    Box(
        modifier = Modifier
            .size(140.dp, 100.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(ShaddaiColors.White),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(Res.drawable.app_logo),
            contentDescription = "Logo de Shaddai",
            modifier = Modifier.size(80.dp)
        )
    }
}
```

## 📱 Ejecutar la App

### Android
```bash
./gradlew :composeApp:installDebug
```

### iOS
Abre el proyecto de Xcode en `iosApp/` y ejecuta desde allí.

### Desktop
```bash
./gradlew :composeApp:run
```

## 🎯 TODOs Pendientes

- [ ] Agregar fuente Manrope a los recursos
- [ ] Implementar sistema de navegación
- [ ] Conectar con base de datos/API
- [ ] Agregar logo real
- [ ] Implementar autenticación con Google/Facebook OAuth
- [ ] Agregar pantalla de registro
- [ ] Agregar pantalla de recuperación de contraseña
- [ ] Tests unitarios para LoginViewModel
- [ ] Tests de UI para LoginScreen

## 📄 Licencia
[Tu licencia aquí]
