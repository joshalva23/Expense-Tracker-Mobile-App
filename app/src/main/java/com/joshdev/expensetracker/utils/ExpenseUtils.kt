package com.joshdev.expensetracker.utils

import com.joshdev.expensetracker.data.entity.ExpenseEntity
import com.joshdev.expensetracker.data.entity.IncomeEntity

import java.util.Calendar
import java.util.Date

object ExpenseUtils {

    /**
     * Returns the timestamp (millis) for the start of the given month (at 00:00:00.000).
     */
    fun getStartOfMonthMillis(year: Int, month: Int): Long {
        val cal = Calendar.getInstance()
        cal.set(year, month - 1, 1, 0, 0, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    /**
     * Returns the timestamp (millis) for the end of the given month (at 23:59:59.999 on the last day).
     */
    fun getEndOfMonthMillis(year: Int, month: Int): Long {
        val cal = Calendar.getInstance()
        cal.set(year, month - 1, 1, 23, 59, 59)
        cal.set(Calendar.MILLISECOND, 999)
        val lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        cal.set(Calendar.DAY_OF_MONTH, lastDay)
        return cal.timeInMillis
    }

    /**
     * Returns the timestamp (millis) for the first Monday of the given month.
     */
    fun getFirstMondayMillis(year: Int, month: Int): Long {
        val cal = Calendar.getInstance()
        cal.set(year, month - 1, 1, 0, 0, 0)
        cal.set(Calendar.MILLISECOND, 0)
        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            cal.add(Calendar.DAY_OF_MONTH, 1)
        }
        return cal.timeInMillis
    }

    /**
     * Checks if the given timestamp falls within the target month.
     */
    fun isDateInMonth(timestamp: Long, year: Int, month: Int): Boolean {
        val cal = Calendar.getInstance()
        cal.timeInMillis = timestamp
        val transYear = cal.get(Calendar.YEAR)
        val transMonth = cal.get(Calendar.MONTH) + 1 // Adjust for 0-index
        return (transYear == year && transMonth == month)
    }

    fun countMondaysPassed(targetYear: Int, targetMonth: Int): Int {
        val now = Calendar.getInstance()
        val isCurrentMonth = (targetYear == now.get(Calendar.YEAR) && targetMonth == now.get(Calendar.MONTH) + 1)
        val endDay = if (isCurrentMonth) now.get(Calendar.DAY_OF_MONTH) else {
            val cal = Calendar.getInstance()
            cal.set(targetYear, targetMonth - 1, 1)
            cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        }
        var count = 0
        val cal = Calendar.getInstance()
        for (day in 1..endDay) {
            cal.set(targetYear, targetMonth - 1, day)
            if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                count++
            }
        }
        return count
    }

    /**
     * For a weekly transaction whose start date is within the target month,
     * counts the number of Mondays from its start day through today (if current month)
     * or through the end of the month.
     */
    fun countMondaysAfter(transactionDateMillis: Long, targetYear: Int, targetMonth: Int): Int {
        val transCal = Calendar.getInstance().apply { timeInMillis = transactionDateMillis }
        val startDay = transCal.get(Calendar.DAY_OF_MONTH)

        val now = Calendar.getInstance()
        val isCurrentMonth = (targetYear == now.get(Calendar.YEAR) && targetMonth == now.get(Calendar.MONTH) + 1)
        val endDay = if (isCurrentMonth) now.get(Calendar.DAY_OF_MONTH) else {
            val cal = Calendar.getInstance()
            cal.set(targetYear, targetMonth - 1, 1)
            cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        }
        var count = 0
        val cal = Calendar.getInstance()
        for (day in startDay..endDay) {
            cal.set(targetYear, targetMonth - 1, day)
            if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                count++
            }
        }
        return count
    }

    fun getDayOfWeek(timestamp: Long): String {
        val calendar = Calendar.getInstance().apply { timeInMillis = timestamp }
        return when(calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> "Sunday"
            Calendar.MONDAY -> "Monday"
            Calendar.TUESDAY -> "Tuesday"
            Calendar.WEDNESDAY -> "Wednesday"
            Calendar.THURSDAY -> "Thursday"
            Calendar.FRIDAY -> "Friday"
            Calendar.SATURDAY -> "Saturday"
            else -> "Unknown"
        }
    }


    fun calculateMonthlyBalance(
        incomes: List<IncomeEntity>,
        expenses: List<ExpenseEntity>,
        targetYear: Int = Calendar.getInstance().get(Calendar.YEAR),
        targetMonth: Int = Calendar.getInstance().get(Calendar.MONTH) + 1  // 1-indexed
    ): CalculationResult {
        val startOfMonthMillis = getStartOfMonthMillis(targetYear, targetMonth)
        val endOfMonthMillis = getEndOfMonthMillis(targetYear, targetMonth)
        val firstMondayMillis = getFirstMondayMillis(targetYear, targetMonth)

        var totalIncome = 0.0
        var totalExpense = 0.0

        incomes.forEach { income ->
            when (income.categoryId) {
                1 -> { // One-Time: count only if the date falls within the target month.
                    if (isDateInMonth(income.date, targetYear, targetMonth)) {
                        totalIncome += income.amount
                    }
                }
                2 -> { // Weekly:
                    // Check if the transaction's start date is within the target month.
                    val transCal = Calendar.getInstance().apply { timeInMillis = income.date }
                    val transYear = transCal.get(Calendar.YEAR)
                    val transMonth = transCal.get(Calendar.MONTH) + 1
                    val mondayCount = if (transYear == targetYear && transMonth == targetMonth) {
                        // Count Mondays from the transaction's day onward.
                        countMondaysAfter(income.date, targetYear, targetMonth) + if(getDayOfWeek(income.date) != "Monday") 1 else 0
                    } else {
                        // Transaction started before the target month, so count all Mondays passed.
                        countMondaysPassed(targetYear, targetMonth)
                    }
                    totalIncome += income.amount * mondayCount
                }
                3 -> { // Monthly: count if the transaction started on or before the first day of the target month.
                    if (income.date >= startOfMonthMillis) {
                        totalIncome += income.amount
                    }
                }
                else -> return@forEach  // Skip unknown categoryId.
            }
        }

        expenses.forEach { expense ->
            when (expense.categoryId) {
                1 -> { // One-Time expense.
                    if (isDateInMonth(expense.date, targetYear, targetMonth)) {
                        totalExpense += expense.amount
                    }
                }
                2 -> { // Weekly expense.
                    val transCal = Calendar.getInstance().apply { timeInMillis = expense.date }
                    val transYear = transCal.get(Calendar.YEAR)
                    val transMonth = transCal.get(Calendar.MONTH) + 1
                    val mondayCount = if (transYear == targetYear && transMonth == targetMonth) {
                        countMondaysAfter(expense.date, targetYear, targetMonth) + if(getDayOfWeek(expense.date) != "Monday") 1 else 0
                    } else {
                        countMondaysPassed(targetYear, targetMonth)
                    }
                    totalExpense += expense.amount * mondayCount
                }
                3 -> { // Monthly expense.
                    if (expense.date >= startOfMonthMillis) {
                        totalExpense += expense.amount
                    }
                }
                else -> return@forEach  // Skip unknown categoryId.
            }
        }

        val netBalance = totalIncome - totalExpense
        return CalculationResult(totalIncome, totalExpense, netBalance)
    }



}

data class CalculationResult(
    val totalIncome: Double,
    val totalExpense: Double,
    val netBalance: Double
)