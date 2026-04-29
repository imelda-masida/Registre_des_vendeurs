package com.example.registredesvendeurs.repository

import com.example.registredesvendeurs.SupabaseClient
import com.example.registredesvendeurs.SupabaseClient.client
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Vendor(
    val id: Int? = null,
    val name: String,
    @SerialName("table_number")
    val tableNumber: String,
    val category: String,
    @SerialName("image_url")
    val imageUrl: String? = null
)

class VendorRepository {

    /**
     * LECTURE : Récupère la liste de tous les vendeurs
     */
    suspend fun getVendors(): List<Vendor> = withContext(Dispatchers.IO) {
        // CORRECTION : On enlève le mot-clé 'return' inutile ici
        // La dernière expression du bloc est automatiquement retournée
        SupabaseClient.client.from("vendors")
            .select()
            .decodeList<Vendor>()
    }

    /**
     * CRÉATION : Ajoute un nouveau vendeur
     */
    suspend fun addVendor(vendor: Vendor) = withContext(Dispatchers.IO) {
        SupabaseClient.client.from("vendors").insert(vendor)
    }

    /**
     * SUPPRESSION : Supprime un vendeur par son ID
     */
    suspend fun deleteVendor(id: Int) = withContext(Dispatchers.IO) {
        SupabaseClient.client.from("vendors").delete {
            filter {
                eq("id", id)
            }
        }
    }

    /**
     * STOCKAGE : Upload l'image et retourne son URL Publique
     */
    suspend fun uploadAndGetUrl(fileName: String, byteArray: ByteArray): String = withContext(Dispatchers.IO) {
        val bucket = SupabaseClient.client.storage.from("images")

        // Upload
        bucket.upload(path = fileName, data = byteArray, upsert = true)

        // Retourne l'URL publique
        bucket.publicUrl(fileName)
    }
}