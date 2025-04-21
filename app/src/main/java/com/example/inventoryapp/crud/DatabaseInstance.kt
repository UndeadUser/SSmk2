package com.example.inventoryapp.crud

import android.content.Context
import androidx.room.Room

object DatabaseInstance {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "product_database"
            )
                .addMigrations(AppDatabase.MIGRATION_1_2) // 🔹 Apply migration
                .build()
            INSTANCE = instance
            instance
        }
    }
}