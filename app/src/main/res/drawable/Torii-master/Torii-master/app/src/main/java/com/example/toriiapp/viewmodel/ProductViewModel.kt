package com.example.toriiapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.toriiapp.repository.ProductRepository
import com.example.toriiapp.room.ProductEntity
import kotlinx.coroutines.launch

class ProductViewModel(private val repository: ProductRepository) : ViewModel() {
    val allProducts = repository.getAllProducts()

    fun addProduct(product: ProductEntity) = viewModelScope.launch {
        repository.addProductToRoom(product)
    }

    fun deleteProduct(product: ProductEntity) = viewModelScope.launch {
        repository.deleteProductFromRoom(product)
    }

    fun updateProduct(product: ProductEntity) = viewModelScope.launch {
        repository.updateProduct(product)
    }
}

class ProductViewModelFactory(private val repository: ProductRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
