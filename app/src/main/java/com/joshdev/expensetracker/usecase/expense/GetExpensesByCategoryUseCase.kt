package com.joshdev.expensetracker.usecase.expense

import com.joshdev.expensetracker.data.entity.ExpenseEntity
import com.joshdev.expensetracker.data.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow

class GetExpensesByCategoryUseCase(private val repository: ExpenseRepository) {
    operator fun invoke(categoryId: Int): Flow<List<ExpenseEntity>> {
        return repository.getExpensesByCategorySortedByDateWithCategory(categoryId)
    }
}