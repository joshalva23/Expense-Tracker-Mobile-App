package com.joshdev.expensetracker.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "expenses"
)
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val amount: Double,
    val date: Long,
    val categoryId: Int
)

@Entity(
    tableName = "categories",
    indices = [Index(value = ["name"], unique = true)]
)
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val icon: String?
)

@Entity(
    tableName = "income"
)
data class IncomeEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title:String,
    val amount: Double,
    val date: Long,
    val categoryId: Int
)

data class ExpenseWithCategory(
    val id: Int,
    val title: String,
    val amount: Double,
    val date: Long,
    val categoryName: String,
    val categoryIcon: String?
)