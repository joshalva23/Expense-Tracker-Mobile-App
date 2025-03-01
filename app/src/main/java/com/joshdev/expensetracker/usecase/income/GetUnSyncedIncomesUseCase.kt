package com.joshdev.expensetracker.usecase.income

import com.joshdev.expensetracker.data.entity.IncomeEntity
import com.joshdev.expensetracker.data.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow

class GetUnSyncedIncomesUseCase(private val expenseRepository: ExpenseRepository) {
    suspend operator fun invoke():List<IncomeEntity>{
        return expenseRepository.getUnSyncedIncomes()
    }
}