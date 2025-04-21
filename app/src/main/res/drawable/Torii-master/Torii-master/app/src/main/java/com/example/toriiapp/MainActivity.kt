package com.example.toriiapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.toriiapp.screens.DashboardScreen
import com.example.toriiapp.ui.theme.ToriiAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToriiAppTheme {
                DashboardScreen()
            }
        }
    }
}
