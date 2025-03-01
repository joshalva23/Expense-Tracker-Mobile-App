package com.joshdev.expensetracker.usecase.expense

import com.joshdev.expensetracker.data.entity.ExpenseEntity
import com.joshdev.expensetracker.data.repository.ExpenseRepository

class GetUnSyncedExpensesUseCase(private val expenseRepository: ExpenseRepository) {
    suspend operator fun invoke(): List<ExpenseEntity>{
        return expenseRepository.getUnSyncedExpenses()
    }
}