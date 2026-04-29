package com.example.registredesvendeurs.ui.theme.vendor

import androidx.compose.animation.core.copy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

// On importe le modèle uniquement depuis le repository
import com.example.registredesvendeurs.repository.Vendor
import com.example.registredesvendeurs.repository.VendorRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel pour la gestion des vendeurs.
 * Conforme au cahier des charges : MVVM, StateFlow, et logique de recherche réactive.
 */
class VendorViewModel(private val repository: VendorRepository) : ViewModel() {

    // Liste brute des vendeurs venant de Supabase
    private val _vendors = MutableStateFlow<List<Vendor>>(emptyList())

    // Requête de recherche saisie par l'utilisateur
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    // État de chargement pour le bouton enregistrer
    private val _isSaving = MutableStateFlow(false)
    val isSaving = _isSaving.asStateFlow()

    /**
     * RECHERCHE DYNAMIQUE (Cahier des charges section 2)
     * Combine la liste totale et la requête pour filtrer en temps réel.
     */
    val filteredVendors = combine(_vendors, _searchQuery) { list, query ->
        if (query.isBlank()) {
            list
        } else {
            list.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.tableNumber.contains(query, ignoreCase = true)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    /**
     * Charge les vendeurs depuis le repository (Supabase PostgreSQL)
     */
    fun loadVendors() {
        viewModelScope.launch {
            try {
                _vendors.value = repository.getVendors()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Met à jour la requête de recherche
     */
    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    /**
     * CRUD : CREATE ou UPDATE (Logique unifiée)
     */
    fun saveVendor(vendor: Vendor, imageBytes: ByteArray?, onComplete: () -> Unit) {
        viewModelScope.launch {
            _isSaving.value = true
            try {
                var finalUrl = vendor.imageUrl

                // Upload d'image si nécessaire
                if (imageBytes != null) {
                    val fileName = "vendor_${System.currentTimeMillis()}.jpg"
                    finalUrl = repository.uploadImage(fileName, imageBytes)
                }

                // Utilisation de .copy() du Data Class Vendor
                val vendorToSave = vendor.copy(imageUrl = finalUrl)

                if (vendorToSave.id == null) {
                    repository.addVendor(vendorToSave)
                } else {
                    repository.updateVendor(vendorToSave)
                }

                loadVendors()
                onComplete()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isSaving.value = false
            }
        }
    }

    /**
     * CRUD : DELETE
     */
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

    /**
     * Récupère un vendeur par son ID pour l'édition ou les détails
     */
    fun getVendorById(id: Int): Vendor? {
        return _vendors.value.find { it.id == id }
    }
}