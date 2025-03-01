package com.joshdev.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.joshdev.expensetracker.firebase.auth.repository.AuthRepository
import com.joshdev.expensetracker.data.local.ExpenseDatabase
import com.joshdev.expensetracker.data.repository.ExpenseRepository
import com.joshdev.expensetracker.firebase.firestore.FirebaseSyncManager
import com.joshdev.expensetracker.ui.navigation.ExpenseTrackerApp
import com.joshdev.expensetracker.ui.viewmodel.AuthViewModel
import com.joshdev.expensetracker.ui.viewmodel.AuthViewModelFactory
import com.joshdev.expensetracker.ui.viewmodel.ExpenseViewModel
import com.joshdev.expensetracker.ui.viewmodel.ExpenseViewModelFactory
import com.joshdev.expensetracker.ui.viewmodel.IncomeViewModel
import com.joshdev.expensetracker.ui.viewmodel.IncomeViewModelFactory
import com.joshdev.expensetracker.usecase.category.GetCategoriesUseCase
import com.joshdev.expensetracker.usecase.expense.AddExpenseUseCase
import com.joshdev.expensetracker.usecase.expense.DeleteAllExpensesUseCase
import com.joshdev.expensetracker.usecase.expense.DeleteExpenseUseCase
import com.joshdev.expensetracker.usecase.expense.DeleteSyncedExpensesUseCase
import com.joshdev.expensetracker.usecase.expense.ExpenseUseCases
import com.joshdev.expensetracker.usecase.expense.GetExpenseBySyncIdUseCase
import com.joshdev.expensetracker.usecase.expense.GetExpensesUseCase
import com.joshdev.expensetracker.usecase.expense.GetSortedExpensesUseCase
import com.joshdev.expensetracker.usecase.expense.GetUnSyncedExpensesUseCase
import com.joshdev.expensetracker.usecase.expense.MarkExpenseAsSyncUseCase
import com.joshdev.expensetracker.usecase.expense.UpdateExpenseSyncIdUseCase
import com.joshdev.expensetracker.usecase.expense.UpdateExpenseUseCase
import com.joshdev.expensetracker.usecase.income.AddIncomeUseCase
import com.joshdev.expensetracker.usecase.income.DeleteAllIncomesUseCase
import com.joshdev.expensetracker.usecase.income.DeleteIncomeByIdUseCase
import com.joshdev.expensetracker.usecase.income.DeleteIncomeUseCase
import com.joshdev.expensetracker.usecase.income.DeleteSyncedIncomesUseCase
import com.joshdev.expensetracker.usecase.income.GetAllIncomesUseCase
import com.joshdev.expensetracker.usecase.income.GetIncomeByIdUseCase
import com.joshdev.expensetracker.usecase.income.GetIncomeBySyncIdUseCase
import com.joshdev.expensetracker.usecase.income.GetUnSyncedIncomesUseCase
import com.joshdev.expensetracker.usecase.income.IncomeUseCases
import com.joshdev.expensetracker.usecase.income.MarkIncomeAsSyncUseCase
import com.joshdev.expensetracker.usecase.income.UpdateIncomeSyncIdUseCase
import com.joshdev.expensetracker.usecase.income.UpdateIncomeUseCase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val database = ExpenseDatabase.getInstance(this)
        val repository = ExpenseRepository(database)


        val getCategoriesUseCase = GetCategoriesUseCase(repository)

        val expenseUseCases = ExpenseUseCases(
            addExpense = AddExpenseUseCase(repository),
            deleteExpense = DeleteExpenseUseCase(repository),
            getExpense = GetExpensesUseCase(repository),
            getSortedExpense =  GetSortedExpensesUseCase(repository),
            updateExpense = UpdateExpenseUseCase(repository),
            markExpenseAsSync = MarkExpenseAsSyncUseCase(repository),
            updateExpenseSyncId = UpdateExpenseSyncIdUseCase(repository),
            deleteSyncedExpenses = DeleteSyncedExpensesUseCase(repository),
            getUnSyncedExpenses = GetUnSyncedExpensesUseCase(repository),
            getExpenseBySyncId = GetExpenseBySyncIdUseCase(repository),
            deleteAllExpenses = DeleteAllExpensesUseCase(repository)
        )

        val expenseFactory = ExpenseViewModelFactory(
            expenseUseCases,
            getCategoriesUseCase
        )
        val expenseViewModel = ViewModelProvider(this, expenseFactory)[ExpenseViewModel::class.java]

        val incomeUseCases = IncomeUseCases(
            addIncome = AddIncomeUseCase(repository),
            getAllIncomes = GetAllIncomesUseCase(repository),
            getIncomeById = GetIncomeByIdUseCase(repository),
            updateIncome = UpdateIncomeUseCase(repository),
            deleteIncome = DeleteIncomeUseCase(repository),
            deleteIncomeById = DeleteIncomeByIdUseCase(repository),
            markIncomeAsSync = MarkIncomeAsSyncUseCase(repository),
            deleteSyncedIncomes = DeleteSyncedIncomesUseCase(repository),
            updateIncomeSyncId = UpdateIncomeSyncIdUseCase(repository),
            getUnSyncedIncomes = GetUnSyncedIncomesUseCase(repository),
            getIncomeBySyncId = GetIncomeBySyncIdUseCase(repository),
            deleteAllIncomes = DeleteAllIncomesUseCase(repository)
        )

        val incomeFactory = IncomeViewModelFactory(
            incomeUseCases,
            getCategoriesUseCase
        )

        val incomeViewModel = ViewModelProvider(this, incomeFactory)[IncomeViewModel::class.java]

        val authRepository = AuthRepository()
        val authFactory = AuthViewModelFactory(authRepository)
        val authViewModel = ViewModelProvider(this, authFactory)[AuthViewModel::class.java]

        val firebaseSyncManager = FirebaseSyncManager(
            firestore = Firebase.firestore,
            expensesUseCase = expenseUseCases,
            incomeUseCase = incomeUseCases,
            authRepository
        )

        setContent {
            ExpenseTrackerApp(expenseViewModel, incomeViewModel, authViewModel, firebaseSyncManager)
        }
    }
}