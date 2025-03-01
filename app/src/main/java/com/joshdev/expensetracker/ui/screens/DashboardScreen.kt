package com.joshdev.expensetracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.joshdev.expensetracker.data.entity.ExpenseEntity
import com.joshdev.expensetracker.data.entity.IncomeEntity
import com.joshdev.expensetracker.data.entity.Transaction
import com.joshdev.expensetracker.ui.screens.components.ExpenseItem
import com.joshdev.expensetracker.ui.screens.components.IncomeItem
import com.joshdev.expensetracker.ui.viewmodel.ExpenseViewModel
import com.joshdev.expensetracker.ui.viewmodel.IncomeViewModel
import com.joshdev.expensetracker.utils.CurrencyUtils
import com.joshdev.expensetracker.utils.ExpenseUtils

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DashboardScreen(
    expenseViewModel: ExpenseViewModel = viewModel(),
    incomeViewModel: IncomeViewModel = viewModel()
) {
    val expenses by expenseViewModel.expenses.collectAsState()
    val incomes by incomeViewModel.incomes.collectAsState()
    val categories by expenseViewModel.categories.collectAsState()


    val result = ExpenseUtils.calculateMonthlyBalance(incomes, expenses)

    val totalExpenses = result.totalExpense
    val totalIncome = result.totalIncome
    val netBalance = result.netBalance

    val mixedTransactions : MutableList<Transaction> = mutableListOf()
    expenses.forEach{ mixedTransactions.add(it) }
    incomes.forEach{ mixedTransactions.add(it) }

    val sortedList = mixedTransactions.sortedByDescending { it.date }

//    val sortedExpenses = expenses.sortedByDescending { it.date }.toMutableList()
//    val sortedIncomes = incomes.sortedByDescending { it.date }.toMutableList()
//    val mergedTransactions = mutableListOf<Pair<Any, String>>()

//    while (sortedExpenses.isNotEmpty() || sortedIncomes.isNotEmpty()) {
//        when {
//            sortedExpenses.isEmpty() -> {
//                mergedTransactions.add(sortedIncomes.removeAt(0) to "income")
//            }
//            sortedIncomes.isEmpty() -> {
//                mergedTransactions.add(sortedExpenses.removeAt(0) to "expense")
//            }
//            sortedExpenses.first().date >= sortedIncomes.first().date -> {
//                mergedTransactions.add(sortedExpenses.removeAt(0) to "expense")
//            }
//            else -> {
//                mergedTransactions.add(sortedIncomes.removeAt(0) to "income")
//            }
//        }
//    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Total Income: ${CurrencyUtils.getDefaultCurrency()} $totalIncome",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Total Expenses: ${CurrencyUtils.getDefaultCurrency()} $totalExpenses",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Net Balance: ${CurrencyUtils.getDefaultCurrency()} $netBalance",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (netBalance >= 0) Color(0xFF4CAF50) else Color(0xFFD32F2F)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Recent Transactions",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp)
            ) {
                items(sortedList) { transaction ->
                    val categoryName: String =
                        categories.find {
                            (it.id == ((transaction as? ExpenseEntity)?.categoryId
                                ?: (transaction as? IncomeEntity)?.categoryId))
                        }?.name ?: ""
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 4.dp
                        )) {
                            if (transaction is ExpenseEntity) {
                                ExpenseItem(transaction , categoryName)
                            } else if(transaction is IncomeEntity) {
                                IncomeItem(transaction , categoryName)
                            }
                        }
                    }
                }
            }
        }
    }
}