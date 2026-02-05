# 📱 Shaddai Multiservicios - Documentación Completa del Proyecto
## Proyecto Académico - Desarrollo de Aplicación Móvil Multiplataforma
---
## 📋 INFORMACIÓN GENERAL
### Datos del Proyecto
- **Nombre:** Shaddai Multiservicios App
- **Tipo:** Aplicación Móvil Multiplataforma (Android, iOS, Desktop)
- **Stack Tecnológico:** Kotlin Multiplatform + Compose Multiplatform
- **Versión:** 1.0.0 (Prototipo Funcional)
- **Fecha de Entrega:** Marzo 2026
- **Estado:** ✅ Completado (Fase 1)
### Contexto Académico
Este proyecto fue desarrollado como parte de los requerimientos universitarios con los siguientes objetivos:
1. ✅ Formar equipos mixtos (desarrollo y diseño)
2. ✅ Seleccionar un problema organizacional o social para resolver
3. ✅ Investigar y definir el público objetivo
4. ✅ Diseñar wireframes y flujos de usuario
5. ✅ Crear un prototipo funcional con navegación básica
6. ✅ Documentar la justificación del proyecto
---
## 🔍 PROBLEMÁTICA Y JUSTIFICACIÓN
### Empresa Cliente: Shaddai Multiservicios
**Shaddai Multiservicios** es una empresa dirigida por Israel, especializada en:
- ❄️ **Sistemas de aire acondicionado** (servicio principal - estacional)
- ⚡ Electricidad
- 🔧 Plomería
- 🏠 Impermeabilización
- 🎨 Pintura
### Problemática Detectada
Durante las reuniones con el cliente, identificamos los siguientes problemas críticos:
#### 1. Gestión Ineficiente de Citas
- ❌ No hay sincronización entre la agenda del propietario y los técnicos
- ❌ Dificultad para estimar tiempos (servicios de 3 horas a 3 días)
- ❌ Ausencia de recordatorios automáticos a clientes
- ❌ Problemas para reorganizar horarios
#### 2. Documentación Manual
- ❌ Reportes de trabajo creados a mano
- ❌ Falta de evidencia fotográfica estandarizada
- ❌ No hay sistema de firma digital
- ❌ Formatos inconsistentes entre servicios
#### 3. Comunicación Desorganizada
- ❌ Dependencia total de WhatsApp
- ❌ No hay sistema centralizado de información
- ❌ Dificultad para dar seguimiento
#### 4. Historial de Clientes
- ❌ No hay registro sistemático de mantenimientos
- ❌ Imposible identificar clientes con servicios pendientes
- ❌ Falta de programas de fidelización
### Solución Propuesta
**Shaddai App** resuelve estos problemas mediante:
- ✅ Calendario interactivo con vistas diarias, semanales y mensuales
- ✅ Generación digital de reportes con fotos y firmas
- ✅ Dashboard centralizado para técnicos
- ✅ Navegación intuitiva diseñada para uso en campo
- ✅ Arquitectura preparada para base de datos
---
## 👥 PÚBLICO OBJETIVO
### Usuario Principal: Técnicos de Campo
- **Edad:** 25-50 años
- **Perfil:** Técnicos especializados en climatización, electricidad, plomería
- **Necesidades:**
  - Ver agenda diaria rápidamente
  - Acceder a detalles de servicios (dirección, tipo, duración)
  - Generar reportes profesionales en sitio
  - Capturar evidencia fotográfica
  - Obtener firmas de conformidad
### Usuario Secundario: Administrador (Israel)
- **Rol:** Dueño y coordinador general
- **Necesidades:**
  - Asignar servicios a técnicos
  - Monitorear avance de trabajos
  - Gestionar calendario maestro
  - Acceder a reportes históricos
---
## 🛠️ TECNOLOGÍAS UTILIZADAS
### Stack Principal
#### Kotlin Multiplatform (KMP) 2.3.0
Permite compartir código entre plataformas:
- ✅ Android (MinSDK 24, TargetSDK 36)
- ✅ iOS (iosArm64, iosSimulatorArm64)
- ✅ Desktop/JVM
#### Compose Multiplatform 1.10.0
Framework declarativo para UI nativa en todas las plataformas.
### Librerías y Dependencias
| Librería | Versión | Propósito |
|----------|---------|-----------|
| androidx.lifecycle.viewmodel | 2.9.6 | Gestión de estado con ViewModels |
| androidx.lifecycle.runtime | 2.9.6 | Ciclo de vida reactivo |
| kotlinx-coroutines | 1.10.2 | Programación asíncrona |
| kotlinx-datetime | 0.6.0 | Manejo de fechas multiplataforma |
| material3 | - | Componentes Material Design 3 |
### Versiones Críticas
\\\	oml
Kotlin: 2.3.0
Android Gradle Plugin: 9.0.0
Compose Multiplatform: 1.10.0
JVM Target: 11
\\\
---
## 📱 PANTALLAS IMPLEMENTADAS
### 1. 🔐 Pantalla de Login
**Funcionalidades:**
- ✅ Campos de usuario/email y contraseña
- ✅ Toggle de visibilidad de contraseña (icono de ojo animado)
- ✅ Validación de campos vacíos
- ✅ Botón \"Iniciar Sesión\" con animación
- ✅ Botones de login social (Facebook, Google)
- ✅ Links de \"Olvidé mi contraseña\" y \"Regístrate\"
**Arquitectura:**
\\\kotlin
data class LoginState(
    val email: String,
    val password: String,
    val isPasswordVisible: Boolean,
    val isLoading: Boolean,
    val errorMessage: String?
)
class LoginViewModel : ViewModel() {
    val uiState: StateFlow<LoginState>
    fun onLoginClick()
    fun togglePasswordVisibility()
}
\\\
---
### 2. 🏠 Pantalla de Inicio del Técnico
**Funcionalidades:**
- ✅ Card de servicio actual destacada
- ✅ Información completa: tipo, título, horario, dirección
- ✅ Botones \"Ir ahora\" (GPS) y \"Llamar\" (dialer)
- ✅ Lista scrolleable de próximas citas
- ✅ Iconos específicos por tipo de servicio
**Componentes:**
\\\kotlin
@Composable
fun CurrentServiceCard(
    service: CurrentService,
    onGoNow: () -> Unit,
    onCall: () -> Unit
)
@Composable
fun UpcomingServiceItem(
    service: UpcomingService,
    onClick: () -> Unit
)
\\\
---
### 3. 📅 Pantalla de Calendario
**Funcionalidades:**
- ✅ Vistas: Día, Semana, Mes (preparadas)
- ✅ Navegación por meses con flechas
- ✅ Selector de semana con días individuales
- ✅ Cards de eventos con código de colores
- ✅ Bloque especial para \"Hora de comida\"
- ✅ Header dinámico: \"Hola, [Nombre del Técnico]\"
**Sistema de Colores por Servicio:**
\\\kotlin
enum class TipoServicio(val colorHex: String) {
    PLOMERIA(\"#4CAF50\"),         // Verde
    ELECTRICIDAD(\"#FFC107\"),    // Amarillo
    AIRE_ACONDICIONADO(\"#2196F3\"), // Azul
    REDES(\"#9C27B0\"),           // Púrpura
    CCTV(\"#FF5722\")             // Naranja
}
\\\
**Arquitectura - Repository Pattern:**
\\\kotlin
interface ServiciosRepository {
    suspend fun obtenerEventos(): List<EventoServicio>?
    suspend fun obtenerEventosPorFecha(fecha: LocalDate): List<EventoServicio>?
}
class CsvServiciosRepository : ServiciosRepository {
    // Implementación con CSV (temporal)
    // Preparada para SQL/NoSQL
}
\\\
---
### 4. 📝 Pantalla de Reporte de Servicio
**Funcionalidades:**
- ✅ Sección de evidencia con galería horizontal
- ✅ Botón \"Subir Foto\" y eliminar fotos
- ✅ Campo de observaciones técnicas (multilínea)
- ✅ Sección de firma de conformidad
- ✅ Diálogo modal para captura de firma
**Eventos:**
\\\kotlin
sealed interface ServiceReportEvent {
    data class OnObservationsChange(val text: String)
    object OnAddPhotoClick
    data class OnDeletePhoto(val photoUri: String)
    object OnSignatureClick
    object OnSignatureSave
}
\\\
---
## 🎨 GUÍA DE COLORES
### Paleta Principal
| Color | Hex | Uso |
|-------|-----|-----|
| **Background** | \#D7F4F5\ | Fondo de pantallas |
| **Text Primary** | \#000000\ | Títulos y texto principal |
| **Text Secondary** | \#A9A9A9\ | Placeholders, subtítulos |
| **Accent Blue** | \#0E88E6\ | Botones, links, elementos activos |
| **White** | \#FFFFFF\ | Cards, fondos de componentes |
### Colores de Calendario
| Elemento | Color Hex |
|----------|-----------|
| Barra superior | \#1565C0\ (Azul oscuro) |
| Día seleccionado | \#1E88E5\ (Azul medio) |
| Fondo calendario | \#F0F9FF\ (Celeste claro) |
### Tipografía: Manrope
\\\kotlin
// Fuente de Google Fonts
headlineLarge:  32sp, Bold       // Títulos grandes
titleLarge:     20sp, Bold       // Títulos de sección
bodyLarge:      16sp, Normal     // Texto principal
bodyMedium:     14sp, Normal     // Texto secundario
labelLarge:     16sp, SemiBold   // Botones
\\\
---
## 🏗️ ARQUITECTURA DEL PROYECTO
### Patrón MVVM (Model-View-ViewModel)
\\\
View (Compose UI)
    ↑ Observa StateFlow
    |
ViewModel (Lógica de presentación)
    ↑ Usa Repository
    |
Repository (Abstracción de datos)
    ↑ Retorna Models
    |
Data Source (CSV → SQL/NoSQL)
\\\
### Estructura de Directorios
\\\
composeApp/src/commonMain/kotlin/
├── data/
│   ├── model/
│   │   ├── Tecnico.kt
│   │   └── EventoServicio.kt
│   └── repository/
│       ├── ServiciosRepository.kt
│       └── CsvServiciosRepository.kt
├── ui/
│   ├── theme/
│   │   ├── Color.kt
│   │   ├── Type.kt
│   │   └── Theme.kt
│   ├── components/
│   │   └── navigation/
│   ├── login/
│   ├── technician_home/
│   ├── calendario/
│   ├── service_report/
│   └── main/
└── navigation/
    └── Screen.kt
\\\
### Principios de Diseño
1. **Separation of Concerns:** Cada capa tiene una responsabilidad única
2. **Dependency Inversion:** Dependencia de abstracciones, no implementaciones
3. **Unidirectional Data Flow:** Eventos suben, estado baja
4. **Immutable State:** Data classes con copy()
---
## 🧭 FLUJO DE NAVEGACIÓN
\\\
LoginScreen
    ↓
MainScreen (Scaffold con BottomBar)
    ├─ Inicio del Técnico
    ├─ Reporte de Servicio
    ├─ Calendario
    └─ Soporte (en desarrollo)
\\\
**Navegación Bottom Bar:**
- 🏠 **Inicio:** Dashboard con servicio actual
- 🔧 **Servicios:** Documentación de trabajos
- 📅 **Calendario:** Vista de servicios programados
- 🎧 **Soporte:** Ayuda y contacto
---
## 📊 REQUISITOS DE KOTLIN IMPLEMENTADOS
| Requisito | Implementación | Ubicación |
|-----------|----------------|-----------|
| **Funciones con parámetros** | \obtenerRangoTiempo()\, \obtenerSaludo()\ | \EventoServicio.kt\, \Tecnico.kt\ |
| **Colecciones** | \List<EventoServicio>\, \Map\ | Todo el proyecto |
| **Manejo de excepciones** | \	ry-catch\ en CSV y ViewModels | \CsvServiciosRepository.kt\ |
| **Null safety** | \?:\, \?.\, evitar \!!\ | Todo el código |
| **Clase** | \EventoServicio\, \Tecnico\, \CalendarioViewModel\ | Múltiples archivos |
| **Objeto** | \TechnicianColors\, \ShaddaiColors\ | \Color.kt\ |
| **Interfaz** | \ServiciosRepository\ | \ServiciosRepository.kt\ |
| **Función de orden superior** | \ilter\, \map\, \mapNotNull\ | \CalendarioViewModel.kt\ |
---
## 🚀 INSTALACIÓN Y EJECUCIÓN
### Requisitos Previos
- Android Studio Ladybug 2024.2.1+
- JDK 11
- Gradle 8.0+
### Clonar y Ejecutar
\\\powershell
# Clonar
git clone https://github.com/usuario/Shaddai_app.git
cd Shaddai_app
# Ejecutar en Android
.\\gradlew :composeApp:assembleDebug
# Ejecutar en Desktop
.\\gradlew :composeApp:run
\\\
### Desde Android Studio
1. File → Open → Seleccionar carpeta \Shaddai_app\
2. Esperar sincronización de Gradle
3. Conectar dispositivo o iniciar emulador
4. Click en Run (▶️)
---
## 👥 EQUIPO DE DESARROLLO
| Nombre | Rol | Responsabilidades |
|--------|-----|-------------------|
| **Erick Mauricio Santiago Díaz** | Líder / Full-Stack | Arquitectura, Login, Calendario, CSV |
| **[Compañero 2]** | Frontend / Diseñador | Inicio, Navegación, Wireframes |
| **[Compañero 3]** | Backend / Diseñador | Reporte, Testing, Figma |
**Cliente:** Israel - Shaddai Multiservicios
---
## 📊 RESULTADOS Y MÉTRICAS
### Objetivos Cumplidos
✅ Equipo mixto formado  
✅ Problema organizacional identificado  
✅ Público objetivo definido  
✅ Wireframes creados en Figma  
✅ Prototipo funcional con 4 pantallas  
✅ Navegación completa implementada  
✅ Documentación exhaustiva  
✅ Archivos entregados  
### Métricas del Proyecto
- **Líneas de código:** ~3,500
- **Pantallas:** 4 (Login, Inicio, Calendario, Reporte)
- **Componentes reutilizables:** 15+
- **Tiempo de desarrollo:** 6 semanas
- **Commits:** 120+
---
## 🔮 PRÓXIMOS PASOS
### Corto Plazo (1-2 meses)
- [ ] Migrar de CSV a SQLite con Room
- [ ] Implementar autenticación real con JWT
- [ ] Agregar captura de fotos desde cámara
- [ ] Canvas de firma funcional
### Mediano Plazo (3-6 meses)
- [ ] Backend con Ktor y PostgreSQL
- [ ] Sincronización offline-first
- [ ] Notificaciones push
- [ ] Exportar reportes a PDF
### Largo Plazo (6-12 meses)
- [ ] Integración con Google Maps
- [ ] Calculadora de tonelaje de A/C
- [ ] Sistema de facturación electrónica
- [ ] Chatbot con IA para soporte 24/7
---
## 📝 CONCLUSIONES
El proyecto **Shaddai App** representa una solución tecnológica real a un problema operativo de una empresa de servicios. A través de:
- **Metodologías modernas:** MVVM, Repository Pattern
- **Tecnologías de vanguardia:** Kotlin Multiplatform, Compose
- **Diseño centrado en el usuario:** UI intuitiva para técnicos
Se creó un prototipo funcional que sienta las bases para un sistema completo de gestión.
### Impacto Esperado
- 📉 Reducir tiempos de coordinación en 60%
- ❌ Eliminar errores de agenda
- 📊 Profesionalizar reportes
- 😊 Mejorar satisfacción del cliente
---
## 🙏 AGRADECIMIENTOS
- **Israel (Shaddai Multiservicios):** Por confiar en nuestro equipo
- **Profesores:** Por la guía y el apoyo
- **Comunidad KMP:** Por la documentación
---
**Última actualización:** Febrero 2026  
**Versión:** 1.0.0  
**Estado:** ✅ Prototipo Funcional Completado
