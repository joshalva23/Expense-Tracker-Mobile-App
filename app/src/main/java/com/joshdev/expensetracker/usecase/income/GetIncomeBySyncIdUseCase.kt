package com.joshdev.expensetracker.usecase.income

import com.joshdev.expensetracker.data.entity.IncomeEntity
import com.joshdev.expensetracker.data.repository.ExpenseRepository

class GetIncomeBySyncIdUseCase(private val expenseRepository: ExpenseRepository) {
    suspend operator fun invoke(syncId:String): IncomeEntity?{
        return expenseRepository.getIncomeBySyncId(syncId)
    }
}