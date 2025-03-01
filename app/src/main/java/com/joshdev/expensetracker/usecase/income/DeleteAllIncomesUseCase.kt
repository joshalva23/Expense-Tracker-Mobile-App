package com.joshdev.expensetracker.usecase.income

import com.joshdev.expensetracker.data.repository.ExpenseRepository

class DeleteAllIncomesUseCase(private val expenseRepository: ExpenseRepository) {
    suspend operator fun invoke(){
        expenseRepository.deleteAllIncomes()
    }
}