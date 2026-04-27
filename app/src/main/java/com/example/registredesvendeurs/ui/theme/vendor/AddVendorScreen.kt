package com.example.registredesvendeurs.ui.theme.vendor

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddVendorScreen(viewModel: VendorViewModel, onBack: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var table by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Ajouter un Vendeur") })
        }
    ) { padding ->
        Column(modifier = Modifier
            .padding(padding)
            .padding(16.dp)) {

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nom du vendeur") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = table,
                onValueChange = { table = it },
                label = { Text("Numéro de table") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // CHAMP CATÉGORIE (Ajouté pour le cahier des charges)
            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Catégorie (ex: Fruits, Habits...)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (name.isNotBlank() && table.isNotBlank()) {
                        // APPEL CORRIGÉ : On passe les paramètres au ViewModel
                        viewModel.addVendor(
                            name = name,
                            table = table,
                            category = if(category.isBlank()) "Général" else category
                        )
                        onBack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Enregistrer le vendeur")
            }
        }
    }
}