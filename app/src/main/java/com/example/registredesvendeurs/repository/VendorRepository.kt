package com.example.registredesvendeurs.repository


import com.example.registredesvendeurs.SupabaseInstance
import com.example.registredesvendeurs.model.Vendor
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers

class VendorRepository {
    private val client = SupabaseInstance.client

    // Lire (Read) : Récupérer tous les vendeurs
    suspend fun getAllVendors(): List<Vendor> {
        return withContext(Dispatchers.IO) {
            client.from("vendors").select().decodeList<Vendor>()
        }
    }

    // Créer (Create) : Ajouter un vendeur
    suspend fun insertVendor(vendor: Vendor) {
        SupabaseInstance.client.postgrest["vendors"].insert(vendor)

    }
}