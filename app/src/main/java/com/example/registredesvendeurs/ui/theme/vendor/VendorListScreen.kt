package com.example.registredesvendeurs.ui.theme.vendor

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.registredesvendeurs.model.Vendor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VendorListScreen(
    viewModel: VendorViewModel,
    onNavigateToAdd: () -> Unit,
    onNavigateToEdit: (Int) -> Unit,
    onNavigateToDetail: (Int) -> Unit
) {
    val vendors by viewModel.filteredVendors.collectAsState()
    val query by viewModel.searchQuery.collectAsState()
    var active by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
                CenterAlignedTopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // LOGO DE MARCHÉ
                            Icon(Icons.Default.Storefront, null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(Modifier.width(12.dp))
                            Text("Registre des vendeurs", fontWeight = FontWeight.ExtraBold)
                        }
                    }
                )
                SearchBar(
                    query = query,
                    onQueryChange = { viewModel.onSearchQueryChange(it) },
                    onSearch = { active = false },
                    active = active,
                    onActiveChange = { active = it },
                    placeholder = { Text("Rechercher un vendeur...") },
                    leadingIcon = { Icon(Icons.Default.Search, null) },
                    trailingIcon = {
                        if (query.isNotEmpty()) {
                            IconButton(onClick = { viewModel.onSearchQueryChange("") }) {
                                Icon(Icons.Default.Clear, null)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = if (active) 0.dp else 16.dp).padding(bottom = 8.dp)
                ) { }
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNavigateToAdd,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, null)
                Spacer(Modifier.width(8.dp))
                Text("Nouveau Vendeur")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
            items(vendors, key = { it.id ?: 0 }) { vendor ->
                VendorCardElegant(
                    vendor = vendor,
                    onClick = { vendor.id?.let { onNavigateToDetail(it) } },
                    onDelete = { vendor.id?.let { viewModel.deleteVendor(it) } }
                )
            }
        }
    }
}

@Composable
fun VendorCardElegant(vendor: Vendor, onClick: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp).clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = if (vendor.imageUrl.isNullOrBlank()) "https://ui-avatars.com/api/?name=${vendor.name}&background=random" else vendor.imageUrl,
                contentDescription = null,
                modifier = Modifier.size(55.dp).clip(CircleShape).background(Color.LightGray),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(vendor.name, fontWeight = FontWeight.Bold)
                Text("Table n°${vendor.tableNumber}", style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = onDelete) { Icon(Icons.Default.DeleteOutline, null, tint = Color.Red.copy(0.6f)) }
        }
    }
}