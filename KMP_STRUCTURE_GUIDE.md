# 📚 Estructura del Proyecto - Kotlin Multiplatform (KMP)

## 🏗️ Organización del Proyecto Shaddai App

Este proyecto está configurado como **Kotlin Multiplatform** con soporte para **Android**, **iOS** y **Desktop (JVM)**.

## 📂 Estructura de Directorios

```
Shaddai_app/
├── composeApp/                      # Módulo principal de la aplicación
│   ├── src/
│   │   ├── commonMain/             # ⭐ Código compartido (Android, iOS, Desktop)
│   │   │   ├── kotlin/
│   │   │   │   └── com/example/shaddai_app/
│   │   │   │       ├── ui/
│   │   │   │       │   ├── theme/  # Tema, colores, tipografía
│   │   │   │       │   └── login/  # Pantalla de login
│   │   │   │       ├── App.kt      # Composable principal
│   │   │   │       └── Platform.kt # Interface de plataforma
│   │   │   └── composeResources/   # Recursos compartidos
│   │   │       ├── drawable/       # Imágenes
│   │   │       └── font/          # Fuentes (agregar Manrope aquí)
│   │   │
│   │   ├── androidMain/            # Código específico de Android
│   │   │   └── kotlin/
│   │   │       └── com/example/shaddai_app/
│   │   │           └── MainActivity.kt
│   │   │
│   │   ├── iosMain/                # Código específico de iOS
│   │   │   └── kotlin/
│   │   │       └── com/example/shaddai_app/
│   │   │           └── MainViewController.kt
│   │   │
│   │   ├── jvmMain/                # Código específico de Desktop
│   │   │   └── kotlin/
│   │   │       └── com/example/shaddai_app/
│   │   │           └── Main.kt
│   │   │
│   │   └── commonTest/             # Tests compartidos
│   │
│   └── build.gradle.kts            # Configuración de Gradle
│
├── iosApp/                         # Proyecto de iOS (Xcode)
│   └── iosApp/
│       └── ContentView.swift       # Vista principal de iOS
│
├── gradle/
│   └── libs.versions.toml          # Versiones de dependencias
│
├── build.gradle.kts                # Configuración raíz
└── settings.gradle.kts             # Configuración de módulos
```

## 🎯 ¿Dónde Trabajar Según la Plataforma?

### 📱 Desarrollo Compartido (Común a todas las plataformas)

**Ubicación**: `composeApp/src/commonMain/`

Aquí es donde debes escribir la **mayor parte del código**:

✅ **UI con Compose Multiplatform**
- Todas las pantallas (Login, Home, Profile, etc.)
- Componentes reutilizables
- Navegación

✅ **Lógica de Negocio**
- ViewModels
- Estados (State)
- Repositorios
- Casos de uso

✅ **Modelos de Datos**
- Data classes
- Entities
- DTOs

✅ **Recursos Compartidos**
- Imágenes (PNG, SVG, WebP)
- Fuentes (TTF, OTF)
- Strings (en el futuro)
- Colores y temas

**Ejemplo actual**:
```
commonMain/kotlin/com/example/shaddai_app/
├── ui/
│   ├── theme/         # ← Tema compartido
│   │   ├── Color.kt
│   │   ├── Type.kt
│   │   └── Theme.kt
│   └── login/         # ← Pantalla de login compartida
│       ├── LoginState.kt
│       ├── LoginViewModel.kt
│       └── LoginScreen.kt
└── App.kt             # ← App principal compartida
```

### 🤖 Android Específico

**Ubicación**: `composeApp/src/androidMain/`

Solo código que **necesita APIs de Android**:

✅ Usar cuando necesitas:
- `Context` de Android
- Permisos del sistema (Cámara, Location, etc.)
- Servicios de Android (NotificationManager, etc.)
- Integración con librerías Android-only
- Work Manager
- Broadcast Receivers

**Ejemplo**:
```kotlin
// androidMain/kotlin/.../Platform.android.kt
actual fun getPlatform(): Platform {
    return AndroidPlatform()
}

class AndroidPlatform : Platform {
    override val name: String = "Android ${android.os.Build.VERSION.SDK_INT}"
}
```

### 🍎 iOS Específico

**Ubicación**: `composeApp/src/iosMain/`

Solo código que **necesita APIs de iOS**:

✅ Usar cuando necesitas:
- UIKit específico
- Core Location
- Push Notifications de iOS
- Integración con librerías iOS-only
- Interop con Swift/Objective-C

**Ejemplo**:
```kotlin
// iosMain/kotlin/.../Platform.ios.kt
import platform.UIKit.UIDevice

actual fun getPlatform(): Platform {
    return IOSPlatform()
}

class IOSPlatform : Platform {
    override val name: String = 
        UIDevice.currentDevice.systemName() + " " + 
        UIDevice.currentDevice.systemVersion
}
```

### 🖥️ Desktop (JVM) Específico

**Ubicación**: `composeApp/src/jvmMain/`

Solo código que **necesita APIs de Desktop/JVM**:

✅ Usar cuando necesitas:
- File system operations específicas de desktop
- APIs de Java/JVM
- Window management
- System tray integration

**Ejemplo**:
```kotlin
// jvmMain/kotlin/.../Main.kt
fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Shaddai App",
    ) {
        App()
    }
}
```

## 🔄 Patrón expect/actual

Para código que necesita implementaciones diferentes por plataforma:

### En `commonMain`:
```kotlin
// Platform.kt (commonMain)
expect fun getPlatform(): Platform

interface Platform {
    val name: String
}
```

### En cada plataforma:
```kotlin
// Platform.android.kt (androidMain)
actual fun getPlatform(): Platform = AndroidPlatform()

// Platform.ios.kt (iosMain)
actual fun getPlatform(): Platform = IOSPlatform()

// Platform.jvm.kt (jvmMain)
actual fun getPlatform(): Platform = JVMPlatform()
```

## 📦 Dependencias

### Configuración en `build.gradle.kts`:

```kotlin
kotlin {
    sourceSets {
        // Dependencias compartidas
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            // Estas están disponibles en TODAS las plataformas
        }
        
        // Dependencias solo para Android
        androidMain.dependencies {
            implementation("androidx.activity:activity-compose:1.x.x")
            // Solo disponible en Android
        }
        
        // Dependencias solo para iOS
        iosMain.dependencies {
            // Dependencias específicas de iOS
        }
        
        // Dependencias solo para Desktop
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            // Solo disponible en Desktop
        }
    }
}
```

## 🚀 Flujo de Trabajo Recomendado

### Para esta App de Login:

1. **✅ Ya hecho - Todo en `commonMain`**:
   - Pantalla de Login (`LoginScreen.kt`)
   - ViewModel (`LoginViewModel.kt`)
   - Estado (`LoginState.kt`)
   - Tema y colores (`ui/theme/`)

2. **🔜 Próximos pasos**:
   - Agregar más pantallas en `commonMain`
   - Implementar navegación en `commonMain`
   - Crear repositorio de datos en `commonMain`

3. **⚠️ Solo cuando sea necesario**:
   - Implementar autenticación biométrica → `androidMain` y `iosMain`
   - Guardar tokens en KeyChain/KeyStore → Específico por plataforma
   - Notificaciones push → Específico por plataforma

## 🎨 Recursos Compartidos

### Cómo agregar imágenes:
```
composeApp/src/commonMain/composeResources/
└── drawable/
    ├── logo.png
    ├── ic_google.xml
    └── ic_facebook.xml
```

### Uso en código:
```kotlin
import org.jetbrains.compose.resources.painterResource
import shaddai_app.composeapp.generated.resources.Res
import shaddai_app.composeapp.generated.resources.logo

Image(
    painter = painterResource(Res.drawable.logo),
    contentDescription = "Logo"
)
```

### Cómo agregar fuentes:
```
composeApp/src/commonMain/composeResources/
└── font/
    ├── manrope_regular.ttf
    ├── manrope_medium.ttf
    ├── manrope_semibold.ttf
    └── manrope_bold.ttf
```

## 🔧 Comandos Útiles

### Compilar para Android:
```bash
./gradlew :composeApp:assembleDebug
```

### Ejecutar en Android:
```bash
./gradlew :composeApp:installDebug
```

### Compilar para iOS:
```bash
./gradlew :composeApp:linkDebugFrameworkIosSimulatorArm64
```

### Ejecutar Desktop:
```bash
./gradlew :composeApp:run
```

### Limpiar proyecto:
```bash
./gradlew clean
```

## 📱 Ventajas de KMP para este Proyecto

1. **Una sola codebase para UI**: La pantalla de Login funciona en Android, iOS y Desktop sin cambios
2. **Compartir lógica de negocio**: El `LoginViewModel` es el mismo en todas las plataformas
3. **Recursos unificados**: Colores, tipografía, imágenes se definen una vez
4. **Mantenimiento simplificado**: Un cambio en `commonMain` afecta a todas las plataformas
5. **Tests compartidos**: Los tests se escriben una vez para todas las plataformas

## 🎯 Mejores Prácticas

1. ✅ **Escribe TODO en `commonMain` primero**
2. ✅ **Solo usa código específico de plataforma cuando sea absolutamente necesario**
3. ✅ **Usa expect/actual para abstraer diferencias de plataforma**
4. ✅ **Mantén las implementaciones de plataforma lo más pequeñas posible**
5. ✅ **Centraliza la configuración en `build.gradle.kts`**

## 📚 Recursos Adicionales

- [Documentación oficial de KMP](https://kotlinlang.org/docs/multiplatform.html)
- [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)
- [Samples de KMP](https://github.com/JetBrains/compose-multiplatform/tree/master/examples)
