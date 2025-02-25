package com.joshdev.expensetracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.joshdev.expensetracker.data.entity.CategoryEntity
import com.joshdev.expensetracker.data.entity.IncomeEntity
import com.joshdev.expensetracker.usecase.category.GetCategoriesUseCase
import com.joshdev.expensetracker.usecase.income.IncomeUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class IncomeViewModel(
    private val incomeUseCases: IncomeUseCases,
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    private val _incomes = MutableStateFlow<List<IncomeEntity>>(emptyList())
    val incomes: StateFlow<List<IncomeEntity>> = _incomes

    private val _categories = MutableStateFlow<List<CategoryEntity>>(emptyList())
    val categories: StateFlow<List<CategoryEntity>> = _categories.asStateFlow()

    init {
        fetchIncomes()
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

    private fun fetchIncomes() {
        viewModelScope.launch {
            incomeUseCases.getAllIncomes().collect { incomeList ->
                _incomes.value = incomeList
            }
        }
    }

    fun addIncome(income: IncomeEntity) {
        viewModelScope.launch {
            incomeUseCases.addIncome(income)
            fetchIncomes()
        }
    }

    fun updateIncome(income: IncomeEntity) {
        viewModelScope.launch {
            incomeUseCases.updateIncome(income)
            fetchIncomes()
        }
    }

    fun deleteIncome(income: IncomeEntity) {
        viewModelScope.launch {
            incomeUseCases.deleteIncome(income)
            fetchIncomes()
        }
    }
}


class IncomeViewModelFactory(
    private val incomeUseCases: IncomeUseCases,
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IncomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return IncomeViewModel(
                incomeUseCases,
                getCategoriesUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
