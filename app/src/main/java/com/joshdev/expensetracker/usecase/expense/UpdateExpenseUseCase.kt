package com.joshdev.expensetracker.usecase.expense

import com.joshdev.expensetracker.data.entity.ExpenseEntity
import com.joshdev.expensetracker.data.repository.ExpenseRepository

class UpdateExpenseUseCase(private val repository: ExpenseRepository) {
    suspend operator fun invoke(expense: ExpenseEntity) {
        repository.updateExpense(expense)
    }
}
