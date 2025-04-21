package com.example.toriiapp.repository

import com.example.toriiapp.data.ProductDAO
import com.example.toriiapp.room.ProductEntity
import kotlinx.coroutines.flow.Flow

class ProductRepository(private val productsDB: ProductDAO) {
    private val productDao: ProductDAO = productsDB

    fun getAllProducts(): Flow<List<ProductEntity>> = productDao.getAllProducts()

    suspend fun addProductToRoom(product: ProductEntity) {
        productDao.addProduct(product)
    }

    suspend fun deleteProductFromRoom(product: ProductEntity) {
        productDao.deleteProduct(product)
    }

    suspend fun updateProduct(product: ProductEntity) {
        productDao.updateProduct(product)
    }
}

