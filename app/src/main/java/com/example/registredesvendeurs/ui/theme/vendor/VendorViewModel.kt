package com.example.registredesvendeurs.ui.theme.vendor

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

    // Logique d'ajout (à lier avec Supabase)
    fun addVendor(name: String, table: String, category: String, uri: Uri?) {
        viewModelScope.launch {
            val newId = (_vendors.value.maxOfOrNull { it.id ?: 0 } ?: 0) + 1
            val newVendor = Vendor(newId, name, table, category, uri?.toString())
            _vendors.value += newVendor
        }
    }
}