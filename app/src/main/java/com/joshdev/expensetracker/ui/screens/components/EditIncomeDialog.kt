package com.joshdev.expensetracker.ui.screens.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.joshdev.expensetracker.data.entity.CategoryEntity
import com.joshdev.expensetracker.data.entity.IncomeEntity

@Composable
fun EditIncomeDialog(
    income: IncomeEntity,
    categories: List<CategoryEntity>,
    onDismiss: () -> Unit,
    onSave: (String, Double, Int) -> Unit
) {
    var title by remember { mutableStateOf(income.title) }
    var amount by remember { mutableStateOf(income.amount.toString()) }
    var selectedCategoryId by remember { mutableStateOf(income.categoryId) }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Income") },
        text = {
            Column {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = amount, onValueChange = { amount = it }, label = { Text("Amount") }, keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number))
                Spacer(modifier = Modifier.height(8.dp))
                Box {
                    OutlinedButton(onClick = { expanded = true }) {
                        Text(categories.firstOrNull { it.id == selectedCategoryId }?.name ?: "Select Category")
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        categories.forEach { category ->
                            DropdownMenuItem(text = { Text(category.name) }, onClick = {
                                selectedCategoryId = category.id
                                expanded = false
                            })
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val amountValue = amount.toDoubleOrNull()
                if (title.isNotEmpty() && amountValue != null) {
                    onSave(title, amountValue, selectedCategoryId)
                }
            }) { Text("Save") }
        },
        dismissButton = { Button(onClick = onDismiss) { Text("Cancel") } }
    )
}