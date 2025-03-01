package com.joshdev.expensetracker.firebase.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.joshdev.expensetracker.data.entity.ExpenseEntity
import com.joshdev.expensetracker.data.entity.IncomeEntity
import com.joshdev.expensetracker.data.local.ExpenseDao
import com.joshdev.expensetracker.data.local.ExpenseDatabase
import com.joshdev.expensetracker.data.local.IncomeDao
import com.joshdev.expensetracker.firebase.auth.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class FirebaseSyncManager(
    private val firestore: FirebaseFirestore,
    private val expenseDatabase: ExpenseDatabase,
    private val authRepository: AuthRepository
) {

    private val expenseDao: ExpenseDao = expenseDatabase.expenseDao()
    private val incomeDao: IncomeDao = expenseDatabase.incomeDao()
    private val expensesCollection = firestore.collection("expenses")
    private val incomesCollection = firestore.collection("incomes")

    private suspend fun syncExpensesToFirebase() = withContext(Dispatchers.IO) {
        val user = authRepository.getUser()
        user?.let {
            val unsyncedExpenses: List<ExpenseEntity> = expenseDao.getUnsyncedExpenses()

            unsyncedExpenses.forEach { expense ->
                val documentRef = if (expense.syncId.isNullOrEmpty()) {
                    expensesCollection.document()
                } else {
                    expensesCollection.document(expense.syncId)
                }

                val expenseData =
                    expense.copy(syncId = documentRef.id).toMutableMap().apply {
                        remove("syncId")
                        remove("isSynced")
                        put("uid", user.uid)
                    }

                try {
                    documentRef.set(expenseData).await()
                    expenseDao.updateSyncId(expense.id, documentRef.id)
                    expenseDao.markExpenseAsSynced(expense.id)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }


    private suspend fun syncIncomesToFirebase() = withContext(Dispatchers.IO) {

        val user = authRepository.getUser()

        user?.let {
            val unsyncedIncomes = incomeDao.getUnsyncedIncomes()
            unsyncedIncomes.forEach { income ->
                val documentRef = if (income.syncId.isNullOrEmpty()) {
                    incomesCollection.document()
                } else {
                    incomesCollection.document(income.syncId)
                }

                val incomeData =
                    income.copy(syncId = documentRef.id).toMutableMap().apply {
                        remove("syncId")
                        remove("isSynced")
                        put("uid", user.uid)
                    }

                try {
                    documentRef.set(incomeData).await()


                    incomeDao.updateSyncId(income.id, documentRef.id)
                    incomeDao.markIncomeAsSynced(income.id)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }

    private suspend fun fetchExpensesFromFirebase(startDate: Long) = withContext(Dispatchers.IO) {
        val user = authRepository.getUser()
        user?.let {
            try {
                val snapshot = expensesCollection
                    .whereEqualTo("uid", user.uid)
                    .whereGreaterThanOrEqualTo("date", startDate)
                    .get()
                    .await()

                snapshot.documents.forEach { doc ->
                    val expense = doc.toObject(ExpenseEntity::class.java)
                        ?.copy(syncId = doc.id, isSynced = true)

                    expense?.let {
                        val existingExpense = expenseDao.getExpenseBySyncId(doc.id)
                        if (existingExpense == null) {
                            expenseDao.insertExpense(expense)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun fetchIncomesFromFirebase(startDate: Long) = withContext(Dispatchers.IO) {
        val user = authRepository.getUser()
        user?.let {
            try {
                val snapshot = incomesCollection
                    .whereEqualTo("uid", user.uid)
                    .whereGreaterThanOrEqualTo("date", startDate)
                    .get()
                    .await()

                snapshot.documents.forEach { doc ->
                    val income = doc.toObject(IncomeEntity::class.java)
                        ?.copy(syncId = doc.id, isSynced = true)

                    income?.let {
                        val existingIncome = incomeDao.getIncomeBySyncId(doc.id)
                        if (existingIncome == null) {
                            incomeDao.insertIncome(income)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun deleteSyncedIncomes(){
        incomeDao.deleteSyncedIncomes()
    }

    private suspend fun deleteSyncedExpenses(){
        expenseDao.deleteSyncedExpenses()
    }


    suspend fun startBackup(){
        syncExpensesToFirebase()
        syncIncomesToFirebase()
    }


    suspend fun fetchBackup(date:Long){
        fetchIncomesFromFirebase(date)
        fetchExpensesFromFirebase(date)
    }

    suspend fun deleteSyncedData(){
        deleteSyncedExpenses()
        deleteSyncedIncomes()
    }
}