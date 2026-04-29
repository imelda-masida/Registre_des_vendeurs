package com.example.registredesvendeurs.ui.theme.vendor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class) // Ajouté pour corriger l'erreur "Experimental"
@Composable
fun VendorDetailScreen(
    viewModel: VendorViewModel,
    vendorId: Int,
    onEdit: (Int) -> Unit,
    onBack: () -> Unit
) {
    val vendor = viewModel.getVendorById(vendorId) ?: return

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(vendor.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Retour"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            AsyncImage(
                model = vendor.imageUrl,
                contentDescription = "Image de l'étalage",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(24.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Numéro de Table : ${vendor.tableNumber}",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        "Catégorie : ${vendor.category}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Button(
                onClick = { onEdit(vendorId) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Modifier les informations du vendeur")
            }
        }
    }
}