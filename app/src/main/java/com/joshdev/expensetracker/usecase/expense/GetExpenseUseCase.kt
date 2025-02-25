package com.joshdev.expensetracker.usecase.expense

import com.joshdev.expensetracker.data.entity.ExpenseEntity
import com.joshdev.expensetracker.data.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow

class GetExpensesUseCase(private val repository: ExpenseRepository) {
    operator fun invoke(): Flow<List<ExpenseEntity>> {
        return repository.getAllExpenses()
    }
}