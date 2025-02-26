package com.joshdev.expensetracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.joshdev.expensetracker.data.entity.CategoryEntity
import com.joshdev.expensetracker.data.entity.ExpenseEntity
import com.joshdev.expensetracker.data.entity.IncomeEntity
import com.joshdev.expensetracker.data.entity.Transaction
import com.joshdev.expensetracker.ui.viewmodel.ExpenseViewModel
import com.joshdev.expensetracker.ui.viewmodel.IncomeViewModel
import com.joshdev.expensetracker.utils.CurrencyUtils
import com.joshdev.expensetracker.utils.DateUtils
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





@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ExpenseScreen(viewModel: ExpenseViewModel = viewModel()) {
    val expenses by viewModel.expenses.collectAsState()
    val categories by viewModel.categories.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Expenses") }) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Expense", tint = Color.White)
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(expenses) { expense ->
                    val categoryName: String = categories.find { it.id == expense.categoryId }?.name ?: ""
                    ExpenseItem(
                        expense = expense,
                        categoryName = categoryName,
                        categories = categories,
                        onEdit = { updatedExpense -> viewModel.updateExpense(updatedExpense) },
                        onDelete = { viewModel.deleteExpense(it) }
                    )
                }
            }
        }
    }

    if (showDialog) {
        AddExpenseDialog(
            categories = categories,
            onDismiss = { showDialog = false },
            onSave = { title, amount, categoryId ->
                viewModel.addExpense(
                    ExpenseEntity(
                        title = title,
                        amount = amount,
                        date = System.currentTimeMillis(),
                        categoryId = categoryId
                    )
                )
                showDialog = false
            }
        )
    }
}

@Composable
fun AddExpenseDialog(
    categories: List<CategoryEntity>,
    onDismiss: () -> Unit,
    onSave: (String, Double, Int) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf(categories.firstOrNull()?.id ?: 1) }

    LaunchedEffect(categories) {
        println("Categories received: $categories")
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Expense") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Dropdown for category selection
                var expanded by remember { mutableStateOf(false) }
                Box {
                    OutlinedButton(onClick = { expanded = true }) {
                        Text(categories.firstOrNull { it.id == selectedCategoryId }?.name ?: "Select Category")
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.name) },
                                onClick = {
                                    selectedCategoryId = category.id
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amountValue = amount.toDoubleOrNull()
                    if (title.isNotEmpty() && amountValue != null) {
                        onSave(title, amountValue, selectedCategoryId)
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun EditExpenseDialog(
    expense: ExpenseEntity,
    categories: List<CategoryEntity>,
    onDismiss: () -> Unit,
    onSave: (String, Double, Int) -> Unit
) {
    var title by remember { mutableStateOf(expense.title) }
    var amount by remember { mutableStateOf(expense.amount.toString()) }
    var selectedCategoryId by remember { mutableStateOf(expense.categoryId) }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Expense") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Box {
                    OutlinedButton(onClick = { expanded = true }) {
                        Text(categories.firstOrNull { it.id == selectedCategoryId }?.name ?: "Select Category")
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.name) },
                                onClick = {
                                    selectedCategoryId = category.id
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amountValue = amount.toDoubleOrNull()
                    if (title.isNotEmpty() && amountValue != null) {
                        onSave(title, amountValue, selectedCategoryId)
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun ExpenseItem(
    expense: ExpenseEntity,
    categoryName: String,
    categories: List<CategoryEntity>? = null,
    onEdit: ((ExpenseEntity) -> Unit)? = null,
    onDelete: ((ExpenseEntity) -> Unit)? = null
) {
    var showMenu by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { showMenu = onEdit != null || onDelete != null }, // Open menu only if actions exist
        elevation = CardDefaults.cardElevation()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                // Display Amount in bold soothing red as "Rs. 1000"
                Text(
                    "-Rs. ${expense.amount}",
                    style = MaterialTheme.typography.titleLarge.copy(color = Color(0xFFD32F2F), fontWeight = FontWeight.ExtraBold)
                )

                // Title and Date displayed as tags with rounded corners and grey background
                Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                    // Title tag
                    Text(
                        expense.title,
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.Black),
                        modifier = Modifier
                            .background(Color(0xFFBDBDBD), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Date tag (assuming expense.date is a String or formatted appropriately)
                    Text(
                        DateUtils.formatDate(expense.date),
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.Black),
                        modifier = Modifier
                            .background(Color(0xFFBDBDBD), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Category badge with rounded corner and background
            Text(
                text = categoryName,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }

        // Show menu only if edit or delete is available
        if (onEdit != null || onDelete != null) {
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                onEdit?.let {
                    DropdownMenuItem(
                        text = { Text("Edit") },
                        onClick = {
                            showMenu = false
                            showEditDialog = true
                        }
                    )
                }
                onDelete?.let {
                    DropdownMenuItem(
                        text = { Text("Delete") },
                        onClick = {
                            showMenu = false
                            it(expense)
                        }
                    )
                }
            }
        }
    }

    // Show Edit Dialog when triggered
    if (showEditDialog && categories != null) {
        EditExpenseDialog(
            expense = expense,
            categories = categories,
            onDismiss = { showEditDialog = false },
            onSave = { newTitle, newAmount, newCategoryId ->
                onEdit?.invoke(
                    expense.copy(
                        title = newTitle,
                        amount = newAmount,
                        categoryId = newCategoryId
                    )
                )
                showEditDialog = false
            }
        )
    }
}



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
        AddIncomeDialog(
            categories = categories,
            onDismiss = { showDialog = false },
            onSave = { title, amount, categoryId ->
                viewModel.addIncome(
                    IncomeEntity(
                        title = title,
                        amount = amount,
                        date = System.currentTimeMillis(),
                        categoryId = categoryId
                    )
                )
                showDialog = false
            }
        )
    }
}

@Composable
fun IncomeItem(
    income: IncomeEntity,
    categoryName: String,
    categories: List<CategoryEntity>? = null,
    onEdit: ((IncomeEntity) -> Unit)? = null,
    onDelete: ((IncomeEntity) -> Unit)? = null
) {
    var showMenu by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { showMenu = onEdit != null || onDelete != null },
        elevation = CardDefaults.cardElevation()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                // Display amount in bold and intense red
                Text(
                    "+Rs. ${income.amount}",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = Color(0xFF388E3C),
                        fontWeight = FontWeight.ExtraBold
                    )
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    // Title tag
                    Text(
                        income.title,
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.Black),
                        modifier = Modifier
                            .background(Color(0xFFBDBDBD), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Date tag (formatted)
                    Text(
                        DateUtils.formatDate(income.date),
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.Black),
                        modifier = Modifier
                            .background(Color(0xFFBDBDBD), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Category badge with rounded corners and primary background
            Text(
                text = categoryName,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }


        if (onEdit != null || onDelete != null) {
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                onEdit?.let {
                    DropdownMenuItem(
                        text = { Text("Edit") },
                        onClick = {
                            showMenu = false
                            showEditDialog = true
                        }
                    )
                }
                onDelete?.let {
                    DropdownMenuItem(
                        text = { Text("Delete") },
                        onClick = {
                            showMenu = false
                            it(income)
                        }
                    )
                }
            }
        }
    }

    if (showEditDialog && categories != null) {
        EditIncomeDialog(
            income = income,
            categories = categories,
            onDismiss = { showEditDialog = false },
            onSave = { newTitle, newAmount, newCategoryId ->
                onEdit?.invoke(
                    income.copy(
                        title = newTitle,
                        amount = newAmount,
                        categoryId = newCategoryId
                    )
                )
                showEditDialog = false
            }
        )
    }
}

@Composable
fun EditIncomeDialog(
    income: IncomeEntity,
    categories: List<CategoryEntity>,
    onDismiss: () -> Unit,
    onSave: (String, Double, Int) -> Unit
) {
    var title by remember { mutableStateOf(income.title) }
    var amount by remember { mutableStateOf(income.amount.toString()) }
    var selectedCategoryId by remember { mutableStateOf(income.categoryId) }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Income") },
        text = {
            Column {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = amount, onValueChange = { amount = it }, label = { Text("Amount") }, keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number))
                Spacer(modifier = Modifier.height(8.dp))
                Box {
                    OutlinedButton(onClick = { expanded = true }) {
                        Text(categories.firstOrNull { it.id == selectedCategoryId }?.name ?: "Select Category")
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        categories.forEach { category ->
                            DropdownMenuItem(text = { Text(category.name) }, onClick = {
                                selectedCategoryId = category.id
                                expanded = false
                            })
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val amountValue = amount.toDoubleOrNull()
                if (title.isNotEmpty() && amountValue != null) {
                    onSave(title, amountValue, selectedCategoryId)
                }
            }) { Text("Save") }
        },
        dismissButton = { Button(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
fun AddIncomeDialog(
    categories: List<CategoryEntity>,
    onDismiss: () -> Unit,
    onSave: (String, Double, Int) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf(categories.firstOrNull()?.id ?: 1) }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Income") },
        text = {
            Column {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = amount, onValueChange = { amount = it }, label = { Text("Amount") }, keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number))
                Spacer(modifier = Modifier.height(8.dp))
                Box {
                    OutlinedButton(onClick = { expanded = true }) {
                        Text(categories.firstOrNull { it.id == selectedCategoryId }?.name ?: "Select Category")
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        categories.forEach { category ->
                            DropdownMenuItem(text = { Text(category.name) }, onClick = {
                                selectedCategoryId = category.id
                                expanded = false
                            })
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val amountValue = amount.toDoubleOrNull()
                if (title.isNotEmpty() && amountValue != null) {
                    onSave(title, amountValue, selectedCategoryId)
                }
            }) { Text("Save") }
        },
        dismissButton = { Button(onClick = onDismiss) { Text("Cancel") } }
    )
}



@Preview
@Composable
fun PreviewScreens() {
    Column {
        DashboardScreen()
        ExpenseScreen()
        IncomeScreen()
    }
}
