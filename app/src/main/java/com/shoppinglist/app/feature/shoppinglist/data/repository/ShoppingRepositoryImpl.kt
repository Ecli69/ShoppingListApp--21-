package com.shoppinglist.app.feature.shoppinglist.data.repository

import com.shoppinglist.app.core.database.ShoppingDao
import com.shoppinglist.app.core.database.ShoppingItemEntity
import com.shoppinglist.app.feature.shoppinglist.domain.model.ShoppingItem
import com.shoppinglist.app.feature.shoppinglist.domain.repository.ShoppingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShoppingRepositoryImpl @Inject constructor(
    private val dao: ShoppingDao
) : ShoppingRepository {

    override fun getAllItems(): Flow<List<ShoppingItem>> {
        return dao.getAllItems().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getActiveItems(): Flow<List<ShoppingItem>> {
        return dao.getActiveItems().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun addItem(item: ShoppingItem): Long {
        return dao.insertItem(item.toEntity())
    }

    override suspend fun updateItem(item: ShoppingItem) {
        dao.updateItem(item.toEntity())
    }

    override suspend fun deleteItem(item: ShoppingItem) {
        dao.deleteItem(item.toEntity())
    }

    override suspend fun deleteCheckedItems() {
        dao.deleteCheckedItems()
    }

    override suspend fun checkItem(itemId: Long, isChecked: Boolean) {
        dao.updateCheckStatus(itemId, isChecked)
    }

    private fun ShoppingItemEntity.toDomain() = ShoppingItem(
        id = id,
        name = name,
        quantity = quantity,
        price = price,
        isChecked = isChecked,
        createdAt = createdAt,
        category = category,
        currency = currency
    )

    private fun ShoppingItem.toEntity() = ShoppingItemEntity(
        id = id,
        name = name,
        quantity = quantity,
        price = price,
        isChecked = isChecked,
        createdAt = createdAt,
        category = category,
        currency = currency
    )
}