package com.joshdev.expensetracker.usecase.income

import com.joshdev.expensetracker.data.entity.IncomeEntity
import com.joshdev.expensetracker.data.repository.ExpenseRepository

class AddIncomeUseCase(private val repository: ExpenseRepository) {
    suspend operator fun invoke(income: IncomeEntity) {
        repository.addIncome(income)
    }
}