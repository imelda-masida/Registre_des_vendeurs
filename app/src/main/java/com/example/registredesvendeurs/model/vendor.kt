package com.example.registredesvendeurs.model

import kotlinx.serialization.Serializable

@Serializable
data class Vendor(
    val id: Int? = null,// Nullable car Supabase génère l'ID lors de la création
    val name: String,
    val tableNumber: String,
    val category: String,
    val imageUrl: String? = null
)