package com.joshdev.expensetracker.usecase.expense

import com.joshdev.expensetracker.data.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import com.joshdev.expensetracker.data.entity.ExpenseWithCategory

class GetSortedExpensesUseCase(private val repository: ExpenseRepository) {
    fun byDate(): Flow<List<ExpenseWithCategory>> = repository.getExpensesSortedByDateWithCategory()
    fun byCategory(): Flow<List<ExpenseWithCategory>> = repository.getExpensesSortedByCategoryWithCategory()
}




