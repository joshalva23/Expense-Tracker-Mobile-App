package com.joshdev.expensetracker.usecase.income

import com.joshdev.expensetracker.data.repository.ExpenseRepository
import kotlin.math.exp

class MarkIncomeAsSyncUseCase(private val expenseRepository: ExpenseRepository) {
    suspend operator fun invoke(id:Int){
        expenseRepository.markIncomeAsSynced(id)
    }
}