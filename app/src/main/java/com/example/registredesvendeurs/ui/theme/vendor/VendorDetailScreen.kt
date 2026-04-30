package com.example.registredesvendeurs.ui.theme.vendor

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VendorDetailScreen(viewModel: VendorViewModel, vendorId: Int, onEdit: (Int) -> Unit, onBack: () -> Unit) {
    val vendor = remember(vendorId) { viewModel.getVendorById(vendorId) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(vendor?.name ?: "Détail") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) } }
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(20.dp)) {
            if (vendor != null) {
                Text("Vendeur : ${vendor.name}", style = MaterialTheme.typography.headlineMedium)
                Text("Emplacement : Table ${vendor.tableNumber}")
                Text("Catégorie : ${vendor.category}")
            } else {
                Text("Erreur : Vendeur introuvable")
            }
        }
    }
}