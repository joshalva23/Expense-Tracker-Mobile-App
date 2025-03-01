package com.joshdev.expensetracker.ui.screens


import androidx.compose.foundation.layout.*

import androidx.compose.runtime.*

import androidx.compose.ui.tooling.preview.Preview



@Preview
@Composable
fun PreviewScreens() {
    Column {
        DashboardScreen()
        ExpenseScreen()
        IncomeScreen()
    }
}

