package com.example.inventoryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.ui.Modifier
import com.example.inventoryapp.auth.AuthNavigation
import com.example.inventoryapp.auth.AuthViewModel
import com.example.inventoryapp.ui.theme.InventoryAppTheme
import com.google.accompanist.insets.ProvideWindowInsets

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val authViewModel: AuthViewModel by viewModels()
        setContent {
            ProvideWindowInsets {
                InventoryAppTheme {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        AuthNavigation(modifier = Modifier.padding(innerPadding), authViewModel = authViewModel)
                    }
                }
            }
        }
    }
}