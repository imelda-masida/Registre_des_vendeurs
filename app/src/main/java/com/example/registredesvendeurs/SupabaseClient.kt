package com.example.registredesvendeurs

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

object SupabaseInstance {
    val client = createSupabaseClient(
        supabaseUrl = "https://nvfegwvjqrgueojfeida.supabase.co",
        supabaseKey = "sb_publishable_F_I3rl27LGejP2R8_1iloQ_fVjSRnW8"
    ) {
        // CORRECTION : Utilisation des modules compatibles version 2.0.0
        install(Postgrest)
        install(Storage)

    }
}