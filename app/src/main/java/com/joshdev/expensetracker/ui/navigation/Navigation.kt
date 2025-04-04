package com.joshdev.expensetracker.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import com.joshdev.expensetracker.firebase.firestore.FirebaseSyncManager
import com.joshdev.expensetracker.ui.screens.AuthScreen
import com.joshdev.expensetracker.ui.screens.DashboardScreen
import com.joshdev.expensetracker.ui.screens.ExpenseScreen
import com.joshdev.expensetracker.ui.viewmodel.ExpenseViewModel
import com.joshdev.expensetracker.ui.viewmodel.IncomeViewModel

import com.joshdev.expensetracker.ui.screens.IncomeScreen
import com.joshdev.expensetracker.ui.screens.ProfileScreen
import com.joshdev.expensetracker.ui.viewmodel.AuthViewModel

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    data object Dashboard : Screen("dashboard", "Dashboard", Icons.Filled.Home)
    data object Expenses : Screen("expenses", "Expenses", Icons.AutoMirrored.Filled.List)
    data object Income : Screen("income", "Income", Icons.Filled.Star)
    data object Auth : Screen("auth", "auth", Icons.Filled.AccountBox)
    data object Profile : Screen("profile", "profile", Icons.Filled.AccountBox)
}

@Composable
fun ExpenseTrackerApp(
    expenseViewModel: ExpenseViewModel,
    incomeViewModel: IncomeViewModel,
    authViewModel: AuthViewModel,
    firebaseSyncManager: FirebaseSyncManager
    ) {
    val navController = rememberNavController()

    val user by authViewModel.user.collectAsState()


    LaunchedEffect(user) {
        if (user == null) {
            navController.navigate("auth") { popUpTo(0) }
        } else {
            navController.navigate("dashboard") { popUpTo(0) }
        }
    }


    Scaffold(
        bottomBar = { if(user != null) BottomNavBar(navController) }
    ) { paddingValue ->

        NavHost(
            navController = navController,
            startDestination = if (user == null) Screen.Auth.route else Screen.Dashboard.route,
            modifier = Modifier.padding(paddingValue)
        ) {
            composable(Screen.Auth.route){
                AuthScreen (authViewModel) {
                    navController.navigate("dashboard") {
                        popUpTo("auth") { inclusive = true }
                    }
                }
            }
            composable(Screen.Dashboard.route) { DashboardScreen(expenseViewModel, incomeViewModel) }
            composable(Screen.Expenses.route) { ExpenseScreen(expenseViewModel) }
            composable(Screen.Income.route) { IncomeScreen(incomeViewModel) }
            composable(Screen.Profile.route) { ProfileScreen(authViewModel, firebaseSyncManager) }
        }
    }
}

@Composable
fun BottomNavBar(navController: NavHostController) {
    val items = listOf(
        Screen.Dashboard,
        Screen.Expenses,
        Screen.Income,
        Screen.Profile
    )
    NavigationBar(
        modifier = Modifier.padding(16.dp)
            .clip(RoundedCornerShape(40.dp)),
        containerColor = Color(red = 200, green = 200, blue = 200, alpha = 80)

    ) {
        val navBackStackEntry = navController.currentBackStackEntryAsState().value
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { screen ->
                NavigationBarItem(
                    icon = { Icon(screen.icon, contentDescription = screen.title) },
                    label = { Text(screen.title) },
                    selected = currentRoute == screen.route,
                    onClick = { navController.navigate(screen.route) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.DarkGray,
                        selectedTextColor =Color.DarkGray,
                        unselectedIconColor = Color.Black,
                        unselectedTextColor = Color.Black
                    )
                )
        }
    }
}
