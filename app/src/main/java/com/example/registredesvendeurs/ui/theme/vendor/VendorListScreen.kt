package com.example.registredesvendeurs.ui.theme.vendor

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
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
import com.example.registredesvendeurs.repository.Vendor

/**
 * ÉCRAN PRINCIPAL : Liste des vendeurs avec recherche dynamique
 * Respecte le cahier des charges : Material 3 SearchBar et MVVM StateFlow
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VendorListScreen(
    viewModel: VendorViewModel,
    onNavigateToAdd: () -> Unit,
    onNavigateToEdit: (Int) -> Unit,
    onNavigateToDetail: (Int) -> Unit
) {
    // Collecte des états depuis le ViewModel (Logique réactive)
    val vendors by viewModel.filteredVendors.collectAsState()
    val query by viewModel.searchQuery.collectAsState()
    var active by remember { mutableStateOf(false) }

    // Charger les données au démarrage
    LaunchedEffect(Unit) {
        viewModel.loadVendors()
    }

    Scaffold(
        topBar = {
            // 1. RECHERCHE DYNAMIQUE (Section 2 du cahier des charges)
            SearchBar(
                query = query,
                onQueryChange = { viewModel.onSearchQueryChange(it) },
                onSearch = { active = false },
                active = active,
                onActiveChange = { active = it },
                placeholder = { Text("Rechercher un vendeur ou table...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (active) {
                        IconButton(onClick = { active = false }) {
                            Icon(Icons.Default.Close, contentDescription = null)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = if (active) 0.dp else 16.dp)
            ) {
                // Suggestions ou historique (optionnel)
            }
        },
        floatingActionButton = {
            // Bouton d'ajout stylisé Material 3
            ExtendedFloatingActionButton(
                onClick = onNavigateToAdd,
                icon = { Icon(Icons.Default.Add, null) },
                text = { Text("Nouveau Vendeur") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {

            // Affichage de la liste ou message vide
            if (vendors.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Aucun vendeur trouvé", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 80.dp) // Espace pour le FAB
                ) {
                    items(vendors) { vendor ->
                        VendorCard(
                            vendor = vendor,
                            onClick = { vendor.id?.let { onNavigateToDetail(it) } },
                            onDelete = { vendor.id?.let { viewModel.deleteVendor(it) } }
                        )
                    }
                }
            }
        }
    }
}

/**
 * COMPOSANT ITEM : La carte d'un vendeur
 */
@Composable
fun VendorCard(
    vendor: Vendor,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = vendor.imageUrl ?: "https://via.placeholder.com/150",
                contentDescription = "Photo étalage",
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = vendor.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Table n°${vendor.tableNumber}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        text = vendor.category,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Supprimer",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}