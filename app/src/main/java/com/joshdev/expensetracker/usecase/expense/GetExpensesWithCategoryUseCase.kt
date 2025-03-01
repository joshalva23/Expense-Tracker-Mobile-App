package com.joshdev.expensetracker.usecase.expense

import com.joshdev.expensetracker.data.entity.ExpenseWithCategory
import com.joshdev.expensetracker.data.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow

class GetExpensesWithCategoryUseCase(private val repository: ExpenseRepository) {
    operator fun invoke(): Flow<List<ExpenseWithCategory>> {
        return repository.getAllExpensesWithCategory()
    }
}