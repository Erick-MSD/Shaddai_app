package com.example.shaddai_app.data.model

/**
 * Modelo temporal (sin Realm).
 *
 * Cuando se re-habilite Realm, este archivo puede volver a ser RealmObject.
 */
data class Task(
    val id: String,
    val description: String,
    val isCompleted: Boolean,
    val ownerId: String
)
