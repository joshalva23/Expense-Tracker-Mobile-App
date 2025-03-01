package com.joshdev.expensetracker.usecase.category

import com.joshdev.expensetracker.data.entity.CategoryEntity
import com.joshdev.expensetracker.data.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow

class GetCategoriesUseCase(private val expenseRepository: ExpenseRepository) {
    operator fun invoke(): Flow<List<CategoryEntity>>{
        return expenseRepository.getAllCategories()
    }


}