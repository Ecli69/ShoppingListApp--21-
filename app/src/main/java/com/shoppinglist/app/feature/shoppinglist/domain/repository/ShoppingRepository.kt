package com.shoppinglist.app.feature.shoppinglist.domain.repository

import com.shoppinglist.app.feature.shoppinglist.domain.model.ShoppingItem
import kotlinx.coroutines.flow.Flow

interface ShoppingRepository {
    fun getAllItems(): Flow<List<ShoppingItem>>
    fun getActiveItems(): Flow<List<ShoppingItem>>
    suspend fun addItem(item: ShoppingItem): Long
    suspend fun updateItem(item: ShoppingItem)
    suspend fun deleteItem(item: ShoppingItem)
    suspend fun deleteCheckedItems()
    suspend fun checkItem(itemId: Long, isChecked: Boolean)
}