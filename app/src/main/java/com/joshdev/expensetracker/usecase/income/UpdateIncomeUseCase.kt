package com.joshdev.expensetracker.usecase.income

import com.joshdev.expensetracker.data.entity.IncomeEntity
import com.joshdev.expensetracker.data.repository.ExpenseRepository

class UpdateIncomeUseCase(private val repository: ExpenseRepository) {
    suspend operator fun invoke(income: IncomeEntity) {
        repository.updateIncome(income)
    }
}