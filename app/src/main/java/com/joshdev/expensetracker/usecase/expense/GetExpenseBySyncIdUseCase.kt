package com.joshdev.expensetracker.usecase.expense

import com.joshdev.expensetracker.data.entity.ExpenseEntity
import com.joshdev.expensetracker.data.repository.ExpenseRepository

class GetExpenseBySyncIdUseCase(private val expenseRepository: ExpenseRepository) {
    suspend operator fun invoke(syncId:String): ExpenseEntity?{
        return expenseRepository.getExpenseBySyncId(syncId)
    }
}