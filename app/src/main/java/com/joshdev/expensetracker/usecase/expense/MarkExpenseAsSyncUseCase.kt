package com.joshdev.expensetracker.usecase.expense

import com.joshdev.expensetracker.data.repository.ExpenseRepository

class MarkExpenseAsSyncUseCase(private val expenseRepository: ExpenseRepository) {
    suspend operator fun invoke(id:Int){
        expenseRepository.markExpenseAsSynced(id)
    }
}