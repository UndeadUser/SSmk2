package com.example.inventoryapp.clouddata

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreProductRepository {

    private val db = FirebaseFirestore.getInstance()
    private val productsCollection = db.collection("products")

    suspend fun insertProduct(product: Product) {
        val newDoc = productsCollection.document()
        product.id = newDoc.id
        newDoc.set(product).await()
    }

    suspend fun getProductById(id: String): Product? {
        val docSnapshot = productsCollection.document(id).get().await()
        return docSnapshot.toObject(Product::class.java)  // Convert snapshot to Product object
    }

    suspend fun getAllProducts(): List<Product> {
        val currentUser = Firebase.auth.currentUser ?: return emptyList()

        val snapshot = productsCollection
            .whereEqualTo("userId", currentUser.uid)
            .get()
            .await()

        return snapshot.toObjects(Product::class.java)
    }

    suspend fun updateProduct(product: Product) {
        productsCollection.document(product.id).set(product).await()  // Overwrite existing document
    }

    suspend fun deleteProduct(product: Product) {
        productsCollection.document(product.id).delete().await()  // Delete document by ID
    }

    suspend fun deleteAllProducts() {
        val snapshot = productsCollection.get().await()  // Fetch all products
        for (doc in snapshot.documents) {
            doc.reference.delete().await()  // Delete each document
        }
    }
}
