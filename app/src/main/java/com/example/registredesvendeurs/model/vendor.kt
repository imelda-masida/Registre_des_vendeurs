package com.example.registredesvendeurs.model



import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Vendor(
    val id: Int? = null,
    val name: String,
    @SerialName("table_number")
    val tableNumber: String,
    val category: String, // Requis par le cahier des charges
    @SerialName("image_url")
    val imageUrl: String? = null
)
