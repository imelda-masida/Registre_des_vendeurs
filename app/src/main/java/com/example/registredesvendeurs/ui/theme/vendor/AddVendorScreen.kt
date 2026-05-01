package com.example.registredesvendeurs.ui.theme.vendor

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddVendorScreen(viewModel: VendorViewModel, onBack: () -> Unit) {
    val context = LocalContext.current

    // États pour le formulaire
    var name by remember { mutableStateOf("") }
    var table by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // DÉFINITION DU LAUNCHER (C'est ce qui manquait !)
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    // Vérifications des doublons
    val nameExists = remember(name) {
        name.isNotBlank() && viewModel.isNameAlreadyExists(name)
    }
    val tableExists = remember(table) {
        table.isNotBlank() && viewModel.isTableNumberAlreadyExists(table)
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
                .padding(20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            // BOUTON AJOUT PHOTO
            Button(
                onClick = { launcher.launch("image/*") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (imageUri == null) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(Icons.Default.AddAPhoto, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(if (imageUri == null) "Ajouter une photo" else "Photo sélectionnée ✅")
            }

            // CHAMP NOM
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

            // CHAMP NUMÉRO DE TABLE
            OutlinedTextField(
                value = table,
                onValueChange = { table = it },
                label = { Text("Numéro de table") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = tableExists,
                supportingText = {
                    if (tableExists) Text("Cette table est déjà occupée", color = MaterialTheme.colorScheme.error)
                },
                trailingIcon = {
                    if (tableExists) Icon(Icons.Default.Error, null, tint = MaterialTheme.colorScheme.error)
                }
            )

            // CHAMP CATÉGORIE
            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Catégorie (ex: Fruits, Tissus)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            // BOUTON DE VALIDATION
            Button(
                onClick = {
                    viewModel.addVendor(context, name, table, category, imageUri)
                    onBack()
                },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                enabled = name.isNotBlank() && table.isNotBlank() && category.isNotBlank() && !nameExists && !tableExists
            ) {
                Text("VALIDER L'INSCRIPTION")
            }
        }
    }
}