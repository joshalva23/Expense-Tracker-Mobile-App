package com.joshdev.expensetracker.usecase.income

import com.joshdev.expensetracker.data.entity.IncomeEntity
import com.joshdev.expensetracker.data.repository.ExpenseRepository

class GetIncomeByIdUseCase(private val repository: ExpenseRepository) {
    suspend operator fun invoke(id:Int): IncomeEntity?{
        return repository.getIncomeById(id)
    }
}