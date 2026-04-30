package com.example.registredesvendeurs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.registredesvendeurs.ui.theme.vendor.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                val vm: VendorViewModel = viewModel()

                NavHost(navController, startDestination = "list") {
                    composable("list") {
                        VendorListScreen(vm, { navController.navigate("add") }, { id -> navController.navigate("edit/$id") }, { id -> navController.navigate("detail/$id") })
                    }
                    composable("add") { AddVendorScreen(vm) { navController.popBackStack() } }
                    composable(
                        "detail/{vendorId}",
                        arguments = listOf(navArgument("vendorId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val id = backStackEntry.arguments?.getInt("vendorId") ?: 0
                        VendorDetailScreen(vm, id, { /* edit */ }, { navController.popBackStack() })
                    }
                }
            }
        }
    }
}