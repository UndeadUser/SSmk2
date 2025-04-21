package com.example.toriiapp.navigation

sealed class Screens (val screen: String){
    data object Home: Screens("Home")
    data object Inventory: Screens("Inventory")
    data object Statistics: Screens("Statistics")
    data object Settings: Screens("Settings")
}