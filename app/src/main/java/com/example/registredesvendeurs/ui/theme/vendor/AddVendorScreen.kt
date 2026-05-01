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
    // États des champs du formulaire
    var name by remember { mutableStateOf("") }
    var table by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // --- LOGIQUE DE VÉRIFICATION DES DOUBLONS ---
    // On vérifie si le nom existe déjà dans la liste du ViewModel
    val nameExists = remember(name) {
        name.isNotBlank() && viewModel.isNameAlreadyExists(name)
    }

    // Sélecteur d'image
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        imageUri = uri
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Inscrire un vendeur") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
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
            // Bouton Photo
            Button(
                onClick = { launcher.launch("image/*") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.AddAPhoto, null)
                Spacer(Modifier.width(8.dp))
                Text(if (imageUri == null) "Ajouter une photo" else "Photo prête !")
            }

            // --- CHAMP NOM AVEC VÉRIFICATION DE DOUBLON ---
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nom du vendeur") },
                modifier = Modifier.fillMaxWidth(),
                isError = nameExists, // Devient rouge si le nom existe déjà
                supportingText = {
                    if (nameExists) {
                        Text(
                            text = "Ce vendeur est déjà inscrit dans le registre",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                trailingIcon = {
                    if (nameExists) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = "Erreur",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )

            // Champ Numéro de table
            // Vers la ligne 94
            OutlinedTextField(
                value = table,
                onValueChange = { table = it },
                label = { Text("Numéro de table") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number) // <-- AJOUTEZ CECI
            )

            // Champ Catégorie
            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Catégorie de produits") },
                placeholder = { Text("ex : habit, fruit, bijoux...") }, // Conseil design
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            // Bouton de validation
            Button(
                onClick = {
                    viewModel.addVendor(context,name, table, category, imageUri)
                    onBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(12.dp),
                // --- VALIDATION DU BOUTON ---
                // Désactivé si : nom vide, table vide, ou si le nom existe déjà
                enabled = name.isNotBlank() && table.isNotBlank() && !nameExists
            ) {
                Text("VALIDER")
            }
        }
    }
}