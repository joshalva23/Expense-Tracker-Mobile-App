package com.joshdev.expensetracker.usecase.income

import com.joshdev.expensetracker.data.entity.IncomeEntity
import com.joshdev.expensetracker.data.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow

class GetAllIncomesUseCase(private val repository: ExpenseRepository) {
    operator fun invoke(): Flow<List<IncomeEntity>> {
        return repository.getAllIncomes()
    }
}
