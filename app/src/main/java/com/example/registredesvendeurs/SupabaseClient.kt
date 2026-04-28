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
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im52ZmVnd3ZqcXJndWVvamZlaWRhIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzcyMjE2MjYsImV4cCI6MjA5Mjc5NzYyNn0.-hlAJa_OHvby_MoaSNcY6Id7oTRt6-5d8NFfLkQS1Oc"                      // Clé API (service role ou anon)
    ) {
        install(Postgrest)   // Active le module PostgREST (CRUD sur la base)
        install(Storage)     // Active le module Storage (upload d’images)
    }
}


