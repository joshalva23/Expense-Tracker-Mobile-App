package com.joshdev.expensetracker.usecase.income

import com.joshdev.expensetracker.data.repository.ExpenseRepository

class UpdateIncomeSyncIdUseCase(private val expenseRepository: ExpenseRepository) {
    suspend operator fun invoke(id:Int, syncId:String){
        expenseRepository.updateIncomeSyncId(id,syncId)
    }
}