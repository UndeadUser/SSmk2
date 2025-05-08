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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
    val statusBarColor = Color(0xFFE97451)

    // Set system UI colors once on composition
    LaunchedEffect(statusBarColor) {
        systemUiController.setStatusBarColor(statusBarColor)
        systemUiController.setNavigationBarColor(statusBarColor)
    }

    // Memoize nav items list
    val navItemList = remember {
        listOf(
            NavItem("Sales", R.drawable.sales),
            NavItem("Inventory", R.drawable.inventory),
            NavItem("Purchasing", R.drawable.purchase),
            NavItem("Account", R.drawable.account),
        )
    }

    // Preserve selected index across config changes
    var selectedIndex by rememberSaveable { mutableStateOf(0) }

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    // Efficiently compute whether to hide bottom bar
    val hideBottomBar by remember(currentRoute) {
        derivedStateOf {
            currentRoute in listOf("products", "add_products") || currentRoute?.startsWith("edit_product") == true
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            if (!hideBottomBar) {
                DashboardBottomBar(
                    navItems = navItemList,
                    selectedIndex = selectedIndex,
                    onItemSelected = { selectedIndex = it }
                )
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
fun DashboardBottomBar(
    navItems: List<NavItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        containerColor = Color(0xFFE97451),
        tonalElevation = 8.dp,
        modifier = modifier.shadow(8.dp, spotColor = Color.Gray)
    ) {
        navItems.forEachIndexed { index, navItem ->
            val isSelected = selectedIndex == index
            val textColor = if (isSelected) Color(0xFFD6CDBA) else Color(0xFFF0EAD6)
            val iconSize = if (isSelected) 28.dp else 24.dp

            NavigationBarItem(
                selected = isSelected,
                onClick = { onItemSelected(index) },
                icon = {
                    Icon(
                        painter = painterResource(id = navItem.icon),
                        contentDescription = navItem.label,
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

@Composable
fun ContentScreen(
    modifier: Modifier = Modifier,
    selectedIndex: Int,
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    key(selectedIndex) {
        when (selectedIndex) {
            0 -> Sales()
            1 -> Inventory()
            2 -> Purchasing()
            3 -> Account(authViewModel = authViewModel)
        }
    }
}

