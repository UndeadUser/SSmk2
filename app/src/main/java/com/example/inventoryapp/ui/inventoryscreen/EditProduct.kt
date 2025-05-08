package com.example.inventoryapp.ui.inventoryscreen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.inventoryapp.clouddata.FirestoreProductRepository
import com.example.inventoryapp.clouddata.Product
import com.example.inventoryapp.clouddata.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProduct(
    navController: NavHostController,
    productId: String,
    firestoreProductRepository: FirestoreProductRepository
) {
    var product by remember { mutableStateOf<Product?>(null) }
    var productName by rememberSaveable { mutableStateOf("") }
    var productPrice by rememberSaveable { mutableStateOf("") }
    var productQuantity by rememberSaveable { mutableStateOf("") }
    var selectedCategory by rememberSaveable { mutableStateOf("Carbonated") }

    val productViewModel = remember { ProductViewModel(firestoreProductRepository) }
    val categories = remember { listOf("Carbonated", "Juice", "Alcohol") }

    LaunchedEffect(productId) {
        product = firestoreProductRepository.getProductById(productId)
        product?.let {
            productName = it.name
            productPrice = it.price.toString()
            productQuantity = it.quantity.toString()
            selectedCategory = it.category
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Edit Product", color = Color(0xFFE97451)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Back",
                            tint = Color(0xFFE97451)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFF0EAD6)
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF0EAD6))
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Product Name",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            OutlinedTextField(
                value = productName,
                onValueChange = { productName = it },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF0EAD6),
                    unfocusedContainerColor = Color(0xFFF0EAD6),
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Black,
                    cursorColor = Color.Black,
                    focusedTextColor = Color(0xFFE97451),
                    unfocusedTextColor = Color(0xFFE97451)
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Product Type:",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            categories.forEach { category ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 50.dp, vertical = 4.dp)
                        .clickable { selectedCategory = category }
                ) {
                    RadioButton(
                        selected = (selectedCategory == category),
                        onClick = { selectedCategory = category },
                        colors = RadioButtonDefaults.colors(selectedColor = Color.Black)
                    )
                    Text(
                        text = category,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Product Price",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            OutlinedTextField(
                value = productPrice,
                onValueChange = { productPrice = it.filter { char -> char.isDigit() || char == '.' } },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF0EAD6),
                    unfocusedContainerColor = Color(0xFFF0EAD6),
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Black,
                    cursorColor = Color.Black,
                    focusedTextColor = Color(0xFFE97451),
                    unfocusedTextColor = Color(0xFFE97451)
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Product Quantity",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            OutlinedTextField(
                value = productQuantity,
                onValueChange = { productQuantity = it.filter { char -> char.isDigit() } },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF0EAD6),
                    unfocusedContainerColor = Color(0xFFF0EAD6),
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Black,
                    cursorColor = Color.Black,
                    focusedTextColor = Color(0xFFE97451),
                    unfocusedTextColor = Color(0xFFE97451)
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        val price = productPrice.toDoubleOrNull()
                        val quantity = productQuantity.toIntOrNull()
                        if (productName.isNotBlank() && price != null && quantity != null) {
                            product?.let {
                                val updatedProduct = it.copy(
                                    name = productName.trim(),
                                    price = price,
                                    quantity = quantity,
                                    category = selectedCategory
                                )
                                productViewModel.updateProduct(updatedProduct)
                                navController.popBackStack()
                            }
                        }
                    },
                    modifier = Modifier
                        .width(150.dp)
                        .height(50.dp),
                    enabled = productName.isNotBlank() && productPrice.isNotBlank() && productQuantity.isNotBlank()
                ) {
                    Text("Update Product")
                }
            }
        }
    }
}

