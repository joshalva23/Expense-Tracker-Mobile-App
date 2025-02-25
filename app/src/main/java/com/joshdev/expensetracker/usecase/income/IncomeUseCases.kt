package com.joshdev.expensetracker.usecase.income

data class IncomeUseCases(
    val addIncome: AddIncomeUseCase,
    val updateIncome: UpdateIncomeUseCase,
    val deleteIncome: DeleteIncomeUseCase,
    val deleteIncomeById: DeleteIncomeByIdUseCase,
    val getAllIncomes: GetAllIncomesUseCase,
    val getIncomeById: GetIncomeByIdUseCase
)