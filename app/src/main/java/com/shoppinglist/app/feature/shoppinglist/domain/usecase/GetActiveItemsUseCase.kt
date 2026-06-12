package com.shoppinglist.app.feature.shoppinglist.domain.usecase

import com.shoppinglist.app.feature.shoppinglist.domain.model.ShoppingItem
import com.shoppinglist.app.feature.shoppinglist.domain.repository.ShoppingRepository
import kotlinx.coroutines.flow.Flow

class GetActiveItemsUseCase(
    private val repository: ShoppingRepository
) {
    operator fun invoke(): Flow<List<ShoppingItem>> = repository.getActiveItems()
}