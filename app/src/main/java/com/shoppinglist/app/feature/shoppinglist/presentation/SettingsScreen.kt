package com.shoppinglist.app.feature.shoppinglist.presentation

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val theme by viewModel.theme.collectAsState()
    val language by viewModel.language.collectAsState()
    val currency by viewModel.currency.collectAsState()
    val context = LocalContext.current
    val strings = getAppStrings()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.settings) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = strings.settings)
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Настройка темы
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = strings.theme,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilterChip(
                                selected = theme == "light",
                                onClick = {
                                    viewModel.updateTheme("light")
                                },
                                label = { Text(strings.light) }
                            )

                            FilterChip(
                                selected = theme == "dark",
                                onClick = {
                                    viewModel.updateTheme("dark")
                                },
                                label = { Text(strings.dark) }
                            )

                            FilterChip(
                                selected = theme == "system",
                                onClick = {
                                    viewModel.updateTheme("system")
                                },
                                label = { Text(strings.system) }
                            )
                        }
                    }
                }
            }

            // Настройка языка
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = strings.language,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilterChip(
                                selected = language == "en",
                                onClick = {
                                    viewModel.updateLanguage("en", context)
                                },
                                label = { Text(strings.english) }
                            )

                            FilterChip(
                                selected = language == "ru",
                                onClick = {
                                    viewModel.updateLanguage("ru", context)
                                },
                                label = { Text(strings.russian) }
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = strings.settingsRestartMessage,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Настройка валюты
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = strings.selectCurrency,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilterChip(
                                selected = currency == "RUB",
                                onClick = {
                                    viewModel.updateCurrency("RUB")
                                },
                                label = { Text(strings.currencyRub) }
                            )

                            FilterChip(
                                selected = currency == "USD",
                                onClick = {
                                    viewModel.updateCurrency("USD")
                                },
                                label = { Text(strings.currencyUsd) }
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (java.util.Locale.getDefault().language == "ru")
                                "Валюта будет применяться при добавлении новых товаров"
                            else
                                "Currency will be applied when adding new items",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}