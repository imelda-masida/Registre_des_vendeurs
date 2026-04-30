package com.example.registredesvendeurs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

// IMPORTS
import com.example.registredesvendeurs.ui.theme.AppTheme
// Si la ligne suivante reste rouge, lisez l'étape 2 ci-dessous
import com.example.registredesvendeurs.ui.theme.vendor.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val vendorViewModel: VendorViewModel = viewModel()

    NavHost(navController = navController, startDestination = "list") {

        // 1. Liste des vendeurs
        composable("list") {
            VendorListScreen(
                viewModel = vendorViewModel,
                onNavigateToAdd = { navController.navigate("add") },
                // On passe le même ID pour le détail et l'édition pour corriger l'erreur de paramètre
                onNavigateToDetail = { vendorId ->
                    navController.navigate("detail/$vendorId")
                },
                onNavigateToEdit = { vendorId ->
                    navController.navigate("detail/$vendorId")
                }
            )
        }

        // 2. Ajouter un vendeur
        composable("add") {
            AddVendorScreen(
                viewModel = vendorViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        // 3. Détails du vendeur
        composable(
            route = "detail/{vendorId}",
            arguments = listOf(navArgument("vendorId") { type = NavType.IntType })
        ) { backStackEntry ->
            val vendorId = backStackEntry.arguments?.getInt("vendorId") ?: 0
            VendorDetailScreen(
                viewModel = vendorViewModel,
                vendorId = vendorId,
                onBack = { navController.popBackStack() },
                onEdit = { id -> /* Logique de modification */ }
            )
        }
    }
}