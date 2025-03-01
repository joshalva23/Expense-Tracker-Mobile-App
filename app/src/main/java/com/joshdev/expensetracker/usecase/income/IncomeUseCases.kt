package com.joshdev.expensetracker.usecase.income

data class IncomeUseCases(
    val addIncome: AddIncomeUseCase,
    val updateIncome: UpdateIncomeUseCase,
    val deleteIncome: DeleteIncomeUseCase,
    val deleteIncomeById: DeleteIncomeByIdUseCase,
    val getAllIncomes: GetAllIncomesUseCase,
    val getIncomeById: GetIncomeByIdUseCase,
    val deleteSyncedIncomes: DeleteSyncedIncomesUseCase,
    val updateIncomeSyncId: UpdateIncomeSyncIdUseCase,
    val markIncomeAsSync: MarkIncomeAsSyncUseCase,
    val getUnSyncedIncomes: GetUnSyncedIncomesUseCase,
    val getIncomeBySyncId: GetIncomeBySyncIdUseCase,
    val deleteAllIncomes: DeleteAllIncomesUseCase
)