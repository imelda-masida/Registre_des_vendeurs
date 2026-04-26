package com.example.registredesvendeurs.ui.theme.vendor


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api // Importé pour l'avertissement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// 1. On ajoute @OptIn pour supprimer l'avertissement sur TopAppBar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddVendorScreen(viewModel: VendorViewModel, onBack: () -> Unit) {
    // États locaux pour le formulaire
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

            // Champ pour le Nom
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nom du vendeur") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Champ pour le Numéro de table
            OutlinedTextField(
                value = table,
                onValueChange = { table = it },
                label = { Text("Numéro de table") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // BOUTON DE SAUVEGARDE
            Button(
                onClick = {
                    // Vérification simple pour ne pas envoyer de données vides
                    if (name.isNotBlank() && table.isNotBlank()) {
                        // On appelle la fonction addVendor du ViewModel
                        viewModel.addVendor(name = name, table = table)
                        // On retourne à l'écran précédent
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