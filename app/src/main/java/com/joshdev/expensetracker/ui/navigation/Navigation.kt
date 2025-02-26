package com.joshdev.expensetracker.ui.navigation

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.joshdev.expensetracker.ui.screens.DashboardScreen
import com.joshdev.expensetracker.ui.screens.ExpenseScreen
import com.joshdev.expensetracker.ui.viewmodel.ExpenseViewModel
import com.joshdev.expensetracker.ui.viewmodel.IncomeViewModel

import com.joshdev.expensetracker.ui.screens.IncomeScreen

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Filled.Home)
    object Expenses : Screen("expenses", "Expenses", Icons.Filled.List)
    object Income : Screen("income", "Income", Icons.Filled.Star)
}

@Composable
fun ExpenseTrackerApp(expenseViewModel: ExpenseViewModel, incomeViewModel: IncomeViewModel) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { paddingValue ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(paddingValue)
        ) {
            composable(Screen.Dashboard.route) { DashboardScreen(expenseViewModel, incomeViewModel) }
            composable(Screen.Expenses.route) { ExpenseScreen(expenseViewModel) }
            composable(Screen.Income.route) { IncomeScreen(incomeViewModel) }
        }
    }
}

@Composable
fun BottomNavBar(navController: NavHostController) {
    val items = listOf(
        Screen.Dashboard,
        Screen.Expenses,
        Screen.Income
    )
    NavigationBar(
        modifier = Modifier.padding(16.dp)
            .clip(RoundedCornerShape(40.dp)),
        containerColor = Color(red = 200, green = 200, blue = 200, alpha = 80)

    ) {
        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.title) },
                label = { Text(screen.title) },
                selected = false,
                onClick = { navController.navigate(screen.route) }
            )
        }
    }
}
