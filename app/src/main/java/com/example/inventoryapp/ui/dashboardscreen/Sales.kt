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
import com.example.inventoryapp.ui.salesscreen.Customers
import com.example.inventoryapp.ui.salesscreen.PickSalesOrder
import com.example.inventoryapp.ui.salesscreen.SalesOrder

@Composable
fun Sales() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "home") {
        composable("home") { SalesScreen(navController) }
        composable("customers") { Customers(navController) }
        composable("sales order") { SalesOrder(navController) }
        composable("pick sales order") { PickSalesOrder(navController) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalesScreen(navController: NavHostController) {
    val items = listOf(
        "CUSTOMERS" to "customers",
        "SALES ORDER" to "sales order",
        "PICK SALES ORDER" to "pick sales order"
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Sales") },
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
                SalesListItem(label) { navController.navigate(route) }
            }
        }
    }
}

@Composable
fun SalesListItem(text: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(vertical = 8.dp)
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
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalesScreenContent(title: String) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(title) })
        },
        contentColor = Color.Black
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF0EAD6))
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text(title, style = MaterialTheme.typography.headlineMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    Sales()
}
