package com.example.inventoryapp.crud

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel(private val productRepository: ProductRepository) : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    init {
        getAllProducts()
    }

    fun insertProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            productRepository.insertProducts(product)
            getAllProducts()
        }
    }

    fun getAllProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            _products.value = productRepository.getAllProducts()
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            productRepository.updateProduct(product)
            getAllProducts()
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            productRepository.deleteProduct(product)
            getAllProducts()
        }
    }

    fun deleteAllProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            productRepository.deleteAllProducts()
            getAllProducts()
        }
    }
}