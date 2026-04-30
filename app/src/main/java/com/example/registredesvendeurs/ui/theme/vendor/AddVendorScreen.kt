package com.example.registredesvendeurs.ui.theme.vendor

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddVendorScreen(viewModel: VendorViewModel, onBack: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var table by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { imageUri = it }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Inscrire un vendeur") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) } }
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(20.dp), verticalArrangement = Arrangement.spacedBy(15.dp)) {
            Button(onClick = { launcher.launch("image/*") }, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.AddAPhoto, null)
                Spacer(Modifier.width(8.dp))
                Text(if (imageUri == null) "Ajouter une photo" else "Photo prête !")
            }
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nom du vendeur") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = table, onValueChange = { table = it }, label = { Text("Numéro de table") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Catégorie de produits(ex : habit, fruit, bijoux...)") }, modifier = Modifier.fillMaxWidth())

            Button(
                onClick = { viewModel.addVendor(name, table, category, imageUri); onBack() },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = name.isNotBlank() && table.isNotBlank()
            ) { Text("VALIDER") }
        }
    }
}