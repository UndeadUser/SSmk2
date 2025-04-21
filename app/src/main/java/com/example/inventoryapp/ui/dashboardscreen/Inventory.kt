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
import com.example.inventoryapp.crud.AppDatabase
import com.example.inventoryapp.crud.ProductRepository
import com.example.inventoryapp.ui.inventoryscreen.AddProducts
import com.example.inventoryapp.ui.inventoryscreen.EditProduct
import com.example.inventoryapp.ui.inventoryscreen.Products

@Composable
fun Inventory(navController: NavHostController) {
    val context = LocalContext.current
    val database = remember { AppDatabase.getDatabase(context) }
    val productRepository = remember { ProductRepository(database.productDao()) }

    NavHost(navController, startDestination = "home") {
        composable("home") { InventoryScreen(navController) }
        composable("products") { Products(navController, productRepository) }
        composable("add_products") { AddProducts(navController, productRepository) }
        composable("edit_product/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")?.toInt() ?: 0
            EditProduct(navController, productId, productRepository)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(navController: NavHostController) {
    val items = listOf(
        "PRODUCTS" to "products",
        "ADD PRODUCTS" to "add_products"
    )

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
                InventoryListItem(label) { navController.navigate(route) }
            }
        }
    }
}

@Composable
fun InventoryListItem(text: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(8.dp)
            .clickable { onClick() },
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
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreenContent(title: String) {
    Scaffold(topBar = { TopAppBar(title = { Text(title) }) }) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
            Text(title, style = MaterialTheme.typography.headlineMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InventoryScreenPreview() {
    val navController = rememberNavController()
    Inventory(navController)
}