package com.joshdev.expensetracker.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.joshdev.expensetracker.data.entity.ExpenseEntity
import com.joshdev.expensetracker.data.entity.ExpenseWithCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseEntity)

    @Update
    suspend fun updateExpense(expense: ExpenseEntity)

    @Delete
    suspend fun deleteExpense(expense: ExpenseEntity)

    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

    @Query("DELETE FROM expenses")
    suspend fun deleteAll()

    @Query("SELECT e.*, c.name AS categoryName, c.icon AS categoryIcon " +
            "FROM expenses AS e " +
            "INNER JOIN categories AS c ON e.categoryId = c.id " +
            "ORDER BY e.date DESC")
    fun getAllExpensesWithCategory(): Flow<List<ExpenseWithCategory>>

    @Query("SELECT e.*, c.name AS categoryName, c.icon AS categoryIcon " +
            "FROM expenses AS e " +
            "INNER JOIN categories AS c ON e.categoryId = c.id " +
            "WHERE e.categoryId = :categoryId " +
            "ORDER BY e.date DESC")
    fun getExpensesByCategoryWithCategory(categoryId: Int): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getExpensesSortedByDate(): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses WHERE isSynced = 0")
    suspend fun getUnsyncedExpenses(): List<ExpenseEntity>

    @Query("DELETE FROM expenses WHERE isSynced = 1")
    suspend fun deleteSyncedExpenses()

    @Query("UPDATE expenses SET isSynced = 1 WHERE id = :expenseId")
    suspend fun markExpenseAsSynced(expenseId: Int)

    @Query("UPDATE expenses SET syncId = :syncId WHERE id = :expenseId")
    suspend fun updateSyncId(expenseId: Int, syncId: String)

    @Query("SELECT * FROM expenses WHERE syncId = :syncId LIMIT 1")
    suspend fun getExpenseBySyncId(syncId: String): ExpenseEntity?


}