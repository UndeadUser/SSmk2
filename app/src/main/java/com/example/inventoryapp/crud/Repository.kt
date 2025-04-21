package com.example.inventoryapp.crud

class ProductRepository(private val productDao: ProductDao) {

    suspend fun insertProducts(product: Product) {
        productDao.insertProduct(product)
    }

    suspend fun getProductByID(id: Int): Product?{
        return productDao.getProductById(id)
    }

    suspend fun getAllProducts(): List<Product> {
        return productDao.getALlProducts()
    }

    suspend fun updateProduct(product: Product) {
        productDao.updateProduct(product)
    }

    suspend fun deleteProduct(product: Product) {
        productDao.deleteProduct(product)
    }

    suspend fun deleteAllProducts() {
        productDao.deleteAllProducts()
    }
}