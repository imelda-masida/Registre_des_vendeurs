package com.example.registredesvendeurs.ui.theme.vendor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage // Nécessaire pour charger l'image distante

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VendorDetailScreen(
    viewModel: VendorViewModel,
    vendorId: Int,
    onEdit: (Int) -> Unit,
    onBack: () -> Unit
) {
    // Récupère les données du vendeur depuis le ViewModel (qui est lié à la DB)
    val vendor = remember(vendorId) { viewModel.getVendorById(vendorId) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(vendor?.name ?: "Détail") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Retour")
                    }
                },
                actions = {
                    // BOUTON MODIFIER
                    IconButton(onClick = { vendor?.id?.let { onEdit(it) } }) {
                        Icon(Icons.Default.Edit, contentDescription = "Modifier")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(20.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (vendor != null) {
                // 1. AFFICHAGE DE L'IMAGE DISTANTE (SUPABASE STORAGE)
                AsyncImage(
                    model = vendor.imageUrl, // L'URL stockée sur Supabase
                    contentDescription = "Photo de ${vendor.name}",
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 2. INFORMATIONS TEXTUELLES
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Vendeur : ${vendor.name}", style = MaterialTheme.typography.headlineMedium)
                        Divider(Modifier.padding(vertical = 8.dp))
                        Text("Emplacement : Table ${vendor.tableNumber}", style = MaterialTheme.typography.bodyLarge)
                        Text("Catégorie : ${vendor.category}", style = MaterialTheme.typography.bodyLarge)
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // 3. BOUTON SUPPRIMER (ACTION DISTANTE)
                Button(
                    onClick = {
                        vendor.id?.let { viewModel.deleteVendor(it) }
                        onBack()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("SUPPRIMER DU REGISTRE")
                }

            } else {
                Text("Erreur : Vendeur introuvable", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}