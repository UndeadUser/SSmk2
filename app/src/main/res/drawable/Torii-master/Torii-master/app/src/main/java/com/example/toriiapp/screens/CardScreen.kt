package com.example.toriiapp.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.toriiapp.R
import com.example.toriiapp.room.ProductEntity
import com.example.toriiapp.viewmodel.ProductViewModel

@Composable
fun CardScreen(
    navController: NavController,
    viewModel: ProductViewModel,
    productId: Int?,
    productName: String?
) {
    val showDialog = remember { mutableStateOf(false) }
    val updateDialog = remember { mutableStateOf(false) }
    var inputProduct by remember { mutableStateOf("") }
    val empty by remember { mutableStateOf("") }
    if (productId == null || productName.isNullOrBlank()) {
        Log.e("CardScreen", "Invalid arguments: ProductId=$productId, ProductName=$productName")
        return
    }
    val displayedProductId = productId
    val displayedProductName = productName


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(15.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(40.dp))
                Row {
                    Text(
                        text = "Id: ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = if (displayedProductId != -1) displayedProductId.toString() else "Invalid ID",
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                    )
                }
                Row {
                    Text(
                        text = "Name: ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = displayedProductName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                    )
                }
                Spacer(modifier = Modifier.height(30.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(MaterialTheme.colorScheme.background)
                ) { }

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    OutlinedButton(
                        onClick = { showDialog.value = true },
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier.size(height = 50.dp, width = 100.dp)
                    ) {
                        Row {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_delete_24),
                                contentDescription = null
                            )
                            Text(text = "Delete")
                        }
                    }
                    OutlinedButton(
                        onClick = { updateDialog.value = true },
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier.size(height = 50.dp, width = 100.dp)
                    ) {
                        Row {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_edit_24),
                                contentDescription = null
                            )
                            Text(text = "Update")
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
                Button(onClick = { showDialog.value = false }) {
                    Text(text = "No")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (displayedProductId != -1) {
                            viewModel.deleteProduct(
                                product = ProductEntity(
                                    id = displayedProductId,
                                    title = displayedProductName
                                )
                            )
                        }
                        showDialog.value = false
                        navController.popBackStack()
                    }
                ) {
                    Text(text = "Yes")
                }
            },
            title = {
                Text(
                    text = "Delete Product",
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp
                )
            },
            text = {
                Text(
                    text = "Are you sure?",
                    fontSize = 20.sp
                )
            }
        )
    }

    if (updateDialog.value) {
        AlertDialog(
            onDismissRequest = { updateDialog.value = false },
            dismissButton = {
                Button(
                    onClick = {
                        updateDialog.value = false
                        inputProduct = empty
                    }
                ) {
                    Text(text = "Cancel")
                }
            },
            confirmButton = {
                if (inputProduct.isNotEmpty()) {
                    Button(
                        onClick = {
                            if (displayedProductId != -1) {
                                val newProduct = ProductEntity(displayedProductId, inputProduct)
                                viewModel.updateProduct(newProduct)
                            }
                            navController.popBackStack()
                            updateDialog.value = false
                            inputProduct = empty
                        }
                    ) {
                        Text(text = "Update")
                    }
                }
            },
            title = {
                Text(
                    text = "Update product name",
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
