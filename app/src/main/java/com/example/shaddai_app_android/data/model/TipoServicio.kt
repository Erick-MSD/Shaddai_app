package com.example.shaddai_app_android.data.model

enum class TipoServicio(
    val displayName: String,
    val colorHex: Long
) {
    PLOMERIA("Plomería", 0xFF2196F3),
    ELECTRICIDAD("Electricidad", 0xFFFFC107),
    AIRE_ACONDICIONADO("Aire Acondicionado", 0xFF00BCD4),
    REDES("Redes", 0xFF9C27B0),
    CCTV("CCTV", 0xFF607D8B),
    CARPINTERIA("Carpintería", 0xFF8D6E63),
    PINTURA("Pintura", 0xFFE91E63),
    JARDINERIA("Jardinería", 0xFF4CAF50),
    ALBANILERIA("Albañilería", 0xFFFF5722),
    CERRAJERIA("Cerrajería", 0xFF795548),
    LIMPIEZA_ESPECIALIZADA("Limpieza Especializada", 0xFF009688),
    TI_SOPORTE("Soporte TI", 0xFF3F51B5)
}

