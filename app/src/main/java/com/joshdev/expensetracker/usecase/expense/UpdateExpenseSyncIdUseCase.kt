package com.joshdev.expensetracker.usecase.expense

import com.joshdev.expensetracker.data.repository.ExpenseRepository

class UpdateExpenseSyncIdUseCase(private val expenseRepository: ExpenseRepository) {
    suspend operator fun invoke(id: Int, syncId:String){
        expenseRepository.updateExpenseSyncId(id, syncId)
    }
}