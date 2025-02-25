package com.joshdev.expensetracker.utils

import java.text.SimpleDateFormat
import java.util.Locale

object DateUtils {
    fun formatDate(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(timestamp)
    }
}