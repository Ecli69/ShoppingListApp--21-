package com.shoppinglist.app.feature.shoppinglist.domain.usecase

import com.shoppinglist.app.feature.shoppinglist.domain.model.ShoppingItem
import com.shoppinglist.app.feature.shoppinglist.domain.repository.ShoppingRepository

class DeleteItemUseCase(
    private val repository: ShoppingRepository
) {
    suspend operator fun invoke(item: ShoppingItem) = repository.deleteItem(item)
}