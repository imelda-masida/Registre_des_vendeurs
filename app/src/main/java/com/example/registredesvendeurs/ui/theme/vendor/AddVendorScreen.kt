package com.example.registredesvendeurs.ui.theme.vendor

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Error // Import pour l'icône d'erreur
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddVendorScreen(viewModel: VendorViewModel, onBack: () -> Unit) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var table by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // --- VÉRIFICATIONS DES DOUBLONS ---
    val nameExists = remember(name) {
        name.isNotBlank() && viewModel.isNameAlreadyExists(name)
    }

    val tableExists = remember(table) {
        table.isNotBlank() && viewModel.isTableNumberAlreadyExists(table)
    }

    Scaffold(
        // ... (votre TopAppBar reste identique)
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            // ... (Bouton photo)

            // CHAMP NOM (Déjà fait)
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nom du vendeur") },
                modifier = Modifier.fillMaxWidth(),
                isError = nameExists,
                supportingText = {
                    if (nameExists) Text("Ce nom est déjà utilisé", color = MaterialTheme.colorScheme.error)
                }
            )

            // --- CHAMP TABLE (Modifié pour détecter les doublons) ---
            OutlinedTextField(
                value = table,
                onValueChange = { table = it },
                label = { Text("Numéro de table") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = tableExists, // Devient rouge si la table existe
                supportingText = {
                    if (tableExists) {
                        Text("Cette table est déjà occupée", color = MaterialTheme.colorScheme.error)
                    }
                },
                trailingIcon = {
                    if (tableExists) Icon(Icons.Default.Error, null, tint = MaterialTheme.colorScheme.error)
                }
            )

            // ... (Champ catégorie)

            Spacer(modifier = Modifier.weight(1f))

            // --- BOUTON VALIDER (Désactivé si doublon de nom OU de table) ---
            Button(
                onClick = {
                    viewModel.addVendor(context, name, table, category, imageUri)
                    onBack()
                },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                enabled = name.isNotBlank() && table.isNotBlank() && !nameExists && !tableExists
            ) {
                Text("VALIDER L'INSCRIPTION")
            }
        }
    }
}
