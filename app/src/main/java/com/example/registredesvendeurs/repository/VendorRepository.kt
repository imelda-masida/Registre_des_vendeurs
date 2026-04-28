package com.example.registredesvendeurs.repository


import com.example.registredesvendeurs.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// 1. AJOUT de @Serializable (Obligatoire pour Supabase)
@Serializable
data class Vendor(
    val id: Int? = null, // Mis en optionnel pour les nouveaux vendeurs
    val name: String,

    // 2. CORRECTION : Liaison avec le nom exact de la colonne dans Supabase
    @SerialName("table_number")
    val tableNumber: String,

    val category: String,

    @SerialName("image_url")
    val imageUrl: String? = null
)

class VendorRepository {

    // Lecture (Read)
    suspend fun getVendors(): List<Vendor> = withContext(Dispatchers.IO) {
        SupabaseClient.client.from("vendors")
            .select()
            .decodeList<Vendor>()
    }

    // Création (Create)
    suspend fun addVendor(vendor: Vendor) = withContext(Dispatchers.IO) {
        SupabaseClient.client.from("vendors").insert(vendor)
    }

    // Mise à jour (Update)
    suspend fun updateVendor(vendor: Vendor) = withContext(Dispatchers.IO) {
        SupabaseClient.client.from("vendors").update(vendor) {
            // CORRECTION : Utilisation du bloc filter
            filter {
                eq("id", vendor.id ?: 0)
            }
        }
    }

    // Suppression (Delete)
    suspend fun deleteVendor(id: Int) = withContext(Dispatchers.IO) {
        SupabaseClient.client.from("vendors").delete {
            // CORRECTION : Utilisation du bloc filter
            filter {
                eq("id", id)
            }
        }
    }
}