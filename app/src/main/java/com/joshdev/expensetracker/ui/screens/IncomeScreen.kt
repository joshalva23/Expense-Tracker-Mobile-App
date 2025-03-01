package com.joshdev.expensetracker.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.joshdev.expensetracker.data.entity.IncomeEntity
import com.joshdev.expensetracker.ui.screens.components.AddIncomeDialog
import com.joshdev.expensetracker.ui.screens.components.IncomeItem
import com.joshdev.expensetracker.ui.viewmodel.IncomeViewModel

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun IncomeScreen(viewModel: IncomeViewModel = viewModel()) {
    val incomeList by viewModel.incomes.collectAsState()
    val categories by viewModel.categories.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Income") }) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Income", tint = Color.White)
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(incomeList) { income ->
                    val categoryName: String = categories.find { it.id == income.categoryId }?.name ?: ""
                    IncomeItem(
                        income = income,
                        categoryName = categoryName,
                        categories = categories,
                        onEdit = { updatedIncome -> viewModel.updateIncome(updatedIncome) },
                        onDelete = { viewModel.deleteIncome(it) }
                    )
                }
            }
        }
    }

    if (showDialog) {
        AddIncomeDialog (
            categories = categories,
            onDismiss = { showDialog = false },
            onSave = { title, amount, categoryId ->
                viewModel.addIncome(
                    IncomeEntity(
                        title = title,
                        amount = amount,
                        date = System.currentTimeMillis(),
                        categoryId = categoryId,
                        isSynced = false,
                        syncId = null
                    )
                )
                showDialog = false
            }
        )
    }
}