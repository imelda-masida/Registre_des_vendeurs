

package com.example.registredesvendeurs.repository
import com.example.registredesvendeurs.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 1. MODÈLE DE DONNÉES (Vendor)
 * @Serializable : Permet de convertir cette classe en JSON pour Supabase.
 */
@Serializable
data class Vendor(
    val id: Int? = null,             // ID auto-incrémenté par Supabase (null pour les nouveaux)
    val name: String,                // Nom du vendeur

    @SerialName("table_number")      // Fait le lien entre 'tableNumber' en Kotlin et 'table_number' dans Supabase
    val tableNumber: String,

    val category: String,            // Catégorie de produits

    @SerialName("image_url")         // Lien vers l'image stockée dans le Storage
    val imageUrl: String? = null
)

/**
 * 2. REPOSITORY
 * Gère toutes les opérations avec la base de données Supabase.
 */
class VendorRepository {

    /**
     * LECTURE : Récupère la liste de tous les vendeurs
     */
    suspend fun getVendors(): List<Vendor> = withContext(Dispatchers.IO) {
        SupabaseClient.client.from("vendors") // Cible la table 'vendors'
            .select()                         // Sélectionne toutes les colonnes
            .decodeList<Vendor>()             // Transforme le JSON en liste d'objets Vendor
    }

    /**
     * CRÉATION : Ajoute un nouveau vendeur dans la table
     */
    suspend fun addVendor(vendor: Vendor) = withContext(Dispatchers.IO) {
        SupabaseClient.client.from("vendors").insert(vendor)
    }

    /**
     * MISE À JOUR : Modifie un vendeur existant
     */
    suspend fun updateVendor(vendor: Vendor) = withContext(Dispatchers.IO) {
        SupabaseClient.client.from("vendors").update(vendor) {
            filter {
                eq("id", vendor.id ?: 0) // Trouve le vendeur par son ID
            }
        }
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
     * CORRECTION : Correction de la syntaxe de l'upload pour la version 2.x
     */
    suspend fun uploadAndGetUrl(fileName: String, byteArray: ByteArray): String = withContext(Dispatchers.IO) {
        // Accès au service de stockage (Storage) et au bucket "images"
        val bucket = SupabaseClient.client.storage.from("images")

        // 1. Upload du fichier vers Supabase
        // Note : Dans la v2.x, upsert est un paramètre direct, pas un bloc de configuration
        bucket.upload(path = fileName, data = byteArray, upsert = true)

        // 2. Génération de l'URL publique
        val url = bucket.publicUrl(fileName)

        return@withContext url
    }
}