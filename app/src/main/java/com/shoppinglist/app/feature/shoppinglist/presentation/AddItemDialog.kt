package com.shoppinglist.app.feature.shoppinglist.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemDialog(
    onDismiss: () -> Unit,
    onAdd: (name: String, quantity: Int, price: Double, category: String, currency: String) -> Unit,
    strings: AppStrings
) {
    var name by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("1") }
    var price by remember { mutableStateOf("0") }
    var categoryValue by remember { mutableStateOf(strings.categories[0]) }
    var selectedCurrency by remember { mutableStateOf("RUB") }
    var expandedCategory by remember { mutableStateOf(false) }
    var expandedCurrency by remember { mutableStateOf(false) }

    val categoryToEnglish = mapOf(
        "Продукты" to "Food",
        "Бытовая химия" to "Household",
        "Одежда" to "Clothing",
        "Электроника" to "Electronics",
        "Другое" to "Other",
        "Food" to "Food",
        "Household" to "Household",
        "Clothing" to "Clothing",
        "Electronics" to "Electronics",
        "Other" to "Other"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(strings.addItem) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(strings.name) },
                    leadingIcon = { Icon(Icons.Default.ShoppingCart, null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = quantity,
                        onValueChange = { quantity = it },
                        label = { Text(strings.quantity) },
                        leadingIcon = { Icon(Icons.Default.ProductionQuantityLimits, null) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = price,
                        onValueChange = { price = it },
                        label = { Text(strings.price) },
                        leadingIcon = {
                            Icon(
                                if (selectedCurrency == "RUB") Icons.Default.AttachMoney else Icons.Default.MonetizationOn,
                                null
                            )
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Done
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }

                ExposedDropdownMenuBox(
                    expanded = expandedCurrency,
                    onExpandedChange = { expandedCurrency = it }
                ) {
                    OutlinedTextField(
                        value = if (selectedCurrency == "RUB") strings.currencyRub else strings.currencyUsd,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(strings.selectCurrency) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCurrency) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = expandedCurrency,
                        onDismissRequest = { expandedCurrency = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(strings.currencyRub) },
                            onClick = {
                                selectedCurrency = "RUB"
                                expandedCurrency = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(strings.currencyUsd) },
                            onClick = {
                                selectedCurrency = "USD"
                                expandedCurrency = false
                            }
                        )
                    }
                }

                ExposedDropdownMenuBox(
                    expanded = expandedCategory,
                    onExpandedChange = { expandedCategory = it }
                ) {
                    OutlinedTextField(
                        value = categoryValue,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(strings.category) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategory) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = expandedCategory,
                        onDismissRequest = { expandedCategory = false }
                    ) {
                        strings.categories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat) },
                                onClick = {
                                    categoryValue = cat
                                    expandedCategory = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val qty = quantity.toIntOrNull() ?: 1
                    val prc = price.toDoubleOrNull() ?: 0.0
                    if (name.isNotBlank() && qty > 0 && prc >= 0) {
                        val englishCategory = categoryToEnglish[categoryValue] ?: categoryValue
                        onAdd(name, qty, prc, englishCategory, selectedCurrency)
                        onDismiss()
                    }
                }
            ) {
                Text(strings.add)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(strings.cancel)
            }
        }
    )
}