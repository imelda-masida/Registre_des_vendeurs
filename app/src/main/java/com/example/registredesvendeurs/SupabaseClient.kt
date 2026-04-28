package com.example.registredesvendeurs

// Import des librairies nécessaires
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

// Objet singleton pour gérer la connexion Supabase
object SupabaseClient {

    // Crée une instance du client Supabase
    val client = createSupabaseClient(
        supabaseUrl = "https://nvfegwvjqrgueojfeida.supabase.co",   // URL de ton projet Supabase
        supabaseKey = "sb_publishable_F_I3rl27LGejP2R8_1iloQ_fVjSRnW8"                      // Clé API (service role ou anon)
    ) {
        install(Postgrest)   // Active le module PostgREST (CRUD sur la base)
        install(Storage)     // Active le module Storage (upload d’images)
    }
}


