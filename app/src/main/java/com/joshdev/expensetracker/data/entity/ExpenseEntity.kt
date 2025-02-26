package com.joshdev.expensetracker.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

open class Transaction(
    open val title:String,
    open val amount: Double,
    open val date:Long
)

@Entity(
    tableName = "expenses"
)
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    override val title: String,
    override val amount: Double,
    override val date: Long,
    val categoryId: Int
) : Transaction(title, amount, date)

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
    override val title:String,
    override val amount: Double,
    override val date: Long,
    val categoryId: Int
): Transaction(title, amount, date)

data class ExpenseWithCategory(
    val id: Int,
    val title: String,
    val amount: Double,
    val date: Long,
    val categoryName: String,
    val categoryIcon: String?
)