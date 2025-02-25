package com.joshdev.expensetracker.utils

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

class MonthUtils {
    fun getFirstMonday(yearMonth: YearMonth): LocalDate {
        var date = yearMonth.atDay(1)
        while (date.dayOfWeek != DayOfWeek.MONDAY) {
            date = date.plusDays(1)
        }
        return date
    }
}