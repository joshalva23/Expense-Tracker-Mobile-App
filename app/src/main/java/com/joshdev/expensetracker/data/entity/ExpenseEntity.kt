package com.joshdev.expensetracker.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

interface Transaction {
    val title: String
    val amount: Double
    val date: Long
    val isSynced: Boolean
    val syncId: String?
}

@Entity(
    tableName = "expenses"
)
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    override val title: String ="",
    override val amount: Double = 0.0,
    override val date: Long = 0L,
    val categoryId: Int = -1,
    override val isSynced: Boolean = false,
    override val syncId: String? = null
) : Transaction {
    fun toMutableMap(): MutableMap<String, Any?> {
        return mutableMapOf(
            "title" to title,
            "amount" to amount,
            "date" to date,
            "categoryId" to categoryId,
            "isSynced" to isSynced,
            "syncId" to syncId
        )
    }
}

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
    override val title: String ="",
    override val amount: Double = 0.0,
    override val date: Long = 0L,
    val categoryId: Int = -1,
    override val isSynced: Boolean = false,
    override val syncId: String? = null
): Transaction {
    fun toMutableMap(): MutableMap<String, Any?> {
        return mutableMapOf(
            "title" to title,
            "amount" to amount,
            "date" to date,
            "categoryId" to categoryId,
            "isSynced" to isSynced,
            "syncId" to syncId
        )
    }
}

data class ExpenseWithCategory(
    val id: Int,
    val title: String,
    val amount: Double,
    val date: Long,
    val categoryName: String,
    val categoryIcon: String?
)