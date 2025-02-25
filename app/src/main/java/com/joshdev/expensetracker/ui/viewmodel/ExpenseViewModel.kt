package com.joshdev.expensetracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.joshdev.expensetracker.data.entity.CategoryEntity
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.joshdev.expensetracker.data.entity.ExpenseEntity
import com.joshdev.expensetracker.data.entity.ExpenseWithCategory
import com.joshdev.expensetracker.usecase.category.GetCategoriesUseCase
import com.joshdev.expensetracker.usecase.expense.AddExpenseUseCase
import com.joshdev.expensetracker.usecase.expense.DeleteExpenseUseCase
import com.joshdev.expensetracker.usecase.expense.GetExpensesUseCase
import com.joshdev.expensetracker.usecase.expense.GetSortedExpensesUseCase
import com.joshdev.expensetracker.usecase.expense.UpdateExpenseUseCase
import kotlinx.coroutines.delay

class ExpenseViewModel(
    private val addExpenseUseCase: AddExpenseUseCase,
    private val deleteExpenseUseCase: DeleteExpenseUseCase,
    private val updateExpenseUseCase: UpdateExpenseUseCase,
    private val getExpensesUseCase: GetExpensesUseCase,
    private val getSortedExpensesUseCase: GetSortedExpensesUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    private val _expenses = MutableStateFlow<List<ExpenseEntity>>(emptyList())
    val expenses: StateFlow<List<ExpenseEntity>> = _expenses.asStateFlow()

    private val _sortedExpenses = MutableStateFlow<List<ExpenseWithCategory>>(emptyList())
    val sortedExpenses: StateFlow<List<ExpenseWithCategory>> = _sortedExpenses.asStateFlow()


    private val _categories = MutableStateFlow<List<CategoryEntity>>(emptyList())
    val categories: StateFlow<List<CategoryEntity>> = _categories.asStateFlow()



    val totalExpenses: Double
        get() = _expenses.value.sumOf { it.amount }

    init {
        loadExpenses()
        loadCategories()
    }


    private fun loadCategories() {
        viewModelScope.launch {
            getCategoriesUseCase().collect { categoryList ->
                println("Fetched Categories: $categoryList")
                _categories.value = categoryList
            }
        }
    }

    private fun loadExpenses() {
        viewModelScope.launch {
            getExpensesUseCase().collect { expenseList ->
                _expenses.value = expenseList
            }
        }
    }

    fun addExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            addExpenseUseCase(expense)
            loadExpenses()
        }
    }

    fun deleteExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            deleteExpenseUseCase(expense)
            loadExpenses()
        }
    }

    fun updateExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            updateExpenseUseCase(expense)
            loadExpenses()
        }
    }

    fun loadExpensesSortedByDate() {
        viewModelScope.launch {
            getSortedExpensesUseCase.byDate().collect { sortedList ->
                _sortedExpenses.value = sortedList
            }
        }
    }

    fun loadExpensesSortedByCategory() {
        viewModelScope.launch {
            getSortedExpensesUseCase.byCategory().collect { sortedList ->
                _sortedExpenses.value = sortedList
            }
        }
    }
}

class ExpenseViewModelFactory(
    private val addExpenseUseCase: AddExpenseUseCase,
    private val deleteExpenseUseCase: DeleteExpenseUseCase,
    private val updateExpenseUseCase: UpdateExpenseUseCase,
    private val getExpensesUseCase: GetExpensesUseCase,
    private val getSortedExpensesUseCase: GetSortedExpensesUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpenseViewModel(
                addExpenseUseCase,
                deleteExpenseUseCase,
                updateExpenseUseCase,
                getExpensesUseCase,
                getSortedExpensesUseCase,
                getCategoriesUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
