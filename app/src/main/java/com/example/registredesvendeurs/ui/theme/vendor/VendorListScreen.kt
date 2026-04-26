package com.example.registredesvendeurs.ui.theme.vendor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VendorListScreen(viewModel: VendorViewModel, onNavigateToAdd: () -> Unit ) {
    // 1. Observation des données : On "écoute" le ViewModel
    // 'by' permet de récupérer directement la valeur du Flow

    val vendors by viewModel.filteredVendors.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    // 2. Structure principale de l'écran (Scaffold)
    Scaffold(
        topBar = { /* ... */ },
        // 5. LE BOUTON FLOTTANT (FAB)
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToAdd) {
                Icon(Icons.Default.Add, contentDescription = "Ajouter")
            }
        }
    ) { paddingValues ->
        // Colonne pour empiler la recherche et la liste
        Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {

            // 3. LA BARRE DE RECHERCHE (Composant Material 3)
            SearchBar(
                query = searchQuery, // Texte actuel
                onQueryChange = { viewModel.onSearchQueryChange(it) }, // Action quand on tape
                onSearch = { /* Action quand on appuie sur Entrée */ },
                active = false, // La barre reste intégrée à la liste
                onActiveChange = { },
                placeholder = { Text("Rechercher un vendeur ou une table...") },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) { }

            // 4. LA LISTE DÉFILANTE (LazyColumn = optimisation mémoire)
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp) // Espace entre les cartes
            ) {
                // Pour chaque vendeur dans la liste filtrée
                items(vendors) { vendor ->
                    // Appel d'un composant personnalisé pour afficher un vendeur
                    VendorCard(vendor)
                }
            }
        }
    }
}
@Composable
fun VendorCard(vendor: Vendor) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {

            // AFFICHAGE DE L'IMAGE
            AsyncImage(
                // Si imageUrl est null, on peut mettre une image par défaut ou gérer le vide
                model = vendor.imageUrl,// L'URL qui vient de Supabase
                contentDescription = "Photo de l'étalage",
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))// Bords arrondis (Material 3)
                    .background(MaterialTheme.colorScheme.surfaceVariant), // Fond si image vide
                contentScale = ContentScale.Crop// Recadre l'image pour remplir le carré
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(text = vendor.name, style = MaterialTheme.typography.titleMedium)
                // tableNumber est maintenant reconnu car ajouté dans la data class
                Text(text = "Table : ${vendor.tableNumber}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}