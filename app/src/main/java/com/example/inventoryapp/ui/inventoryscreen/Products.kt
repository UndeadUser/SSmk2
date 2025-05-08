package com.example.inventoryapp.ui.inventoryscreen

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.inventoryapp.clouddata.Product
import com.example.inventoryapp.clouddata.ProductViewModel
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.example.inventoryapp.R
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.inventoryapp.clouddata.FirestoreProductRepository
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Products(
    navController: NavHostController,
    firestoreProductRepository: FirestoreProductRepository
) {
    // ViewModel should be provided by a ViewModelProvider or Hilt in a real app
    val productViewModel: ProductViewModel = remember { ProductViewModel(firestoreProductRepository) }
    val products by productViewModel.products.collectAsState()
    val categories = remember { listOf("All", "Carbonated", "Juice", "Alcohol") }
    var selectedCategory by remember { mutableStateOf("All") }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var showFilterPanel by remember { mutableStateOf(false) }

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val panelOffset by animateDpAsState(
        targetValue = if (showFilterPanel) 0.dp else screenWidth,
        label = ""
    )

    // Memoize filtering for performance
    val filteredProducts = remember(products, selectedCategory, searchQuery) {
        products.filter {
            (selectedCategory == "All" || it.category == selectedCategory) &&
                    (searchQuery.isBlank() || it.name.contains(searchQuery, ignoreCase = true))
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.systemBarsPadding(),
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Products", color = Color(0xFFE97451)) },
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
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color(0xFFF0EAD6))
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search by product or category", color = Color.Gray) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.Gray) },
                    trailingIcon = {
                        IconButton(onClick = { showFilterPanel = true }) {
                            Icon(
                                painter = painterResource(R.drawable.filter),
                                contentDescription = "Filter",
                                tint = Color.Gray
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color.Gray,
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = Color.Black
                    )
                )

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(filteredProducts, key = { it.id }) { product ->
                        ProductCard(
                            product = product,
                            navController = navController,
                            onDelete = { productViewModel.deleteProduct(product) }
                        )
                    }
                }
            }
        }

        // Filter Panel Overlay
        if (showFilterPanel) {
            FilterPanelOverlay(
                categories = categories,
                selectedCategory = selectedCategory,
                onCategorySelected = {
                    selectedCategory = it
                    showFilterPanel = false
                },
                panelOffset = panelOffset,
                onDismiss = { showFilterPanel = false }
            )
        }
    }
}

@Composable
fun ProductCard(
    product: Product,
    navController: NavHostController,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val productTextColor = Color(0xFFE97451)
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { navController.navigate("edit_product/${product.id}") },
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0EAD6))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = productTextColor
                )
                Text(
                    text = "Price: ₱${product.price}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = productTextColor
                )
                Text(
                    text = "Quantity: x${product.quantity}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = productTextColor
                )
                Text(
                    text = "Category: ${product.category}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = productTextColor
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete Product",
                    tint = productTextColor
                )
            }
        }
    }
}

@Composable
fun FilterPanelOverlay(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    panelOffset: Dp,
    onDismiss: () -> Unit
) {
    val density = LocalDensity.current

    // Semi-transparent background to dismiss the panel
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f))
            .clickable { onDismiss() }
            .windowInsetsPadding(WindowInsets.systemBars)
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(250.dp)
                .offset {
                    with(density) {
                        IntOffset(panelOffset.toPx().roundToInt(), 0)
                    }
                }
                .background(Color(0xFFF0EAD6))
                .padding(
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
                .align(Alignment.CenterEnd)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    "Filter by Category",
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                categories.forEach { category ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onCategorySelected(category) },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedCategory == category,
                            colors = RadioButtonDefaults.colors(selectedColor = Color.Black),
                            onClick = { onCategorySelected(category) }
                        )
                        Text(
                            category,
                            modifier = Modifier.padding(start = 8.dp),
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductsScreenPreview() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val firestoreProductRepository = remember { FirestoreProductRepository() }
    Products(navController, firestoreProductRepository)
}

