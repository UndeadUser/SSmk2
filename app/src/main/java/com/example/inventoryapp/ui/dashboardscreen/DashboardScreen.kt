package com.example.inventoryapp.ui.dashboardscreen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.inventoryapp.NavItem
import com.example.inventoryapp.R
import com.example.inventoryapp.auth.AuthViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setStatusBarColor(Color(0xFFE97451))
        systemUiController.setNavigationBarColor(Color(0xFFE97451))
    }

    val navController = rememberNavController()
    val navItemList = listOf(
        NavItem("Sales", R.drawable.sales),
        NavItem("Inventory", R.drawable.inventory),
        NavItem("Purchasing", R.drawable.purchase),
        NavItem("Account", R.drawable.account),
    )

    var selectedIndex by remember { mutableIntStateOf(0) }

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val hideBottomBar = currentRoute in listOf("products", "add_products") || currentRoute?.startsWith("edit_product") == true

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (!hideBottomBar) {
                NavigationBar(
                    containerColor = Color(0xFFE97451),
                    tonalElevation = 8.dp,
                    modifier = Modifier.shadow(8.dp, spotColor = Color.Gray)
                ) {
                    navItemList.forEachIndexed { index, navItem ->
                        val isSelected = selectedIndex == index
                        val textColor = if (isSelected) Color(0xFFD6CDBA) else Color(0xFFF0EAD6)
                        val iconSize = if (isSelected) 28.dp else 24.dp

                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { selectedIndex = index },
                            icon = {
                                Icon(
                                    painter = painterResource(id = navItem.icon),
                                    contentDescription = null,
                                    tint = textColor,
                                    modifier = Modifier.size(iconSize)
                                )
                            },
                            label = {
                                Text(
                                    text = navItem.label,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = textColor
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = Color.Transparent,
                                selectedIconColor = textColor,
                                selectedTextColor = textColor,
                                unselectedIconColor = textColor,
                                unselectedTextColor = textColor
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        ContentScreen(
            modifier = Modifier.padding(innerPadding),
            selectedIndex = selectedIndex,
            navController = navController,
            authViewModel = authViewModel
        )
    }
}

@Composable
fun ContentScreen(
    modifier: Modifier = Modifier,
    selectedIndex: Int,
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    when (selectedIndex) {
        0 -> Sales()
        1 -> Inventory(navController)
        2 -> Purchasing()
        3 -> Account(authViewModel = authViewModel)
    }
}
