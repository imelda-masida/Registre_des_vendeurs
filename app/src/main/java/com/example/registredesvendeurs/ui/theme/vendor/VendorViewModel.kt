package com.example.registredesvendeurs.ui.theme.vendor


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.registredesvendeurs.SupabaseInstance
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage // Assurez-vous que cet import est présent
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Vendor(
    val id: Int? = null,
    val name: String,
    @SerialName("table_number")
    val tableNumber: String,
    val imageUrl: String? = null
)

class VendorRepository {
    suspend fun getAllVendors(): List<Vendor> {
        return SupabaseInstance.client.postgrest["vendors"]
            .select()
            .decodeList<Vendor>()
    }

    suspend fun insertVendor(vendor: Vendor) {
        SupabaseInstance.client.postgrest["vendors"].insert(vendor)
    }
}

class VendorViewModel(private val repository: VendorRepository) : ViewModel() {

    private val _allVendors = MutableStateFlow<List<Vendor>>(emptyList())
    private val _searchQuery = MutableStateFlow("")

    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val filteredVendors = _allVendors.combine(_searchQuery) { vendors, query ->
        if (query.isBlank()) vendors else vendors.filter { it.name.contains(query, ignoreCase = true) }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        loadVendors()
    }

    fun loadVendors() {
        viewModelScope.launch {
            try {
                val result = repository.getAllVendors()
                _allVendors.value = result
            } catch (e: Exception) {
                println("Erreur de chargement : ${e.message}")
            }
        }
    }

    fun addVendor(name: String, table: String) {
        viewModelScope.launch {
            try {
                val newVendor = Vendor(name = name, tableNumber = table)
                repository.insertVendor(newVendor)
                loadVendors()
            } catch (e: Exception) {
                println("Erreur d'ajout : ${e.message}")
            }
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }


    fun uploadImageAndSaveVendor(vendor: Vendor, imageBytes: ByteArray) {
        viewModelScope.launch { // Maintenant reconnu car dans le ViewModel
            try {
                // 1. Nom unique pour l'image
                val fileName = "etalage_${System.currentTimeMillis()}.jpg"

                // 2. Accès au Storage Supabase
                val bucket = SupabaseInstance.client.storage.from("images")

                // 3. Upload du fichier
                bucket.upload(fileName, imageBytes)

                // 4. Récupération de l'URL publique
                val publicUrl = bucket.publicUrl(fileName)

                // 5. Enregistrement final avec l'URL de l'image
                val finalVendor = vendor.copy(imageUrl = publicUrl)
                repository.insertVendor(finalVendor) // Maintenant reconnu

                // 6. Rafraîchir la liste
                loadVendors() // Maintenant reconnu
            } catch (e: Exception) {
                println("Erreur upload : ${e.message}")
            }
        }
    }
}