package com.example.registredesvendeurs

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

object SupabaseInstance {
    val client = createSupabaseClient(
        supabaseUrl = "https://nvfegwvjqrgueojfeida.supabase.co/rest/v1/",
        supabaseKey = "sb_publishable_F_I3rl27LGejP2R8_1iloQ_fVjSRnW8"
    ) {
        // On active les modules dont on a besoin
        install(Postgrest) // Pour la base de données (CRUD)
        install(Storage)   // Pour l'upload des photos d'étalages
    }
} 