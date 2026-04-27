package com.example.registredesvendeurs


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.registredesvendeurs.ui.theme.AppTheme
import com.example.registredesvendeurs.ui.theme.vendor.AddVendorScreen
import com.example.registredesvendeurs.ui.theme.vendor.VendorListScreen
import com.example.registredesvendeurs.ui.theme.vendor.VendorRepository
import com.example.registredesvendeurs.ui.theme.vendor.VendorViewModel



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // On applique ton thème personnalisé Material 3
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Initialisation propre du ViewModel avec sa Factory
                    val viewModel: VendorViewModel = viewModel(
                        factory = object : ViewModelProvider.Factory {
                            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                @Suppress("UNCHECKED_CAST")
                                return VendorViewModel(VendorRepository()) as T
                            }
                        }
                    )
                    // Lancement de la navigation
                    AppNavigation(viewModel)
                }
            }
        }
    }
}

@Composable
fun AppNavigation(viewModel: VendorViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "list"
    ) {
        // Écran de la liste des vendeurs
        composable("list") {
            VendorListScreen(
                viewModel = viewModel,
                onNavigateToAdd = { navController.navigate("add") }
            )
        }

        // Écran d'ajout d'un vendeur
        composable("add") {
            AddVendorScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}