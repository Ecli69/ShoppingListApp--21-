package com.shoppinglist.app.feature.shoppinglist.presentation

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.shoppinglist.app.feature.shoppinglist.domain.model.ShoppingItem

@Composable
fun ShoppingItemCard(
    item: ShoppingItem,
    onCheck: () -> Unit,
    onDelete: () -> Unit,
    onStartShopping: () -> Unit,
    strings: AppStrings,
    modifier: Modifier = Modifier
) {
    var showMenu by remember { mutableStateOf(false) }

    // Форматирование цены и общей суммы с валютой товара
    val formattedPrice = when (item.currency) {
        "USD" -> "$${item.price}"
        else -> "${item.price} ₽"
    }

    val formattedTotal = when (item.currency) {
        "USD" -> "$${item.total}"
        else -> "${item.total} ₽"
    }

    // Используем ключ для принудительной перерисовки при изменении isChecked
    key(item.id) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = { showMenu = true }
                    )
                }
                .animateContentSize(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (item.isChecked)
                    MaterialTheme.colorScheme.surfaceVariant
                else
                    MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = item.isChecked,
                        onCheckedChange = { onCheck() }
                    )

                    // Информация о товаре
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // Название товара
                        Text(
                            text = item.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            textDecoration = if (item.isChecked) TextDecoration.LineThrough else null,
                            color = if (item.isChecked)
                                MaterialTheme.colorScheme.onSurfaceVariant
                            else
                                MaterialTheme.colorScheme.onSurface
                        )

                        // Строка с количеством, ценой и итогом
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Количество
                            Text(
                                text = "${item.quantity}",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (item.isChecked)
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                else
                                    MaterialTheme.colorScheme.onSurface
                            )
                            // Штуки
                            Text(
                                text = strings.pcs,
                                style = MaterialTheme.typography.bodySmall,
                                color = if (item.isChecked)
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                else
                                    MaterialTheme.colorScheme.onSurface
                            )
                            // Знак умножения
                            Text(
                                text = "×",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (item.isChecked)
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                else
                                    MaterialTheme.colorScheme.onSurface
                            )
                            // Цена за единицу
                            Text(
                                text = formattedPrice,
                                style = MaterialTheme.typography.bodySmall,
                                color = if (item.isChecked)
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                else
                                    MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                            // Знак равенства
                            Text(
                                text = "=",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (item.isChecked)
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                else
                                    MaterialTheme.colorScheme.onSurface
                            )
                            // Общая сумма
                            Text(
                                text = formattedTotal,
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (item.isChecked)
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                else
                                    MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Text(
                                text = item.getLocalizedCategory(),
                                style = MaterialTheme.typography.labelSmall,
                                color = if (item.isChecked)
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                else
                                    MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                // Корзинка :>
                IconButton(
                    onClick = { onStartShopping() },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        Icons.Default.ShoppingCart,
                        contentDescription = strings.startShopping,
                        tint = if (item.isChecked)
                            MaterialTheme.colorScheme.onSurfaceVariant
                        else
                            MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }

        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false }
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        strings.delete,
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
                },
                onClick = {
                    onDelete()
                    showMenu = false
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            )
        }
    }
}