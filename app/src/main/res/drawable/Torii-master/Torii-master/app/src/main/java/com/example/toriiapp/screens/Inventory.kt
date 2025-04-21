package com.example.toriiapp.screens

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.toriiapp.R
import com.example.toriiapp.data.ProductsDB
import com.example.toriiapp.repository.ProductRepository
import com.example.toriiapp.room.ProductEntity
import com.example.toriiapp.viewmodel.ProductViewModel
import com.example.toriiapp.viewmodel.ProductViewModelFactory


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint
@Composable
fun Inventory(viewModel: ProductViewModel, navController: NavController) {
    var inputProduct by remember { mutableStateOf("") }
    val emty by remember { mutableStateOf("") }
    val showDialog = remember { mutableStateOf(false) }
    val products by viewModel.allProducts.collectAsState(initial = emptyList())
    if (products.isEmpty()) {
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Product name",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.background
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primary)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog.value = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.background,
                shape = CircleShape
            ) {
                androidx.compose.material3.Icon(
                    painter = painterResource(id = R.drawable.baseline_add_24),
                    contentDescription = null
                )

            }
        }
    ) {
        if (products.isEmpty()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {
                Text(text = "no name available")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(it)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(10.dp)
            ) {
                items(products) {product ->
                    Card(
                        onClick = {
                            try {
                                val encodedName = Uri.encode(product.title ?: "Unknown") // Default value if title is null
                                navController.navigate("Card/${product.id}/$encodedName")
                            } catch (e: Exception) {
                                Log.e("NavigationError", "Failed to navigate to CardScreen: ${e.message}")
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row {
                            Text(
                                text = "" + product.title,
                                fontSize = 24.sp,
                                modifier = Modifier.padding(14.dp),
                            )
                        }
                    }
                }
            }
        }
    }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            dismissButton = {
                Button(
                    onClick = {
                        showDialog.value = false
                        inputProduct = emty
                    }
                ) {
                    Text(text = "Cancel")
                }
            },
            confirmButton = {
                if (inputProduct.isNotEmpty()) {
                    Button(
                        onClick = {
                            viewModel.addProduct(ProductEntity(0, inputProduct))
                            showDialog.value = false
                            inputProduct = emty
                        }
                    ) {
                        Text(text = "Save")
                    }
                }
            },
            title = {
                Text(
                    text = "Add product name",
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                    modifier = Modifier.padding(5.dp)
                )
            },
            text = {
                OutlinedTextField(
                    value = inputProduct,
                    onValueChange = { inputProduct = it },
                    label = { Text(text = "Product Name") },
                    placeholder = { Text(text = "Enter Your Product Name") }
                )
            }
        )
    }
}

