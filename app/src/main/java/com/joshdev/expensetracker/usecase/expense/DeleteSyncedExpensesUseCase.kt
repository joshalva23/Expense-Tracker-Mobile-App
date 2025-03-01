package com.joshdev.expensetracker.usecase.expense

import com.joshdev.expensetracker.data.repository.ExpenseRepository

class DeleteSyncedExpensesUseCase(private val expenseRepository: ExpenseRepository) {
    suspend operator fun invoke(){
        expenseRepository.deleteSyncedExpenses()
    }
}