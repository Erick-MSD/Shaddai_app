# 📅 Pantalla de Calendario para Técnicos - Shaddai App

## 🎯 Descripción General

La pantalla de calendario permite a los técnicos visualizar y gestionar sus servicios programados por día, semana o mes. La implementación sigue el patrón **MVVM** (Model-View-ViewModel) y está diseñada para ser multiplataforma usando **Kotlin Multiplatform** y **Compose Multiplatform**.

## 📁 Estructura del Proyecto

```
composeApp/src/commonMain/
├── composeResources/
│   └── files/
│       └── services_data.csv          # Datos de servicios (temporal)
├── kotlin/com/example/shaddai_app/
    ├── data/
    │   ├── model/
    │   │   ├── Tecnico.kt             # Data class para técnico
    │   │   └── EventoServicio.kt      # Data class para eventos y enum TipoServicio
    │   └── repository/
    │       ├── ServiciosRepository.kt      # Interfaz del repositorio
    │       └── CsvServiciosRepository.kt   # Implementación con CSV
    └── ui/
        └── calendario/
            ├── CalendarioState.kt     # Estados y eventos de la UI
            ├── CalendarioViewModel.kt # Lógica de negocio
            └── CalendarioScreen.kt    # Componente UI
```

## 🏗️ Arquitectura

### 1. **Capa de Datos (Data Layer)**

#### **Modelos**
- **`Tecnico`**: Representa un técnico del sistema
  - Propiedades: `id`, `nombre`
  - Métodos: `obtenerSaludo()`, `esIdValido()`

- **`EventoServicio`**: Representa un servicio programado
  - Propiedades: serviceId, ticketNumber, tipoServicio, descripcion, ubicación, fecha, horas, etc.
  - Métodos: `obtenerRangoTiempo()`, `obtenerDuracionFormateada()`, `perteneceATecnico()`

- **`TipoServicio`** (Enum): Tipos de servicios disponibles
  - PLOMERIA, ELECTRICIDAD, AIRE_ACONDICIONADO, REDES, CCTV, etc.
  - Cada tipo tiene un `displayName` y `colorHex` asociado

#### **Repositorio**
- **`ServiciosRepository`** (Interfaz): Define el contrato para acceder a datos
  - `obtenerEventos()`: Obtiene todos los eventos
  - `obtenerEventosPorFecha()`: Filtra eventos por fecha
  - `obtenerEventosPorTecnico()`: Filtra eventos por técnico
  - `obtenerTecnico()`: Obtiene información de un técnico

- **`CsvServiciosRepository`** (Implementación temporal):
  - Lee datos desde `services_data.csv`
  - Parsea el CSV y convierte a objetos Kotlin
  - Incluye manejo de excepciones y null safety
  - **Preparado para ser reemplazado** por implementaciones SQL/NoSQL

### 2. **Capa de Presentación (Presentation Layer)**

#### **ViewModel**
- **`CalendarioViewModel`**: Gestiona el estado y lógica de negocio
  - Usa **StateFlow** para emitir cambios de estado
  - Maneja eventos de la UI (cambio de fecha, vista, etc.)
  - Utiliza **coroutines** para operaciones asíncronas
  - Implementa funciones de orden superior para filtrado

#### **State Management**
- **`CalendarioState`**: Estado inmutable de la UI
  - tecnicoActual, fechaSeleccionada, eventos, vistaCalendario, isLoading, error

- **`CalendarioEvent`** (Sealed Class): Eventos que puede generar la UI
  - CambiarFecha, CambiarVista, SeleccionarEvento, navegación, etc.

#### **UI Components**
- **`CalendarioScreen`**: Pantalla principal
- **`CalendarioTopBar`**: Barra superior con saludo
- **`MesSelectorRow`**: Selector de mes con navegación
- **`SemanaSelectorRow`**: Selector de días de la semana
- **`EventoCard`**: Card individual para cada servicio
- **`HoraComidaCard`**: Card especial para hora de comida
- **`BottomNavigationBar`**: Navegación inferior

## 🎨 Diseño Visual

### Paleta de Colores
- **Fondo principal**: `#F0F9FF` (Celeste claro)
- **Barra superior**: `#1565C0` (Azul oscuro)
- **Día seleccionado**: `#1E88E5` (Azul medio)
- **Cards**: Colores según tipo de servicio
  - Plomería: Verde `#4CAF50`
  - Electricidad: Amarillo `#FFC107`
  - Clima: Azul `#2196F3`
  - etc.

### Componentes Principales
1. **Header**: "Hola, [Nombre del Técnico]"
2. **Selector de Mes**: ENERO 2026 con flechas de navegación
3. **Selector de Semana**: LUN 20, MAR 21, MIE 22, etc.
4. **Lista de Eventos**: Cards con información del servicio
5. **Bottom Navigation**: Iconos para Home, Herramientas, Calendario, Soporte

## 📊 Requisitos Cumplidos

### ✅ Requisitos de Kotlin
- [x] **Funciones con parámetros y retorno**: `obtenerSaludo()`, `filtrarEventosPorFecha()`, etc.
- [x] **Colecciones**: List, Map para almacenar eventos y técnicos
- [x] **Manejo de excepciones**: try-catch en parseo de CSV
- [x] **Null safety**: Uso de `?`, `?.`, `?:` en todo el código
- [x] **Clases**: `Tecnico`, `EventoServicio`, `EventoComida`
- [x] **Objeto**: `TipoServicio` (enum object)
- [x] **Interfaz**: `ServiciosRepository`
- [x] **Funciones de orden superior**: `filter`, `map`, `sortedBy` en ViewModel

### ✅ Arquitectura MVVM
- [x] ViewModel para lógica de calendario
- [x] Data classes para modelos
- [x] Repository pattern para abstracción de datos

### ✅ Funcionalidades
- [x] Navegación entre días
- [x] Selector de semana
- [x] Vista de eventos por día
- [x] Cards con información completa del servicio
- [x] Iconos según tipo de servicio
- [x] Hora de comida fija
- [x] Bottom navigation bar

## 🔄 Integración con CSV

El archivo CSV (`services_data.csv`) está ubicado en:
```
composeApp/src/commonMain/composeResources/files/services_data.csv
```

### Estructura del CSV
El CSV contiene 45 columnas con información completa de cada servicio:
- service_id, ticket_number, estado, prioridad
- tipo_servicio, subservicio, descripcion
- cliente_id, cliente_nombre
- ubicacion_direccion, ciudad, estado, CP
- programacion_fecha_programada, ventana_inicio, ventana_fin
- tecnico_id, tecnico_nombre
- tiempo_estimado_min, precio_estimado
- Y más...

## 🚀 Próximos Pasos

### Migración a Base de Datos

#### SQL (Room / PostgreSQL)
```kotlin
class SqlServiciosRepository(
    private val database: AppDatabase
) : ServiciosRepository {
    override suspend fun obtenerEventos(): List<EventoServicio> {
        return database.eventosDao().getAll()
    }
    // ... más métodos
}
```

#### NoSQL (Firebase / MongoDB)
```kotlin
class FirebaseServiciosRepository(
    private val firestore: FirebaseFirestore
) : ServiciosRepository {
    override suspend fun obtenerEventos(): List<EventoServicio> {
        return firestore.collection("eventos")
            .get()
            .await()
            .toObjects(EventoServicio::class.java)
    }
    // ... más métodos
}
```

### Cambio de Repositorio
Solo necesitas cambiar la instancia en el ViewModel:
```kotlin
// Antes:
val repository = CsvServiciosRepository()

// Después (SQL):
val repository = SqlServiciosRepository(database)

// O (NoSQL):
val repository = FirebaseServiciosRepository(firestore)
```

## 📱 Uso en la Aplicación

### Integración en App.kt

```kotlin
import com.example.shaddai_app.data.repository.CsvServiciosRepository
import com.example.shaddai_app.ui.calendario.CalendarioScreen
import com.example.shaddai_app.ui.calendario.CalendarioViewModel

@Composable
fun App() {
    ShaddaiTheme {
        val repository = remember { CsvServiciosRepository() }
        val viewModel = remember { CalendarioViewModel(repository) }
        
        CalendarioScreen(
            viewModel = viewModel,
            onNavigateHome = { /* Navegar a Home */ },
            onNavigateHerramientas = { /* Navegar a Herramientas */ },
            onNavigateSoporte = { /* Navegar a Soporte */ }
        )
    }
}
```

## 🛠️ Dependencias Agregadas

En `libs.versions.toml`:
```toml
[versions]
kotlinx-datetime = "0.6.0"

[libraries]
kotlinx-datetime = { module = "org.jetbrains.kotlinx:kotlinx-datetime", version.ref = "kotlinx-datetime" }
```

En `composeApp/build.gradle.kts`:
```kotlin
commonMain.dependencies {
    // ... otras dependencias
    implementation(libs.kotlinx.datetime)
}
```

## 🎓 Conceptos Aplicados

1. **Kotlin Multiplatform**: Código compartido entre Android, iOS y Desktop
2. **Compose Multiplatform**: UI declarativa multiplataforma
3. **MVVM**: Separación de responsabilidades
4. **Repository Pattern**: Abstracción de fuentes de datos
5. **State Management**: StateFlow para manejo reactivo de estado
6. **Coroutines**: Operaciones asíncronas
7. **Null Safety**: Prevención de NullPointerException
8. **Sealed Classes**: Para eventos tipo-seguro
9. **Data Classes**: Para modelos inmutables
10. **Higher-Order Functions**: filter, map, sortedBy

## 📝 Notas

- El CSV es temporal y debe moverse a una base de datos en producción
- El técnico actual está hardcodeado como "TEC-017" (J. Martinez)
- Los iconos de servicios usan emojis (pueden reemplazarse por SVG/PNG)
- La navegación inferior es funcional pero las rutas están pendientes
- El diseño sigue fielmente la imagen proporcionada

---

**Desarrollado con ❤️ usando Kotlin Multiplatform y Compose**
