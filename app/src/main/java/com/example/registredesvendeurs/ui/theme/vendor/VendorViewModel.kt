package com.example.registredesvendeurs.ui.theme.vendor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.registredesvendeurs.model.Vendor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VendorViewModel : ViewModel() {
    // État de l'interface : liste des vendeurs
    private val _vendors = MutableStateFlow<List<Vendor>>(emptyList())
    val vendors: StateFlow<List<Vendor>> = _vendors.asStateFlow()

    // Variable pour la recherche
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    fun updateSearchQuery(newQuery: String) {
        _searchQuery.value = newQuery
        // Logique de filtrage ici
    }

    init {
        fetchVendors()
    }

    private fun fetchVendors() {
        viewModelScope.launch {
            // Appel Supabase ici
            // _vendors.value = supabase.from("vendors").select().decodeList<Vendor>()
        }
    }
}