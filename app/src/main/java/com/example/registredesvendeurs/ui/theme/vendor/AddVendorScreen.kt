package com.example.registredesvendeurs.ui.theme.vendor

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

import com.example.registredesvendeurs.R // Assurez-vous que cet import est correct pour votre logo

import com.example.registredesvendeurs.repository.Vendor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditVendorScreen(
    viewModel: VendorViewModel,
    vendorId: Int? = null, // Si null = Ajout, si Int = Modification
    onBack: () -> Unit
) {
    // États du formulaire
    var name by remember { mutableStateOf("") }
    var tableNumber by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var existingImageUrl by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val isSaving by viewModel.isSaving.collectAsState()

    // INITIALISATION : Si on est en mode modification, on charge les données
    LaunchedEffect(vendorId) {
        if (vendorId != null && vendorId != -1) {
            val vendor = viewModel.getVendorById(vendorId)
            vendor?.let {
                name = it.name
                tableNumber = it.tableNumber
                category = it.category
                existingImageUrl = it.imageUrl
            }
        }
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri -> selectedImageUri = uri }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (vendorId == null || vendorId == -1) "Nouveau Vendeur" else "Modifier Vendeur")
                },
                navigationIcon = {
                    IconButton(onClick = onBack, enabled = !isSaving) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // --- LOGO (Respect du Branding MD3) ---
            Spacer(modifier = Modifier.height(10.dp))
            // Remplacer par votre logo ou une icône de magasin
            Icon(
                imageVector = Icons.Default.Store,
                contentDescription = "Logo",
                modifier = Modifier.size(60.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            // --- ZONE PHOTO ---
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                if (selectedImageUri != null) {
                    AsyncImage(model = selectedImageUri, contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                } else if (existingImageUrl != null) {
                    AsyncImage(model = existingImageUrl, contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                } else {
                    Icon(Icons.Default.PhotoCamera, null, modifier = Modifier.size(40.dp))
                }
            }

            TextButton(onClick = { photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }) {
                Text("Choisir une photo de l'étalage")
            }

            // --- CHAMPS DE SAISIE ---
            OutlinedTextField(
                value = name, onValueChange = { name = it },
                label = { Text("Nom du vendeur") },
                leadingIcon = { Icon(Icons.Default.Person, null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )

            OutlinedTextField(
                value = tableNumber, onValueChange = { tableNumber = it },
                label = { Text("Numéro de table / Pavillon") },
                leadingIcon = { Icon(Icons.Default.TableBar, null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )

            OutlinedTextField(
                value = category, onValueChange = { category = it },
                label = { Text("Catégorie (ex: Habits, Fruits...)") },
                leadingIcon = { Icon(Icons.Default.Category, null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // --- BOUTON SAUVEGARDER ---
            Button(
                onClick = {
                    val imageBytes = selectedImageUri?.let { uri ->
                        context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                    }

                    // On crée l'objet Vendor (avec l'ID si c'est une modification)
                    val vendorToSave = Vendor(
                        id = if (vendorId == -1) null else vendorId,
                        name = name,
                        tableNumber = tableNumber,
                        category = category,
                        imageUrl = existingImageUrl
                    )

                    viewModel.saveVendor(vendorToSave, imageBytes) {
                        onBack() // Ferme l'écran après succès
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = name.isNotBlank() && tableNumber.isNotBlank() && !isSaving
            ) {
                if (isSaving) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text("ENREGISTRER DANS LA BASE")
                }
            }
        }
    }
}