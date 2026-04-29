package com.example.registredesvendeurs.repository

import com.example.registredesvendeurs.SupabaseClient

import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VendorRepository {
    private val client = SupabaseClient.client

    // LECTURE : Récupère tous les vendeurs
    suspend fun getVendors(): List<Vendor> = withContext(Dispatchers.IO) {
        client.from("vendors").select().decodeList<Vendor>()
    }

    // CRÉATION : Insère un nouveau vendeur
    suspend fun addVendor(vendor: Vendor) = withContext(Dispatchers.IO) {
        client.from("vendors").insert(vendor)
    }

    // MISE À JOUR : Modifie un vendeur existant (Requis par le cahier des charges)
    suspend fun updateVendor(vendor: Vendor) = withContext(Dispatchers.IO) {
        client.from("vendors").update(vendor) {
            filter { eq("id", vendor.id ?: 0) }
        }
    }

    // SUPPRESSION : Supprime un vendeur par son ID
    suspend fun deleteVendor(id: Int) = withContext(Dispatchers.IO) {
        client.from("vendors").delete {
            filter { eq("id", id) }
        }
    }

    // STORAGE : Upload l'image et retourne son URL publique
    suspend fun uploadImage(fileName: String, bytes: ByteArray): String = withContext(Dispatchers.IO) {
        val bucket = client.storage.from("images")
        bucket.upload(fileName, bytes, upsert = true)
        bucket.publicUrl(fileName)
    }
}