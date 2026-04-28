package com.example.registredesvendeurs



import android.os.Bundle
import androidx.activity.ComponentActivity          // Classe de base pour une Activity
import androidx.activity.compose.setContent         // Permet d’afficher du Compose
import androidx.compose.material3.MaterialTheme     // Thème Material 3
import androidx.compose.runtime.Composable          // Annotation pour une fonction UI
import androidx.navigation.compose.NavHost          // Gestionnaire de navigation
import androidx.navigation.compose.composable       // Définition des écrans
import androidx.navigation.compose.rememberNavController // Contrôleur de navigation
import com.example.registredesvendeurs.repository.VendorRepository
import com.example.registredesvendeurs.ui.theme.vendor.AddVendorScreen
import com.example.registredesvendeurs.ui.theme.vendor.VendorListScreen
import com.example.registredesvendeurs.ui.theme.vendor.VendorViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = VendorViewModel(VendorRepository()) // Crée le ViewModel

        setContent {
            MaterialTheme {   // Applique le thème Material 3
                AppNavigation(viewModel) // Lance la navigation
            }
        }
    }
}

@Composable
fun AppNavigation(viewModel: VendorViewModel) {val navController = rememberNavController()

    NavHost(navController, startDestination = "list") {
        composable("list") {
            // On passe l'action pour aller vers l'écran "add"
            VendorListScreen(
                viewModel = viewModel,
                onNavigateToAdd = { navController.navigate("add") }
            )
        }
        composable("add") {
            // On passe l'action pour revenir en arrière après l'ajout
            AddVendorScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}