package com.example.toriiapp.room


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ProductEntity")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String
)

