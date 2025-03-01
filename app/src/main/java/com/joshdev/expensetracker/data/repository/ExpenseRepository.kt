package com.joshdev.expensetracker.data.repository

import com.joshdev.expensetracker.data.entity.ExpenseEntity
import com.joshdev.expensetracker.data.entity.CategoryEntity
import com.joshdev.expensetracker.data.entity.ExpenseWithCategory
import com.joshdev.expensetracker.data.entity.IncomeEntity
import com.joshdev.expensetracker.data.local.ExpenseDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.math.exp

class ExpenseRepository(private val expenseDatabase: ExpenseDatabase) {

    private val expenseDao = expenseDatabase.expenseDao()
    private val categoryDao = expenseDatabase.categoryDao()
    private val incomeDao = expenseDatabase.incomeDao()

    suspend fun addExpense(expense: ExpenseEntity) {
        expenseDao.insertExpense(expense)
    }

    suspend fun updateExpense(expense: ExpenseEntity) {
        expenseDao.updateExpense(expense)
    }

    suspend fun deleteExpense(expense: ExpenseEntity) {
        expenseDao.deleteExpense(expense)
    }

    fun getAllExpenses(): Flow<List<ExpenseEntity>> {
        return expenseDao.getAllExpenses()
    }

    fun getAllExpensesWithCategory(): Flow<List<ExpenseWithCategory>> {
        return expenseDao.getAllExpensesWithCategory()
    }

    fun getExpensesSortedByDateWithCategory(): Flow<List<ExpenseWithCategory>> {
        return expenseDao.getAllExpensesWithCategory().map { expenses ->
            expenses.sortedByDescending { it.date }
        }
    }

    fun getExpensesSortedByCategoryWithCategory(): Flow<List<ExpenseWithCategory>> {
        return expenseDao.getAllExpensesWithCategory().map { expenses ->
            expenses.sortedBy { it.categoryName }
        }
    }

    fun getExpensesByCategorySortedByDateWithCategory(categoryId: Int): Flow<List<ExpenseEntity>> {
        return expenseDao.getExpensesByCategoryWithCategory(categoryId)
    }


    suspend fun addCategory(category: CategoryEntity) {
        categoryDao.insertCategory(category)
    }

    fun getAllCategories(): Flow<List<CategoryEntity>> {
        return categoryDao.getAllCategories()
    }

    suspend fun getCategoryById(categoryId: Int): CategoryEntity?{
        return categoryDao.getCategoryById(categoryId)
    }


    suspend fun addIncome(income: IncomeEntity) {
        incomeDao.insertIncome(income)
    }

    suspend fun addMultipleIncomes(incomes: List<IncomeEntity>) {
        incomeDao.insertIncomes(incomes)
    }

    suspend fun updateIncome(income: IncomeEntity) {
        incomeDao.updateIncome(income)
    }

    suspend fun deleteIncome(income: IncomeEntity) {
        incomeDao.deleteIncome(income)
    }

    suspend fun deleteIncomeById(incomeId: Int) {
        incomeDao.deleteIncomeById(incomeId)
    }

    fun getAllIncomes(): Flow<List<IncomeEntity>> {
        return incomeDao.getAllIncomes()
    }

    suspend fun getIncomeById(incomeId: Int): IncomeEntity? {
        return incomeDao.getIncomeById(incomeId)
    }

    fun getIncomesByCategory(categoryId: Int): Flow<List<IncomeEntity>> {
        return incomeDao.getIncomesByCategory(categoryId)
    }

    fun getTotalIncome(): Flow<Double> {
        return incomeDao.getTotalIncome()
    }

    suspend fun markExpenseAsSynced(id: Int){
        expenseDao.markExpenseAsSynced(id)
    }

    suspend fun deleteSyncedExpenses(){
        expenseDao.deleteSyncedExpenses()
    }

    suspend fun updateExpenseSyncId(id:Int, syncId:String){
        expenseDao.updateSyncId(id, syncId)
    }

    suspend fun getUnSyncedExpenses(): List<ExpenseEntity>{
        return expenseDao.getUnsyncedExpenses()
    }

    suspend fun getExpenseBySyncId(syncId:String):ExpenseEntity?{
        return expenseDao.getExpenseBySyncId(syncId)
    }

    suspend fun markIncomeAsSynced(id:Int){
        incomeDao.markIncomeAsSynced(id)
    }

    suspend fun deleteSyncedIncomes(){
        incomeDao.deleteSyncedIncomes()
    }

    suspend fun updateIncomeSyncId(id:Int, syncId: String){
        incomeDao.updateSyncId(id, syncId)
    }

    suspend fun getUnSyncedIncomes(): List<IncomeEntity>{
        return incomeDao.getUnsyncedIncomes()
    }

    suspend fun getIncomeBySyncId(syncId:String): IncomeEntity?{
        return incomeDao.getIncomeBySyncId(syncId)
    }
}