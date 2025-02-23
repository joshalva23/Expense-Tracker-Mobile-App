package com.joshdev.expensetracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: String,
    val amount: Double,
    val category: String,
    val description: String,
    val date: Long
)
