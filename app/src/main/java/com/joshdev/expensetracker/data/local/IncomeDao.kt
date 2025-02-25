package com.joshdev.expensetracker.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.joshdev.expensetracker.data.entity.IncomeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IncomeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIncome(income: IncomeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIncomes(incomes: List<IncomeEntity>)

    @Update
    suspend fun updateIncome(income: IncomeEntity)

    @Delete
    suspend fun deleteIncome(income: IncomeEntity)

    @Query("DELETE FROM income WHERE id = :incomeId")
    suspend fun deleteIncomeById(incomeId: Int)

    @Query("SELECT * FROM income ORDER BY date DESC")
    fun getAllIncomes(): Flow<List<IncomeEntity>>

    @Query("SELECT * FROM income WHERE id = :incomeId")
    suspend fun getIncomeById(incomeId: Int): IncomeEntity?

    @Query("SELECT * FROM income WHERE categoryId = :categoryId ORDER BY date DESC")
    fun getIncomesByCategory(categoryId: Int): Flow<List<IncomeEntity>>

    @Query("SELECT SUM(amount) FROM income")
    fun getTotalIncome(): Flow<Double>
}
