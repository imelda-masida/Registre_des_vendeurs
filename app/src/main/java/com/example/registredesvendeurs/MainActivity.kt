package com.example.registredesvendeurs



import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.example.registredesvendeurs.ui.theme.RegistreDesVendeursTheme
import com.example.registredesvendeurs.ui.theme.vendor.AddVendorScreen
import com.example.registredesvendeurs.ui.theme.vendor.VendorListScreen
import com.example.registredesvendeurs.ui.theme.vendor.VendorViewModel
import com.example.registredesvendeurs.ui.theme.vendor.VendorRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RegistreDesVendeursTheme {
                // Initialisation du ViewModel avec son Repository
                val viewModel: VendorViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return VendorViewModel(VendorRepository()) as T
                        }
                    }
                )
                AppNavigation(viewModel)
            }
        }
    }
}

@Composable
fun AppNavigation(viewModel: VendorViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            VendorListScreen(
                viewModel = viewModel,
                // On essaie le nom standard
                onNavigateToAdd = { navController.navigate("add") }
            )
        }

        composable("add") {
            AddVendorScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}