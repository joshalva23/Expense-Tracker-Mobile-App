package com.joshdev.expensetracker.firebase.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.joshdev.expensetracker.data.entity.ExpenseEntity
import com.joshdev.expensetracker.data.entity.IncomeEntity
import com.joshdev.expensetracker.firebase.auth.repository.AuthRepository
import com.joshdev.expensetracker.usecase.expense.ExpenseUseCases
import com.joshdev.expensetracker.usecase.income.IncomeUseCases
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class FirebaseSyncManager(
    private val firestore: FirebaseFirestore,
    private val expensesUseCase: ExpenseUseCases,
    private val incomeUseCase: IncomeUseCases,
    private val authRepository: AuthRepository
) {

    private val expensesCollection = firestore.collection("expenses")
    private val incomesCollection = firestore.collection("incomes")

    private suspend fun syncExpensesToFirebase() = withContext(Dispatchers.IO) {
        val user = authRepository.getUser()
        user?.let {
            val unsyncedExpenses: List<ExpenseEntity> = expensesUseCase.getUnSyncedExpenses()

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
                    expensesUseCase.updateExpenseSyncId(expense.id, documentRef.id)
                    expensesUseCase.markExpenseAsSync(expense.id)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }


    private suspend fun syncIncomesToFirebase() = withContext(Dispatchers.IO) {

        val user = authRepository.getUser()

        user?.let {
            val unsyncedIncomes = incomeUseCase.getUnSyncedIncomes()
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

                    incomeUseCase.updateIncomeSyncId(income.id, documentRef.id)
                    incomeUseCase.markIncomeAsSync(income.id)
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
                        val existingExpense = expensesUseCase.getExpenseBySyncId(doc.id)
                        if (existingExpense == null) {
                            expensesUseCase.addExpense(expense)
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
                        val existingIncome = incomeUseCase.getIncomeBySyncId(doc.id)
                        if (existingIncome == null) {
                            incomeUseCase.addIncome(income)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun deleteSyncedIncomes(){
        incomeUseCase.deleteSyncedIncomes()
    }

    private suspend fun deleteSyncedExpenses(){
        expensesUseCase.deleteSyncedExpenses()
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