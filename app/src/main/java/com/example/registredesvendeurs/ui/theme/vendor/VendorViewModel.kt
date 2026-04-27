package com.example.registredesvendeurs.ui.theme.vendor



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.registredesvendeurs.SupabaseInstance
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// 1. MODÈLE COMPLET (Ajout de category et SerialName pour imageUrl)
@Serializable
data class Vendor(
    val id: Int? = null,
    val name: String,
    @SerialName("table_number")
    val tableNumber: String,
    val category: String, // AJOUTÉ : Requis par le cahier des charges
    @SerialName("image_url") // Correction : assure la correspondance avec Supabase
    val imageUrl: String? = null
)

// 2. REPOSITORY AMÉLIORÉ (CRUD COMPLET)
class VendorRepository {
    private val postgrest = SupabaseInstance.client.postgrest["vendors"]

    suspend fun getAllVendors(): List<Vendor> = postgrest.select().decodeList<Vendor>()

    suspend fun insertVendor(vendor: Vendor) = postgrest.insert(vendor)

    // AJOUTÉ : Mise à jour (Update)
    suspend fun updateVendor(vendor: Vendor) {
        postgrest.update(vendor) {
            filter { eq("id", vendor.id ?: 0) }
        }
    }

    // AJOUTÉ : Suppression (Delete)
    suspend fun deleteVendor(id: Int) {
        postgrest.delete {
            filter { eq("id", id) }
        }
    }
}

// 3. VIEWMODEL RÉACTIF
class VendorViewModel(private val repository: VendorRepository) : ViewModel() {

    private val _allVendors = MutableStateFlow<List<Vendor>>(emptyList())
    private val _searchQuery = MutableStateFlow("")
    private val _isLoading = MutableStateFlow(false)

    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // RECHERCHE DYNAMIQUE (Filtrage sur Nom OU Table OU Catégorie)
    val filteredVendors = combine(_allVendors, _searchQuery) { vendors, query ->
        if (query.isBlank()) {
            vendors
        } else {
            vendors.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.tableNumber.contains(query) ||
                        it.category.contains(query, ignoreCase = true)
            }
        }
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
            _isLoading.value = true
            try {
                _allVendors.value = repository.getAllVendors()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addVendor(name: String, table: String, category: String) {
        viewModelScope.launch {
            try {
                val newVendor = Vendor(
                    name = name,
                    tableNumber = table, // Assurez-vous que le nom dans Vendor.kt est tableNumber
                    category = category,
                    imageUrl = null
                )
                repository.insertVendor(newVendor)
                loadVendors() // Recharge la liste pour voir le nouveau vendeur
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // SUPPRESSION (Delete)
    fun deleteVendor(id: Int) {
        viewModelScope.launch {
            try {
                repository.deleteVendor(id)
                loadVendors()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    // UPLOAD IMAGE ET SAUVEGARDE (Create / Update)
    fun uploadImageAndSaveVendor(vendor: Vendor, imageBytes: ByteArray?) {
        viewModelScope.launch {
            try {
                var finalImageUrl = vendor.imageUrl

                // Si une nouvelle image est fournie, on l'upload
                if (imageBytes != null) {
                    val fileName = "etalage_${System.currentTimeMillis()}.jpg"
                    val bucket = SupabaseInstance.client.storage.from("images")
                    bucket.upload(fileName, imageBytes)
                    finalImageUrl = bucket.publicUrl(fileName)
                }

                val finalVendor = vendor.copy(imageUrl = finalImageUrl)

                // Si l'id existe, c'est un Update, sinon c'est un Insert
                if (finalVendor.id != null) {
                    repository.updateVendor(finalVendor)
                } else {
                    repository.insertVendor(finalVendor)
                }

                loadVendors()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}