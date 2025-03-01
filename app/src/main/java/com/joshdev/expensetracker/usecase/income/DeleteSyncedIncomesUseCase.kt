package com.joshdev.expensetracker.usecase.income

import com.joshdev.expensetracker.data.repository.ExpenseRepository

class DeleteSyncedIncomesUseCase(private val expenseRepository: ExpenseRepository) {
    suspend operator fun invoke(){
        expenseRepository.deleteSyncedIncomes()
    }
}