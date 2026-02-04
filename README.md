# 📱 Shaddai Multiservicios - Aplicación Móvil

## 🎯 Descripción

**Shaddai App** es una aplicación multiplataforma desarrollada con **Kotlin Multiplatform (KMP)** y **Compose Multiplatform** para optimizar la gestión operativa de Shaddai Multiservicios, una empresa especializada en servicios de climatización, plomería, electricidad, impermeabilización y pintura.

La aplicación permite a los técnicos:
- 📅 Visualizar citas programadas en un calendario interactivo
- 📝 Generar reportes de servicio con evidencia fotográfica
- 🏠 Ver su agenda diaria desde un dashboard centralizado
- 📍 Acceder a información de clientes y ubicaciones

---

## 📚 Documentación Completa

Para la documentación técnica completa del proyecto, consulta:

### 👉 [**DOCUMENTACION_PROYECTO.md**](./DOCUMENTACION_PROYECTO.md)

Este documento incluye:
- 🔍 Problemática y Justificación
- 👥 Público Objetivo
- 🛠️ Tecnologías Utilizadas (versiones, librerías)
- 🏛️ Arquitectura del Proyecto (MVVM, Repository Pattern)
- 📱 Pantallas Implementadas (Login, Inicio, Calendario, Reporte)
- 🎨 Guía de Colores y Diseño
- 📂 Estructura del Código
- 🧭 Flujo de Navegación
- 🚀 Instalación y Ejecución
- 👥 Equipo de Desarrollo

---

## 🚀 Inicio Rápido

### Requisitos Previos
- Android Studio Ladybug 2024.2.1+
- JDK 11
- Gradle 8.0+

### Clonar y Ejecutar

```powershell
# Clonar el repositorio
git clone https://github.com/usuario/Shaddai_app.git
cd Shaddai_app

# Compilar y ejecutar en Android
.\gradlew :composeApp:assembleDebug

# O ejecutar en Desktop (Windows)
.\gradlew :composeApp:run
```

### Ejecutar en Android Studio

1. Abre Android Studio
2. File → Open → Selecciona la carpeta `Shaddai_app`
3. Espera a que Gradle sincronice
4. Conecta un dispositivo o inicia un emulador
5. Click en Run (▶️)

---

## 📱 Pantallas Implementadas

### 🔐 Login
Pantalla de autenticación con:
- Campos de usuario y contraseña
- Toggle de visibilidad de contraseña
- Validación de campos
- Botones de login social (Facebook, Google)

### 🏠 Inicio del Técnico
Dashboard que muestra:
- Servicio actual con botones de acción (Ir ahora, Llamar)
- Lista de próximas citas
- Información organizada por tipo de servicio

### 📅 Calendario
Vista de servicios programados:
- Navegación por mes y semana
- Cards de eventos con código de colores
- Filtrado por fecha
- Detalles de cada servicio

### 📝 Reporte de Servicio
Documentación de trabajos:
- Sección de evidencia fotográfica
- Campo de observaciones técnicas
- Captura de firma de conformidad

---

## 🎨 Colores Principales

```kotlin
Background:      #D7F4F5  // Fondo turquesa claro
TextPrimary:     #000000  // Negro
TextSecondary:   #A9A9A9  // Gris
AccentBlue:      #0E88E6  // Azul (botones)
White:           #FFFFFF  // Blanco (cards)
```

### Colores por Tipo de Servicio
- 🟢 Plomería: `#4CAF50`
- 🟡 Electricidad: `#FFC107`
- 🔵 Clima: `#2196F3`
- 🟣 Redes: `#9C27B0`
- 🟠 CCTV: `#FF5722`

---

## 🏗️ Arquitectura

### Stack Tecnológico
- **Kotlin Multiplatform (KMP)** 2.3.0
- **Compose Multiplatform** 1.10.0
- **Material Design 3**
- **Kotlinx Coroutines** 1.10.2
- **Kotlinx DateTime** 0.6.0

### Patrón MVVM
```
View (Compose UI) ← StateFlow ← ViewModel ← Repository ← Data Source (CSV)
```

### Targets Soportados
- ✅ Android (MinSDK 24, TargetSDK 36)
- ✅ iOS (iosArm64, iosSimulatorArm64)
- ✅ Desktop/JVM

---

## 📂 Estructura del Proyecto

```
Shaddai_app/
├── composeApp/                    # Código compartido
│   └── src/
│       ├── commonMain/            # 95% del código
│       │   └── kotlin/
│       │       ├── data/          # Modelos y Repositorios
│       │       ├── ui/            # Pantallas y Componentes
│       │       │   ├── login/
│       │       │   ├── technician_home/
│       │       │   ├── calendario/
│       │       │   └── service_report/
│       │       └── navigation/    # Lógica de navegación
│       ├── androidMain/           # Específico Android
│       ├── iosMain/               # Específico iOS
│       └── jvmMain/               # Específico Desktop
├── iosApp/                        # App nativa iOS
└── gradle/                        # Configuración de dependencias
```

---

## 🔧 Comandos Útiles

### Android
```powershell
# Debug APK
.\gradlew :composeApp:assembleDebug

# Release APK
.\gradlew :composeApp:assembleRelease

# Bundle para Play Store
.\gradlew :composeApp:bundleRelease
```

### Desktop
```powershell
# Ejecutar aplicación
.\gradlew :composeApp:run

# Generar instalador MSI (Windows)
.\gradlew :composeApp:packageMsi
```

### iOS (macOS)
```bash
cd iosApp
open iosApp.xcodeproj
# Ejecutar desde Xcode
```

---

## 👥 Equipo de Desarrollo

- **Erick Mauricio Santiago Díaz** - Líder de Proyecto / Full-Stack Developer
- **Gael Marroquin Torres** -  Full-Stack Developer
- **Andres Jahir Abarca Ulloa** -  Full-Stack Developer

**Cliente:** Israel - Shaddai Multiservicios

---

## 📊 Estado del Proyecto

**Versión:** 1.0.0 (Prototipo Funcional)  
**Última actualización:** Febrero 2026  
**Estado:** ✅ Completado (Fase 1)

### Próximos Pasos
- [ ] Migrar de CSV a SQLite/PostgreSQL
- [ ] Implementar autenticación real
- [ ] Agregar captura de fotos desde cámara
- [ ] Backend con Ktor
- [ ] Notificaciones push

---

## 📞 Contacto

**Equipo de Desarrollo**  
📧 Email: agewebdev@gmail.com  
🔗 GitHub: [Shaddai App Repository](https://github.com/usuario/Shaddai_app)

**Cliente**  
📧 Email: israelsdiaz83@gmail.com

---

## 📄 Licencia

Proyecto académico desarrollado para **Universidad Tecmilenio**.  
Código fuente propiedad del equipo de desarrollo.  
Derecho de uso exclusivo de Shaddai Multiservicios.

---

## 🙏 Agradecimientos

- **Israel (Shaddai Multiservicios):** Por confiar en nuestro equipo
- **Profesores:** Por la guía y el apoyo
- **Comunidad KMP:** Por la documentación y recursos

---

**⭐ Si este proyecto te resulta útil, no olvides darle una estrella en GitHub!**
