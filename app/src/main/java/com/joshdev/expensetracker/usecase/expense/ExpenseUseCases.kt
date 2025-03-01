package com.joshdev.expensetracker.usecase.expense

data class ExpenseUseCases (
    val addExpense: AddExpenseUseCase,
    val deleteExpense: DeleteExpenseUseCase,
    val deleteSyncedExpenses: DeleteSyncedExpensesUseCase,
    val getExpense: GetExpensesUseCase,
    val getSortedExpense: GetSortedExpensesUseCase,
    val markExpenseAsSync: MarkExpenseAsSyncUseCase,
    val updateExpenseSyncId: UpdateExpenseSyncIdUseCase,
    val updateExpense: UpdateExpenseUseCase,
    val getUnSyncedExpenses: GetUnSyncedExpensesUseCase,
    val getExpenseBySyncId: GetExpenseBySyncIdUseCase,
    val deleteAllExpenses: DeleteAllExpensesUseCase
)