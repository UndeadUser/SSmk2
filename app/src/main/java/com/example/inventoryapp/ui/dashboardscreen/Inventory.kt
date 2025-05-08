package com.example.inventoryapp.ui.dashboardscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.inventoryapp.clouddata.FirestoreProductRepository
import com.example.inventoryapp.ui.inventoryscreen.AddProducts
import com.example.inventoryapp.ui.inventoryscreen.EditProduct
import com.example.inventoryapp.ui.inventoryscreen.Products

@Composable
fun Inventory() {
    val firestoreProductRepository = remember { FirestoreProductRepository() }
    val navController = rememberNavController() // Create a new NavController here

    NavHost(navController = navController, startDestination = "home") {
        composable("home") { InventoryScreen(navController) }
        composable("products") { Products(navController, firestoreProductRepository) }
        composable("add_products") { AddProducts(navController, firestoreProductRepository) }
        composable("edit_product/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId").orEmpty()
            EditProduct(navController, productId, firestoreProductRepository)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(navController: NavHostController) {
    // Memoize the items list
    val items = remember {
        listOf(
            "PRODUCTS" to "products",
            "ADD PRODUCTS" to "add_products"
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Inventory") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFF0EAD6),
                    titleContentColor = Color(0xFFE97451)
                )
            )
        },
        contentColor = Color(0xFFE97451)
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF0EAD6))
                .padding(horizontal = 8.dp)
        ) {
            items(items) { (label, route) ->
                InventoryListItem(
                    text = label,
                    onClick = { navController.navigate(route) }
                )
            }
        }
    }
}

@Composable
fun InventoryListItem(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0EAD6))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreenContent(title: String) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(title) })
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

