package com.example.toriiapp.screens

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.toriiapp.data.ProductsDB
import com.example.toriiapp.repository.ProductRepository
import com.example.toriiapp.viewmodel.ProductViewModel
import com.example.toriiapp.viewmodel.ProductViewModelFactory

@Composable
fun InventoryHomeScreen() {
    val context = LocalContext.current
    val db = ProductsDB.getDatabase(context)
    val dao = db.productDao()

    if (dao == null) { // Check for DAO instead of db instance
        Log.e("Inventory", "ProductDao is null")
    }

    val repository = ProductRepository(dao)
    val myViewModel: ProductViewModel = viewModel(factory = ProductViewModelFactory(repository))
    val navController = rememberNavController() // Move outside of NavHost

    NavHost(navController = navController, startDestination = "Inventory") {
        composable("Inventory") {
            Inventory(viewModel = myViewModel, navController = navController)
        }
        composable("Card/{ProductId}/{ProductName}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("ProductId")?.toIntOrNull() // Ensure conversion to Int
            val productName = backStackEntry.arguments?.getString("ProductName") ?: "Unknown"

            CardScreen(
                navController = navController,
                viewModel = myViewModel,
                productId = productId,
                productName = productName
            )
        }
    }
}

