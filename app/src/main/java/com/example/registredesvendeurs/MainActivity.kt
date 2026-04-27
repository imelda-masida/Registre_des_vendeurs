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
// CORRECTION : On importe AppTheme au lieu de RegistreDesVendeursTheme
import com.example.registredesvendeurs.ui.theme.AppTheme
import com.example.registredesvendeurs.ui.theme.vendor.AddVendorScreen
import com.example.registredesvendeurs.ui.theme.vendor.VendorListScreen
import com.example.registredesvendeurs.ui.theme.vendor.VendorRepository
import com.example.registredesvendeurs.ui.theme.vendor.VendorViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // CORRECTION : Utilisation de AppTheme ici
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Initialisation du ViewModel
                    val viewModel: VendorViewModel = viewModel(
                        factory = object : ViewModelProvider.Factory {
                            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                @Suppress("UNCHECKED_CAST")
                                return VendorViewModel(VendorRepository()) as T
                            }
                        }
                    )
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
        composable("list") {
            VendorListScreen(
                viewModel = viewModel,
                onNavigateToAdd = { navController.navigate("add") }
            )
        }

        composable("add") {
            AddVendorScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}