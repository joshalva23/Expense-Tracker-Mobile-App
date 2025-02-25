package com.joshdev.expensetracker.usecase.income

import com.joshdev.expensetracker.data.repository.ExpenseRepository

class DeleteIncomeByIdUseCase(private val repository: ExpenseRepository) {
    suspend operator fun invoke(incomeId: Int) {
        repository.deleteIncomeById(incomeId)
    }
}