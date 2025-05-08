package com.example.inventoryapp.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.inventoryapp.SplashScreen
import com.example.inventoryapp.ui.dashboardscreen.DashboardScreen

@Composable
fun AuthNavigation(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel
) {
    val navController = rememberNavController()
    val authState by authViewModel.authState.observeAsState(AuthState.Loading)

    when (authState) {
        AuthState.Loading -> {
            SplashScreen()
        }
        AuthState.Authenticated, AuthState.Idle, is AuthState.Error -> {
            // Set startDestination based on auth state
            val startDestination = when (authState) {
                AuthState.Authenticated -> "home"
                else -> "login"
            }

            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = modifier
            ) {
                composable("login") {
                    LoginScreen(modifier, navController, authViewModel)
                }
                composable("signup") {
                    SignupScreen(modifier, navController, authViewModel)
                }
                composable("home") {
                    DashboardScreen(modifier, navController, authViewModel)
                }
            }
        }
    }
}


