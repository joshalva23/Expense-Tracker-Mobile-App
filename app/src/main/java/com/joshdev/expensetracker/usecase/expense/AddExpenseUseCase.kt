package com.joshdev.expensetracker.usecase.expense

import com.joshdev.expensetracker.data.entity.ExpenseEntity
import com.joshdev.expensetracker.data.repository.ExpenseRepository

class AddExpenseUseCase(private val repository: ExpenseRepository) {
    suspend operator fun invoke(expense: ExpenseEntity) {
        repository.addExpense(expense)
    }
}