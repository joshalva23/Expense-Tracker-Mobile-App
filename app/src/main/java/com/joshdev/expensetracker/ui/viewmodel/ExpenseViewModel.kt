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
import com.joshdev.expensetracker.usecase.expense.ExpenseUseCases

class ExpenseViewModel(
    private val expenseUseCases: ExpenseUseCases,
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
                _categories.value = categoryList
            }
        }
    }

    private fun loadExpenses() {
        viewModelScope.launch {
            expenseUseCases.getExpense().collect { expenseList ->
                _expenses.value = expenseList
            }
        }
    }

    fun addExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            expenseUseCases.addExpense(expense)
            loadExpenses()
        }
    }

    fun deleteExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            expenseUseCases.deleteExpense(expense)
            loadExpenses()
        }
    }

    fun updateExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            expenseUseCases.updateExpense(expense)
            loadExpenses()
        }
    }

    fun loadExpensesSortedByDate() {
        viewModelScope.launch {
            expenseUseCases.getSortedExpense.byDate().collect { sortedList ->
                _sortedExpenses.value = sortedList
            }
        }
    }

    fun loadExpensesSortedByCategory() {
        viewModelScope.launch {
            expenseUseCases.getSortedExpense.byCategory().collect { sortedList ->
                _sortedExpenses.value = sortedList
            }
        }
    }
}

class ExpenseViewModelFactory(
    private val expenseUseCases: ExpenseUseCases,
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpenseViewModel(
                expenseUseCases,
                getCategoriesUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
