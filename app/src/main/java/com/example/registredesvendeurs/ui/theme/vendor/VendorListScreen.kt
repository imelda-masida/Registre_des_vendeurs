package com.example.registredesvendeurs.ui.theme.vendor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.registredesvendeurs.repository.Vendor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VendorListScreen(viewModel: VendorViewModel, onNavigateToAdd: () -> Unit) {
    // Observation de la liste des vendeurs
    val vendors by viewModel.filteredVendors.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    // Charger les données dès l'ouverture de l'écran
    LaunchedEffect(Unit) {
        viewModel.loadVendors()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // LOGO : Remplacer Storefront par votre logo personnalisé si besoin
                        Icon(
                            imageVector = Icons.Default.Storefront,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text("Mes Vendeurs", fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNavigateToAdd,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                icon = { Icon(Icons.Default.Add, null) },
                text = { Text("Ajouter un vendeur") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Barre de recherche stylisée
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.onSearchQueryChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Rechercher un vendeur...") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                shape = RoundedCornerShape(16.dp),
                singleLine = true
            )

            if (vendors.isEmpty()) {
                // ÉCRAN VIDE
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // CORRECTION ICI : size se met dans le Modifier
                        Icon(
                            imageVector = Icons.Default.CloudOff,
                            contentDescription = null,
                            modifier = Modifier.size(80.dp),
                            tint = Color.Gray
                        )
                        Spacer(Modifier.height(16.dp))
                        Text("Aucun vendeur trouvé", style = MaterialTheme.typography.headlineSmall)
                        Spacer(Modifier.height(8.dp))
                        Button(onClick = { viewModel.loadVendors() }) {
                            Text("Actualiser la liste")
                        }
                    }
                }
            } else {
                // LISTE DES CARTES
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(vendors) { vendor ->
                        VendorCard(vendor = vendor, onDelete = { viewModel.deleteVendor(it) })
                    }
                }
            }
        }
    }
}

@Composable
fun VendorCard(vendor: Vendor, onDelete: (Int) -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image du vendeur
            AsyncImage(
                model = vendor.imageUrl ?: "https://via.placeholder.com/150",
                contentDescription = null,
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Textes
            Column(modifier = Modifier.weight(1f)) {
                Text(vendor.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("Table n°${vendor.tableNumber}", style = MaterialTheme.typography.bodyMedium)
                Text(
                    vendor.category,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Bouton supprimer
            IconButton(onClick = { vendor.id?.let { onDelete(it) } }) {
                Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red)
            }
        }
    }
}