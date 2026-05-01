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
import androidx.compose.ui.text.style.TextAlign
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
    // Collecte des états depuis le ViewModel
    val vendors by viewModel.filteredVendors.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    var active by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
                CenterAlignedTopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Storefront, null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(Modifier.width(10.dp))
                            Text("Registre des vendeurs", fontWeight = FontWeight.ExtraBold)
                        }
                    }
                )

                SearchBar(
                    query = searchQuery,
                    onQueryChange = { viewModel.onSearchQueryChange(it) },
                    onSearch = { active = false },
                    active = active,
                    onActiveChange = { active = it },
                    placeholder = { Text("Trouver un vendeur...") },
                    leadingIcon = { Icon(Icons.Default.Search, null) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.onSearchQueryChange("") }) {
                                Icon(Icons.Default.Clear, null)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = if (active) 0.dp else 16.dp)
                        .padding(bottom = 8.dp)
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
    ) { paddingValues -> // On récupère les valeurs d'espacement ici

        // On utilise un Box comme conteneur principal pour appliquer le padding du Scaffold
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {

            if (vendors.isEmpty()) {
                // --- ÉTAT VIDE (BIENVENUE OU RECHERCHE VIDE) ---
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (searchQuery.isEmpty()) {
                        // État : Liste totalement vide
                        Icon(Icons.Default.Storefront, null, modifier = Modifier.size(100.dp), tint = Color.LightGray)
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Bienvenue !",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Votre registre est vide. Commencez par ajouter votre premier vendeur à l'aide du bouton + en bas à droite.",
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 40.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    } else {
                        // État : Recherche sans résultat
                        Icon(Icons.Default.SearchOff, null, modifier = Modifier.size(100.dp), tint = Color.LightGray)
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Aucun résultat pour \"$searchQuery\"",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Vérifiez l'orthographe ou essayez un autre nom.",
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 40.dp)
                        )
                    }
                }
            } else {
                // --- LISTE DES VENDEURS ---
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(vendors, key = { it.id ?: 0 }) { vendor ->
                        VendorCardElegant(
                            vendor = vendor,
                            onClick = { vendor.id?.let { onNavigateToDetail(it) } },
                            onEdit = { vendor.id?.let { onNavigateToEdit(it) } },
                            onDelete = { vendor.id?.let { viewModel.deleteVendor(it) } }
                        )
                    }
                }
            }
        }
    } // Fin du Scaffold
} // Fin de la fonction VendorListScreen

@Composable
fun VendorCardElegant(
    vendor: Vendor,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = if (vendor.imageUrl.isNullOrBlank())
                    "https://ui-avatars.com/api/?name=${vendor.name}&background=random"
                else vendor.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(55.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = vendor.name,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Table n°${vendor.tableNumber}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Modifier")
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "Supprimer",
                    tint = Color.Red.copy(alpha = 0.6f)
                )
            }
        }
    }
}