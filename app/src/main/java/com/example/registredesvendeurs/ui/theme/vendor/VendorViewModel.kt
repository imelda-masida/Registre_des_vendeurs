package com.example.registredesvendeurs.ui.theme.vendor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.registredesvendeurs.repository.Vendor
import com.example.registredesvendeurs.repository.VendorRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel pour gérer la logique métier de l'écran des vendeurs.
 * Gère la recherche, le chargement, l'enregistrement et la suppression.
 */
class VendorViewModel(private val repository: VendorRepository) : ViewModel() {

    // --- ÉTATS PRIVÉS (StateFlow pour la réactivité) ---
    private val _vendors = MutableStateFlow<List<Vendor>>(emptyList())
    private val _searchQuery = MutableStateFlow("")
    private val _showSuccessMessage = MutableStateFlow(false)

    // États pour gérer l'interface pendant l'enregistrement
    private val _isSaving = MutableStateFlow(false)
    private val _isSaved = MutableStateFlow(false)

    // --- ÉTATS PUBLICS (Exposés à l'UI en lecture seule) ---
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    val showSuccessMessage: StateFlow<Boolean> = _showSuccessMessage.asStateFlow()
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()
    val isSaved: StateFlow<Boolean> = _isSaved.asStateFlow()

    /**
     * Liste filtrée en temps réel. combine() fusionne la liste totale et la recherche.
     */
    val filteredVendors: StateFlow<List<Vendor>> = combine(_vendors, _searchQuery) { vendors, query ->
        if (query.isBlank()) {
            vendors
        } else {
            vendors.filter { vendor ->
                vendor.name.contains(query, ignoreCase = true) ||
                        vendor.tableNumber.contains(query, ignoreCase = true)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        loadVendors() // Charger les données dès le démarrage
    }

    /**
     * Récupère la liste des vendeurs depuis la table PostgreSQL de Supabase
     */
    // Dans VendorViewModel.kt
    fun loadVendors() {
        viewModelScope.launch {
            try {
                val result = repository.getVendors()
                _vendors.value = result
                println("DEBUG: ${result.size} vendeurs récupérés")
            } catch (e: Exception) {
                println("ERREUR CRITIQUE: ${e.message}")
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
     * ENREGISTREMENT COMPLET :
     * 1. Upload l'image vers Storage
     * 2. Enregistre les données textuelles vers PostgreSQL
     */
    fun saveVendor(name: String, table: String, category: String, imageBytes: ByteArray?) {
        viewModelScope.launch {
            _isSaving.value = true // Affiche le cercle de chargement dans l'UI
            try {
                var finalImageUrl: String? = null

                // 1. GESTION DE L'IMAGE
                if (imageBytes != null) {
                    val fileName = "vendor_${System.currentTimeMillis()}.jpg"
                    // On récupère l'URL publique après l'upload
                    finalImageUrl = repository.uploadAndGetUrl(fileName, imageBytes)
                }

                // 2. PRÉPARATION DE L'OBJET
                val newVendor = Vendor(
                    name = name,
                    tableNumber = table,
                    category = category,
                    imageUrl = finalImageUrl
                )

                // 3. ENREGISTREMENT EN BASE DE DONNÉES
                repository.addVendor(newVendor)

                // 4. ACTIONS DE SUCCÈS
                loadVendors() // Rafraîchir la liste en arrière-plan
                _isSaved.value = true // Déclenche la navigation retour dans AddVendorScreen
                _showSuccessMessage.value = true // Prépare l'affichage du message de succès

            } catch (e: Exception) {
                // Log important pour le débogage dans Logcat
                println("ERREUR SUPABASE: ${e.localizedMessage}")
                e.printStackTrace()
            } finally {
                _isSaving.value = false // Arrête le chargement quoi qu'il arrive
            }
        }
    }

    /**
     * Réinitialise l'état après que la navigation retour a été effectuée
     */
    fun resetSaveState() {
        _isSaved.value = false
    }

    /**
     * Réinitialise le message de succès (Snackbar)
     */
    fun resetSuccessMessage() {
        _showSuccessMessage.value = false
    }

    /**
     * Supprime un vendeur de Supabase
     */
    fun deleteVendor(id: Int) {
        viewModelScope.launch {
            try {
                repository.deleteVendor(id)
                loadVendors() // Rafraîchir la liste après suppression
            } catch (e: Exception) {
                println("ERREUR SUPPRESSION: ${e.localizedMessage}")
                e.printStackTrace()
            }
        }
    }
}
