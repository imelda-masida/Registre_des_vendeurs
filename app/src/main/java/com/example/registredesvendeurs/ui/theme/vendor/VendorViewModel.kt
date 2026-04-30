package com.example.registredesvendeurs.ui.theme.vendor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.registredesvendeurs.repository.Vendor
import kotlinx.coroutines.launch
// Assurez-vous d'importer votre client Supabase
// import com.example.registredesvendeurs.SupabaseClient.client
// import io.github.jan_tennert.supabase.postgrest.postgrest

class VendorViewModel : ViewModel() {

    // Cette fonction manquait !
    fun addVendor(name: String, tableNumber: String, category: String) {
        viewModelScope.launch {
            try {
                // Création de l'objet vendeur (ajustez selon votre data class)
                val newVendor = Vendor(
                    name = name,
                    tableNumber = tableNumber,
                    category = category,
                    imageUrl = "" // Sera mis à jour plus tard avec l'upload d'image
                )

                /*
                // Logique Supabase (à décommenter quand votre client est prêt) :
                client.postgrest["vendors"].insert(newVendor)
                */

                println("Vendeur ajouté localement : $name")

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Ajoutez également cette fonction si elle manque pour MainActivity
    fun getVendorById(id: Int): Vendor? {
        // Logique pour trouver un vendeur dans votre liste actuelle
        return null
    }
}