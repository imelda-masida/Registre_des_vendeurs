package com.example.registredesvendeurs.ui.theme.vendor

import android.net.Uri // Correction : Import pour Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons // Correction : Import pour Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack // Correction : Import pour ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.* // Correction : Importe getValue et setValue pour le mot-clé 'by'
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditVendorScreen(
    viewModel: VendorViewModel,
    vendorId: Int,
    onBack: () -> Unit
) {
    // On récupère les données actuelles du vendeur
    val vendor = remember(vendorId) { viewModel.getVendorById(vendorId) }

    // États pour les champs (initialisés avec les données actuelles)
    // Note : On utilise 'by' grâce à l'import androidx.compose.runtime.*
    var name by remember { mutableStateOf(vendor?.name ?: "") }
    var table by remember { mutableStateOf(vendor?.tableNumber ?: "") }
    var category by remember { mutableStateOf(vendor?.category ?: "") }

    // Correction : Spécifier explicitement <Uri?> pour accepter la valeur null
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Modifier le vendeur") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        // Correction : Utilisation de l'icône AutoMirrored pour le retour
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {

            // Champ Nom
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nom du commerce") },
                modifier = Modifier.fillMaxWidth()
            )

            // MODIFICATION DE LA TABLE (Conseil Design : Clavier numérique)
            OutlinedTextField(
                value = table,
                onValueChange = { table = it },
                label = { Text("Numéro de table") },
                placeholder = { Text("Ex: 12") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            // Champ Catégorie
            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Catégorie") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            // Bouton de validation
            Button(
                onClick = {
                    // On appelle la fonction de mise à jour du ViewModel
                    viewModel.updateVendor(vendorId, name, table, category, imageUri)
                    onBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("ENREGISTRER LES MODIFICATIONS")
            }
        }
    }
}