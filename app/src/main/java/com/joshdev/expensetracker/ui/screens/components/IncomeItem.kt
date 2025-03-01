package com.joshdev.expensetracker.ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.joshdev.expensetracker.data.entity.CategoryEntity
import com.joshdev.expensetracker.data.entity.IncomeEntity
import com.joshdev.expensetracker.utils.DateUtils

@Composable
fun IncomeItem(
    income: IncomeEntity,
    categoryName: String,
    categories: List<CategoryEntity>? = null,
    onEdit: ((IncomeEntity) -> Unit)? = null,
    onDelete: ((IncomeEntity) -> Unit)? = null
) {
    var showMenu by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { showMenu = onEdit != null || onDelete != null },
        elevation = CardDefaults.cardElevation()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                // Display amount in bold and intense red
                Text(
                    "+Rs. ${income.amount}",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = Color(0xFF388E3C),
                        fontWeight = FontWeight.ExtraBold
                    )
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    // Title tag
                    Text(
                        income.title,
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.Black),
                        modifier = Modifier
                            .background(Color(0xFFBDBDBD), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Date tag (formatted)
                    Text(
                        DateUtils.formatDate(income.date),
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.Black),
                        modifier = Modifier
                            .background(Color(0xFFBDBDBD), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Category badge with rounded corners and primary background
            Text(
                text = categoryName,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }


        if (onEdit != null || onDelete != null) {
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                onEdit?.let {
                    DropdownMenuItem(
                        text = { Text("Edit") },
                        onClick = {
                            showMenu = false
                            showEditDialog = true
                        }
                    )
                }
                onDelete?.let {
                    DropdownMenuItem(
                        text = { Text("Delete") },
                        onClick = {
                            showMenu = false
                            it(income)
                        }
                    )
                }
            }
        }
    }

    if (showEditDialog && categories != null) {
        EditIncomeDialog(
            income = income,
            categories = categories,
            onDismiss = { showEditDialog = false },
            onSave = { newTitle, newAmount, newCategoryId ->
                onEdit?.invoke(
                    income.copy(
                        title = newTitle,
                        amount = newAmount,
                        categoryId = newCategoryId
                    )
                )
                showEditDialog = false
            }
        )
    }
}




