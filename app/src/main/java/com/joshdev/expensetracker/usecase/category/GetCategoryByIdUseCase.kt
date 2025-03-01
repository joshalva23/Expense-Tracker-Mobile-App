package com.joshdev.expensetracker.usecase.category

import com.joshdev.expensetracker.data.entity.CategoryEntity
import com.joshdev.expensetracker.data.repository.ExpenseRepository

class GetCategoryByIdUseCase(private val expenseRepository: ExpenseRepository) {
    suspend operator fun invoke(categoryId:Int): CategoryEntity?{
        return expenseRepository.getCategoryById(categoryId)
    }
}