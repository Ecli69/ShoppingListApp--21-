package com.shoppinglist.app.feature.shoppinglist.presentation

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shoppinglist.app.core.notification.ShoppingForegroundService

private const val TAG = "ShoppingListScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(
    viewModel: ShoppingViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val items by viewModel.items.collectAsState()
    val context = LocalContext.current
    var showSettings by remember { mutableStateOf(false) }
    val strings = getAppStrings()

    // Сортируем при каждом изменении items
    val sortedItems = remember(items) {
        items.sortedBy { it.isChecked }
    }

    // Отладка
    LaunchedEffect(items) {
        Log.d(TAG, "Items updated, size: ${items.size}")
        items.forEach { item ->
            Log.d(TAG, "Item: ${item.name}, isChecked: ${item.isChecked}")
        }
    }

    if (showSettings) {
        SettingsScreen(
            viewModel = settingsViewModel,
            onBack = { showSettings = false }
        )
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            strings.shoppingList,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )
                    },
                    actions = {
                        IconButton(onClick = { showSettings = true }) {
                            Icon(Icons.Default.Settings, contentDescription = strings.settings)
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { viewModel.showAddDialog(true) },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.Add, contentDescription = strings.addItem)
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when {
                    state.isLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    sortedItems.isEmpty() -> {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.ShoppingCart,
                                contentDescription = null,
                                modifier = Modifier.size(80.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = strings.emptyList,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = strings.tapToAdd,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                    else -> {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                items = sortedItems,
                                key = { it.id }
                            ) { item ->
                                ShoppingItemCard(
                                    item = item,
                                    onCheck = {
                                        Log.d(TAG, "Checkbox clicked for: ${item.name}")
                                        viewModel.toggleItemCheck(item)
                                    },
                                    onDelete = { viewModel.deleteItem(item) },
                                    onStartShopping = {
                                        viewModel.setCurrentItemForService(item)
                                        ShoppingForegroundService.startService(context, item.name)
                                    },
                                    strings = strings
                                )
                            }

                            if (sortedItems.any { it.isChecked }) {
                                item {
                                    Button(
                                        onClick = { viewModel.deleteCheckedItems() },
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.error
                                        )
                                    ) {
                                        Text(strings.deleteCheckedItems)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (state.showAddDialog) {
            AddItemDialog(
                onDismiss = { viewModel.showAddDialog(false) },
                onAdd = { name, quantity, price, category, currency ->
                    viewModel.addItem(name, quantity, price, category, currency)
                },
                strings = strings
            )
        }
    }
}