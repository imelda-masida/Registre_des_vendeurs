package com.example.registredesvendeurs.ui.theme.vendor

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.registredesvendeurs.model.Vendor
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class VendorViewModel : ViewModel() {

    // Liste source (privée)
    private val _vendors = MutableStateFlow<List<Vendor>>(emptyList())

    // Texte de recherche
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    // Liste filtrée automatiquement selon la recherche
    val filteredVendors = _searchQuery
        .combine(_vendors) { query, list ->
            if (query.isBlank()) list
            else list.filter { it.name.contains(query, ignoreCase = true) }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    // Utilisé par l'écran détail
    fun getVendorById(id: Int): Vendor? {
        return _vendors.value.find { it.id == id }
    }

    // Suppression d'un vendeur
    fun deleteVendor(id: Int) {
        viewModelScope.launch {
            _vendors.value = _vendors.value.filter { it.id != id }
        }
    }


    /**
     * Vérifie si un numéro de table existe déjà
     */
    fun isTableNumberAlreadyExists(tableNumber: String): Boolean {
        return _vendors.value.any { it.tableNumber == tableNumber }
    }

    /**
     * Vérifie si un nom de vendeur existe déjà (insensible à la casse)
     */
    fun isNameAlreadyExists(name: String): Boolean {
        return _vendors.value.any { it.name.equals(name, ignoreCase = true) }
    }

    /**
     * Met à jour les informations d'un vendeur existant
     */
    fun updateVendor(id: Int, name: String, table: String, category: String, imageUri: Uri?) {
        viewModelScope.launch {
            _vendors.update { currentList ->
                currentList.map { vendor ->
                    if (vendor.id == id) {
                        vendor.copy(
                            name = name,
                            tableNumber = table,
                            category = category,
                            imageUrl = imageUri?.toString() ?: vendor.imageUrl
                        )
                    } else {
                        vendor
                    }
                }
            }
            onSearchQueryChange(_searchQuery.value)
        }
    }

    /**
     * Logique d'ajout d'un vendeur
     */
    fun addVendor(
        context: Context,
        name: String,
        table: String,
        category: String,
        uri: Uri?
    ) {
        viewModelScope.launch {
            try {
                // On calcule l'ID (en attendant l'ID automatique de Supabase)
                val newId = (_vendors.value.maxOfOrNull { it.id ?: 0 } ?: 0) + 1

                // Création du vendeur
                val newVendor = Vendor(
                    id = newId,
                    name = name,
                    tableNumber = table,
                    category = category,
                    imageUrl = uri?.toString()
                )

                // Mise à jour de la liste locale
                _vendors.value = _vendors.value + newVendor

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
