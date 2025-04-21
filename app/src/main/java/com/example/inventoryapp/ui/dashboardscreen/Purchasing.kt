package com.example.inventoryapp.ui.dashboardscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.inventoryapp.ui.purchasingscreen.Vendors
import com.example.inventoryapp.ui.purchasingscreen.PurchaseOrders
import com.example.inventoryapp.ui.purchasingscreen.ReceivePurchaseOrders
@Composable
fun Purchasing() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "home") {
        composable("home") { PurchasingScreen(navController) }
        composable("vendors") { Vendors(navController) }
        composable("purchase orders") { PurchaseOrders(navController) }
        composable("receive purchase orders") { ReceivePurchaseOrders(navController) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchasingScreen(navController: NavHostController) {
    val items = listOf(
        "VENDORS" to "vendors",
        "PURCHASE ORDERS" to "purchase orders",
        "RECEIVE PURCHASE ORDERS" to "receive purchase orders"
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Purchasing") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFF0EAD6),
                    titleContentColor = Color(0xFFE97451)
                )
            )
        },
        contentColor = Color(0xFFE97451)
    ) { padding ->
        LazyColumn(modifier = Modifier
            .padding(padding)
            .fillMaxSize()
            .background(Color(0xFFF0EAD6))
            .padding(horizontal = 8.dp)
        ) {
            items(items) { (label, route) ->
                PurchasingListItem(label) { navController.navigate(route) }
            }
        }
    }
}

@Composable
fun PurchasingListItem(text: String, onClick: () -> Unit) {
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
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchasingScreenContent(title: String) {
    Scaffold(topBar = { TopAppBar(title = { Text(title) }) }) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = androidx.compose.ui.Alignment.Center) {
            Text(title, style = androidx.compose.material3.MaterialTheme.typography.headlineMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PurchasingScreenPreview() {
    Purchasing()
}